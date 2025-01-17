package com.internal.manager.mapper;

import java.util.List;

import com.internal.common.annotation.DataScope;
import com.internal.manager.domain.ManageSoft;
import com.internal.manager.dto.ManageSoftQueryDTO;
import com.internal.manager.vo.ManageSoftVO;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 管理系统-软著管理Mapper接口
 * 
 * @author internal
 * @date 2024-12-11
 */
@Repository
public interface ManageSoftMapper extends BaseMapper<ManageSoft>
{

    /**
     * 查询管理系统-软著管理列表
     * 
     * @param manageSoft 管理系统-软著管理
     * @return 管理系统-软著管理集合
     */
    @DataScope(userAlias = "s1")
    public List<ManageSoftVO> selectManageSoftList(ManageSoftQueryDTO manageSoft);

    /**
     * 删除管理系统-软著管理
     * 
     * @param id 管理系统-软著管理主键
     * @return 结果
     */
    public int deleteManageSoftById(Long id);

    /**
     * 批量删除管理系统-软著管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteManageSoftByIds(Long[] ids);
}
