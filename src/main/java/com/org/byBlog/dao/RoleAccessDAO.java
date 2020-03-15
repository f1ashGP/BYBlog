package com.org.byBlog.dao;

import com.org.byBlog.pojo.po.RoleAccessPO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAccessDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleAccessPO record);

    int insertSelective(RoleAccessPO record);

    RoleAccessPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleAccessPO record);

    int updateByPrimaryKey(RoleAccessPO record);

    List<RoleAccessPO> getRoleAccessList();
}