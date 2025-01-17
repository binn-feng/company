package com.internal.manager.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.manager.domain.ManageSoft;
import com.internal.manager.dto.ManageSoftOptionsDTO;
import com.internal.manager.dto.ManageSoftQueryDTO;
import com.internal.manager.dto.ManageSoftSaveDTO;
import com.internal.manager.vo.ManageSoftVO;

/**
 * 管理系统-软著管理Service接口
 * 
 * @author internal
 * @date 2024-12-11
 */
public interface IManageSoftService extends IService<ManageSoft>
{
    /**
     * 查询管理系统-软著管理
     * 
     * @param id 管理系统-软著管理主键
     * @return 管理系统-软著管理
     */
    public ManageSoft selectManageSoftById(Long id);

    /**
     * 查询管理系统-软著管理列表
     * 
     * @param manageSoft 管理系统-软著管理
     * @return 管理系统-软著管理集合
     */
    public List<ManageSoftVO> selectManageSoftList(ManageSoftQueryDTO manageSoft);

    /**
     * 新增管理系统-软著管理
     * 
     * @param manageSoft 管理系统-软著管理
     * @return 结果
     */
    public Boolean insertManageSoft(ManageSoftSaveDTO manageSoft);

    /**
     * 修改管理系统-软著管理
     * 
     * @param manageSoft 管理系统-软著管理
     * @return 结果
     */
    public Boolean updateManageSoft(ManageSoft manageSoft);

    /**
     * 批量删除管理系统-软著管理
     * 
     * @param ids 需要删除的管理系统-软著管理主键集合
     * @return 结果
     */
    public int deleteManageSoftByIds(Long[] ids);

    /**
     * 删除管理系统-软著管理信息
     * 
     * @param id 管理系统-软著管理主键
     * @return 结果
     */
    public int deleteManageSoftById(Long id);

    List<ManageSoftVO> getOptions(ManageSoftOptionsDTO dto);
}
