package com.internal.quote.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.internal.common.annotation.DataScope;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.dto.QuoteOpportunitiesQuery;
import com.internal.quote.vo.QuoteOpportunitiesVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 报价系统-商机报价信息Mapper接口
 * 
 * @author internal
 * @date 2024-10-14
 */
@Repository
public interface QuoteOpportunitiesMapper extends BaseMapper<QuoteOpportunities>
{
    /**
     * 查询报价系统-商机报价信息
     * 
     * @param id 报价系统-商机报价信息主键
     * @return 报价系统-商机报价信息
     */
    //public QuoteOpportunities selectQuoteOpportunitiesById(Long id);

    /**
     * 查询报价系统-商机报价信息列表
     * 
     * @param quoteOpportunities 报价系统-商机报价信息
     * @return 报价系统-商机报价信息集合
     */
    @DataScope(userAlias = "qo")
    public List<QuoteOpportunitiesVO> selectQuoteOpportunitiesList(QuoteOpportunitiesQuery quoteOpportunities);

    /**
     * 商机报价信息列表-Count(用于分页)
     */
    @DataScope(userAlias = "qo")
    public Long selectQuoteOpportunitiesCount(QuoteOpportunitiesQuery quoteOpportunities);

    /**
     * 新增报价系统-商机报价信息
     * 
     * @param quoteOpportunities 报价系统-商机报价信息
     * @return 结果
     */
    //public int insertQuoteOpportunities(QuoteOpportunities quoteOpportunities);

    /**
     * 修改报价系统-商机报价信息
     * 
     * @param quoteOpportunities 报价系统-商机报价信息
     * @return 结果
     */
    //public int updateQuoteOpportunities(QuoteOpportunities quoteOpportunities);

    /**
     * 删除报价系统-商机报价信息
     * 
     * @param id 报价系统-商机报价信息主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesById(Long id);

    /**
     * 批量删除报价系统-商机报价信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesByIds(Long[] ids);

    /**
     * 查询商机报价信息-销售手机号
     * @param opportunitiesId
     * @return
     */
    String selectQuoteSalePhone(Long opportunitiesId);
}
