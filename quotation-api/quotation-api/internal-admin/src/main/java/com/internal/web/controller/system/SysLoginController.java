package com.internal.web.controller.system;

import java.util.List;
import java.util.Set;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.internal.common.constant.Constants;
import com.internal.common.core.domain.AjaxResult;
import com.internal.common.core.domain.entity.SysMenu;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.core.domain.model.LoginBody;
import com.internal.common.utils.SecurityUtils;
import com.internal.framework.web.service.SysLoginService;
import com.internal.framework.web.service.SysPermissionService;
import com.internal.system.service.ISysMenuService;

/**
 * 登录验证
 * 
 * @author every
 */
@RestController
@Api(value = "SysLoginController", tags = "登录验证")
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @ApiOperation(value = "登录方法", notes = "登录方法")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        System.out.println(SecurityUtils.encryptPassword(loginBody.getPassword()));
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @ApiOperation(value = "路由信息", notes = "路由信息")
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId,null);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @ApiOperation(value = "路由信息", notes = "路由信息")
    @GetMapping("getRouters/category")
    public AjaxResult getRouters(@RequestParam("category") String category)
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId,category);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
