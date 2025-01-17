package com.internal.manager.service.Impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.enums.ProductStatusEnum;
import com.internal.common.enums.SoftCategoryEnum;
import com.internal.common.enums.SoftOptionsQueryFromEnum;
import com.internal.common.utils.MapUtil;
import com.internal.manager.domain.ManageProduct;
import com.internal.manager.dto.ManageSoftOptionsDTO;
import com.internal.manager.mapper.ManageProductMapper;
import com.internal.manager.vo.ManageSoftVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.manager.dto.ManageSoftQueryDTO;
import com.internal.manager.dto.ManageSoftSaveDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.manager.mapper.ManageSoftMapper;
import com.internal.manager.domain.ManageSoft;
import com.internal.manager.service.IManageSoftService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理系统-软著管理Service业务层处理
 * 
 * @author internal
 * @date 2024-12-11
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ManageSoftServiceImpl extends ServiceImpl<ManageSoftMapper, ManageSoft> implements IManageSoftService
{

    private final ManageSoftMapper manageSoftMapper;
    private final ManageProductMapper productMapper;

    /**
     * 查询管理系统-软著管理
     * 
     * @param id 管理系统-软著管理主键
     * @return 管理系统-软著管理
     */
    @Override
    public ManageSoft selectManageSoftById(Long id)
    {
        return manageSoftMapper.selectById(id);
    }

    /**
     * 查询管理系统-软著管理列表
     * 
     * @param manageSoft 管理系统-软著管理
     * @return 管理系统-软著管理
     */
    @Override
    public List<ManageSoftVO> selectManageSoftList(ManageSoftQueryDTO manageSoft)
    {
        List<ManageSoftVO> result = manageSoftMapper.selectManageSoftList(manageSoft);
        if(CollUtil.isEmpty(result)){
            return new LinkedList<>();
        }
        Map<Long,ManageProduct> productMap = null;
        Set<Long> productIdList = result.stream().filter(x -> ObjectUtil.isNotEmpty(x.getProductId())).map(x ->x.getProductId()).collect(Collectors.toSet());
        if(CollUtil.isNotEmpty(productIdList)){
            productMap = productMapper.selectList(Wrappers.<ManageProduct>lambdaQuery()
                    .in(ManageProduct::getId,productIdList)).stream()
                    .collect(Collectors.toMap(ManageProduct::getId,x -> x));
        }

        Map<Long, ManageProduct> finalProductMap = productMap;
        result.forEach(x ->{
            //关联主产品信息
            ManageProduct product = MapUtil.getMapValue(finalProductMap,x.getProductId());
            if(ObjectUtil.isNotEmpty(product)){
                x.setProductName(product.getName());
                if(ProductStatusEnum.DISABLE.getCode().equals(product.getStatus())){
                    x.setProductNameAndStatus(product.getName() + "(" + ProductStatusEnum.DISABLE.getInfo() + ")");
                }else {
                    x.setProductNameAndStatus(product.getName());
                }
            }

        });

        return result;
    }

    @Override
    public List<ManageSoftVO> getOptions(ManageSoftOptionsDTO dto) {
        List<ManageSoft> manageSoftList = new LinkedList<>();
        if(SoftOptionsQueryFromEnum.PRODUCT.getCode().equals(dto.getFrom())){
            manageSoftList = baseMapper.selectList(Wrappers.<ManageSoft>lambdaQuery()
                    .isNull(ManageSoft::getProductId)
                    .eq(ManageSoft::getCategory, dto.getCategory())
            );
        }
        else if (SoftOptionsQueryFromEnum.SOFT.getCode().equals(dto.getFrom())) {
            manageSoftList = baseMapper.selectList(Wrappers.<ManageSoft>lambdaQuery()
                    .isNull(ManageSoft::getLinkId)
                    .eq(ManageSoft::getCategory, dto.getCategory())
            );
        }
        else {
            throw new RuntimeException("无效的参数");
        }

        if(CollUtil.isEmpty(manageSoftList)){
            return new LinkedList<>();
        }
        return BeanUtil.copyToList(manageSoftList, ManageSoftVO.class);
    }
    /**
     * 新增管理系统-软著管理
     * 
     * @param saveVO 管理系统-软著管理
     * @return 结果
     */
    @Override
    public Boolean insertManageSoft(ManageSoftSaveDTO saveVO)
    {
        saveVO.setCreateTime(DateUtils.getNowDate());
        if(ObjectUtil.isNotEmpty(saveVO.getLinkId())){
            ManageSoft link = baseMapper.selectById(saveVO.getLinkId());
            if(ObjectUtil.isEmpty(link)){
                throw new RuntimeException("无效的关联软著");
            }else {
                if(saveVO.getCategory().equals(link.getCategory())){
                    throw new RuntimeException("相同类别的软著无法关联");
                }
            }
        }
        ManageSoft entity = BeanUtil.toBean(saveVO,ManageSoft.class);
        baseMapper.insert(entity);

        //绑定
        updateBind(entity);
        return true;
    }

    /**
     * 修改管理系统-软著管理
     * 
     * @param manageSoft 管理系统-软著管理
     * @return 结果
     */
    @Override
    public Boolean updateManageSoft(ManageSoft manageSoft)
    {
        if(ObjectUtil.isNotEmpty(manageSoft.getLinkId())){
            ManageSoft link = baseMapper.selectById(manageSoft.getLinkId());
            if(ObjectUtil.isEmpty(link)){
                throw new RuntimeException("无效的关联软著");
            }else {
                if(manageSoft.getCategory().equals(link.getCategory())){
                    throw new RuntimeException("相同类别的软著无法关联");
                }
            }
        }

        //清理旧绑定
        ManageSoft oldEntity = baseMapper.selectById(manageSoft.getId());
        if(ObjectUtil.isEmpty(oldEntity)){
            throw new RuntimeException("无效的软著");
        }

        clearBind(oldEntity);
        updateBind(manageSoft);

        manageSoft.setUpdateTime(DateUtils.getNowDate());
        manageSoftMapper.updateById(manageSoft);

        return true;
    }

    private void updateBind(ManageSoft entity){
        //重新绑定新的
        Boolean hasLink = ObjectUtil.isNotEmpty(entity.getLinkId());
        Boolean hasProduct = ObjectUtil.isNotEmpty(entity.getProductId());
        if(hasLink && !hasProduct){
            baseMapper.update(null,Wrappers.<ManageSoft>lambdaUpdate()
                    .eq(ManageSoft::getId,entity.getLinkId())
                    .set(ManageSoft::getLinkId,entity.getId())
            );
        }
        else if(!hasLink && hasProduct){
            if(SoftCategoryEnum.NL.getCode().equals(entity.getCategory())){
                productMapper.update(null,Wrappers.<ManageProduct>lambdaUpdate()
                        .eq(ManageProduct::getId,entity.getProductId())
                        .set(ManageProduct::getNlSoftId,entity.getId())
                        .set(ManageProduct::getXxSoftId,null)
                );
            }else {
                productMapper.update(null,Wrappers.<ManageProduct>lambdaUpdate()
                        .eq(ManageProduct::getId,entity.getProductId())
                        .set(ManageProduct::getNlSoftId,null)
                        .set(ManageProduct::getXxSoftId,entity.getId())
                );
            }
        }
        else if(hasLink && hasProduct)
        {
            baseMapper.update(null,Wrappers.<ManageSoft>lambdaUpdate()
                    .eq(ManageSoft::getId,entity.getLinkId())
                    .set(ManageSoft::getLinkId,entity.getId())
                    .set(ManageSoft::getProductId,entity.getProductId())
            );
            if(SoftCategoryEnum.NL.getCode().equals(entity.getCategory())){
                productMapper.update(null,Wrappers.<ManageProduct>lambdaUpdate()
                        .eq(ManageProduct::getId,entity.getProductId())
                        .set(ManageProduct::getNlSoftId,entity.getId())
                        .set(ManageProduct::getXxSoftId,entity.getLinkId())
                );
            }else {
                productMapper.update(null,Wrappers.<ManageProduct>lambdaUpdate()
                        .eq(ManageProduct::getId,entity.getProductId())
                        .set(ManageProduct::getNlSoftId,entity.getLinkId())
                        .set(ManageProduct::getXxSoftId,entity.getId())
                );
            }
        }
    }

    private void clearBind(ManageSoft entity)
    {
        //清理link
        if(ObjectUtil.isNotEmpty(entity.getLinkId())){
            baseMapper.update(null,Wrappers.<ManageSoft>lambdaUpdate()
                    .eq(ManageSoft::getId,entity.getLinkId())
                    .set(ManageSoft::getLinkId,null)
                    .set(ManageSoft::getProductId,null)
            );
        }
        //清理product
        if(ObjectUtil.isNotEmpty(entity.getProductId())){
            productMapper.update(null,Wrappers.<ManageProduct>lambdaUpdate()
                    .eq(ManageProduct::getId,entity.getProductId())
                    .set(ManageProduct::getNlSoftId,null)
                    .set(ManageProduct::getXxSoftId,null)
            );
        }
        //清理自己
        baseMapper.update(null,Wrappers.<ManageSoft>lambdaUpdate()
                .eq(ManageSoft::getId,entity.getId())
                .set(ManageSoft::getLinkId,null)
                .set(ManageSoft::getProductId,null)
        );
    }

    /**
     * 批量删除管理系统-软著管理
     * 
     * @param ids 需要删除的管理系统-软著管理主键
     * @return 结果
     */
    @Override
    public int deleteManageSoftByIds(Long[] ids)
    {
        return manageSoftMapper.deleteManageSoftByIds(ids);
    }

    /**
     * 删除管理系统-软著管理信息
     * 
     * @param id 管理系统-软著管理主键
     * @return 结果
     */
    @Override
    public int deleteManageSoftById(Long id)
    {
        return manageSoftMapper.deleteManageSoftById(id);
    }

}
