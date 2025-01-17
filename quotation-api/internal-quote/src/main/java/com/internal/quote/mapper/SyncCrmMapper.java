package com.internal.quote.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.internal.common.constant.DataSourceTypeConstants;
import com.internal.common.core.domain.entity.SysDept;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.manager.domain.ManagerBuGoal;
import com.internal.manager.domain.ManagerBuGoals;
import com.internal.quote.domain.QuotePaymentPlan;
import com.internal.quote.domain.QuoteSalesContract;
import com.internal.quote.dto.BusinessOpportunityDTO;
import com.internal.quote.dto.BusinessOpportunityStatusDTO;
import com.internal.quote.dto.BusinessOpportunityUpdateDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 调度任务日志信息 数据层
 * 使用时请勿添加@Transactional注解, 否则会导致无法正常切换到数据源
 * @author every
 */
@Repository
public interface SyncCrmMapper {

    /**
     * 同步crm数据 报备状态=通过，and 商机状态 不等于 暂停，终止，失败，成单 and 商机有 售前支持 且 售前支持的支持类型=成本报价
     * @return
     */
    @DS(DataSourceTypeConstants.CRM_APP)
    List<BusinessOpportunityDTO> syncCrmInfo();

    /**
     * 同步CRM商机状态为暂停，终止，失败，成单的商机
     * @return
     */
    @DS(DataSourceTypeConstants.CRM_APP)
    List<BusinessOpportunityStatusDTO> syncCrmStatusInfo();

    /**
     * 同步报价状态到CRM
     */
    @DS(DataSourceTypeConstants.CRM_APP)
    Boolean syncQuoteStatus(@Param("param") BusinessOpportunityUpdateDTO businessOpportunityUpdateDTO);

    /**
     * 更新CRM商机阶段
     */
    @DS(DataSourceTypeConstants.CRM_APP)
    Boolean updateCrmStage(@Param("param") BusinessOpportunityUpdateDTO businessOpportunityUpdateDTO);

    /**
     * 获取crm中的用户信息
     * @return List<SysUser>
     */
    @DS(DataSourceTypeConstants.CRM_BASE)
    List<SysUser> syncCrmUserInfo();

    /**
     * 获取crm中的用户信息
     * @return List<SysUser>
     */
    @DS(DataSourceTypeConstants.CRM_BASE)
    List<SysDept> syncCrmDeptInfo();

    /**
     * 获取CRM中的收款计划
     */
    @DS(DataSourceTypeConstants.CRM_APP)
    List<QuotePaymentPlan> syncPaymentPlan();

    /**
     * 获取CRM中的销售合同
     */
    @DS(DataSourceTypeConstants.CRM_APP)
    List<QuoteSalesContract> syncSaleContract();

    /**
     * 获取crm中的销售目标信息
     */
    @DS(DataSourceTypeConstants.CRM_APP)
    List<ManagerBuGoal> syncSalesTarget();

    /**
     * 获取CRM中的销售目标子表信息
     */
    @DS(DataSourceTypeConstants.CRM_APP)
    List<ManagerBuGoals> syncSalesTargetGoal();

}
