package cn.thinkfree.core.security.filter;


import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.event.MyEventBus;
import cn.thinkfree.core.event.model.UserLoginAfter;
import cn.thinkfree.core.security.filter.util.SecurityRequestUtil;
import cn.thinkfree.core.security.model.SecurityUser;
import cn.thinkfree.core.security.utils.JwtUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecuritySuccessAuthHandler
        extends SimpleUrlAuthenticationSuccessHandler {



    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private boolean forwardToDestination = false;

//    @Autowired
//    JwtUtils jwtUtils;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);


    }

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        String targetUrl = determineTargetUrl(request, response);
        if (response.isCommitted()) {
            this.logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        MyEventBus.getInstance().publicEvent(
                new UserLoginAfter(user.getUsername(),user.getPhone(),SecurityRequestUtil.getRequestIp(request)));
//        sendAjaxResult(request,response);
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String,Object> userModel = new HashMap<>();
        userModel.put("username",user.getUsername());
        userModel.put("companyName",user.getCompanyName());
        userModel.put("name",user.getName());
        userModel.put("createTime",user.getCreateTime());
        result.put("userModel",userModel);
//        String token = jwtUtils.generateToken(user);
//        System.out.println(token);
//        result.put("token",token);
        saveMessage(request, result);
        request.getRequestDispatcher("/index").forward(request, response);

    }

    /**
     * 发送回执
     * 用于应对AJAX提交
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void sendAjaxResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin","*");
        MyRespBundle<String> resp = new MyRespBundle<>();
        resp.setTimestamp(Instant.now().toEpochMilli());
        resp.setMessage("登录成功!");
        resp.setCode(200);
        resp.setData("登录成功!");
        response.getWriter().write(new Gson().toJson(resp));

    }

    protected final void saveMessage(HttpServletRequest request, Map<String, Object> result) {
        if (result == null) {
            return;
        }
        if (isUseForward()) {
            for (String key : result.keySet()) {
                request.setAttribute(key, result.get(key));
            }
        } else {
            HttpSession session = request.getSession(true);
            if (session != null) {
                for (String key : result.keySet()) {
                    request.getSession().setAttribute(key, result.get(key));
                }
            }
        }
    }


    protected boolean isUseForward() {
        return this.forwardToDestination;
    }

    protected boolean isUseForward(HttpServletRequest request) {
        String redirect = request.getParameter("_redirect");
        if (redirect != null) {
            if (redirect.equals("1")) {
                return false;
            }
            if (redirect.equals("0")) {
                return true;
            }
        }
        return this.forwardToDestination;
    }

    public void setUseForward(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }

}

