package com.org.byBlog.filter;

import com.org.byBlog.jwt.JWTToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class JWTFilter extends FormAuthenticationFilter {
    private String jwtSecret;

    /**
     * 正常的账号密码验证如果不通过，则验证jwtToken
     * *
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String token = ((HttpServletRequest) request).getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            request.getRequestDispatcher("/user/loginTimeout").forward(request, response);
            return false;
        }

        JWTToken jwtToken = new JWTToken(token);
        try {
            getSubject(request, response).login(jwtToken);
            return true;
        } catch (AuthenticationException e) {
            request.getRequestDispatcher("/user/loginTimeout").forward(request, response);
            return false;
        }
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }
}