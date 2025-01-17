package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteRadio;
import com.internal.quote.dto.BaseDTO;
import com.internal.quote.dto.QuoteRadioDTO;
import com.internal.quote.vo.QuoteRadioVO;
import com.internal.quote.vo.SysDeptVO;

/**
 * 报价系统-报价部门占比Service接口
 * 
 * @author internal
 * @date 2025-01-07
 */
public interface IQuoteRadioService extends IService<QuoteRadio>
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
     * 新增报价系统-报价部门占比
     * 
     * @param quoteRadio 报价系统-报价部门占比
     * @return 结果
     */
    public int insertQuoteRadio(QuoteRadio quoteRadio);

    /**
     * 修改报价系统-报价部门占比
     * 
     * @param quoteRadio 报价系统-报价部门占比
     * @return 结果
     */
    public int updateQuoteRadio(QuoteRadio quoteRadio);

    /**
     * 批量删除报价系统-报价部门占比
     * 
     * @param ids 需要删除的报价系统-报价部门占比主键集合
     * @return 结果
     */
    public int deleteQuoteRadioByIds(Long[] ids);

    /**
     * 删除报价系统-报价部门占比信息
     * 
     * @param id 报价系统-报价部门占比主键
     * @return 结果
     */
    public int deleteQuoteRadioById(Long id);

    List<QuoteRadioVO> getQuoteDept(QuoteRadioDTO dto);

    List<QuoteRadioVO> getRadioList(Long costId);

    List<QuoteRadioVO> getEmptyRadioList(QuoteOpportunities newQuote);
}
