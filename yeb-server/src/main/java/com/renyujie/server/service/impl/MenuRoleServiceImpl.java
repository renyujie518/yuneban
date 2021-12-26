package com.renyujie.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renyujie.server.mapper.MenuRoleMapper;
import com.renyujie.server.pojo.MenuRole;
import com.renyujie.server.pojo.RespBean;
import com.renyujie.server.service.IMenuRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-20
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {
    @Resource
    private MenuRoleMapper menuRoleMapper;
    /**
     * @Description: 更新角色下所属的菜单
     * 按照原本方法，因为角色下有多菜单  对其中的某个菜单做更新，需要先对菜单的权限做判断 菜单一旦多起来就很繁琐
     * 所以跟新之前 可以先把该角色下的所有菜单先清空，然后传参里又有新的mids[],再把这个有新的mids[]与rid关联起来
     * 这样两步形成了更新
     *
     */
    @Override
    @Transactional
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        //首先根据传进来rid删除该角色下的所有菜单（只要删除t_menu_role这个关联表中对应的行即可）
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        //考虑这么一种情况  前端就仅仅是删除操作 这时候mids[]是空的（因为只有添加的时候才需要前端输入对应的mids[]）
        //这时候也要允许单单删除的能力
        if (mids == null || mids.length == 0) {
            return RespBean.success("更新成功");
        }
        //第二步就是将前端传来的新的mids[]一一与rid这个角色关联起来（实际上就是t_menu_role添加对印关系）
        Integer result = menuRoleMapper.insertMenus2Role(rid, mids);
        //返回的操作数=所有的mids个数
        if (mids.length == result){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");

    }
}
