package com.org.byBlog.shiro;

import com.org.byBlog.dao.PublicUserDAO;
import com.org.byBlog.pojo.po.PublicUserPO;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthRealm extends AuthorizingRealm {

	@Autowired
	private PublicUserDAO publicUserDAO;

	/**
	 * 认证.登录
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//获取用户输入的token
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		String account = usernamePasswordToken.getUsername();
		PublicUserPO publicUserPO = this.publicUserDAO.getAdminByAccount(account);
		//放入shiro.调用CredentialsMatcher检验密码
		return new SimpleAuthenticationInfo(publicUserPO, publicUserPO.getPassword(), this.getClass().getName());
	}

	/**
	 *授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		//获取session中的用户
		PublicUserPO publicUserPO = (PublicUserPO) principal.fromRealm(this.getClass().getName()).iterator().next();
		PublicUserPO userRole = this.publicUserDAO.selectByPrimaryKey(publicUserPO.getId());
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRole(userRole.getRole());
		return info;
	}
}