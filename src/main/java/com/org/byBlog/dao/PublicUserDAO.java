package com.org.byBlog.dao;

import com.org.byBlog.pojo.dto.UserDTO;
import com.org.byBlog.pojo.po.PublicUserPO;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicUserDAO {
    int deleteByPrimaryKey(Long id);

    int insert(PublicUserPO record);

    int insertSelective(PublicUserPO record);

    PublicUserPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PublicUserPO record);

    int updateByPrimaryKey(PublicUserPO record);

    PublicUserPO getAdminByAccount(String account);

    PublicUserPO getUserByInfo(UserDTO userDTO);
}