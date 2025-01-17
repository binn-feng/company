package com.internal.quartz.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.core.domain.dto.EmailModelInfoConvertDTO;
import com.internal.common.core.domain.dto.EmailSubjectInfoConvertDTO;
import com.internal.common.core.domain.dto.SendEmailInfoDTO;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.email.SystemConfig;
import com.internal.common.enums.*;
import com.internal.common.utils.MailUtil;
import com.internal.common.utils.ThreadUtil;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteOpportunitiesParent;
import com.internal.quote.dto.BusinessOpportunityDTO;
import com.internal.quote.mapper.QuoteEmailSettingMapper;
import com.internal.quote.mapper.QuoteOpportunitiesMapper;
import com.internal.quote.mapper.QuoteOpportunitiesParentMapper;
import com.internal.quote.mapper.SyncCrmMapper;
import com.internal.quote.util.QuoteMailUtil;
import com.internal.system.domain.SystemEmail;
import com.internal.system.mapper.SysUserMapper;
import com.internal.system.mapper.SystemEmailMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 定时任务调度测试
 *
 * @author every
 */
@Component("CrmSynOpportunitiesTask")
@Slf4j
@AllArgsConstructor
public class CrmSynOpportunitiesTask {

    private final SyncCrmMapper syncCrmMapper;
    private final SystemConfig systemConfig;
    private final QuoteOpportunitiesMapper quoteOpportunitiesMapper;
    private final QuoteOpportunitiesParentMapper quoteOpportunitiesParentMapper;
    private final SysUserMapper sysUserMapper;
    private final QuoteEmailSettingMapper quoteEmailSettingMapper;
    private final QuoteMailUtil quoteMailUtil;
    private final SystemEmailMapper systemEmailMapper;


    /**
     * CRM商机同步定时任务
     */
    public void sync() throws MessagingException, GeneralSecurityException {
        log.info("--------------开始CRM商机同步---------------");
        // 定义邮件相关信息
        String title = "";
        String msg = "";
        String strDetail = "";
        String content = "";
        boolean result = true;

        List<BusinessOpportunityDTO> businessOpportunityDTOList = null;
        try {
            businessOpportunityDTOList = syncCrmMapper.syncCrmInfo();
            log.info("businessOpportunityDTOList: {}", businessOpportunityDTOList);
        } catch (Exception e) {
            msg = "调用crm数据库同步失败，请查看邮件详情 ";
            strDetail = e.getMessage();
            log.error("调用crm数据库同步失败，请查看邮件详情 " + e.getMessage());
        }
        List<SendEmailInfoDTO> sendEmailInfoDTOList = new ArrayList<>();
        // 开始设置同步信息
        if (CollUtil.isNotEmpty(businessOpportunityDTOList)) {
            //同步本地的项目字典
            try {
                Map<String, Object> resMap = checkCrmProject(businessOpportunityDTOList);
                result = (Boolean) resMap.get("result");
                strDetail = resMap.get("strDetail") == null ? "" :  (String) resMap.get("strDetail");
                sendEmailInfoDTOList = Optional.ofNullable(resMap.get("sendEmailInfoDTOList"))
                        .map(list -> (List<?>) list)  // 不强转为具体的泛型类型
                        .map(list -> list.stream()  // 使用 Stream 过滤类型
                                .filter(SendEmailInfoDTO.class::isInstance)
                                .map(SendEmailInfoDTO.class::cast)
                                .collect(Collectors.toList()))
                        .orElseGet(ArrayList::new);
            } catch (Exception e) {
                msg = "同步出现异常，请查看邮件详情 ";
                strDetail = strDetail + e.getMessage();
            }

            if (ObjectUtil.isNotEmpty(result) && result) {
                title = "CRM项目名同步【成功】";
            } else {
                title = "CRM项目名同步【异常】";
            }
        }

        //指定收件人
        SysUser user = new SysUser();
        user.setEmail(systemConfig.getSyncProjectManagerEmail());
        user.setNickName(systemConfig.getSyncProjectManagerName());
        List<SysUser> receiveUser = Collections.singletonList(user);
        // 获取邮件信息
        SystemEmail systemEmail = systemEmailMapper.selectOne(Wrappers.<SystemEmail>lambdaQuery()
                .eq(SystemEmail::getType, "QUOTE")
                .ge(SystemEmail::getSendNum, 0)
                .lt(SystemEmail::getSendNum, 90).last("limit 1"));
        if(ObjectUtil.isNotEmpty(systemEmail)){
            log.info("----------开发发送同步邮件----------");
            //发送邮件
            MailUtil.sendEmailToSyncProjectManager("true", systemEmail.getHost(),
                    systemEmail.getUsername(), systemEmail.getPassword(),
                    title, msg + " <br/> " + strDetail + content,
                    false, null, true, receiveUser);
            log.info("----------开发发送同步邮件成功----------");
            systemEmail.setSendNum(systemEmail.getSendNum() + 1);
            systemEmailMapper.updateById(systemEmail);

            // 发送邮件
            List<SendEmailInfoDTO> finalSendEmailInfoDTOList = sendEmailInfoDTOList;
            ThreadUtil.runInThread(() ->{
                quoteMailUtil.sendEmail(finalSendEmailInfoDTOList,EmailTypeEnum.NEED_COST_QUOTATION.getCode());
            });
        }else{
            log.error("邮件已无发送次数。");
        }

        log.info("--------------结束执行CRM商机同步---------------");
    }

    /**
     * 通过更新时间判断同步数据是否一致
     *
     * @param businessOpportunityDTOList businessOpportunityDTOList
     * @return Map<String, Object>
     */
    private Map<String, Object> checkCrmProject(List<BusinessOpportunityDTO> businessOpportunityDTOList) {
        // 公共参数
        AtomicReference<String> errorQuoteName = new AtomicReference<>("");
        AtomicReference<String> successQuoteName = new AtomicReference<>("");
        Map<String, Object> resultMap = new HashMap<>();
        AtomicReference<String> strDetail = new AtomicReference<>("");
        Set<String> quoteId = new HashSet<>(); // 解决商机重复添加
        Set<String> supportId = new HashSet<>(); // 解决商机详情重复添加
        List<SysUser> receiveUser = new ArrayList<>(); // 收件人
        Map<String, Boolean> isNewData = new HashMap<>(); // 解决一条商机发送两封邮件

        List<SendEmailInfoDTO> sendEmailInfoDTOList = new ArrayList<>();
        try {
            // 获取系统已存在数据
            List<QuoteOpportunities> existsQuoteOpportunitiesList = quoteOpportunitiesMapper.selectList(Wrappers.lambdaQuery());
            List<QuoteOpportunitiesParent> existsQuoteOpportunitiesParentList = quoteOpportunitiesParentMapper.selectList(Wrappers.lambdaQuery());
            List<SysUser> sysUserList = sysUserMapper.selectList(Wrappers.lambdaQuery());
            Map<String, SysUser> sysUserInfoMap = sysUserList.stream().collect(Collectors.toMap(SysUser::getCrmUserId, Function.identity(), (v1, v2) -> v1));
            // 数据处理，如果相关数据不存在则保存，存在需要判断更新时间是否一致
            businessOpportunityDTOList.forEach(item -> {
                boolean detailInfoFlag = false;
                StringBuilder emailTemplateBuilder = new StringBuilder();
                errorQuoteName.set(item.getName() + item.getVdef3());
                // 处理商机主表信息
                QuoteOpportunitiesParent quoteOpportunitiesParent = existsQuoteOpportunitiesParentList.stream().filter(existItem ->
                        ObjectUtil.isNotEmpty(existItem.getCrmId()) && existItem.getCrmId().equals(item.getId())).findFirst().orElse(null);
                if (!quoteId.contains(item.getId())) {
                    if (ObjectUtil.isNotNull(quoteOpportunitiesParent)) {
                        syncQuoteParentLogic(strDetail, item, quoteOpportunitiesParent);
                    } else {
                        quoteOpportunitiesParent = new QuoteOpportunitiesParent();
                        fillQuoteOpportunitiesParentInfo(item, quoteOpportunitiesParent);
                        quoteOpportunitiesParent.setStatus(QuoteParentStatusEnum.PROCESSING.getCode());
                        quoteOpportunitiesParent.setSaleId(sysUserInfoMap.get(item.getUserId()).getUserId());
                        quoteOpportunitiesParentMapper.insert(quoteOpportunitiesParent);
                        // 同步本地缓存列表
                        quoteId.add(item.getId());
                        existsQuoteOpportunitiesParentList.add(quoteOpportunitiesParent);
                        strDetail.set(strDetail + emailTemplateBuilder.append("<h2>商机信息</h2>")
                                    .append("<p><strong>名称: </strong> ").append(item.getName()).append("</p>")
                                    .append("<p><strong>状态: </strong> 已成功进行新增</p>").toString());
                        detailInfoFlag = true;
                    }
                    // 同步本地缓存列表
                    quoteId.add(item.getId());
                }
               // 处理明细 判断item.getVdef2()中是否包含, 如果有将字段拆分成数组,拆分的目的主要是给不同的售前发邮件
                if (item.getVdef2().contains(",")) {
                    String[] supportIds = item.getVdef2().split(",");
                    if(detailInfoFlag){
                        strDetail.set(strDetail + "<h3>商机明细</h3>" + "<ul>");
                    }
                    for (String saleSupportId : supportIds) {
                        Boolean aBoolean = quoteDetailLogic(strDetail, supportId, existsQuoteOpportunitiesList, item,
                                quoteOpportunitiesParent, item.getVdef2(), fillSaleName(item, sysUserInfoMap, new StringBuilder()),
                                sysUserInfoMap, receiveUser, isNewData);
                        // 如果从CRM新同步过来的的数据，记录商机信息，发邮件
                        boolean flag = (MapUtils.isNotEmpty(isNewData)  && isNewData.containsKey(item.getSupportId()) && isNewData.get(item.getSupportId())) || aBoolean;
                        if (flag){
                            SendEmailInfoDTO sendEmailInfoDTO = new SendEmailInfoDTO();
                            sendEmailInfoDTO.setSysUser(sysUserInfoMap.get(saleSupportId));
                            sendEmailInfoDTO.setEmailSubjectInfoConvertDTO(EmailSubjectInfoConvertDTO.builder()
                                            .businessSubject(item.getName())
                                            .currentDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")).build());
                            sendEmailInfoDTO.setEmailModelInfoConvertDTO(EmailModelInfoConvertDTO.builder()
                                            .businessSubject(item.getName())
                                            .quoteSystemLink(systemConfig.getDomainName()).build());
                            sendEmailInfoDTO.setCopyUser(new ArrayList<SysUser>(){{
                                add(sysUserInfoMap.get(item.getUserId()));
                            }});
                            sendEmailInfoDTOList.add(sendEmailInfoDTO);
                        }
                    }
                    if(detailInfoFlag){
                        strDetail.set(strDetail + "</ul>"); // 使用无序列表来展示明细信息
                    }
                }else{
                    if(detailInfoFlag){
                        strDetail.set(strDetail + "<h3>商机明细</h3>" + "<ul>");
                    }
                    Boolean aBoolean = quoteDetailLogic(strDetail, supportId, existsQuoteOpportunitiesList, item,
                            quoteOpportunitiesParent, item.getVdef2(), fillSaleName(item, sysUserInfoMap, new StringBuilder()),
                            sysUserInfoMap, receiveUser,isNewData);
                    if (aBoolean){
                        SendEmailInfoDTO sendEmailInfoDTO = new SendEmailInfoDTO();
                        sendEmailInfoDTO.setSysUser(sysUserInfoMap.get(item.getVdef2()));
                        sendEmailInfoDTO.setEmailSubjectInfoConvertDTO(EmailSubjectInfoConvertDTO.builder()
                                .businessSubject(item.getName())
                                .currentDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")).build());
                        sendEmailInfoDTO.setEmailModelInfoConvertDTO(EmailModelInfoConvertDTO.builder()
                                .businessSubject(item.getName())
                                .quoteSystemLink(systemConfig.getDomainName()).build());
                        sendEmailInfoDTO.setCopyUser(new ArrayList<SysUser>(){{
                            add(sysUserInfoMap.get(item.getUserId()));
                        }});
                        sendEmailInfoDTOList.add(sendEmailInfoDTO);
                    }
                    if(detailInfoFlag){
                        strDetail.set(strDetail + "</ul>"); // 使用无序列表来展示明细信息
                    }
                }

            });

            resultMap.put("sendEmailInfoDTOList", sendEmailInfoDTOList);
        } catch (Exception e) {
            log.error("CRM商机数据同步异常", e);
            resultMap.put("result", Boolean.FALSE);
            strDetail.set(strDetail + errorQuoteName.get() + "该项目同步数据失败，请检查数据是否正确, 失败原因:  " + e);
            resultMap.put("strDetail", strDetail.get());
            return resultMap;
        }
        strDetail.set(strDetail + "同步数据已全部成功。");
        resultMap.put("strDetail", strDetail.get());
        resultMap.put("result", Boolean.TRUE);
        return resultMap;
    }


    /**
     * 商机明细处理
     * @param strDetail 邮件内容
     * @param supportId 售前人员ID
     * @param existsQuoteOpportunitiesList 系统明细已存在数据
     * @param item item
     * @param quoteOpportunitiesParent 商机主表信息
     * @param saleSupportId 售前ID
     * @param saleSupportName 售前名称
     */
    private Boolean quoteDetailLogic(AtomicReference<String> strDetail, Set<String> supportId,
                                  List<QuoteOpportunities> existsQuoteOpportunitiesList,
                                  BusinessOpportunityDTO item,
                                  QuoteOpportunitiesParent quoteOpportunitiesParent,
                                  String saleSupportId,String saleSupportName,
                                  Map<String, SysUser> sysUserInfoMap,
                                  List<SysUser> receiveUser,Map<String, Boolean> isNewData) {
        boolean isInsertable = false;
        // 处理售前支持明细
        QuoteOpportunities quoteOpportunities = existsQuoteOpportunitiesList
                .stream().filter(existItem -> ObjectUtil.isNotEmpty(existItem.getSupportCrmId())
                        && existItem.getSupportCrmId().equals(item.getSupportId())
                        && !existItem.getStatus().equals(QuoteStatusEnum.ABANDON.getCode())).findFirst().orElse(null);
        StringBuilder emailTemplateBuilder = new StringBuilder();
        if (!supportId.contains(item.getSupportId())) {
            if (ObjectUtil.isNotNull(quoteOpportunities)) {
                syncQuoteLogic(strDetail, item, quoteOpportunities, quoteOpportunitiesParent, saleSupportId, saleSupportName);
            } else {
                isInsertable = true;
                isNewData.put(item.getSupportId(), true);
                quoteOpportunities = new QuoteOpportunities();
                assert quoteOpportunitiesParent != null;
                fillQuoteInfo(item, quoteOpportunities, quoteOpportunitiesParent, saleSupportId,saleSupportName);
                quoteOpportunities.setStatus(QuoteStatusEnum.WAIT_COST_QUOTE.getCode());
                quoteOpportunities.setSupportPerson(item.getVdef2());
                // 设置创建人
                SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getCrmUserId, item.getUserId()));
                if(ObjectUtil.isNotEmpty(sysUser)){
                    quoteOpportunities.setCreateBy(String.valueOf(sysUser.getUserId()));
                }else{
                    quoteOpportunities.setCreateBy("1");
                }
                if(!item.getVdef2().contains(",")){
                    quoteOpportunities.setQuoteMethod(QuoteMethod.SINGLE.getCode());
                    quoteOpportunities.setQuoters(item.getVdef2());
                }
                quoteOpportunitiesMapper.insert(quoteOpportunities);
                existsQuoteOpportunitiesList.add(quoteOpportunities);
                strDetail.set(strDetail + emailTemplateBuilder.append("<li>")
                                .append("<strong>明细名称: </strong> ").append(item.getVdef3())
                                .append("<p><strong>所属售前支持人员: </strong> ").append(saleSupportName).append("</p>")
                                .append("<strong>状态: </strong> 已成功进行新增")
                                .append("</li>").toString());
                // 设置收件人员
                if(MapUtils.isNotEmpty(sysUserInfoMap)){
                    receiveUser.add(sysUserInfoMap.get(saleSupportId));
                }
            }
            // 同步本地缓存列表
            supportId.add(item.getSupportId());
        }
        return isInsertable;
    }


    /**
     * 商机售前支持明细同步逻辑
     * CRM时间和本地时间都为null：不更新。
     * CRM时间为null：不更新。
     * CRM时间不为null，本地时间为null：进行更新。
     * CRM时间不为null，本地时间不为null，且CRM时间不等于本地时间：进行更新。
     * CRM时间不为null，本地时间不为null，且CRM时间等于本地时间：不更新。
     *
     * @param strDetail                信息
     * @param item                     子项
     * @param quoteOpportunities       商机信息
     * @param quoteOpportunitiesParent 商机售前支持信息
     * @param saleSupportId 商机售前人员ID
     */
    private void syncQuoteLogic(AtomicReference<String> strDetail,
                                BusinessOpportunityDTO item,
                                QuoteOpportunities quoteOpportunities,
                                QuoteOpportunitiesParent quoteOpportunitiesParent,
                                String saleSupportId, String saleSupportName) {
        // 判断更新时间是否应该进行更新
        if (ObjectUtil.isNotEmpty(item.getSupportUpdateTime()) || ObjectUtil.isNotEmpty(quoteOpportunities.getCrmUpdateTime())) {
            StringBuilder emailTemplateBuilder = new StringBuilder();
            // 当CRM时间不为null且本地时间为null，更新本地商机明细
            if (ObjectUtil.isNotEmpty(item.getSupportUpdateTime()) && ObjectUtil.isEmpty(quoteOpportunities.getCrmUpdateTime())) {
                fillQuoteInfo(item, quoteOpportunities, quoteOpportunitiesParent, saleSupportId, saleSupportName);
                quoteOpportunitiesMapper.updateById(quoteOpportunities);
                strDetail.set(strDetail + emailTemplateBuilder.append("<li>")
                        .append("<strong>明细名称: </strong> ").append(item.getVdef3())
                        .append("<p><strong>所属售前支持人员: </strong> ").append(saleSupportName).append("</p>")
                        .append(", <strong>状态: </strong> 由于本地时间为空同时CRM时间不为空，数据已进行同步更新")
                        .append("</li>").toString());
            } else if (ObjectUtil.isNotEmpty(item.getSupportUpdateTime()) && ObjectUtil.isNotEmpty(quoteOpportunities.getCrmUpdateTime())
                    && item.getSupportUpdateTime().getTime() != quoteOpportunities.getCrmUpdateTime().getTime()) {
                // 当CRM时间和本地时间都不为null，且CRM时间不等于本地时间，更新本地商机明细
                fillQuoteInfo(item, quoteOpportunities, quoteOpportunitiesParent, saleSupportId, saleSupportName);
                quoteOpportunitiesMapper.updateById(quoteOpportunities);
                strDetail.set(strDetail + emailTemplateBuilder.append("<li>")
                        .append("<strong>明细名称: </strong> ").append(item.getVdef3())
                        .append("<p><strong>所属售前支持人员: </strong> ").append(saleSupportName).append("</p>")
                        .append(", <strong>状态: </strong> 由于数据时间不一致，数据已进行同步更新")
                        .append("</li>").toString());

            }
//            else if (ObjectUtil.isNotEmpty(item.getSupportUpdateTime()) && ObjectUtil.isNotEmpty(quoteOpportunities.getUpdateTime())
//                    && item.getSupportUpdateTime().getTime() == quoteOpportunities.getUpdateTime().getTime()) {
//                // 当CRM时间和本地时间都不为null，且它们相等，不更新
//                strDetail.set(strDetail + emailTemplateBuilder.append("<li>")
//                        .append("<strong>明细名称: </strong> ").append(item.getVdef3())
//                        .append("<p><strong>所属售前支持人员: </strong> ").append(saleSupportName).append("</p>")
//                        .append(", <strong>状态: </strong> 由于数据时间一致，无需进行更新数据")
//                        .append("</li>").toString());
//            } else if (ObjectUtil.isEmpty(item.getSupportUpdateTime()) && ObjectUtil.isEmpty(quoteOpportunities.getUpdateTime())) {
//                // CRM时间和本地时间都为null，不更新
//                strDetail.set(strDetail + emailTemplateBuilder.append("<li>")
//                        .append("<strong>明细名称: </strong> ").append(item.getVdef3())
//                        .append("<p><strong>所属售前支持人员: </strong> ").append(saleSupportName).append("</p>")
//                        .append(", <strong>状态: </strong> 由于CRM和本地时间均为空，不需要更新数据")
//                        .append("</li>").toString());
//            } else if (ObjectUtil.isEmpty(item.getSupportUpdateTime())) {
//                // CRM时间为null，不更新
//                strDetail.set(strDetail + emailTemplateBuilder.append("<li>")
//                        .append("<strong>明细名称: </strong> ").append(item.getVdef3())
//                        .append("<p><strong>所属售前支持人员: </strong> ").append(saleSupportName).append("</p>")
//                        .append(", <strong>状态: </strong> 由于CRM时间为空，不需要更新数据")
//                        .append("</li>").toString());
//            }
        }
    }

    /**
     * 商机售前支持同步逻辑
     * CRM时间和本地时间都为null：不更新。
     * CRM时间为null：不更新。
     * CRM时间不为null，本地时间为null：进行更新。
     * CRM时间不为null，本地时间不为null，且CRM时间不等于本地时间：进行更新。
     * CRM时间不为null，本地时间不为null，且CRM时间等于本地时间：不更新。
     *
     * @param strDetail                信息
     * @param item                     子项
     * @param quoteOpportunitiesParent 商机信息
     */
    private void syncQuoteParentLogic(AtomicReference<String> strDetail, BusinessOpportunityDTO item, QuoteOpportunitiesParent quoteOpportunitiesParent) {
        StringBuilder emailTemplateBuilder = new StringBuilder();
        // 判断是否需要同步CRM商机到本地
        if (ObjectUtil.isNotEmpty(item.getUpdateTime()) || ObjectUtil.isNotEmpty(quoteOpportunitiesParent.getCrmUpdateTime())) {
            if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isEmpty(quoteOpportunitiesParent.getCrmUpdateTime())) {
                // 当CRM时间不为null且本地时间为null，更新本地商机
                fillQuoteOpportunitiesParentInfo(item, quoteOpportunitiesParent);
                quoteOpportunitiesParentMapper.updateById(quoteOpportunitiesParent);
                if (!strDetail.toString().contains(item.getName())) {
                    strDetail.set(strDetail + emailTemplateBuilder.append("<h2>商机信息</h2>")
                            .append("<p><strong>名称: </strong> ").append(item.getName()).append("</p>")
                            .append("<p><strong>状态: </strong> 由于本地时间为空同时CRM时间不为空，已进行数据更新</p>").toString());
                }
            } else if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isNotEmpty(quoteOpportunitiesParent.getCrmUpdateTime())
                    && item.getUpdateTime().getTime() != quoteOpportunitiesParent.getCrmUpdateTime().getTime()) {
                // 当CRM时间和本地时间都不为null，且CRM时间不等于本地时间，更新本地商机
                fillQuoteOpportunitiesParentInfo(item, quoteOpportunitiesParent);
                quoteOpportunitiesParentMapper.updateById(quoteOpportunitiesParent);
                if (!strDetail.toString().contains(item.getName())) {
                    strDetail.set(strDetail + emailTemplateBuilder.append("<h2>商机信息</h2>")
                            .append("<p><strong>名称: </strong> ").append(item.getName()).append("</p>")
                            .append("<p><strong>状态: </strong> 由于数据时间不一致，已进行数据更新</p>").toString());
                }
            }
//            else if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isNotEmpty(quoteOpportunitiesParent.getUpdateTime())
//                    && item.getUpdateTime().getTime() == quoteOpportunitiesParent.getUpdateTime().getTime()) {
//                if (!strDetail.toString().contains(item.getName())) {
//                    // 当CRM时间和本地时间都不为null，且它们相等，不更新
//                    strDetail.set(strDetail + emailTemplateBuilder.append("<h2>商机信息</h2>")
//                            .append("<p><strong>名称: </strong> ").append(item.getName()).append("</p>")
//                            .append("<p><strong>状态: </strong> 由于数据时间一致，无需进行数据更新</p>").toString());
//                }
//            } else if (ObjectUtil.isEmpty(item.getUpdateTime()) && ObjectUtil.isEmpty(quoteOpportunitiesParent.getUpdateTime())) {
//                // CRM时间和本地时间都为null，不更新
//                if (!strDetail.toString().contains(item.getName())) {
//                    strDetail.set(strDetail + emailTemplateBuilder.append("<h2>商机信息</h2>")
//                            .append("<p><strong>名称: </strong> ").append(item.getName()).append("</p>")
//                            .append("<p><strong>状态: </strong> 由于CRM和本地时间均为空，不需要数据更新</p>").toString());
//                }
//            } else if (ObjectUtil.isEmpty(item.getUpdateTime())) {
//                // CRM时间为null，不更新
//                if (!strDetail.toString().contains(item.getName())) {
//                    strDetail.set(strDetail + emailTemplateBuilder.append("<h2>商机信息</h2>")
//                            .append("<p><strong>名称: </strong> ").append(item.getName()).append("</p>")
//                            .append("<p><strong>状态: </strong> 由于CRM时间为空，不需要数据更新</p>").toString());
//                }
//            }
        }
    }

    /**
     * 填充商机售前支持信息
     *
     * @param item                     item
     * @param quoteOpportunities       quoteOpportunities
     * @param quoteOpportunitiesParent quoteOpportunitiesParent
     */
    private static void fillQuoteInfo(BusinessOpportunityDTO item,
                                      QuoteOpportunities quoteOpportunities,
                                      QuoteOpportunitiesParent quoteOpportunitiesParent,
                                      String saleSupportId,String saleSupportName) {
        quoteOpportunities.setOpportunitiesParentId(quoteOpportunitiesParent.getId());
        quoteOpportunities.setSupportCrmId(item.getSupportId());
        quoteOpportunities.setSupportType(item.getSupportType());
        quoteOpportunities.setSupportIdentification(item.getVdef3());
        quoteOpportunities.setSupportPerson(saleSupportId);
        quoteOpportunities.setSupportName(saleSupportName);
        quoteOpportunities.setCrmUpdateTime(item.getSupportUpdateTime());
    }

    /**
     * 填充商机信息
     *
     * @param item                     item
     * @param quoteOpportunitiesParent quoteOpportunitiesParent
     */
    private static void fillQuoteOpportunitiesParentInfo(BusinessOpportunityDTO item, QuoteOpportunitiesParent quoteOpportunitiesParent) {
        quoteOpportunitiesParent.setCrmId(item.getId());
        quoteOpportunitiesParent.setCrmStage(item.getStage());
        quoteOpportunitiesParent.setCrmState(item.getCurrentState());
        quoteOpportunitiesParent.setName(item.getName());
        quoteOpportunitiesParent.setCategory(item.getProductName());
        quoteOpportunitiesParent.setSaleName(item.getUserName());
        quoteOpportunitiesParent.setCustomersName(item.getCustomerName());
        quoteOpportunitiesParent.setIntroduce(item.getInfoDesc());
        quoteOpportunitiesParent.setCrmUpdateTime(item.getUpdateTime());
    }

    // 处理多售前名称
    private static String fillSaleName(BusinessOpportunityDTO item, Map<String, SysUser> sysUserInfoMap, StringBuilder supportName) {
        String[] saleID = item.getVdef2().split(",");
        for (String saleId : saleID) {
            // 判断sysUserInfoMap.get(saleId).getNickName() 非null
            if (ObjectUtil.isNotEmpty(sysUserInfoMap.get(saleId))){
                supportName.append(sysUserInfoMap.get(saleId).getNickName());
            }else{
                supportName.append("未知售前");
            }
            // 如果是最后一个不添加逗号
            if (!saleID[saleID.length - 1].equals(saleId)) {
                supportName.append(",");
            }
        }
        return supportName.toString();
    }
}
