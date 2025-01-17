package com.internal.quote.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.core.domain.entity.SysDept;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.enums.QuoteStatusEnum;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.utils.MapUtil;
import com.internal.common.utils.StrUtil;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.dto.BaseDTO;
import com.internal.quote.dto.QuoteRadioDTO;
import com.internal.quote.mapper.QuoteOpportunitiesMapper;
import com.internal.quote.vo.QuoteRadioVO;
import com.internal.quote.vo.SysDeptVO;
import com.internal.system.mapper.SysDeptMapper;
import com.internal.system.mapper.SysUserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteRadioMapper;
import com.internal.quote.domain.QuoteRadio;
import com.internal.quote.service.IQuoteRadioService;

/**
 * 报价系统-报价部门占比Service业务层处理
 * 
 * @author internal
 * @date 2025-01-07
 */
@Service
@AllArgsConstructor
public class QuoteRadioServiceImpl extends ServiceImpl<QuoteRadioMapper, QuoteRadio> implements IQuoteRadioService
{

    private final QuoteRadioMapper quoteRadioMapper;
    private final QuoteOpportunitiesMapper quoteMapper;
    private final SysUserMapper userMapper;
    private final SysDeptMapper deptMapper;

    /**
     * 查询报价系统-报价部门占比
     * 
     * @param id 报价系统-报价部门占比主键
     * @return 报价系统-报价部门占比
     */
    @Override
    public QuoteRadio selectQuoteRadioById(Long id)
    {
        return quoteRadioMapper.selectQuoteRadioById(id);
    }

    /**
     * 查询报价系统-报价部门占比列表
     * 
     * @param quoteRadio 报价系统-报价部门占比
     * @return 报价系统-报价部门占比
     */
    @Override
    public List<QuoteRadio> selectQuoteRadioList(QuoteRadio quoteRadio)
    {
        return quoteRadioMapper.selectQuoteRadioList(quoteRadio);
    }

    /**
     * 新增报价系统-报价部门占比
     * 
     * @param quoteRadio 报价系统-报价部门占比
     * @return 结果
     */
    @Override
    public int insertQuoteRadio(QuoteRadio quoteRadio)
    {
        quoteRadio.setCreateTime(DateUtils.getNowDate());
        return quoteRadioMapper.insert(quoteRadio);
    }

    /**
     * 修改报价系统-报价部门占比
     * 
     * @param quoteRadio 报价系统-报价部门占比
     * @return 结果
     */
    @Override
    public int updateQuoteRadio(QuoteRadio quoteRadio)
    {
        quoteRadio.setUpdateTime(DateUtils.getNowDate());
        return quoteRadioMapper.updateById(quoteRadio);
    }

    /**
     * 批量删除报价系统-报价部门占比
     * 
     * @param ids 需要删除的报价系统-报价部门占比主键
     * @return 结果
     */
    @Override
    public int deleteQuoteRadioByIds(Long[] ids)
    {
        return quoteRadioMapper.deleteQuoteRadioByIds(ids);
    }

    /**
     * 删除报价系统-报价部门占比信息
     * 
     * @param id 报价系统-报价部门占比主键
     * @return 结果
     */
    @Override
    public int deleteQuoteRadioById(Long id)
    {
        return quoteRadioMapper.deleteQuoteRadioById(id);
    }

    /**
     * 用于成本报价点确定时
     * @param dto
     * @return
     */
    @Override
    public List<QuoteRadioVO> getQuoteDept(QuoteRadioDTO dto) {
        //由于服务引用问题，无法调用quoteService里的方法，所以现写一下
        QuoteOpportunities currentQuote = quoteMapper.selectById(dto.getOpportunitiesId());
        if(ObjectUtil.isEmpty(currentQuote)){
            throw new RuntimeException("无效的报价信息");
        }
        List<QuoteOpportunities> aboutQuoteList = quoteMapper.selectList(Wrappers.<QuoteOpportunities>lambdaQuery()
                .eq(QuoteOpportunities::getSupportCrmId,currentQuote.getSupportCrmId()));
        QuoteOpportunities quote = aboutQuoteList.stream().filter(x -> !QuoteStatusEnum.ABANDON.getCode().equals(x.getStatus())).findFirst().orElse(null);
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价信息");
        }

        List<QuoteRadioVO> emptyList = getEmptyRadioList(quote);
        if(ObjectUtil.isNotEmpty(dto.getCostId())){
            List<QuoteRadioVO> radioList = getRadioList(dto.getCostId());
            Set<Long> emptyDeptIds = emptyList.stream()
                    .filter(x -> ObjectUtil.isNotEmpty(x) && ObjectUtil.isNotEmpty(x.getDeptId()))
                    .map(x -> x.getDeptId())
                    .collect(Collectors.toSet());
            Set<Long> radioDeptIds = radioList.stream()
                    .filter(x -> ObjectUtil.isNotEmpty(x) && ObjectUtil.isNotEmpty(x.getDeptId()))
                    .map(x -> x.getDeptId())
                    .collect(Collectors.toSet());

            if(emptyDeptIds.containsAll(radioDeptIds) && radioDeptIds.containsAll(emptyDeptIds)){
                //如果没换人,输出旧的(保证有值)
                return radioList;
            }else {
                //如果没换人了,输出空的
                return emptyList;
            }
        }else {
            return emptyList;
        }
    }

    /**
     * ,用于查询详细报价或粗略报价历史,如果换人了，就部根据历史查，而是创建一个新的RadioList
     * @param newQuote
     * @return
     */
    @Override
    public List<QuoteRadioVO> getEmptyRadioList(QuoteOpportunities newQuote)
    {
        List<String> crmIdList = StrUtil.splitList(newQuote.getSupportPerson(),",");
        if(CollUtil.isEmpty(crmIdList)){
            return new LinkedList<>();
        }
        List<SysUser> userList = userMapper.selectList(Wrappers.<SysUser>lambdaQuery()
                .in(SysUser::getCrmUserId,crmIdList));

        Set<Long> deptIdList = userList.stream().map(SysUser::getDeptId).collect(Collectors.toSet());
        if(CollUtil.isEmpty(deptIdList) || deptIdList.size() == 1){
            //如果只有一个部门，也返回空，防止单部门详情的时候显示这个区域
            return new LinkedList<>();
        }
        List<SysDept> deptList = deptMapper.selectList(Wrappers.<SysDept>lambdaQuery()
                .in(SysDept::getDeptId,deptIdList));

        List<QuoteRadioVO> resultList = new LinkedList<>();

        deptList.forEach(d ->{
            List<SysUser> users = userList.stream().filter(x -> x.getDeptId().equals(d.getDeptId())).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(users)){
                QuoteRadioVO vo = new QuoteRadioVO();
                vo.setDeptId(d.getDeptId());
                vo.setDeptName(d.getDeptName());

                vo.setOpportunitiesId(newQuote.getId());
                vo.setRadio(BigDecimal.ZERO);
                vo.setUserNames(
                        String.join(",",
                                users.stream()
                                        .filter(x -> ObjectUtil.isNotEmpty(x.getNickName()))
                                        .map(x -> x.getNickName())
                                        .collect(Collectors.toList())
                        ));
                vo.setSupportPerson(
                        String.join(",",
                                users.stream()
                                        .filter(x -> ObjectUtil.isNotEmpty(x.getCrmUserId()))
                                        .map(x -> x.getCrmUserId())
                                        .collect(Collectors.toList())
                        )
                );
                resultList.add(vo);
            }
        });
        return resultList;
    }

    /**
     * 用于查询详细报价或粗略报价历史,返回历史的
     * @param costId
     * @return
     */
    @Override
    public List<QuoteRadioVO> getRadioList(Long costId) {
        List<QuoteRadio> radioList = baseMapper.selectList(Wrappers.<QuoteRadio>lambdaQuery()
                .eq(QuoteRadio::getCostId,costId));
        if(CollUtil.isEmpty(radioList)){
            return new LinkedList<>();
        }
        Set<Long> deptIds = radioList.stream().filter(x -> ObjectUtil.isNotEmpty(x.getDeptId())).map(x -> x.getDeptId()).collect(Collectors.toSet());
        if(CollUtil.isEmpty(deptIds)){
            return new LinkedList<>();
        }

        Map<Long,SysDept> deptMap = deptMapper.selectList(Wrappers.<SysDept>lambdaQuery()
                .in(SysDept::getDeptId,deptIds))
                .stream().collect(Collectors.toMap(SysDept::getDeptId,x -> x));

        Set<String> userCrmIds = new HashSet<>();
        radioList.forEach(r ->{
            userCrmIds.addAll(
                    StrUtil.splitList(r.getSupportPerson(),",")
            );
        });
        if(CollUtil.isEmpty(userCrmIds)){
            return new LinkedList<>();
        }
        List<SysUser> userList = userMapper.selectList(Wrappers.<SysUser>lambdaQuery()
                        .in(SysUser::getCrmUserId,userCrmIds));

        List<QuoteRadioVO> result = BeanUtil.copyToList(radioList, QuoteRadioVO.class);
        result.forEach(r ->{
            SysDept dept = MapUtil.getMapValue(deptMap,r.getDeptId());
            if(ObjectUtil.isNotEmpty(dept)){
                r.setDeptName(dept.getDeptName());
            }

            List<SysUser> currentUsers = userList.stream().filter(x -> r.getDeptId().equals(x.getDeptId())).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(currentUsers)){
                r.setUserNames(
                        String.join(",",
                                currentUsers.stream().filter(x -> ObjectUtil.isNotEmpty(x.getNickName())).map(x -> x.getNickName()).collect(Collectors.toList())
                            )
                );
            }

        });

        return result;
    }
}
