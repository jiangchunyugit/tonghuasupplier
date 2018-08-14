 package cn.thinkfree.core.security.filter;

 import org.springframework.security.core.Authentication;
 import org.springframework.security.web.DefaultRedirectStrategy;
 import org.springframework.security.web.RedirectStrategy;
 import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

 import javax.servlet.ServletException;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.servlet.http.HttpSession;
 import java.io.IOException;
 import java.util.HashMap;
 import java.util.Map;


 public class SercuitySuccessAuthHandler
   extends SimpleUrlAuthenticationSuccessHandler
 {
  
   private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
   private boolean forwardToDestination = false;
   
   
   public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
     throws ServletException, IOException
   {
 	 super.onAuthenticationSuccess(request, response, authentication);
       
   }
   
   protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
     throws IOException, ServletException
   {
     String targetUrl = determineTargetUrl(request, response);
     if (response.isCommitted())
     {
       this.logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
       return;
     }
     Map<String, Object> result = new HashMap<String,Object>();
//     result.put("user", ((User)authentication.getPrincipal()).getUsername());
     saveMessage(request, result);
       if (isUseForward(request)) {
         request.getRequestDispatcher(targetUrl).forward(request, response);
     } else {
         this.redirectStrategy.sendRedirect(request, response, targetUrl);
     }
      
   }
   
   protected final void saveMessage(HttpServletRequest request, Map<String, Object> result)
   {
     if (result == null) {
       return;
     }
     if (isUseForward())
     {
       for (String key : result.keySet()) {
         request.setAttribute(key, result.get(key));
       }
     }
     else
     {
       HttpSession session = request.getSession(true);
       if (session != null) {
         for (String key : result.keySet()) {
           request.getSession().setAttribute(key, result.get(key));
         }
       }
     }
   }
   
 
 
 
   
   protected boolean isUseForward()
   {
     return this.forwardToDestination;
   }
   
   protected boolean isUseForward(HttpServletRequest request)
   {
     String redirect = request.getParameter("_redirect");
     if (redirect != null)
     {
       if (redirect.equals("1")) {
         return false;
       }
       if (redirect.equals("0")) {
         return true;
       }
     }
     return this.forwardToDestination;
   }
   
   public void setUseForward(boolean forwardToDestination)
   {
     this.forwardToDestination = forwardToDestination;
   }
   
  
 }

