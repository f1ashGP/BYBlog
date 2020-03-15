package com.org.byBlog.shiro;

import com.org.byBlog.dao.PublicUserDAO;
import com.org.byBlog.jwt.JWTToken;
import com.org.byBlog.utils.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class JWTRealm extends AuthorizingRealm {

	@Autowired
	private PublicUserDAO publicUserDAO;
	@Autowired
	private Environment environment;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JWTToken;
	}

	//认证.登录
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		//获取用户输入的token
		JWTToken jwtToken = (JWTToken) authenticationToken;

		String username = JWTUtil.getUserId(jwtToken.getToken());
		if (!JWTUtil.verify(jwtToken.getToken(), username, environment.getProperty("jwt.secret"))) {
			return null;
		}

		return new SimpleAuthenticationInfo(username, jwtToken.getToken(), this.getClass().getName());//放入shiro.调用CredentialsMatcher检验密码
	}

	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRole("user");
		return info;
	}
}