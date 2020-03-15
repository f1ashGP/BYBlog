package com.org.byBlog.service;

import com.org.byBlog.dao.PublicUserDAO;
import com.org.byBlog.enums.Role;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class UserService {

    @Resource
    private PublicUserDAO publicUserDAO;

    @Transactional(rollbackFor = Exception.class)
    public Result register(UserDTO userDTO) {
        // 验证账号和昵称是否存在
        PublicUserPO userIsExist = publicUserDAO.getUserByInfo(userDTO);
        if (Objects.nonNull(userIsExist)) {
            return new Result(1,"账号或昵称已经存在");
        }

        // 加密以后的密码
        String password = "%by_blog_" + userDTO.getPassword();
        String encryptedPassword = DigestUtils.md5Hex(password);

        PublicUserPO user = new PublicUserPO();
        user.setNickname(userDTO.getNickname());
        user.setUsername(userDTO.getAccount());
        user.setPassword(encryptedPassword);
        user.setRole(Role.NORMAL.getDesc());
        user.setFreeze(false);
        user.setCreateTime(new Date());
        int insertSelective = publicUserDAO.insertSelective(user);
        return new Result(insertSelective > 0 ? 0 : 1, insertSelective > 0 ? "注册成功" : "注册失败");
    }

    public Result login(UserDTO userDTO) {
        String password = "%by_blog_" + userDTO.getPassword();
        String encryptedPassword = DigestUtils.md5Hex(password);

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