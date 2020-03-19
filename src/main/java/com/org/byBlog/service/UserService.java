package com.org.byBlog.service;

import com.org.byBlog.dao.PublicUserDAO;
import com.org.byBlog.enums.Role;
import com.org.byBlog.pojo.dto.UserDTO;
import com.org.byBlog.pojo.po.PublicUserPO;
import com.org.byBlog.pojo.vo.ListVO;
import com.org.byBlog.pojo.vo.UserVO;
import com.org.byBlog.utils.JWTUtil;
import com.org.byBlog.utils.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Resource
    private PublicUserDAO publicUserDAO;

    @Autowired
    private Environment environment;

    @Transactional(rollbackFor = Exception.class)
    public Result register(UserDTO userDTO) {
        // 验证账号和昵称是否存在
        PublicUserPO userIsExist = publicUserDAO.getUserByInfo(userDTO);
        if (Objects.nonNull(userIsExist)) {
            return new Result(1, "账号或昵称已经存在");
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

    public Result<UserVO> login(UserDTO userDTO) {
        String password = "%by_blog_" + userDTO.getPassword();
        String encryptedPassword = DigestUtils.md5Hex(password);

        // 生成一个token
        String token = JWTUtil.sign(userDTO.getAccount(), environment.getProperty("jwt.secret"));

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userDTO.getAccount(), encryptedPassword);
        try {
            subject.login(usernamePasswordToken);
            PublicUserPO user = publicUserDAO.getUserByAccount(userDTO.getAccount());
            UserVO userVO = new UserVO();
            userVO.setToken(token);
            userVO.setRole(user.getRole());
            return new Result(0, "登录成功", userVO);
        } catch (AuthenticationException e) {
            return new Result(1, "账号或密码错误");
        }
    }

    public Result<UserVO> getLoginInfo(String account) {
        PublicUserPO user = publicUserDAO.getUserByAccount(account);
        if (user == null) {
            return new Result(1, "账号不存在");
        }
        UserVO userVO = new UserVO();
        userVO.setNickname(user.getNickname());
        userVO.setRole(user.getRole());
        return new Result<UserVO>(0, "查询成功", userVO);
    }

    public Result getUserList(UserDTO userDTO) {
        Integer totalCount = publicUserDAO.getUserTotalCount(userDTO);
        if (totalCount == 0) {
            return new Result(1, "暂无数据");
        }

        List<PublicUserPO> userList = publicUserDAO.getUserList(userDTO);
        List<UserVO> userVOList = userList.stream().map(
                publicUserPO -> UserVO.fromPO(publicUserPO)
        ).collect(Collectors.toList());

        ListVO listVO = new ListVO();
        listVO.setList(userVOList);
        listVO.setTotal(totalCount);

        Result result = new Result(0, "获取成功");
        result.setData(listVO);
        return result;
    }

    public Result operateUser(UserDTO userDTO) {
        PublicUserPO user = publicUserDAO.selectByPrimaryKey(userDTO.getId());
        if (Objects.isNull(user)) {
            return new Result(1, "用户不存在");
        }

        PublicUserPO publicUserPO = new PublicUserPO();
        publicUserPO.setId(userDTO.getId());
        publicUserPO.setFreeze(userDTO.getStatus());
        int keySelective = publicUserDAO.updateByPrimaryKeySelective(publicUserPO);
        return new Result(keySelective > 0 ? 0 : 1, keySelective > 0 ? "修改成功" : "修改失败");
    }
}