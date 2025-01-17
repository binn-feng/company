package com.internal.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.manager.domain.ManageProduct;
import com.internal.manager.dto.ManageProductQueryDTO;
import com.internal.manager.vo.ManageProductChildVO;
import com.internal.manager.vo.ManageProductVO;

import java.util.List;

/**
 * 管理系统-产品管理Service接口
 * 
 * @author internal
 * @date 2024-10-11
 */
public interface IManageProductService extends IService<ManageProduct>
{
    /**
     * 查询管理系统-产品管理
     * 
     * @param id 管理系统-产品管理主键
     * @return 管理系统-产品管理
     */
    public ManageProductVO selectManageProductById(Long id);

    /**
     * 查询管理系统-产品管理列表
     * 
     * @param queryDTO 管理系统-产品管理
     * @return 管理系统-产品管理集合
     */
    public List<ManageProductVO> selectManageProductList(ManageProductQueryDTO queryDTO);
    /**
     * 查询管理系统-产品管理Count
     *
     * @param manageProduct 管理系统-产品管理
     * @return 管理系统-产品管理Count
     */
    public Long selectManageProductCount(ManageProductQueryDTO manageProduct);
    /**
     * 查询管理系统-产品管理列表
     *
     * @param manageProduct 管理系统-产品管理
     * @return 管理系统-产品管理集合
     */
    public List<ManageProductChildVO> selectProductChildrenList(ManageProductQueryDTO manageProduct);
    /**
     * 查询管理系统-产品管理Count
     *
     * @param manageProduct 管理系统-产品管理
     * @return 管理系统-产品管理Count
     */
    public Long selectProductChildrenCount(ManageProductQueryDTO manageProduct);

    /**
     * 新增管理系统-产品管理
     * 
     * @param manageProduct 管理系统-产品管理
     * @return 结果
     */
    public Boolean insertManageProduct(ManageProduct manageProduct);

    /**
     * 修改管理系统-产品管理
     * 
     * @param manageProduct 管理系统-产品管理
     * @return 结果
     */
    public int updateManageProduct(ManageProduct manageProduct);

    /**
     * 批量删除管理系统-产品管理
     * 
     * @param ids 需要删除的管理系统-产品管理主键集合
     * @return 结果
     */
    public int deleteManageProductByIds(Long[] ids);

    /**
     * 删除管理系统-产品管理信息
     * 
     * @param id 管理系统-产品管理主键
     * @return 结果
     */
    public int deleteManageProductById(Long id);

    Boolean publish(ManageProduct product);

    Boolean unPublish(ManageProduct product);

    List<ManageProductVO> getOptions(ManageProductQueryDTO dto);
}
