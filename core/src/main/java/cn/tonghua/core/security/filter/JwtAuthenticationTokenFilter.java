package cn.tonghua.core.security.filter;

import cn.tonghua.core.bundle.MyRespBundle;
import cn.tonghua.core.security.dao.SecurityUserDao;
import cn.tonghua.core.security.utils.JwtUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private SecurityUserDao securityUserDao;
    private JwtUtils jwtTokenUtil;

    @Autowired
    public JwtAuthenticationTokenFilter(SecurityUserDao userDetailsService, JwtUtils jwtTokenUtil) {
        this.securityUserDao = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        String tokenHead = "Bearer ";
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            String authToken = authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.securityUserDao.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else{
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=utf-8");
                    response.setHeader("Access-Control-Allow-Origin","*");
                    MyRespBundle<String> resp = new MyRespBundle<>();
                    resp.setTimestamp(Instant.now().toEpochMilli());
                    resp.setMsg("非法授权!");
                    resp.setCode(HttpStatus.FORBIDDEN.value());
                    resp.setData("非法授权");
                    response.getWriter().write(new Gson().toJson(resp));
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

}