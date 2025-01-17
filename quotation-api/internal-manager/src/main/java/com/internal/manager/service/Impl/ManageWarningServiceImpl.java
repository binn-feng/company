package com.internal.manager.service.Impl;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.utils.DateUtils;
import com.internal.manager.domain.ManageWarning;
import com.internal.manager.mapper.ManageWarningMapper;
import com.internal.manager.service.IManageWarningService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 管理系统-预警设置Service业务层处理
 * 
 * @author internal
 * @date 2024-10-09
 */
@Service
@AllArgsConstructor
public class ManageWarningServiceImpl extends ServiceImpl<ManageWarningMapper, ManageWarning> implements IManageWarningService
{

    private final ManageWarningMapper manageWarningMapper;

    /**
     * 查询管理系统-预警设置
     * 
     * @param id 管理系统-预警设置主键
     * @return 管理系统-预警设置
     */
    @Override
    public ManageWarning selectManageWarningById(Long id)
    {
        ManageWarning result = manageWarningMapper.selectManageWarningById(id);
        result.setContractProfit(result.getContractProfit().multiply(BigDecimal.valueOf(100)));
        result.setExternalProfit(result.getExternalProfit().multiply(BigDecimal.valueOf(100)));
        return result;
    }

    /**
     * 查询管理系统-预警设置列表
     * 
     * @param manageWarning 管理系统-预警设置
     * @return 管理系统-预警设置
     */
    @Override
    public List<ManageWarning> selectManageWarningList(ManageWarning manageWarning)
    {
        List<ManageWarning> list = manageWarningMapper.selectManageWarningList(manageWarning);
        list.forEach(x -> {
            x.setContractProfit(x.getContractProfit().multiply(BigDecimal.valueOf(100)));
            x.setExternalProfit(x.getExternalProfit().multiply(BigDecimal.valueOf(100)));
        });
        return list;
    }

    /**
     * 新增管理系统-预警设置
     * 
     * @param manageWarning 管理系统-预警设置
     * @return 结果
     */
    @Override
    public int insertManageWarning(ManageWarning manageWarning)
    {
        manageWarning.setContractProfit(manageWarning.getContractProfit().divide(BigDecimal.valueOf(100)));
        manageWarning.setExternalProfit(manageWarning.getExternalProfit().divide(BigDecimal.valueOf(100)));
        manageWarning.setCreateTime(DateUtils.getNowDate());
        return manageWarningMapper.insert(manageWarning);
    }

    /**
     * 修改管理系统-预警设置
     * 
     * @param manageWarning 管理系统-预警设置
     * @return 结果
     */
    @Override
    public int updateManageWarning(ManageWarning manageWarning)
    {
        manageWarning.setContractProfit(manageWarning.getContractProfit().divide(BigDecimal.valueOf(100)));
        manageWarning.setExternalProfit(manageWarning.getExternalProfit().divide(BigDecimal.valueOf(100)));
        manageWarning.setUpdateTime(DateUtils.getNowDate());
        return manageWarningMapper.updateById(manageWarning);
    }

    /**
     * 批量删除管理系统-预警设置
     * 
     * @param ids 需要删除的管理系统-预警设置主键
     * @return 结果
     */
    @Override
    public int deleteManageWarningByIds(Long[] ids)
    {
        return manageWarningMapper.deleteManageWarningByIds(ids);
    }

    /**
     * 删除管理系统-预警设置信息
     * 
     * @param id 管理系统-预警设置主键
     * @return 结果
     */
    @Override
    public int deleteManageWarningById(Long id)
    {
        return manageWarningMapper.deleteManageWarningById(id);
    }

    @Override
    public ManageWarning getProfit() {
        ManageWarning result = baseMapper.selectList(Wrappers.<ManageWarning>lambdaQuery()).stream().findFirst().get();
        return result;
    }
}
