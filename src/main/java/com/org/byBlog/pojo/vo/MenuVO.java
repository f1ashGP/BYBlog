package com.org.byBlog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.org.byBlog.dao.MenuListDAO;
import com.org.byBlog.pojo.po.MenuListPO;
import com.org.byBlog.utils.ApplicationContextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

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

    @ApiModelProperty("子级菜单")
    private List<MenuVO> childMenuList;

    public static MenuVO fromPO(MenuListPO menuListPO) {
        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menuListPO, menuVO);

        return menuVO;
    }

    public static MenuVO fromListVO(MenuListPO menuListPO) {
        MenuListDAO menuListDAO = ApplicationContextUtils.getBean(MenuListDAO.class);

        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menuListPO, menuVO);
        if (menuListPO.getParent() > 0) {
            MenuListPO parentMenu = menuListDAO.selectByPrimaryKey(menuListPO.getParent());
            menuVO.setParent(parentMenu.getName());
        }

        return menuVO;
    }
}
