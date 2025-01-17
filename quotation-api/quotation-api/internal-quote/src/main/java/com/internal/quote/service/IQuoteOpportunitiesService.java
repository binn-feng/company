package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.enums.VersionStatus;
import com.internal.common.request.PageParams;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.dto.*;
import com.internal.quote.vo.*;

/**
 * 报价系统-商机报价信息Service接口
 * 
 * @author internal
 * @date 2024-10-14
 */
public interface IQuoteOpportunitiesService extends IService<QuoteOpportunities>
{
    /**
     * 查询报价系统-商机报价信息
     * 
     * @param id 报价系统-商机报价信息主键
     * @return 报价系统-商机报价信息
     */
    public QuoteOpportunities selectQuoteOpportunitiesById(Long id);

    /**
     * 查询报价系统-商机报价信息列表
     * 
     * @param quoteOpportunities 报价系统-商机报价信息
     * @return 报价系统-商机报价信息集合
     */
    public List<QuoteOpportunitiesVO> selectQuoteOpportunitiesList(QuoteOpportunitiesQuery quoteOpportunities);

    /**
     * 商机报价信息列表-Count(用于分页)
     */
    public Long selectQuoteOpportunitiesCount(QuoteOpportunitiesQuery quoteOpportunities);

    /**
     * 新增报价系统-商机报价信息
     * 
     * @param quoteOpportunities 报价系统-商机报价信息
     * @return 结果
     */
    public int insertQuoteOpportunities(QuoteOpportunities quoteOpportunities);

    /**
     * 修改报价系统-商机报价信息
     * 
     * @param quoteOpportunities 报价系统-商机报价信息
     * @return 结果
     */
    public int updateQuoteOpportunities(QuoteOpportunities quoteOpportunities);

    /**
     * 批量删除报价系统-商机报价信息
     * 
     * @param ids 需要删除的报价系统-商机报价信息主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesByIds(Long[] ids);

    /**
     * 删除报价系统-商机报价信息信息
     * 
     * @param id 报价系统-商机报价信息主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesById(Long id);

    /**
     * 获取版本
     * @param quoteOpportunitiesRoughQuery quoteOpportunitiesRoughQuery
     * @return
     */
    List<OpportunitiesDetailVersionVO> getVersionList(QuoteOpportunitiesRoughQuery quoteOpportunitiesRoughQuery);

    /**
     *  申请报价
     * @param dto dto
     * @return Boolean
     */
    Boolean quoteApply(ReQuoteDTO dto);

    /**
     * 丢单处理
     * @param quoteOpportunitiesUpdateDTO quoteOpportunitiesUpdateDTO
     * @return
     */
    Boolean lose(QuoteOpportunitiesUpdateDTO quoteOpportunitiesUpdateDTO);

    Boolean salesApproval(ApprovalDTO updateDTO);

    Boolean salesReject(ApprovalDTO updateDTO);

    List<OpportunitiesSalesVersionVO> getSalesVersionList(QuoteOpportunitiesRoughQuery quoteOpportunitiesRoughQuery);

    /**
     * 主查询
     * @param model 查询条件
     * @param selectDetail 是否查询明细
     * @return
     */
    List<QuoteOpportunitiesVO> getQuoteOpportunitiesList(QuoteOpportunitiesQuery model,Boolean selectDetail);

    Boolean incapable(QuoteOpportunitiesUpdateDTO dto);

    String getCurrentVersion(String supportCrmId);

    /**
     * 修改所有成本报价的状态，包括详细报价，粗略报价和无法报价
     * @param id
     * @param status
     */
    void setAllVersionStatus(List<Long> id,VersionStatus status);

    /**
     * 修改所有商机的所有成本报价的状态，包括详细报价，粗略报价和无法报价
     * @param parentId
     * @param status
     */
    void setAllVersionStatusByParent(Long parentId, VersionStatus status);

    void setAllVersionStatusByCrm(String crmId, VersionStatus status);

    Boolean signApproval(ApprovalDTO dto);

    Boolean signReject(ApprovalDTO dto);

    List<OpportunitiesSignVersionVO> getSignVersionList(QuoteOpportunitiesRoughQuery quoteOpportunitiesRoughQuery);

    Boolean updateRequired(ReQuoteDTO dto);

    Boolean quoteBySelf(QuoteOpportunitiesUpdateDTO opportunitiesId);

    Boolean quoteByOthers(QuoteOpportunitiesUpdateDTO opportunitiesId);

    /**
     * 获取商机对应销售手机号
     * @param opportunitiesId opportunitiesId
     * @return
     */
    String selectQuoteSalePhone(Long opportunitiesId);

    List<QuoteOpportunities> getAboutQuoteList(Long id);

    /**
     * 填充权限相关信息
     * @param currentUser
     * @param params
     */
    void fillAuthInfo(SysUser currentUser, PageParams<QuoteOpportunitiesQuery> params);

    QuoteIndexVO getIndexPageInfo();

    Boolean loseApproval(ApprovalDTO dto);

    Boolean loseReject(ApprovalDTO dto);
}
