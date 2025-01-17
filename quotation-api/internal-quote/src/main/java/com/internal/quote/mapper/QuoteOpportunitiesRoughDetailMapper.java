package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesRoughDetail;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-商机粗略详细报价Mapper接口
 * 
 * @author internal
 * @date 2024-10-29
 */
@Repository
public interface QuoteOpportunitiesRoughDetailMapper extends BaseMapper<QuoteOpportunitiesRoughDetail>
{
    /**
     * 查询报价系统-商机粗略详细报价
     * 
     * @param id 报价系统-商机粗略详细报价主键
     * @return 报价系统-商机粗略详细报价
     */
    public QuoteOpportunitiesRoughDetail selectQuoteOpportunitiesRoughDetailById(Long id);

    /**
     * 查询报价系统-商机粗略详细报价列表
     * 
     * @param quoteOpportunitiesRoughDetail 报价系统-商机粗略详细报价
     * @return 报价系统-商机粗略详细报价集合
     */
    public List<QuoteOpportunitiesRoughDetail> selectQuoteOpportunitiesRoughDetailList(QuoteOpportunitiesRoughDetail quoteOpportunitiesRoughDetail);

    /**
     * 新增报价系统-商机粗略详细报价
     * 
     * @param quoteOpportunitiesRoughDetail 报价系统-商机粗略详细报价
     * @return 结果
     */
    public int insertQuoteOpportunitiesRoughDetail(QuoteOpportunitiesRoughDetail quoteOpportunitiesRoughDetail);

    /**
     * 修改报价系统-商机粗略详细报价
     * 
     * @param quoteOpportunitiesRoughDetail 报价系统-商机粗略详细报价
     * @return 结果
     */
    public int updateQuoteOpportunitiesRoughDetail(QuoteOpportunitiesRoughDetail quoteOpportunitiesRoughDetail);

    /**
     * 删除报价系统-商机粗略详细报价
     * 
     * @param id 报价系统-商机粗略详细报价主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesRoughDetailById(Long id);

    /**
     * 批量删除报价系统-商机粗略详细报价
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesRoughDetailByIds(Long[] ids);
}
