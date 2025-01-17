package com.internal.quote.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.enums.CostType;
import com.internal.common.enums.UnitType;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.utils.StringUtils;
import com.internal.manager.domain.ManageCost;
import com.internal.manager.service.IManageCostService;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.domain.QuoteOpportunitiesSupport;
import com.internal.quote.dto.QuoteOpportunitiesCustomizableSaveDTO;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.vo.QuoteOpportunitiesCustomizableVO;
import com.internal.quote.vo.QuoteOpportunitiesSupportVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteOpportunitiesCustomizableMapper;
import com.internal.quote.domain.QuoteOpportunitiesCustomizable;
import com.internal.quote.service.IQuoteOpportunitiesCustomizableService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报价系统-商机定制开发小记Service业务层处理
 * 
 * @author internal
 * @date 2024-10-15
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QuoteOpportunitiesCustomizableServiceImpl extends ServiceImpl<QuoteOpportunitiesCustomizableMapper, QuoteOpportunitiesCustomizable> implements IQuoteOpportunitiesCustomizableService
{

    private final QuoteOpportunitiesCustomizableMapper quoteOpportunitiesCustomizableMapper;
    private final IManageCostService manageCostService;

    private static final BigDecimal PERSON_DAY_TO_MONTH_RATE = BigDecimal.valueOf(21.75);

    /**
     * 查询报价系统-商机定制开发小记
     * 
     * @param id 报价系统-商机定制开发小记主键
     * @return 报价系统-商机定制开发小记
     */
    @Override
    public QuoteOpportunitiesCustomizable selectQuoteOpportunitiesCustomizableById(Long id)
    {
        return quoteOpportunitiesCustomizableMapper.selectQuoteOpportunitiesCustomizableById(id);
    }

    /**
     * 查询报价系统-商机定制开发小记列表
     * 
     * @param quoteOpportunitiesCustomizable 报价系统-商机定制开发小记
     * @return 报价系统-商机定制开发小记
     */
    @Override
    public List<QuoteOpportunitiesCustomizable> selectQuoteOpportunitiesCustomizableList(QuoteOpportunitiesCustomizable quoteOpportunitiesCustomizable)
    {
        return quoteOpportunitiesCustomizableMapper.selectQuoteOpportunitiesCustomizableList(quoteOpportunitiesCustomizable);
    }

    /**
     * 新增报价系统-商机定制开发小记
     * 
     * @param quoteOpportunitiesCustomizable 报价系统-商机定制开发小记
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesCustomizable(QuoteOpportunitiesCustomizable quoteOpportunitiesCustomizable)
    {
        quoteOpportunitiesCustomizable.setCreateTime(DateUtils.getNowDate());
        return quoteOpportunitiesCustomizableMapper.insert(quoteOpportunitiesCustomizable);
    }

    /**
     * 修改报价系统-商机定制开发小记
     * 
     * @param quoteOpportunitiesCustomizable 报价系统-商机定制开发小记
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesCustomizable(QuoteOpportunitiesCustomizable quoteOpportunitiesCustomizable)
    {
        quoteOpportunitiesCustomizable.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesCustomizableMapper.updateById(quoteOpportunitiesCustomizable);
    }

    /**
     * 批量删除报价系统-商机定制开发小记
     * 
     * @param ids 需要删除的报价系统-商机定制开发小记主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesCustomizableByIds(Long[] ids)
    {
        return quoteOpportunitiesCustomizableMapper.deleteQuoteOpportunitiesCustomizableByIds(ids);
    }

    /**
     * 删除报价系统-商机定制开发小记信息
     * 
     * @param id 报价系统-商机定制开发小记主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesCustomizableById(Long id)
    {
        return quoteOpportunitiesCustomizableMapper.deleteQuoteOpportunitiesCustomizableById(id);
    }

    @Override
    public void saveBatchDetail(List<QuoteOpportunitiesCustomizableSaveDTO> quoteOpportunitiesCustomizableList, QuoteOpportunitiesDetail quoteOpportunitiesDetail) {
        List<QuoteOpportunitiesCustomizable> quoteOpportunitiesCustomizableLists = BeanUtil.copyToList(quoteOpportunitiesCustomizableList, QuoteOpportunitiesCustomizable.class);
        quoteOpportunitiesCustomizableLists.forEach(quoteOpportunitiesCustomizable ->{
            quoteOpportunitiesCustomizable.setOpportunitiesId(quoteOpportunitiesDetail.getOpportunitiesId());
            quoteOpportunitiesCustomizable.setOpportunitiesDetailId(quoteOpportunitiesDetail.getId());
        });
        saveBatch(quoteOpportunitiesCustomizableLists);
    }

    @Override
    public QuoteOpportunitiesCustomizableVO getCustomizableInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery) {
        QuoteOpportunitiesCustomizableVO quoteOpportunitiesCustomizableVO =  new QuoteOpportunitiesCustomizableVO();
        BigDecimal totalSoftwareCosts = BigDecimal.ZERO;
        List<QuoteOpportunitiesCustomizable> quoteOpportunitiesSupportList = baseMapper
                .selectList(Wrappers.<QuoteOpportunitiesCustomizable>lambdaQuery().eq(QuoteOpportunitiesCustomizable::getOpportunitiesDetailId, quoteOpportunitiesDetailQuery.getOpportunitiesDetailId()));
        if(CollUtil.isNotEmpty(quoteOpportunitiesSupportList)){

            Map<CostType, ManageCost> manageCostMap = manageCostService.getCostMap();

            quoteOpportunitiesCustomizableVO.setQuoteOpportunitiesCustomizableList(quoteOpportunitiesSupportList);
            quoteOpportunitiesSupportList.forEach(item ->{
                // 需求评估
                quoteOpportunitiesCustomizableVO
                        .setInternal1(getTypeCalculation(quoteOpportunitiesCustomizableVO.getInternal1()
                                , item.getDemandInternalUnit(),item.getDemandInternalWorkload()));
                quoteOpportunitiesCustomizableVO
                        .setDemandLocalWorkload(getTypeCalculation(quoteOpportunitiesCustomizableVO.getDemandLocalWorkload()
                                ,item.getDemandExternalUnit(),item.getDemandLocalWorkload()));
                quoteOpportunitiesCustomizableVO
                        .setDemandExternalWorkload(getTypeCalculation(quoteOpportunitiesCustomizableVO.getDemandExternalWorkload()
                                ,item.getDemandExternalUnit(),item.getDemandExternalWorkload()));
                // 开发评估
                quoteOpportunitiesCustomizableVO
                        .setInternal2(getTypeCalculation(quoteOpportunitiesCustomizableVO.getInternal2()
                                ,item.getDevInternalUnit(),item.getDevInternalWorkload()));
                quoteOpportunitiesCustomizableVO
                        .setDevLocalWorkload(getTypeCalculation(quoteOpportunitiesCustomizableVO.getDevLocalWorkload()
                                ,item.getDevExternalUnit(),item.getDevLocalWorkload()));
                quoteOpportunitiesCustomizableVO
                        .setDevExternalWorkload(getTypeCalculation(quoteOpportunitiesCustomizableVO.getDevExternalWorkload()
                                ,item.getDevExternalUnit(),item.getDevExternalWorkload()));
                // 测试评估
                quoteOpportunitiesCustomizableVO
                        .setInternal3(getTypeCalculation(quoteOpportunitiesCustomizableVO.getInternal3()
                                ,item.getTestInternalUnit(),item.getTestInternalWorkload()));
                quoteOpportunitiesCustomizableVO
                        .setTestLocalWorkload(getTypeCalculation(quoteOpportunitiesCustomizableVO.getTestLocalWorkload()
                                ,item.getTestExternalUnit(),item.getTestLocalWorkload()));
                quoteOpportunitiesCustomizableVO
                        .setTestExternalWorkload(getTypeCalculation(quoteOpportunitiesCustomizableVO.getTestExternalWorkload()
                                ,item.getTestExternalUnit(),item.getTestExternalWorkload()));
                // UI评估
                quoteOpportunitiesCustomizableVO
                        .setInternal4(getTypeCalculation(quoteOpportunitiesCustomizableVO.getInternal4(),item.getUiInternalUnit()
                                ,item.getUiInternalWorkload()));
                quoteOpportunitiesCustomizableVO
                        .setUiLocalWorkload(getTypeCalculation(quoteOpportunitiesCustomizableVO.getUiLocalWorkload()
                                ,item.getUiExternalUnit(),item.getUiLocalWorkload()));
                quoteOpportunitiesCustomizableVO
                        .setUiExternalWorkload(getTypeCalculation(quoteOpportunitiesCustomizableVO.getUiExternalWorkload()
                                ,item.getUiExternalUnit(),item.getUiExternalWorkload()));
                // 项目管理
                quoteOpportunitiesCustomizableVO
                        .setInternal5(getTypeCalculation(quoteOpportunitiesCustomizableVO.getInternal5()
                                ,item.getPmInternalUnit(),item.getPmInternalWorkload()));
                quoteOpportunitiesCustomizableVO
                        .setPmLocalWorkload(getTypeCalculation(quoteOpportunitiesCustomizableVO.getPmLocalWorkload()
                                ,item.getPmExternalUnit(),item.getPmLocalWorkload()));
                quoteOpportunitiesCustomizableVO
                        .setPmExternalWorkload(getTypeCalculation(quoteOpportunitiesCustomizableVO.getPmExternalWorkload()
                                ,item.getPmExternalUnit(),item.getPmExternalWorkload()));

            });
            // 计算内部开发
            BigDecimal internalDecimal = calculateSum(
                    quoteOpportunitiesCustomizableVO.getInternal1().multiply(manageCostMap.get(CostType.RA).getInsiderMonth()),
                    quoteOpportunitiesCustomizableVO.getInternal2().multiply(manageCostMap.get(CostType.DEV).getInsiderMonth()),
                    quoteOpportunitiesCustomizableVO.getInternal3().multiply(manageCostMap.get(CostType.TEST).getInsiderMonth()),
                    quoteOpportunitiesCustomizableVO.getInternal4().multiply(manageCostMap.get(CostType.UI).getInsiderMonth()),
                    quoteOpportunitiesCustomizableVO.getInternal5().multiply(manageCostMap.get(CostType.PM).getInsiderMonth())
            );
            totalSoftwareCosts = totalSoftwareCosts.add(internalDecimal);
            // 计算本地驻场
            BigDecimal localDecimal = calculateSum(
                    quoteOpportunitiesCustomizableVO.getDemandLocalWorkload().multiply(manageCostMap.get(CostType.RA).getLocalPresenceDays()),
                    quoteOpportunitiesCustomizableVO.getDevLocalWorkload().multiply(manageCostMap.get(CostType.DEV).getLocalPresenceDays()),
                    quoteOpportunitiesCustomizableVO.getTestLocalWorkload().multiply(manageCostMap.get(CostType.TEST).getLocalPresenceDays()),
                    quoteOpportunitiesCustomizableVO.getUiLocalWorkload().multiply(manageCostMap.get(CostType.UI).getLocalPresenceDays()),
                    quoteOpportunitiesCustomizableVO.getPmLocalWorkload().multiply(manageCostMap.get(CostType.PM).getLocalPresenceDays())
            ).multiply(PERSON_DAY_TO_MONTH_RATE);
            totalSoftwareCosts = totalSoftwareCosts.add(localDecimal);
            // 计算外地驻场
            BigDecimal externalDecimal = calculateSum(
                    quoteOpportunitiesCustomizableVO.getDemandExternalWorkload().multiply(manageCostMap.get(CostType.RA).getRemotePresenceDays()),
                    quoteOpportunitiesCustomizableVO.getDevExternalWorkload().multiply(manageCostMap.get(CostType.DEV).getRemotePresenceDays()),
                    quoteOpportunitiesCustomizableVO.getTestExternalWorkload().multiply(manageCostMap.get(CostType.TEST).getRemotePresenceDays()),
                    quoteOpportunitiesCustomizableVO.getUiExternalWorkload().multiply(manageCostMap.get(CostType.UI).getRemotePresenceDays()),
                    quoteOpportunitiesCustomizableVO.getPmExternalWorkload().multiply(manageCostMap.get(CostType.PM).getRemotePresenceDays())
            ).multiply(PERSON_DAY_TO_MONTH_RATE);
            totalSoftwareCosts = totalSoftwareCosts.add(externalDecimal);
            // 软件成本总计（元）
            quoteOpportunitiesCustomizableVO.setTotalSoftwareCosts(totalSoftwareCosts);

            // 处理小数问题
            //roundFieldsToTwoDecimalPlaces(Collections.singletonList(quoteOpportunitiesCustomizableVO));

            return quoteOpportunitiesCustomizableVO;
        }
        return null;
    }


    /**
     * 获取人月统计计算
     * @param startingValue 初始值
     * @param unit 单位
     * @param changeValue 变化的值
     * @return 结果
     */
    private static BigDecimal getTypeCalculation(BigDecimal startingValue, String unit, BigDecimal changeValue) {
        if(ObjectUtil.isNotNull(changeValue) && changeValue.compareTo(BigDecimal.ZERO) > 0){
            if(StringUtils.isNotEmpty(unit) && unit.equals(UnitType.HUMAN_MONTH.getCode())){
                return changeValue.add(startingValue);
            }else if (StringUtils.isNotEmpty(unit) && unit.equals(UnitType.HUMAN_NATURE.getCode())){
                BigDecimal bigDecimal = startingValue.add(changeValue.divide(PERSON_DAY_TO_MONTH_RATE, 8, RoundingMode.HALF_EVEN));
                return bigDecimal;
            }
        }
       return startingValue;
    }

    private BigDecimal calculateSum(BigDecimal... values) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            if (value != null) {
                sum = sum.add(value);
            }
        }
        return sum;
    }

    /**
     * 将列表项中的所有BigDecember字段四舍五入到小数点后2位。
     * @param list QuoteOpportunitiesCustomizableVO对象列表
     */
    /*public static void roundFieldsToTwoDecimalPlaces(List<QuoteOpportunitiesCustomizableVO> list) {
        for (QuoteOpportunitiesCustomizableVO vo : list) {
            vo.setInternal1(roundBigDecimal(vo.getInternal1()));
            vo.setInternal2(roundBigDecimal(vo.getInternal2()));
            vo.setInternal3(roundBigDecimal(vo.getInternal3()));
            vo.setInternal4(roundBigDecimal(vo.getInternal4()));
            vo.setInternal5(roundBigDecimal(vo.getInternal5()));
            vo.setDemandLocalWorkload(roundBigDecimal(vo.getDemandLocalWorkload()));
            vo.setPmLocalWorkload(roundBigDecimal(vo.getPmLocalWorkload()));
            vo.setUiLocalWorkload(roundBigDecimal(vo.getUiLocalWorkload()));
            vo.setTestLocalWorkload(roundBigDecimal(vo.getTestLocalWorkload()));
            vo.setDevLocalWorkload(roundBigDecimal(vo.getDevLocalWorkload()));
            vo.setDemandExternalWorkload(roundBigDecimal(vo.getDemandExternalWorkload()));
            vo.setPmExternalWorkload(roundBigDecimal(vo.getPmExternalWorkload()));
            vo.setUiExternalWorkload(roundBigDecimal(vo.getUiExternalWorkload()));
            vo.setTestExternalWorkload(roundBigDecimal(vo.getTestExternalWorkload()));
            vo.setDevExternalWorkload(roundBigDecimal(vo.getDevExternalWorkload()));
            vo.setTotalSoftwareCosts(roundBigDecimal(vo.getTotalSoftwareCosts()));
        }
    }*/

    /**
     * Helper method to round a BigDecimal to 2 decimal places.
     * 将BigDecember四舍五入到小数点后2位的帮助方法。
     * @param value The BigDecimal value to be rounded
     * @return The rounded BigDecimal value with 2 decimal places
     */
    /*private static BigDecimal roundBigDecimal(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }*/
}
