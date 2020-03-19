package com.org.byBlog.dao;

import com.org.byBlog.pojo.dto.RoleDTO;
import com.org.byBlog.pojo.po.RoleAccessPO;

import java.util.List;

public interface RoleAccessDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleAccessPO record);

    int insertSelective(RoleAccessPO record);

    RoleAccessPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleAccessPO record);

    int updateByPrimaryKey(RoleAccessPO record);

    List<RoleAccessPO> getRoleAccessList();

    List<RoleAccessPO> getRoleList(RoleDTO roleDTO);

    Integer getRoleTotalCount(RoleDTO roleDTO);

    RoleAccessPO checkMenuIsExists(RoleDTO roleDTO);
}