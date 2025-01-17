package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuotePresaleInfo;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-商机售前报价信息Mapper接口
 * 
 * @author internal
 * @date 2024-10-30
 */
@Repository
public interface QuotePresaleInfoMapper extends BaseMapper<QuotePresaleInfo>
{
    /**
     * 查询报价系统-商机售前报价信息
     * 
     * @param id 报价系统-商机售前报价信息主键
     * @return 报价系统-商机售前报价信息
     */
    public QuotePresaleInfo selectQuotePresaleInfoById(Long id);

    /**
     * 查询报价系统-商机售前报价信息列表
     * 
     * @param quotePresaleInfo 报价系统-商机售前报价信息
     * @return 报价系统-商机售前报价信息集合
     */
    public List<QuotePresaleInfo> selectQuotePresaleInfoList(QuotePresaleInfo quotePresaleInfo);

    /**
     * 删除报价系统-商机售前报价信息
     * 
     * @param id 报价系统-商机售前报价信息主键
     * @return 结果
     */
    public int deleteQuotePresaleInfoById(Long id);

    /**
     * 批量删除报价系统-商机售前报价信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuotePresaleInfoByIds(Long[] ids);
}
