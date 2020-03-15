package com.org.byBlog.jwt;

import org.apache.shiro.authc.AuthenticationToken;

public class JWTToken implements AuthenticationToken {
	private String token;

	public JWTToken(String token) {
		this.token = token;
	}

	public Object getPrincipal() {
		return token;
	}

	public Object getCredentials() {
		return token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
