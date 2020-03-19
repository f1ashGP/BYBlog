package com.org.byBlog.dao;

import com.org.byBlog.pojo.dto.UserDTO;
import com.org.byBlog.pojo.po.PublicUserPO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicUserDAO {
    int deleteByPrimaryKey(Long id);

    int insert(PublicUserPO record);

    int insertSelective(PublicUserPO record);

    PublicUserPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PublicUserPO record);

    int updateByPrimaryKey(PublicUserPO record);

    PublicUserPO getUserByAccount(String account);

    PublicUserPO getUserByInfo(UserDTO userDTO);

    List<PublicUserPO> getUserList(UserDTO userDTO);

    Integer getUserTotalCount(UserDTO userDTO);
}