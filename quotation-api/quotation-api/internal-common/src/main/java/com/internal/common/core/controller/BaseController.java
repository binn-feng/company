package com.internal.common.core.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import com.internal.common.request.PageParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.internal.common.constant.HttpStatus;
import com.internal.common.core.domain.AjaxResult;
import com.internal.common.core.domain.model.LoginUser;
import com.internal.common.core.page.PageDomain;
import com.internal.common.core.page.TableDataInfo;
import com.internal.common.core.page.TableSupport;
import com.internal.common.utils.DateUtils;
import com.internal.common.utils.PageUtils;
import com.internal.common.utils.SecurityUtils;
import com.internal.common.utils.StringUtils;
import com.internal.common.utils.sql.SqlUtil;

/**
 * web层通用数据处理
 * 
 * @author every
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageUtils.startPage();
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage(Integer pageNumber, Integer pageSize)
    {
        PageUtils.startPageNew(pageNumber,pageSize);
    }

    protected <T> void startPage(PageParams<T> params) {
        if (ObjectUtil.isNotEmpty(params)) {
            String sort = params.getSort();
            String order = params.getOrder();

            if (StringUtils.isEmpty(sort)) {
                sort = "createTime"; // 默认排序字段
            }
            if (StringUtils.isEmpty(order)) {
                order = "descending"; // 默认排序规则
            }

            // 使用下划线格式化排序字段
            String formattedSort = StringUtils.toUnderScoreCase(sort) + " " + isAscFormat(order);

            // 调用分页方法
            PageUtils.startPageNew(params.getCurrent(), params.getSize(), formattedSort);
        } else {
            throw new IllegalArgumentException("PageParams cannot be null");
        }
    }
    /**
     * 设置请求排序数据
     */
    protected void startOrderBy()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy()))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage()
    {
        PageUtils.clearPage();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T> TableDataInfo <T> getDataTable(T list)
    {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo((List) list).getTotal());
        return rspData;
    }

    /**
     * 返回成功
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }
    
    /**
     * 返回成功消息
     */
    public AjaxResult success(Object data)
    {
        return AjaxResult.success(data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     * 返回警告消息
     */
    public AjaxResult warn(String message)
    {
        return AjaxResult.warn(message);
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     * 
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result)
    {
        return result ? success() : error();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url)
    {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser()
    {
        return SecurityUtils.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    public Long getUserId()
    {
        return getLoginUser().getUserId();
    }

    /**
     * 获取登录部门id
     */
    public Long getDeptId()
    {
        return getLoginUser().getDeptId();
    }

    /**
     * 获取登录用户名
     */
    public String getUsername()
    {
        return getLoginUser().getUsername();
    }

    public String isAscFormat(String isAsc)
    {
        if (StringUtils.isNotEmpty(isAsc))
        {
            // 兼容前端排序类型
            if ("ascending".equals(isAsc))
            {
                isAsc = "asc";
            }
            else if ("descending".equals(isAsc))
            {
                isAsc = "desc";
            }
            return isAsc;
        }
        return null;
    }
}
