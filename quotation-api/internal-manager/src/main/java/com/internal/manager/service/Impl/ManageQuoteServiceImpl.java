package com.internal.manager.service.Impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.enums.RealQuoteType;
import com.internal.common.utils.DateUtils;
import com.internal.common.utils.MapUtil;
import com.internal.manager.domain.ManageQuote;
import com.internal.manager.mapper.ManageQuoteMapper;
import com.internal.manager.service.IManageQuoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * 管理系统-报价设置Service业务层处理
 * 
 * @author internal
 * @date 2024-10-09
 */
@Service
@AllArgsConstructor
public class ManageQuoteServiceImpl extends ServiceImpl<ManageQuoteMapper, ManageQuote> implements IManageQuoteService
{

    private final ManageQuoteMapper manageQuoteMapper;

    /**
     * 查询管理系统-报价设置
     * 
     * @param id 管理系统-报价设置主键
     * @return 管理系统-报价设置
     */
    @Override
    public ManageQuote selectManageQuoteById(Long id)
    {
        ManageQuote result =  manageQuoteMapper.selectManageQuoteById(id);
        result.setDutyRate(result.getDutyRate().multiply(BigDecimal.valueOf(100)));
        result.setProfitability(result.getProfitability().multiply(BigDecimal.valueOf(100)));
        return result;
    }

    /**
     * 查询管理系统-报价设置列表
     * 
     * @param manageQuote 管理系统-报价设置
     * @return 管理系统-报价设置
     */
    @Override
    public List<ManageQuote> selectManageQuoteList(ManageQuote manageQuote)
    {
        List<ManageQuote> list = manageQuoteMapper.selectManageQuoteList(manageQuote);
        list.forEach(x ->
        {
            x.setDutyRate(x.getDutyRate().multiply(BigDecimal.valueOf(100)));
            x.setProfitability(x.getProfitability().multiply(BigDecimal.valueOf(100)));
        });
        return list;
    }

    /**
     * 新增管理系统-报价设置
     * 
     * @param manageQuote 管理系统-报价设置
     * @return 结果
     */
    @Override
    public int insertManageQuote(ManageQuote manageQuote)
    {
        manageQuote.setDutyRate(manageQuote.getDutyRate().divide(BigDecimal.valueOf(100)));
        manageQuote.setProfitability(manageQuote.getProfitability().divide(BigDecimal.valueOf(100)));
        manageQuote.setCreateTime(DateUtils.getNowDate());
        return manageQuoteMapper.insert(manageQuote);
    }

    /**
     * 修改管理系统-报价设置
     * 
     * @param manageQuote 管理系统-报价设置
     * @return 结果
     */
    @Override
    public int updateManageQuote(ManageQuote manageQuote)
    {
        manageQuote.setDutyRate(manageQuote.getDutyRate().divide(BigDecimal.valueOf(100)));
        manageQuote.setProfitability(manageQuote.getProfitability().divide(BigDecimal.valueOf(100)));
        manageQuote.setUpdateTime(DateUtils.getNowDate());
        return manageQuoteMapper.updateById(manageQuote);
    }

    /**
     * 批量删除管理系统-报价设置
     * 
     * @param ids 需要删除的管理系统-报价设置主键
     * @return 结果
     */
    @Override
    public int deleteManageQuoteByIds(Long[] ids)
    {
        return manageQuoteMapper.deleteManageQuoteByIds(ids);
    }

    /**
     * 删除管理系统-报价设置信息
     * 
     * @param id 管理系统-报价设置主键
     * @return 结果
     */
    @Override
    public int deleteManageQuoteById(Long id)
    {
        return manageQuoteMapper.deleteManageQuoteById(id);
    }

    @Override
    public Map<String,ManageQuote> getManageQuoteMap() {
        Map<String,ManageQuote> quoteMap = baseMapper.selectList(Wrappers.<ManageQuote>lambdaQuery())
                .stream().collect(Collectors.toMap(x -> x.getType(),x -> x));
        return quoteMap;
    }
}
