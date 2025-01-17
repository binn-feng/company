package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesFile;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 商机附件Mapper接口
 * 
 * @author internal
 * @date 2024-10-28
 */
@Repository
public interface QuoteOpportunitiesFileMapper extends BaseMapper<QuoteOpportunitiesFile>
{
    /**
     * 查询商机附件
     * 
     * @param id 商机附件主键
     * @return 商机附件
     */
    public QuoteOpportunitiesFile selectQuoteOpportunitiesFileById(Long id);

    /**
     * 查询商机附件列表
     * 
     * @param quoteOpportunitiesFile 商机附件
     * @return 商机附件集合
     */
    public List<QuoteOpportunitiesFile> selectQuoteOpportunitiesFileList(QuoteOpportunitiesFile quoteOpportunitiesFile);

    /**
     * 新增商机附件
     * 
     * @param quoteOpportunitiesFile 商机附件
     * @return 结果
     */
    public int insertQuoteOpportunitiesFile(QuoteOpportunitiesFile quoteOpportunitiesFile);

    /**
     * 修改商机附件
     * 
     * @param quoteOpportunitiesFile 商机附件
     * @return 结果
     */
    public int updateQuoteOpportunitiesFile(QuoteOpportunitiesFile quoteOpportunitiesFile);

    /**
     * 删除商机附件
     * 
     * @param id 商机附件主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesFileById(Long id);

    /**
     * 批量删除商机附件
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesFileByIds(Long[] ids);
}
