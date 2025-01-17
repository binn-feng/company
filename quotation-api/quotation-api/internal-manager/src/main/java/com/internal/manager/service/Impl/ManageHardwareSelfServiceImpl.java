package com.internal.manager.service.Impl;

import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.enums.ProductStatusEnum;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.manager.domain.ManageHardwareExt;
import com.internal.manager.domain.ManageHardwareSelf;
import com.internal.manager.mapper.ManageHardwareSelfMapper;
import com.internal.manager.service.IManageHardwareSelfService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 管理系统-硬件自研Service业务层处理
 * 
 * @author internal
 * @date 2024-12-19
 */
@Service
@AllArgsConstructor
public class ManageHardwareSelfServiceImpl extends ServiceImpl<ManageHardwareSelfMapper, ManageHardwareSelf> implements IManageHardwareSelfService
{

    private final ManageHardwareSelfMapper manageHardwareSelfMapper;

    /**
     * 查询管理系统-硬件自研
     * 
     * @param id 管理系统-硬件自研主键
     * @return 管理系统-硬件自研
     */
    @Override
    public ManageHardwareSelf selectManageHardwareSelfById(Long id)
    {
        return manageHardwareSelfMapper.selectManageHardwareSelfById(id);
    }

    /**
     * 查询管理系统-硬件自研列表
     * 
     * @param manageHardwareSelf 管理系统-硬件自研
     * @return 管理系统-硬件自研
     */
    @Override
    public List<ManageHardwareSelf> selectManageHardwareSelfList(ManageHardwareSelf manageHardwareSelf)
    {
        return manageHardwareSelfMapper.selectManageHardwareSelfList(manageHardwareSelf);
    }

    /**
     * 新增管理系统-硬件自研
     * 
     * @param manageHardwareSelf 管理系统-硬件自研
     * @return 结果
     */
    @Override
    public int insertManageHardwareSelf(ManageHardwareSelf manageHardwareSelf)
    {
        manageHardwareSelf.setCreateTime(DateUtils.getNowDate());
        return manageHardwareSelfMapper.insert(manageHardwareSelf);
    }

    /**
     * 修改管理系统-硬件自研
     * 
     * @param manageHardwareSelf 管理系统-硬件自研
     * @return 结果
     */
    @Override
    public int updateManageHardwareSelf(ManageHardwareSelf manageHardwareSelf)
    {
        manageHardwareSelf.setUpdateTime(DateUtils.getNowDate());
        return manageHardwareSelfMapper.updateById(manageHardwareSelf);
    }

    /**
     * 批量删除管理系统-硬件自研
     * 
     * @param ids 需要删除的管理系统-硬件自研主键
     * @return 结果
     */
    @Override
    public int deleteManageHardwareSelfByIds(Long[] ids)
    {
        return manageHardwareSelfMapper.deleteManageHardwareSelfByIds(ids);
    }

    /**
     * 删除管理系统-硬件自研信息
     * 
     * @param id 管理系统-硬件自研主键
     * @return 结果
     */
    @Override
    public int deleteManageHardwareSelfById(Long id)
    {
        return manageHardwareSelfMapper.deleteManageHardwareSelfById(id);
    }

    @Override
    public Boolean publish(ManageHardwareSelf self) {
        baseMapper.update(null, Wrappers.<ManageHardwareSelf>lambdaUpdate()
                .eq(ManageHardwareSelf::getId,self.getId())
                .set(ManageHardwareSelf::getStatus, ProductStatusEnum.ENABLE.getCode())
        );
        return true;
    }

    @Override
    public Boolean unPublish(ManageHardwareSelf self) {
        baseMapper.update(null, Wrappers.<ManageHardwareSelf>lambdaUpdate()
                .eq(ManageHardwareSelf::getId,self.getId())
                .set(ManageHardwareSelf::getStatus, ProductStatusEnum.DISABLE.getCode())
        );
        return true;
    }
}
