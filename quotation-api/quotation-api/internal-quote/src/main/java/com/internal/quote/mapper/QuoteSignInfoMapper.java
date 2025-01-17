package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteSignInfo;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-待签合同Mapper接口
 * 
 * @author internal
 * @date 2024-11-14
 */
@Repository
public interface QuoteSignInfoMapper extends BaseMapper<QuoteSignInfo>
{
    /**
     * 查询报价系统-待签合同
     * 
     * @param id 报价系统-待签合同主键
     * @return 报价系统-待签合同
     */
    public QuoteSignInfo selectQuoteSignInfoById(Long id);

    /**
     * 查询报价系统-待签合同列表
     * 
     * @param quoteSignInfo 报价系统-待签合同
     * @return 报价系统-待签合同集合
     */
    public List<QuoteSignInfo> selectQuoteSignInfoList(QuoteSignInfo quoteSignInfo);

    /**
     * 新增报价系统-待签合同
     * 
     * @param quoteSignInfo 报价系统-待签合同
     * @return 结果
     */
    public int insertQuoteSignInfo(QuoteSignInfo quoteSignInfo);

    /**
     * 修改报价系统-待签合同
     * 
     * @param quoteSignInfo 报价系统-待签合同
     * @return 结果
     */
    public int updateQuoteSignInfo(QuoteSignInfo quoteSignInfo);

    /**
     * 删除报价系统-待签合同
     * 
     * @param id 报价系统-待签合同主键
     * @return 结果
     */
    public int deleteQuoteSignInfoById(Long id);

    /**
     * 批量删除报价系统-待签合同
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteSignInfoByIds(Long[] ids);
}
