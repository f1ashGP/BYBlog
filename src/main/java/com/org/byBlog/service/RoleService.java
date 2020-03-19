package com.org.byBlog.service;

import com.org.byBlog.dao.RoleAccessDAO;
import com.org.byBlog.enums.OperateMode;
import com.org.byBlog.pojo.dto.RoleDTO;
import com.org.byBlog.pojo.po.RoleAccessPO;
import com.org.byBlog.pojo.vo.ListVO;
import com.org.byBlog.pojo.vo.RoleVO;
import com.org.byBlog.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class RoleService {

    @Resource
    private RoleAccessDAO roleAccessDAO;

    public Result<ListVO<RoleVO>> getRoleList(RoleDTO roleDTO) {
        Integer totalCount = roleAccessDAO.getRoleTotalCount(roleDTO);
        if (totalCount == 0) {
            return new Result<>(1, "暂无数据");
        }
        List<RoleAccessPO> roleList = roleAccessDAO.getRoleList(roleDTO);

        ListVO listVO = new ListVO();
        listVO.setTotal(totalCount);
        listVO.setList(roleList);

        Result result = new Result(0, "获取成功");
        result.setData(listVO);
        return result;
    }

    public Result getRoleInfo(Integer id) {
        RoleAccessPO roleAccessPO = roleAccessDAO.selectByPrimaryKey(id);
        if (Objects.isNull(roleAccessPO)) {
            return new Result(1, "该权限不存在");
        }
        RoleVO roleVO = RoleVO.fromPO(roleAccessPO);
        return new Result(0, "获取成功", roleVO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result addNewRole(RoleDTO roleDTO) {
        roleDTO.setMode(OperateMode.ADD.getMode());
        Result result = validateData(roleDTO);
        if (result.getCode() != 0) {
            return result;
        }

        RoleAccessPO roleAccessPO = new RoleAccessPO();
        BeanUtils.copyProperties(roleDTO, roleAccessPO);
        roleAccessPO.setRole(roleDTO.getRoleName());
        roleAccessPO.setCreateTime(new Date());
        int insertSelective = roleAccessDAO.insertSelective(roleAccessPO);
        return new Result(insertSelective > 0 ? 0 : 1, insertSelective > 0 ? "添加成功" : "添加失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public Result operateRole(RoleDTO roleDTO) {
        RoleAccessPO roleAccess = roleAccessDAO.selectByPrimaryKey(roleDTO.getId());
        if (Objects.isNull(roleAccess)) {
            return new Result(1, "信息不存在");
        }

        String mode = roleDTO.getMode();
        RoleAccessPO roleAccessPO = roleAccessDAO.selectByPrimaryKey(roleDTO.getId());
        if (OperateMode.DELETE.getMode().equals(mode)) {
            if (roleAccessPO.getStatus()) {
                return new Result(1, "权限未关闭无法删除");
            }
            int delete = roleAccessDAO.deleteByPrimaryKey(roleDTO.getId());
            return new Result<>(delete > 0 ? 0 : 1, delete > 0 ? "删除成功" : "删除失败");
        }
        if (OperateMode.OPERATE_STATUS.getMode().equals(mode)) {
            BeanUtils.copyProperties(roleDTO, roleAccessPO);
            int selective = roleAccessDAO.updateByPrimaryKeySelective(roleAccessPO);
            return new Result<>(selective > 0 ? 0 : 1, selective > 0 ? "修改成功" : "修改失败");
        }
        if (OperateMode.UPDATE.getMode().equals(mode)) {
            Boolean roleName = getRoleName(roleDTO.getRoleName());
            if (OperateMode.UPDATE.equals(roleDTO.getMode())) {
                if (roleName) {
                    return new Result(1, "权限不符合规则");
                }
            }
            Result result = validateData(roleDTO);
            if (result.getCode() != 0) {
                return result;
            }
            BeanUtils.copyProperties(roleDTO, roleAccessPO);
            roleAccessPO.setRole(roleDTO.getRoleName());
            int selective = roleAccessDAO.updateByPrimaryKeySelective(roleAccessPO);
            return new Result<>(selective > 0 ? 0 : 1, selective > 0 ? "修改成功" : "修改失败");
        }
        return new Result<>(1, "暂无操作类型");
    }

    public Result validateData(RoleDTO roleDTO) {
        String mode = roleDTO.getMode();
        Boolean dataValidate = false;
        RoleAccessPO roleAccessPO;
        if (OperateMode.UPDATE.equals(mode)) {
            roleDTO.setMode("name");
            roleAccessPO = roleAccessDAO.checkMenuIsExists(roleDTO);
            if (!roleAccessPO.getId().equals(roleDTO.getId())) {
                dataValidate = true;
            }
            roleDTO.setMode("path");
            roleAccessPO = roleAccessDAO.checkMenuIsExists(roleDTO);
            if (!roleAccessPO.getId().equals(roleDTO.getId())) {
                dataValidate = true;
            }
        } else {
            roleDTO.setMode("name");
            roleAccessPO = roleAccessDAO.checkMenuIsExists(roleDTO);
            if (Objects.nonNull(roleAccessPO)) {
                dataValidate = true;
            }
            roleDTO.setMode("path");
            roleAccessPO = roleAccessDAO.checkMenuIsExists(roleDTO);
            if (Objects.nonNull(roleAccessPO)) {
                dataValidate = true;
            }
        }
        return new Result(!dataValidate ? 0 : 1, !dataValidate ? "不存在信息，可以添加" : "信息存在，不可以添加");
    }

    public Boolean getRoleName(String role) {
        if (role.contains(",") && !role.equals("normal,admin")) {
            return true;
        }
        return false;
    }
}
