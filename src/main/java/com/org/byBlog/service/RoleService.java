package com.org.byBlog.service;

import com.org.byBlog.dao.RoleAccessDAO;
import com.org.byBlog.pojo.dto.RoleDTO;
import com.org.byBlog.pojo.po.RoleAccessPO;
import com.org.byBlog.pojo.vo.ListVO;
import com.org.byBlog.pojo.vo.RoleVO;
import com.org.byBlog.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleService {

    @Resource
    private RoleAccessDAO roleAccessDAO;

    public Result<ListVO<RoleVO>> getRoleList(RoleDTO roleDTO) {
        Integer totalCount = roleAccessDAO.getRoleTotalCount(roleDTO);
        if (totalCount == 0) {
            return new Result<>(1,"暂无数据");
        }
        List<RoleAccessPO> roleList = roleAccessDAO.getRoleList(roleDTO);

        ListVO listVO = new ListVO();
        listVO.setTotal(totalCount);
        listVO.setList(roleList);

        Result result = new Result(0,"获取成功");
        result.setData(listVO);
        return result;
    }

}
