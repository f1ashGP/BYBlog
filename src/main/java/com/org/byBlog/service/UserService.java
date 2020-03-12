package com.org.byBlog.service;

import com.org.byBlog.dao.PublicUserDAO;
import com.org.byBlog.pojo.dto.UserDTO;
import com.org.byBlog.pojo.po.PublicUserPO;
import com.org.byBlog.pojo.vo.UserVO;
import com.org.byBlog.utils.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private PublicUserDAO publicUserDAO;

    public Result login(UserDTO userDTO) {
        String encryptedPassword = DigestUtils.md5Hex(String.format("%s_pocketadmin", userDTO.getPassword()));

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userDTO.getAccount(), encryptedPassword);
        try {
            subject.login(usernamePasswordToken);
            PublicUserPO userByAccount = publicUserDAO.getAdminByAccount(userDTO.getAccount());
            return new Result(0, "登录成功", userByAccount);
        } catch (AuthenticationException e) {
            return new Result(1, "账号或密码错误");
        }
    }

    public Result getLoginInfo(Long uid) {
        PublicUserPO user = publicUserDAO.selectByPrimaryKey(uid);
        if (user == null) {
            return new Result(1, "账号不存在");
        }
        UserVO userVO = new UserVO();
        userVO.setNickname(user.getNickname());
        return new Result(0, "查询成功", userVO);
    }
}