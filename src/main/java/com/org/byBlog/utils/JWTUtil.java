package com.org.byBlog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JWTUtil {
	public static final long EXPIRE_TIME = 35 * 24 * 60 * 60 * 1000L;

	/**
	 * 校验token是否正确
	 *
	 * @param token  密钥
	 * @param secret 用户的密码
	 * @return 是否正确
	 */
	public static boolean verify(String token, String username, String secret) {
		try {
			//根据密码生成JWT效验器
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm)
					.withClaim("username", username)
					.build();
			//校验TOKEN
			DecodedJWT jwt = verifier.verify(token);
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}

	/**
	 * 获得token中的信息无需secret解密也能获得
	 *
	 * @return token中包含的用户ID
	 */
	public static String getUserId(String token) {
		if (token == null) {
			return null;
		}

		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("username").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 获得token中的信息无需secret解密也能获得
	 *
	 * @return token的过期时间
	 */
	public static Date getExpireTime(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getExpiresAt();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 生成签名,指定时间后过期
	 *
	 * @param username 用户ID
	 * @param secret 秘钥
	 * @return 加密的token
	 */
	public static String sign(String username, String secret) {
		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Algorithm algorithm = Algorithm.HMAC256(secret);
		// 附带username信息
		return JWT.create()
				.withClaim("username", username)
				.withExpiresAt(date)
				.sign(algorithm);
	}
}
