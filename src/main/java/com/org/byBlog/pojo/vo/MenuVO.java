package com.org.byBlog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.org.byBlog.dao.MenuListDAO;
import com.org.byBlog.pojo.dto.MenuDTO;
import com.org.byBlog.pojo.po.MenuListPO;
import com.org.byBlog.service.MenuService;
import com.org.byBlog.utils.ApplicationContextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ApiModel("菜单")
public class MenuVO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("图标")
    private String icon;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("父级")
    private String parent;

    @ApiModelProperty("状态")
    private Boolean status;

    @ApiModelProperty("权限")
    private String role;

    @ApiModelProperty("子级菜单")
    private List<MenuVO> childMenuList;

    @ApiModelProperty("是否为父级")
    private Boolean menuIsParent = true;

    public static MenuVO fromPO(MenuListPO menuListPO) {
        MenuListDAO menuListDAO = ApplicationContextUtils.getBean(MenuListDAO.class);

        MenuVO menuVO = new MenuVO();
        if (menuListPO.getParent() > MenuService.PARENT){
            MenuListPO menu = menuListDAO.selectByPrimaryKey(menuListPO.getParent());
            menuVO.setParent(menu.getName());
            menuVO.setMenuIsParent(false);
        }

        BeanUtils.copyProperties(menuListPO, menuVO);

        return menuVO;
    }

    public static MenuVO fromListVO(MenuListPO menu) {
        MenuListDAO menuListDAO = ApplicationContextUtils.getBean(MenuListDAO.class);

        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menu, menuVO);
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setPagination(false);
        menuDTO.setParentId(menu.getId());
        List<MenuListPO> menuList = menuListDAO.getMenuList(menuDTO);
        List<MenuVO> menuVOList = menuList.stream().map(menuListPO -> MenuVO.fromPO(menuListPO)).collect(Collectors.toList());
        menuVO.setChildMenuList(menuVOList);

        return menuVO;
    }
}
