package com.internal.quote.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteOpportunitiesFile;
import com.internal.quote.dto.UpLoadDTO;
import com.internal.quote.vo.QuoteOpportunitiesVO;

/**
 * 商机附件Service接口
 * 
 * @author internal
 * @date 2024-10-28
 */
public interface IQuoteOpportunitiesFileService extends IService<QuoteOpportunitiesFile>
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
     * 批量删除商机附件
     * 
     * @param ids 需要删除的商机附件主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesFileByIds(Long[] ids);

    /**
     * 删除商机附件信息
     * 
     * @param id 商机附件主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesFileById(Long id);

    Boolean upLoadFiles(List<QuoteOpportunitiesFile> fileList);

    Map<Long, List<QuoteOpportunitiesFile>> selectOpportunitiesFileMap(List<QuoteOpportunitiesVO> voList);
}
