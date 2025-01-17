package com.internal.quote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.internal.quote.domain.QuoteSalesContract;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报价系统-销售合同Mapper接口
 *
 * @author internal
 * @date 2024-12-19
 */
@Repository
public interface QuoteSalesContractMapper extends BaseMapper<QuoteSalesContract> {
    /**
     * 查询报价系统-销售合同
     *
     * @param id 报价系统-销售合同主键
     * @return 报价系统-销售合同
     */
    public QuoteSalesContract selectQuoteSalesContractById(Long id);

    /**
     * 查询报价系统-销售合同列表
     *
     * @param quoteSalesContract 报价系统-销售合同
     * @return 报价系统-销售合同集合
     */
    public List<QuoteSalesContract> selectQuoteSalesContractList(QuoteSalesContract quoteSalesContract);

    /**
     * 新增报价系统-销售合同
     *
     * @param quoteSalesContract 报价系统-销售合同
     * @return 结果
     */
    public int insertQuoteSalesContract(QuoteSalesContract quoteSalesContract);

    /**
     * 修改报价系统-销售合同
     *
     * @param quoteSalesContract 报价系统-销售合同
     * @return 结果
     */
    public int updateQuoteSalesContract(QuoteSalesContract quoteSalesContract);

    /**
     * 删除报价系统-销售合同
     *
     * @param id 报价系统-销售合同主键
     * @return 结果
     */
    public int deleteQuoteSalesContractById(Long id);

    /**
     * 批量删除报价系统-销售合同
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteSalesContractByIds(Long[] ids);
}
