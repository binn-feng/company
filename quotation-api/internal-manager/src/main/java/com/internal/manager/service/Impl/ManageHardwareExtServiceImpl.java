package com.internal.manager.service.Impl;

import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.enums.ProductStatusEnum;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.manager.domain.ManageHardwareExt;
import com.internal.manager.domain.ManageProduct;
import com.internal.manager.mapper.ManageHardwareExtMapper;
import com.internal.manager.service.IManageHardwareExtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 管理系统-硬件外采Service业务层处理
 * 
 * @author internal
 * @date 2024-12-19
 */
@Service
@AllArgsConstructor
public class ManageHardwareExtServiceImpl extends ServiceImpl<ManageHardwareExtMapper, ManageHardwareExt> implements IManageHardwareExtService
{

    private final ManageHardwareExtMapper manageHardwareExtMapper;

    /**
     * 查询管理系统-硬件外采
     * 
     * @param id 管理系统-硬件外采主键
     * @return 管理系统-硬件外采
     */
    @Override
    public ManageHardwareExt selectManageHardwareExtById(Long id)
    {
        return manageHardwareExtMapper.selectManageHardwareExtById(id);
    }

    /**
     * 查询管理系统-硬件外采列表
     * 
     * @param manageHardwareExt 管理系统-硬件外采
     * @return 管理系统-硬件外采
     */
    @Override
    public List<ManageHardwareExt> selectManageHardwareExtList(ManageHardwareExt manageHardwareExt)
    {
        return manageHardwareExtMapper.selectManageHardwareExtList(manageHardwareExt);
    }

    /**
     * 新增管理系统-硬件外采
     * 
     * @param manageHardwareExt 管理系统-硬件外采
     * @return 结果
     */
    @Override
    public int insertManageHardwareExt(ManageHardwareExt manageHardwareExt)
    {
        manageHardwareExt.setCreateTime(DateUtils.getNowDate());
        return manageHardwareExtMapper.insert(manageHardwareExt);
    }

    /**
     * 修改管理系统-硬件外采
     * 
     * @param manageHardwareExt 管理系统-硬件外采
     * @return 结果
     */
    @Override
    public int updateManageHardwareExt(ManageHardwareExt manageHardwareExt)
    {
        manageHardwareExt.setUpdateTime(DateUtils.getNowDate());
        return manageHardwareExtMapper.updateById(manageHardwareExt);
    }

    /**
     * 批量删除管理系统-硬件外采
     * 
     * @param ids 需要删除的管理系统-硬件外采主键
     * @return 结果
     */
    @Override
    public int deleteManageHardwareExtByIds(Long[] ids)
    {
        return manageHardwareExtMapper.deleteManageHardwareExtByIds(ids);
    }

    /**
     * 删除管理系统-硬件外采信息
     * 
     * @param id 管理系统-硬件外采主键
     * @return 结果
     */
    @Override
    public int deleteManageHardwareExtById(Long id)
    {
        return manageHardwareExtMapper.deleteManageHardwareExtById(id);
    }

    @Override
    public Boolean publish(ManageHardwareExt ext) {
        baseMapper.update(null, Wrappers.<ManageHardwareExt>lambdaUpdate()
                .eq(ManageHardwareExt::getId,ext.getId())
                .set(ManageHardwareExt::getStatus, ProductStatusEnum.ENABLE.getCode())
        );
        return true;
    }

    @Override
    public Boolean unPublish(ManageHardwareExt ext) {
        baseMapper.update(null, Wrappers.<ManageHardwareExt>lambdaUpdate()
                .eq(ManageHardwareExt::getId,ext.getId())
                .set(ManageHardwareExt::getStatus, ProductStatusEnum.DISABLE.getCode())
        );
        return true;
    }
}
