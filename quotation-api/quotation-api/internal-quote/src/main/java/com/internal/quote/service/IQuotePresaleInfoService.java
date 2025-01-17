package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuotePresaleInfo;
import com.internal.quote.dto.QuotePresaleInfoQuery;
import com.internal.quote.dto.QuotePresaleInfoSaveDTO;
import com.internal.quote.vo.ApprovalAndSignInfoVO;
import com.internal.quote.vo.QuotePreSaleDetailInfoVO;
import com.internal.quote.vo.QuotePresaleInfoVO;
import com.internal.quote.vo.SignDetailInfoVO;

/**
 * 报价系统-商机售前报价信息Service接口
 * 
 * @author internal
 * @date 2024-10-30
 */
public interface IQuotePresaleInfoService extends IService<QuotePresaleInfo>
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
     * 新增报价系统-商机售前报价信息
     * 
     * @param quotePresaleInfo 报价系统-商机售前报价信息
     * @return 结果
     */
    public int insertQuotePresaleInfo(QuotePresaleInfo quotePresaleInfo);

    /**
     * 修改报价系统-商机售前报价信息
     * 
     * @param quotePresaleInfo 报价系统-商机售前报价信息
     * @return 结果
     */
    public int updateQuotePresaleInfo(QuotePresaleInfo quotePresaleInfo);

    /**
     * 批量删除报价系统-商机售前报价信息
     * 
     * @param ids 需要删除的报价系统-商机售前报价信息主键集合
     * @return 结果
     */
    public int deleteQuotePresaleInfoByIds(Long[] ids);

    /**
     * 删除报价系统-商机售前报价信息信息
     * 
     * @param id 报价系统-商机售前报价信息主键
     * @return 结果
     */
    public int deleteQuotePresaleInfoById(Long id);

    /**
     * 销售报价-售前粗略报价详情
     * @param quotePresaleInfoQuery quotePresaleInfoQuery
     * @return
     */
    QuotePreSaleDetailInfoVO getSalesRoughInfo(QuotePresaleInfoQuery quotePresaleInfoQuery);

    /**
     * 销售报价-售前详细报价详情
     * @param quotePresaleInfoQuery quotePresaleInfoQuery
     * @return
     */
    QuotePreSaleDetailInfoVO getSalesDetailInfo(QuotePresaleInfoQuery quotePresaleInfoQuery);

    SignDetailInfoVO approvalAndSignInfo(Long opportunitiesId, Boolean fromManage);

    Integer addSalesQuotesVersion(QuotePresaleInfoSaveDTO quotePresaleInfo);

    QuotePresaleInfoVO getDetailInfoById(QuotePresaleInfoQuery quotePresaleInfoQuery);

    QuotePresaleInfo getCurrentPresaleInfo(Long opportunitiesId);

    Boolean existCostInfo(Long opportunitiesId);

    /**
     * 签约申请页-填充成本报价
     * @param vo
     * @param type
     * @param versionId
     */
    Boolean fillQuoteInfo(ApprovalAndSignInfoVO vo,String type,Long versionId,Boolean fromManage);


    /**
     * 签约申请页-计算其它数据
     * @param vo
     */
    void fillApprovalAndSignInfo(ApprovalAndSignInfoVO vo,Boolean roughOnlyTotal);

    QuotePreSaleDetailInfoVO reSales(QuotePresaleInfoQuery query);
}
