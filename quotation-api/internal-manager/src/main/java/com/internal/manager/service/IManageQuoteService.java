package com.internal.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.manager.domain.ManageQuote;

import java.util.List;
import java.util.Map;

/**
 * 管理系统-报价设置Service接口
 * 
 * @author internal
 * @date 2024-10-09
 */
public interface IManageQuoteService extends IService<ManageQuote>
{
    /**
     * 查询管理系统-报价设置
     * 
     * @param id 管理系统-报价设置主键
     * @return 管理系统-报价设置
     */
    public ManageQuote selectManageQuoteById(Long id);

    /**
     * 查询管理系统-报价设置列表
     * 
     * @param manageQuote 管理系统-报价设置
     * @return 管理系统-报价设置集合
     */
    public List<ManageQuote> selectManageQuoteList(ManageQuote manageQuote);

    /**
     * 新增管理系统-报价设置
     * 
     * @param manageQuote 管理系统-报价设置
     * @return 结果
     */
    public int insertManageQuote(ManageQuote manageQuote);

    /**
     * 修改管理系统-报价设置
     * 
     * @param manageQuote 管理系统-报价设置
     * @return 结果
     */
    public int updateManageQuote(ManageQuote manageQuote);

    /**
     * 批量删除管理系统-报价设置
     * 
     * @param ids 需要删除的管理系统-报价设置主键集合
     * @return 结果
     */
    public int deleteManageQuoteByIds(Long[] ids);

    /**
     * 删除管理系统-报价设置信息
     * 
     * @param id 管理系统-报价设置主键
     * @return 结果
     */
    public int deleteManageQuoteById(Long id);

    Map<String,ManageQuote> getManageQuoteMap();
}
