package com.financecrm.webportal.auth;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {


    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String userId = null;

      /*  if (authHeader != null && authHeader.contains("Bearer")) {
            token = authHeader.substring(7);*/
            try {

                String jwt = getJwtFromRequest(request);

                if(StringUtils.hasText(jwt) && tokenManager.tokenValidate(jwt)){
                    userId = tokenManager.parseUserIdFromToken(jwt);
                    User user = customUserService.findByUserId(userId);
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

                    if(!user.isDeleted()){
                        UsernamePasswordAuthenticationToken upassToken =
                                new UsernamePasswordAuthenticationToken(userId, jwt, userDetails.getAuthorities());
                        upassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(upassToken);
                    }

                }

                /*userId = tokenManager.parseUserIdFromToken(token);
                User user = customUserService.findByUserId(userId).getData();
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

                if (tokenManager.tokenValidate(token)) {
                    UsernamePasswordAuthenticationToken upassToken =
                            new UsernamePasswordAuthenticationToken(userId, token, userDetails.getAuthorities());
                    upassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(upassToken);

                }
*/
            } catch (Exception e) {
                e.printStackTrace();
            }
      //  }

        filterChain.doFilter(request, response);
    }


    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.getSessionTimeout();
    }

    public String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        else{
            return null;
        }
    }
}
