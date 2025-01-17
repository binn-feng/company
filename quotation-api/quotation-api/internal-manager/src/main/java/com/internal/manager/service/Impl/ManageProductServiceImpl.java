package com.internal.manager.service.Impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.enums.ProductCategoryEnum;
import com.internal.common.enums.ProductStatusEnum;
import com.internal.common.utils.DateUtils;
import com.internal.common.utils.MapUtil;
import com.internal.manager.domain.ManageProduct;
import com.internal.manager.domain.ManageSoft;
import com.internal.manager.dto.ManageProductQueryDTO;
import com.internal.manager.mapper.ManageProductMapper;
import com.internal.manager.mapper.ManageSoftMapper;
import com.internal.manager.service.IManageProductService;
import com.internal.manager.vo.ManageProductVO;
import com.internal.manager.vo.ManageProductChildVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理系统-产品管理Service业务层处理
 * 
 * @author internal
 * @date 2024-10-11
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ManageProductServiceImpl extends ServiceImpl<ManageProductMapper, ManageProduct> implements IManageProductService
{

    private final ManageProductMapper manageProductMapper;
    private final ManageSoftMapper softMapper;

    /**
     * 查询管理系统-产品管理
     * 
     * @param id 管理系统-产品管理主键
     * @return 管理系统-产品管理
     */
    @Override
    public ManageProductVO selectManageProductById(Long id)
    {
        ManageProductVO result = manageProductMapper.selectManageProductById(id);
        setNameAndStatus(result);
        return result;
    }

    @Override
    public List<ManageProductVO> selectManageProductList(ManageProductQueryDTO queryDTO)
    {
        List<ManageProductVO> result = manageProductMapper.selectManageProductList(queryDTO);
        if(CollUtil.isEmpty(result)){
            return new LinkedList<>();
        }
        //软著map
        List<Long> xxSoftIdList = result.stream().map(x -> x.getXxSoftId()).collect(Collectors.toList());
        List<Long> nlSoftIdList = result.stream().map(x -> x.getNlSoftId()).collect(Collectors.toList());
        HashSet<Long> softIdList = new HashSet<>();
        softIdList.addAll(xxSoftIdList);
        softIdList.addAll(nlSoftIdList);
        Map<Long, ManageSoft> softMap = new HashMap<>();

        if(CollUtil.isNotEmpty(softIdList)){
            softMap = softMapper.selectList(Wrappers.<ManageSoft>lambdaQuery()
                    .in(ManageSoft::getId,softIdList)).stream().collect(Collectors.toMap(ManageSoft::getId,x -> x));
        }
        Map<Long, ManageSoft> finalSoftMap = softMap;
        result.forEach(item ->{
            setNameAndStatus(item);

            //软著信息
            ManageSoft nlSoft = MapUtil.getMapValue(finalSoftMap,item.getNlSoftId());
            if(ObjectUtil.isNotEmpty(nlSoft)){
                item.setNlSoftName(nlSoft.getName());
            }
            ManageSoft xxSoft = MapUtil.getMapValue(finalSoftMap,item.getXxSoftId());
            if(ObjectUtil.isNotEmpty(xxSoft)){
                item.setXxSoftName(xxSoft.getName());
            }

        });
        return result;
    }

    @Override
    public List<ManageProductVO> getOptions(ManageProductQueryDTO dto) {
        List<ManageProduct> manageProductList = baseMapper.selectList(Wrappers.<ManageProduct>lambdaQuery()
                .isNull(ManageProduct::getXxSoftId)
                .isNull(ManageProduct::getNlSoftId)
                .eq(ManageProduct::getStatus,dto.getStatus())
                .eq(ManageProduct::getCategory,dto.getCategory()));
        if(CollUtil.isEmpty(manageProductList)){
            return new LinkedList<>();
        }
        List<ManageProductVO> result = BeanUtil.copyToList(manageProductList,ManageProductVO.class);
        result.forEach(item ->{
            setNameAndStatus(item);
        });
        return result;
    }

    @Override
    public Long selectManageProductCount(ManageProductQueryDTO queryDTO) {
        return manageProductMapper.selectManageProductCount(queryDTO);
    }

    @Override
    public List<ManageProductChildVO> selectProductChildrenList(ManageProductQueryDTO queryDTO) {
        List<ManageProductChildVO> result = new ArrayList<>();
        if(queryDTO.getIsAuth()){
            result.addAll(manageProductMapper.selectProductChildrenList(queryDTO));
        }else{
            result.addAll(manageProductMapper.selectProductChildrenListNoAuth(queryDTO));
        }
        result.forEach(item ->{
            setNameAndStatus(item);
            if(ProductStatusEnum.DISABLE.getCode().equals(item.getParentStatus())){
                if(ObjectUtil.isEmpty(item.getParentShortName())){
                    item.setParentNameAndStatus(item.getParentName()  + "(" + ProductStatusEnum.DISABLE.getInfo() + ")");
                }else {
                    item.setParentNameAndStatus(item.getParentName() + "-" + item.getParentShortName() + "(" + ProductStatusEnum.DISABLE.getInfo() + ")");
                }
            }
            else if(ProductStatusEnum.ENABLE.getCode().equals(item.getParentStatus())){
                if(ObjectUtil.isEmpty(item.getParentShortName())){
                    item.setParentNameAndStatus(item.getParentName());
                }
                else{
                    item.setParentNameAndStatus(item.getParentName() + "-" + item.getParentShortName());
                }
            }
        });
        return result;
    }

    /**
     * 名字+昵称+状态
     * @param item
     */
    private void setNameAndStatus(ManageProductVO item){
        if(ProductStatusEnum.DISABLE.getCode().equals(item.getStatus())){
            if(ObjectUtil.isEmpty(item.getShortName())){
                item.setNameAndStatus(item.getName() + "(" + ProductStatusEnum.DISABLE.getInfo() + ")");
            }else {
                item.setNameAndStatus(item.getName() + "-" + item.getShortName() + "(" + ProductStatusEnum.DISABLE.getInfo() + ")");
            }
        }
        else if(ProductStatusEnum.ENABLE.getCode().equals(item.getStatus())){
            if(ObjectUtil.isEmpty(item.getShortName())){
                item.setNameAndStatus(item.getName());
            }else {
                item.setNameAndStatus(item.getName()+ "-" + item.getShortName());
            }

        }
    }

    @Override
    public Long selectProductChildrenCount(ManageProductQueryDTO queryDTO) {
        return manageProductMapper.selectProductChildrenCount(queryDTO);
    }

    /**
     * 新增管理系统-产品管理
     * 
     * @param product 管理系统-产品管理
     * @return 结果
     */
    @Override
    public Boolean insertManageProduct(ManageProduct product)
    {
        product.setCreateTime(DateUtils.getNowDate());
        product.setUpdateTime(DateUtils.getNowDate());
        if (ProductCategoryEnum.FIRST.getCode().equals(product.getCategory()))
        {
            manageProductMapper.insert(product);
            ManageProduct child = BeanUtil.toBean(product,ManageProduct.class);
            child.setId(null);
            child.setCategory(ProductCategoryEnum.SECOND.getCode());
            child.setParentId(product.getId());
            baseMapper.insert(child);
        }
        else if(ProductCategoryEnum.SECOND.getCode().equals(product.getCategory()))
        {
            if(ObjectUtil.isNotEmpty(product.getParentId())){
                ManageProduct parent = baseMapper.selectById(product.getParentId());
                if(ObjectUtil.isEmpty(parent)){
                    throw new RuntimeException("无效的主产品");
                }
                if(ProductStatusEnum.DISABLE.getCode().equals(parent.getStatus())){
                    product.setStatus(ProductStatusEnum.DISABLE.getCode());
                }
            }
            manageProductMapper.insert(product);
        }
        else {
           throw new RuntimeException("无效的产品类别");
        }
        updateBind(product);

        return true;
    }

    /**
     * 修改管理系统-产品管理
     * 
     * @param product 管理系统-产品管理
     * @return 结果
     */
    @Override
    public int updateManageProduct(ManageProduct product)
    {
        ManageProduct oldProduct = baseMapper.selectById(product.getId());
        if(ObjectUtil.isEmpty(oldProduct)){
            return 0;
        }

        product.setUpdateTime(DateUtils.getNowDate());

        if (ProductCategoryEnum.FIRST.getCode().equals(product.getCategory())){
            //新状态是下架，那就把子的也下架。（如果主本来就是下架状态，那子产品一定全是被下架的，所以覆盖一次也没影响）
            if(ProductStatusEnum.DISABLE.getCode().equals(product.getStatus())){
                unPublish(product);
            }
            //新状态是上架，得分情况
            else if (ProductStatusEnum.ENABLE.getCode().equals(product.getStatus())) {
                //如果原状态是下架，那调用上架很合理。
                if(ProductStatusEnum.DISABLE.getCode().equals(oldProduct.getStatus())){
                    publish(product);
                }
                //如果原状态是上架，就不动了
                //因为父产品上架，子产品下架的情况理论上是有可能的，如果再覆盖一次就把子产品也上架了
            }
        }
        else if(ProductCategoryEnum.SECOND.getCode().equals(product.getCategory()))
        {
            if(ObjectUtil.isNotEmpty(product.getParentId())){
                ManageProduct parent = baseMapper.selectById(product.getParentId());
                if(ObjectUtil.isEmpty(parent)){
                    throw new RuntimeException("无效的主产品");
                }
                //如果主产品是下架状态，那子产品顺便也下架了，覆盖当前状态
                if(ProductStatusEnum.DISABLE.getCode().equals(parent.getStatus())){
                    product.setStatus(ProductStatusEnum.DISABLE.getCode());
                }
            }
        }
        else {
            throw new RuntimeException("无效的产品类别");
        }

        clearBind(oldProduct);
        updateBind(product);

        return manageProductMapper.updateById(product);
    }

    private void updateBind(ManageProduct product){
        Boolean hasNl = ObjectUtil.isNotEmpty(product.getNlSoftId());
        Boolean hasXx = ObjectUtil.isNotEmpty(product.getXxSoftId());

        if(hasNl){
            softMapper.update(null,Wrappers.<ManageSoft>lambdaUpdate()
                    .eq(ManageSoft::getId,product.getNlSoftId())
                    .set(ManageSoft::getProductId,product.getId()));
        }
        if(hasXx){
            softMapper.update(null,Wrappers.<ManageSoft>lambdaUpdate()
                    .eq(ManageSoft::getId,product.getXxSoftId())
                    .set(ManageSoft::getProductId,product.getId()));
        }
    }
    private void clearBind(ManageProduct product){
        Boolean hasNl = ObjectUtil.isNotEmpty(product.getNlSoftId());
        Boolean hasXx = ObjectUtil.isNotEmpty(product.getXxSoftId());

        //清理nl相关数据
        if(hasNl){
            softMapper.update(null,Wrappers.<ManageSoft>lambdaUpdate()
                    .eq(ManageSoft::getId,product.getNlSoftId())
                    .set(ManageSoft::getProductId,null));
        }
        //清理xx相关数据
        if(hasXx){
            softMapper.update(null,Wrappers.<ManageSoft>lambdaUpdate()
                    .eq(ManageSoft::getId,product.getXxSoftId())
                    .set(ManageSoft::getProductId,null));
        }
        //清理自己
        baseMapper.update(null,Wrappers.<ManageProduct>lambdaUpdate()
                .eq(ManageProduct::getId,product.getId())
                .set(ManageProduct::getXxSoftId,null)
                .set(ManageProduct::getNlSoftId,null)
        );
    }

    /**
     * 批量删除管理系统-产品管理
     * 
     * @param ids 需要删除的管理系统-产品管理主键
     * @return 结果
     */
    @Override
    public int deleteManageProductByIds(Long[] ids)
    {
        return manageProductMapper.deleteManageProductByIds(ids);
    }

    /**
     * 删除管理系统-产品管理信息
     * 
     * @param id 管理系统-产品管理主键
     * @return 结果
     */
    @Override
    public int deleteManageProductById(Long id)
    {
        return manageProductMapper.deleteManageProductById(id);
    }

    @Override
    public Boolean publish(ManageProduct product) {
        if (ProductCategoryEnum.FIRST.getCode().equals(product.getCategory())){
            baseMapper.update(null,Wrappers.<ManageProduct>lambdaUpdate()
                    .set(ManageProduct::getStatus,ProductStatusEnum.ENABLE.getCode())
                    .eq(ManageProduct::getParentId,product.getId()));
        }
        baseMapper.update(null,Wrappers.<ManageProduct>lambdaUpdate()
                .set(ManageProduct::getStatus,ProductStatusEnum.ENABLE.getCode())
                .eq(ManageProduct::getId,product.getId()));
        return true;
    }

    @Override
    public Boolean unPublish(ManageProduct product) {
        if (ProductCategoryEnum.FIRST.getCode().equals(product.getCategory())){
            baseMapper.update(null,Wrappers.<ManageProduct>lambdaUpdate()
                    .set(ManageProduct::getStatus,ProductStatusEnum.DISABLE.getCode())
                    .eq(ManageProduct::getParentId,product.getId()));
        }
        baseMapper.update(null,Wrappers.<ManageProduct>lambdaUpdate()
                .set(ManageProduct::getStatus,ProductStatusEnum.DISABLE.getCode())
                .eq(ManageProduct::getId,product.getId()));
        return true;
    }

}
