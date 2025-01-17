package com.internal.quote.util;

import cn.hutool.core.util.ObjectUtil;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.enums.QuoteMethod;
import com.internal.common.utils.DecimalUtil;
import com.internal.common.utils.MapUtil;
import com.internal.common.utils.StrUtil;
import com.internal.quote.domain.QuoteOpportunitiesRoughDetail;
import com.internal.quote.domain.QuotePresaleInfo;
import com.internal.quote.vo.QuoteOpportunitiesVO;
import com.internal.quote.vo.QuotePresaleInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author qmjia
 * @apiNote 工具类，存放一些无需查数据库的公共方法
 */
@Component
@Slf4j
public class QuoteUtil {

    /**
     * 判断粗略报价是不是只填了总价
     * @param roughDetail
     * @return
     */
    public Boolean roughDetailIsEmpty(QuoteOpportunitiesRoughDetail roughDetail){
        if(ObjectUtil.isEmpty(roughDetail)){
            return true;
        }
        if(DecimalUtil.addAll(
                roughDetail.getCustomAmount(),roughDetail.getSupportAmount(),roughDetail.getOtherAmount(),
                roughDetail.getSelfAmount(),roughDetail.getExternalAmount(),roughDetail.getImplementAmount(),
                roughDetail.getProductAmount()
        ).compareTo(BigDecimal.ZERO) > 0){
            return false;
        }
        return true;
    }

    /**
     * 判断粗略报价是不是只填了总价
     * @param info
     * @return
     */
    public Boolean roughDetailIsEmpty(QuotePresaleInfo info){
        if(ObjectUtil.isEmpty(info)){
            return true;
        }
        if(DecimalUtil.addAll(
                info.getPrePrice(),info.getDevPrice(),info.getProdPrice(),
                info.getOtherPrice(),info.getSelfPrice(),info.getExtPrice(),
                info.getImpExtQuote()
        ).compareTo(BigDecimal.ZERO) > 0){
            return false;
        }
        return true;
    }

    /**
     * 判断是否已经选择报价方式、是否有报价权限（如果选择了其他人报价，则视为暂时放弃权限）
     * @param vo
     * @param user
     */
    public void setQuoteMethod(QuoteOpportunitiesVO vo, SysUser user, Map<String,SysUser> userMap)
    {
        //如果只有一个人，那就默认已选择报价方式，且不是由别人报
        List<String> personList = StrUtil.splitList(vo.getSupportPerson(),",")
                .stream().filter(x -> ObjectUtil.isNotEmpty(x)).collect(Collectors.toList());
        if(personList.size() == 1){
            vo.setQuoteByOthers(false); // 判断是否由他人报价
            vo.setMethodSelected(true); // 判断是否有其他人是否选了有其他人报价
            return;
        } else if (personList.size() > 0) {
            Set<Long> deptIdList = new HashSet<>();
            personList.forEach(p ->{
                SysUser u = MapUtil.getMapValue(userMap,p);
                if(ObjectUtil.isNotEmpty(u)){
                    deptIdList.add(u.getDeptId());
                }
            });
            if(deptIdList.size() == 1){
                //如果所有人都在同一个部门，就无需弹窗
                vo.setQuoteByOthers(false);
                vo.setMethodSelected(true);
                return;
            }

        }


        //如果没选人
        if(QuoteMethod.DEFAULT.getCode().equals(vo.getQuoteMethod())){
            //还有选择权限
            vo.setQuoteByOthers(false);
            //没选人
            vo.setMethodSelected(false);
        }
        //单人报价的情况
        else if(QuoteMethod.SINGLE.getCode().equals(vo.getQuoteMethod())){
            List<String> quoterList = StrUtil.splitList(vo.getQuoters(),",");
            if(quoterList.size() == 0){
                //没有报价人
                if(vo.getNonQuoters().contains(user.getCrmUserId())){
                    //在不报价列表：说明已经选过了，是由其他人报价
                    vo.setMethodSelected(true);
                    vo.setQuoteByOthers(true);
                }else {
                    //不在不报价列表：说明还没有选过，是否由其他人报价给默认值false，还有选择权限
                    vo.setMethodSelected(false);
                    vo.setQuoteByOthers(false);
                }
            }else if(quoterList.size() == 1){
                //有报价人了,一定视为选过了
                vo.setMethodSelected(true);
                if(user.isAdmin()){
                    //如果是管理员，也视为自己报价,可以报价
                    vo.setQuoteByOthers(false);
                }else {
                    //不是管理员，就正常逻辑
                    //报价人是这个人，就可以报价
                    if(quoterList.get(0).equals(user.getCrmUserId())){
                        vo.setQuoteByOthers(false);
                    }else {
                        //如果和报价人是同一个部门，也可以报价（任意一人没有部门就视为不在同一个部门）
                        SysUser quoterUser = MapUtil.getMapValue(userMap,quoterList.get(0));
                        if(ObjectUtil.isNotEmpty(quoterUser.getDeptId()) && ObjectUtil.isNotEmpty(user.getDeptId()) && quoterUser.getDeptId().equals(user.getDeptId())){
                            vo.setQuoteByOthers(false);
                        }else {
                            vo.setQuoteByOthers(true);
                        }
                    }
                }
            }else {
                //报价人有多个，说明数据出问题了
            }
        }else {
            //扩展：多人报价的情况(后面可能还会改逻辑)
            //一律视为选过报价方式了,不用再选了
            vo.setMethodSelected(true);
            //一律视为自己报价，每人都得报一次
            vo.setQuoteByOthers(false);
        }

    }

    /**
     * 给建议报价，对外报价和欣象对外报价取整
     * @param vo
     */
    public void toRoundPresaleVO(QuotePresaleInfoVO vo,RoundingMode mode){
        vo.setPreSugQuote(vo.getPreSugQuote().setScale(0, mode));
        vo.setPreExtQuote(vo.getPreExtQuote().setScale(0, mode));
        vo.setXxPreQuote(vo.getXxPreQuote().setScale(0, mode));

        vo.setDevSugQuote(vo.getDevSugQuote().setScale(0, mode));
        vo.setDevExtQuote(vo.getDevExtQuote().setScale(0, mode));
        vo.setXxDevQuote(vo.getXxDevQuote().setScale(0, mode));

        vo.setProdSugQuote(vo.getProdSugQuote().setScale(0, mode));
        vo.setProdExtQuote(vo.getProdExtQuote().setScale(0, mode));
        vo.setXxProdQuote(vo.getXxProdQuote().setScale(0, mode));

        vo.setOtherSugQuote(vo.getOtherSugQuote().setScale(0, mode));
        vo.setOtherExtQuote(vo.getOtherExtQuote().setScale(0, mode));
        vo.setXxOtherQuote(vo.getXxOtherQuote().setScale(0, mode));

        vo.setSelfSugQuote(vo.getSelfSugQuote().setScale(0, mode));
        vo.setSelfExtQuote(vo.getSelfExtQuote().setScale(0, mode));
        vo.setXxSelfQuote(vo.getXxSelfQuote().setScale(0, mode));

        vo.setExtSugQuote(vo.getExtSugQuote().setScale(0, mode));
        vo.setExtExtQuote(vo.getExtExtQuote().setScale(0, mode));
        vo.setXxExtQuote(vo.getXxExtQuote().setScale(0, mode));

        vo.setImpSugQuote(vo.getImpSugQuote().setScale(0, mode));
        vo.setImpExtQuote(vo.getImpExtQuote().setScale(0, mode));
        vo.setXxImpQuote(vo.getXxImpQuote().setScale(0, mode));
        vo.setXxExtProxyQuote(vo.getXxExtProxyQuote().setScale(0, mode));
    }

    /**
     * 给建议报价，对外报价和欣象对外报价取整
     * @param info
     */

    public void toRoundPresale(QuotePresaleInfo info,RoundingMode mode){
        info.setPreSugQuote(info.getPreSugQuote().setScale(0, mode));
        info.setPreExtQuote(info.getPreExtQuote().setScale(0, mode));
        info.setXxPreQuote(info.getXxPreQuote().setScale(0, mode));

        info.setDevSugQuote(info.getDevSugQuote().setScale(0, mode));
        info.setDevExtQuote(info.getDevExtQuote().setScale(0, mode));
        info.setXxDevQuote(info.getXxDevQuote().setScale(0, mode));

        info.setProdSugQuote(info.getProdSugQuote().setScale(0, mode));
        info.setProdExtQuote(info.getProdExtQuote().setScale(0, mode));
        info.setXxProdQuote(info.getXxProdQuote().setScale(0, mode));

        info.setOtherSugQuote(info.getOtherSugQuote().setScale(0, mode));
        info.setOtherExtQuote(info.getOtherExtQuote().setScale(0, mode));
        info.setXxOtherQuote(info.getXxOtherQuote().setScale(0, mode));

        info.setSelfSugQuote(info.getSelfSugQuote().setScale(0, mode));
        info.setSelfExtQuote(info.getSelfExtQuote().setScale(0, mode));
        info.setXxSelfQuote(info.getXxSelfQuote().setScale(0, mode));

        info.setExtSugQuote(info.getExtSugQuote().setScale(0, mode));
        info.setExtExtQuote(info.getExtExtQuote().setScale(0, mode));
        info.setXxExtQuote(info.getXxExtQuote().setScale(0, mode));

        info.setImpSugQuote(info.getImpSugQuote().setScale(0, mode));
        info.setImpExtQuote(info.getImpExtQuote().setScale(0, mode));
        info.setXxImpQuote(info.getXxImpQuote().setScale(0, mode));
    }

}
