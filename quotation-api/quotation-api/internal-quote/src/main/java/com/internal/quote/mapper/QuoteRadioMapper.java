package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteRadio;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-报价部门占比Mapper接口
 * 
 * @author internal
 * @date 2025-01-07
 */
@Repository
public interface QuoteRadioMapper extends BaseMapper<QuoteRadio>
{
    /**
     * 查询报价系统-报价部门占比
     * 
     * @param id 报价系统-报价部门占比主键
     * @return 报价系统-报价部门占比
     */
    public QuoteRadio selectQuoteRadioById(Long id);

    /**
     * 查询报价系统-报价部门占比列表
     * 
     * @param quoteRadio 报价系统-报价部门占比
     * @return 报价系统-报价部门占比集合
     */
    public List<QuoteRadio> selectQuoteRadioList(QuoteRadio quoteRadio);


    /**
     * 删除报价系统-报价部门占比
     * 
     * @param id 报价系统-报价部门占比主键
     * @return 结果
     */
    public int deleteQuoteRadioById(Long id);

    /**
     * 批量删除报价系统-报价部门占比
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteRadioByIds(Long[] ids);
}
