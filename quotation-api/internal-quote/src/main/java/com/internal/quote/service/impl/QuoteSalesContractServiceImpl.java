package com.internal.quote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.utils.DateUtils;
import com.internal.quote.domain.QuoteSalesContract;
import com.internal.quote.mapper.QuoteSalesContractMapper;
import com.internal.quote.service.IQuoteSalesContractService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报价系统-销售合同Service业务层处理
 *
 * @author internal
 * @date 2024-12-19
 */
@Service
@AllArgsConstructor
public class QuoteSalesContractServiceImpl extends ServiceImpl<QuoteSalesContractMapper, QuoteSalesContract> implements IQuoteSalesContractService {

    private final QuoteSalesContractMapper quoteSalesContractMapper;

    /**
     * 查询报价系统-销售合同
     *
     * @param id 报价系统-销售合同主键
     * @return 报价系统-销售合同
     */
    @Override
    public QuoteSalesContract selectQuoteSalesContractById(Long id) {
        return quoteSalesContractMapper.selectQuoteSalesContractById(id);
    }

    /**
     * 查询报价系统-销售合同列表
     *
     * @param quoteSalesContract 报价系统-销售合同
     * @return 报价系统-销售合同
     */
    @Override
    public List<QuoteSalesContract> selectQuoteSalesContractList(QuoteSalesContract quoteSalesContract) {
        return quoteSalesContractMapper.selectQuoteSalesContractList(quoteSalesContract);
    }

    /**
     * 新增报价系统-销售合同
     *
     * @param quoteSalesContract 报价系统-销售合同
     * @return 结果
     */
    @Override
    public int insertQuoteSalesContract(QuoteSalesContract quoteSalesContract) {
        quoteSalesContract.setCreateTime(DateUtils.getNowDate());
        return quoteSalesContractMapper.insert(quoteSalesContract);
    }

    /**
     * 修改报价系统-销售合同
     *
     * @param quoteSalesContract 报价系统-销售合同
     * @return 结果
     */
    @Override
    public int updateQuoteSalesContract(QuoteSalesContract quoteSalesContract) {
        quoteSalesContract.setUpdateTime(DateUtils.getNowDate());
        return quoteSalesContractMapper.updateById(quoteSalesContract);
    }

    /**
     * 批量删除报价系统-销售合同
     *
     * @param ids 需要删除的报价系统-销售合同主键
     * @return 结果
     */
    @Override
    public int deleteQuoteSalesContractByIds(Long[] ids) {
        return quoteSalesContractMapper.deleteQuoteSalesContractByIds(ids);
    }

    /**
     * 删除报价系统-销售合同信息
     *
     * @param id 报价系统-销售合同主键
     * @return 结果
     */
    @Override
    public int deleteQuoteSalesContractById(Long id) {
        return quoteSalesContractMapper.deleteQuoteSalesContractById(id);
    }
}
