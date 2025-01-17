package com.internal.quote.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.dto.QuoteOpportunitiesProductSaveDTO;
import com.internal.quote.vo.QuoteOpportunitiesProductVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteOpportunitiesProductMapper;
import com.internal.quote.domain.QuoteOpportunitiesProduct;
import com.internal.quote.service.IQuoteOpportunitiesProductService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报价系统-商机产品平台小记Service业务层处理
 * 
 * @author internal
 * @date 2024-10-15
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QuoteOpportunitiesProductServiceImpl extends ServiceImpl<QuoteOpportunitiesProductMapper, QuoteOpportunitiesProduct> implements IQuoteOpportunitiesProductService
{

    private final QuoteOpportunitiesProductMapper quoteOpportunitiesProductMapper;

    /**
     * 查询报价系统-商机产品平台小记
     * 
     * @param id 报价系统-商机产品平台小记主键
     * @return 报价系统-商机产品平台小记
     */
    @Override
    public QuoteOpportunitiesProduct selectQuoteOpportunitiesProductById(Long id)
    {
        return quoteOpportunitiesProductMapper.selectQuoteOpportunitiesProductById(id);
    }

    /**
     * 查询报价系统-商机产品平台小记列表
     * 
     * @param quoteOpportunitiesProduct 报价系统-商机产品平台小记
     * @return 报价系统-商机产品平台小记
     */
    @Override
    public List<QuoteOpportunitiesProduct> selectQuoteOpportunitiesProductList(QuoteOpportunitiesProduct quoteOpportunitiesProduct)
    {
        return quoteOpportunitiesProductMapper.selectQuoteOpportunitiesProductList(quoteOpportunitiesProduct);
    }

    /**
     * 新增报价系统-商机产品平台小记
     * 
     * @param quoteOpportunitiesProduct 报价系统-商机产品平台小记
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesProduct(QuoteOpportunitiesProduct quoteOpportunitiesProduct)
    {
        quoteOpportunitiesProduct.setCreateTime(DateUtils.getNowDate());
        return quoteOpportunitiesProductMapper.insert(quoteOpportunitiesProduct);
    }

    /**
     * 修改报价系统-商机产品平台小记
     * 
     * @param quoteOpportunitiesProduct 报价系统-商机产品平台小记
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesProduct(QuoteOpportunitiesProduct quoteOpportunitiesProduct)
    {
        quoteOpportunitiesProduct.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesProductMapper.updateById(quoteOpportunitiesProduct);
    }

    /**
     * 批量删除报价系统-商机产品平台小记
     * 
     * @param ids 需要删除的报价系统-商机产品平台小记主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesProductByIds(Long[] ids)
    {
        return quoteOpportunitiesProductMapper.deleteQuoteOpportunitiesProductByIds(ids);
    }

    /**
     * 删除报价系统-商机产品平台小记信息
     * 
     * @param id 报价系统-商机产品平台小记主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesProductById(Long id)
    {
        return quoteOpportunitiesProductMapper.deleteQuoteOpportunitiesProductById(id);
    }

    @Override
    public QuoteOpportunitiesProductVO getProductInfo(Long opportunitiesId) {
        QuoteOpportunitiesProductVO quoteOpportunitiesProductVO = new QuoteOpportunitiesProductVO();
        List<QuoteOpportunitiesProduct> quoteOpportunitiesProductList = baseMapper
                .selectList(Wrappers.<QuoteOpportunitiesProduct>lambdaQuery().eq(QuoteOpportunitiesProduct::getOpportunitiesDetailId, opportunitiesId));
        if (CollUtil.isNotEmpty(quoteOpportunitiesProductList)){
        quoteOpportunitiesProductVO.setQuoteOpportunitiesProductList(quoteOpportunitiesProductList);
            quoteOpportunitiesProductVO.setTotalCost(quoteOpportunitiesProductList.stream()
                    .map(QuoteOpportunitiesProduct::getAmount).filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
            return quoteOpportunitiesProductVO;
        }
        return null;
    }

    @Override
    public void saveBatchDetail(List<QuoteOpportunitiesProductSaveDTO> quoteOpportunitiesProductList, QuoteOpportunitiesDetail quoteOpportunitiesDetail) {
        List<QuoteOpportunitiesProduct> quoteOpportunitiesProducts = BeanUtil.copyToList(quoteOpportunitiesProductList, QuoteOpportunitiesProduct.class);
        quoteOpportunitiesProducts.forEach(quoteOpportunitiesProduct -> {
            quoteOpportunitiesProduct.setOpportunitiesId(quoteOpportunitiesDetail.getOpportunitiesId());
            quoteOpportunitiesProduct.setOpportunitiesDetailId(quoteOpportunitiesDetail.getId());
        });
        saveBatch(quoteOpportunitiesProducts);
    }
}
