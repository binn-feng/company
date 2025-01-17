package com.internal.manager.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.internal.common.annotation.DataScope;
import com.internal.manager.domain.ManageProduct;
import com.internal.manager.dto.ManageProductQueryDTO;
import com.internal.manager.vo.ManageProductChildVO;
import com.internal.manager.vo.ManageProductVO;
import org.springframework.stereotype.Repository;

/**
 * 管理系统-产品管理Mapper接口
 * 
 * @author internal
 * @date 2024-10-11
 */
@Repository
public interface ManageProductMapper extends BaseMapper<ManageProduct>
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
     * @param queryDTO 查询条件
     * @return 管理系统-产品管理集合
     */
    @DataScope(userAlias = "mp")
    public List<ManageProductVO> selectManageProductList(ManageProductQueryDTO queryDTO);
    /**
     * 查询管理系统-产品管理Count
     *
     * @param queryDTO 查询条件
     * @return 管理系统-产品管理Count
     */
    @DataScope(userAlias = "mp")
    public Long selectManageProductCount(ManageProductQueryDTO queryDTO);

    /**
     * 查询管理系统-子产品管理列表
     *
     * @param queryDTO 查询条件
     * @return 管理系统-子产品管理集合
     */
    @DataScope(userAlias = "child")
    public List<ManageProductChildVO> selectProductChildrenList(ManageProductQueryDTO queryDTO);
    /**
     * 查询管理系统-子产品管理列表(无权限)
     *
     * @param queryDTO 查询条件
     * @return 管理系统-子产品管理集合
     */
    public List<ManageProductChildVO> selectProductChildrenListNoAuth(ManageProductQueryDTO queryDTO);
    /**
     * 查询管理系统-子产品管理Count
     *
     * @param queryDTO 查询条件
     * @return 管理系统-子产品管理Count
     */
    @DataScope(userAlias = "mp")
    public Long selectProductChildrenCount(ManageProductQueryDTO queryDTO);

    /**
     * 删除管理系统-产品管理
     * 
     * @param id 管理系统-产品管理主键
     * @return 结果
     */
    public int deleteManageProductById(Long id);

    /**
     * 批量删除管理系统-产品管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteManageProductByIds(Long[] ids);
}
