package cn.thinkfree.core.security.filter;


import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Session并发控制策略
 */
public class MyConcurrentSessionControlStrategy extends SessionFixationProtectionStrategy implements MessageSourceAware {
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private final SessionRegistry sessionRegistry;
    private boolean exceptionIfMaximumExceeded = false;
    private int maximumSessions = 1;

    public MyConcurrentSessionControlStrategy(SessionRegistry sessionRegistry) {
        Assert.notNull(sessionRegistry, "The sessionRegistry cannot be null");
        super.setAlwaysCreateSession(true);
        this.sessionRegistry = sessionRegistry;
    }

    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        checkAuthenticationAllowed(authentication, request);


        super.onAuthentication(authentication, request, response);
        this.sessionRegistry.registerNewSession(request.getSession().getId(), authentication.getPrincipal());
    }

    private void checkAuthenticationAllowed(Authentication authentication, HttpServletRequest request) throws AuthenticationException {

        List<SessionInformation> sessions = this.sessionRegistry.getAllSessions(authentication.getPrincipal(), false);

        int sessionCount = sessions.size();
        int allowedSessions = getMaximumSessionsForThisUser(authentication);
        if (allowedSessions == -1) {
            return;
        }
        if (sessionCount < allowedSessions) {
            return;
        }
        HttpSession session;
        if (sessionCount == allowedSessions) {
            session = request.getSession(false);
            if (session != null) {
                for (SessionInformation si : sessions) {
                    if (si.getSessionId().equals(session.getId())) {
                        return;
                    }
                }
            }
        }
        allowableSessionsExceeded(sessions, allowedSessions, this.sessionRegistry, request);
    }

    protected int getMaximumSessionsForThisUser(Authentication authentication) {
        return this.maximumSessions;
    }

    protected void allowableSessionsExceeded(List<SessionInformation> sessions, int allowableSessions, SessionRegistry registry, HttpServletRequest request) throws SessionAuthenticationException {
        String force = request.getParameter("force_login");
        if ((!StringUtils.isEmpty(force)) && (force.equalsIgnoreCase("1"))) {
            request.setAttribute("FORCE_LOGIN", Boolean.valueOf(true));
        } else if ((this.exceptionIfMaximumExceeded) || (sessions == null)) {
            request.getSession().setAttribute("CAN_FORCE_LOGIN", Boolean.valueOf(true));
            throw new SessionAuthenticationException("auth.exceededAllowed");
        }
        SessionInformation leastRecentlyUsed = null;
        for (int i = 0; i < sessions.size(); i++) {
            if ((leastRecentlyUsed == null) || (((SessionInformation) sessions.get(i)).getLastRequest().before(leastRecentlyUsed.getLastRequest()))) {
                leastRecentlyUsed = (SessionInformation) sessions.get(i);
            }
        }
        leastRecentlyUsed.expireNow();
    }

    public void setExceptionIfMaximumExceeded(boolean exceptionIfMaximumExceeded) {
        this.exceptionIfMaximumExceeded = exceptionIfMaximumExceeded;
    }

    public void setMaximumSessions(int maximumSessions) {
        Assert.isTrue(maximumSessions != 0, "MaximumLogins must be either -1 to allow unlimited logins, or a positive integer to specify a maximum");
        this.maximumSessions = maximumSessions;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public final void setAlwaysCreateSession(boolean alwaysCreateSession) {
        if (!alwaysCreateSession) {
            throw new IllegalArgumentException("Cannot set alwaysCreateSession to false when concurrent session control is required");
        }
    }

}

