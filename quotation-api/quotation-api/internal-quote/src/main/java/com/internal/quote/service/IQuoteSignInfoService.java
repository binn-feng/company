package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteSignInfo;
import com.internal.quote.dto.BaseDTO;
import com.internal.quote.dto.SignApplicationSaveDTO;
import com.internal.quote.dto.SignInfoUpdateDTO;
import com.internal.quote.vo.QuoteSignInfoVO;
import com.internal.quote.vo.SignDetailInfoVO;

/**
 * 报价系统-待签合同Service接口
 * 
 * @author internal
 * @date 2024-11-14
 */
public interface IQuoteSignInfoService extends IService<QuoteSignInfo>
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
     * 批量删除报价系统-待签合同
     * 
     * @param ids 需要删除的报价系统-待签合同主键集合
     * @return 结果
     */
    public int deleteQuoteSignInfoByIds(Long[] ids);

    /**
     * 删除报价系统-待签合同信息
     * 
     * @param id 报价系统-待签合同主键
     * @return 结果
     */
    public int deleteQuoteSignInfoById(Long id);

    SignDetailInfoVO getSignInfoById(BaseDTO dto);

    QuoteSignInfoVO getStatusUpdatePageInfo(BaseDTO dto);

    Boolean updateStatus(SignInfoUpdateDTO dto);

    SignDetailInfoVO getSignInfoByOpportunitiesId(BaseDTO dto);

    Boolean signApplication(SignApplicationSaveDTO vo);

    SignDetailInfoVO getReSignInfo(BaseDTO dto);
}
