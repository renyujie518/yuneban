package com.renyujie.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.renyujie.server.pojo.Menu;
import com.renyujie.server.pojo.MenuRole;
import com.renyujie.server.pojo.RespBean;
import com.renyujie.server.pojo.Role;
import com.renyujie.server.service.IMenuRoleService;
import com.renyujie.server.service.IMenuService;
import com.renyujie.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName PermissionController.java
 * @Description role和menu的权限组
 * @createTime 2021年12月26日 16:21:00
 */
@RestController
@RequestMapping("/system/basic/permission")
public class PermissionController {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;


    /**
     * @Description: 角色
     */
    @ApiOperation(value = "查询所有角色")
    @GetMapping("/")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role){
        //由于t_role表中的name都是诸如ROLE_manager，这个开头之前设置过是被security管控的，所以这里要确保这一点
        if (!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }
        if (roleService.save(role)){
            return RespBean.success("添加角色成功");
        }
        return RespBean.error("添加角色失败");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    public RespBean deleteRole(@PathVariable Integer rid){
        if (roleService.removeById(rid)){
            return RespBean.success("删除角色成功");
        }
        return RespBean.error("删除角色失败");
    }



    /**
     * @Description: 菜单
     */
    @ApiOperation(value = "获取所有菜单")
    @GetMapping("/menus")
    private List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色ID查询所属的菜单ID")
    @GetMapping("/mid/{rid}")
    private List<Integer> getMidByRid(@PathVariable Integer rid){
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid",rid)).stream()
                .map(MenuRole::getMid).collect(Collectors.toList());
    }

    @ApiOperation(value = "更新角色下所属的菜单")
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid,Integer[] mids){
        return menuRoleService.updateMenuRole(rid,mids);
    }

}
