package com.internal.quote.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.config.InternalConfig;
import com.internal.common.core.domain.AjaxResult;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.utils.StringUtils;
import com.internal.common.utils.file.FileUploadUtils;
import com.internal.common.utils.file.FileUtils;
import com.internal.quote.dto.UpLoadDTO;
import com.internal.quote.vo.QuoteOpportunitiesVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteOpportunitiesFileMapper;
import com.internal.quote.domain.QuoteOpportunitiesFile;
import com.internal.quote.service.IQuoteOpportunitiesFileService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 商机附件Service业务层处理
 * 
 * @author internal
 * @date 2024-10-28
 */
@Service
@AllArgsConstructor
public class QuoteOpportunitiesFileServiceImpl extends ServiceImpl<QuoteOpportunitiesFileMapper, QuoteOpportunitiesFile> implements IQuoteOpportunitiesFileService
{

    private final QuoteOpportunitiesFileMapper quoteOpportunitiesFileMapper;

    /**
     * 查询商机附件
     * 
     * @param id 商机附件主键
     * @return 商机附件
     */
    @Override
    public QuoteOpportunitiesFile selectQuoteOpportunitiesFileById(Long id)
    {
        return quoteOpportunitiesFileMapper.selectQuoteOpportunitiesFileById(id);
    }

    /**
     * 查询商机附件列表
     * 
     * @param quoteOpportunitiesFile 商机附件
     * @return 商机附件
     */
    @Override
    public List<QuoteOpportunitiesFile> selectQuoteOpportunitiesFileList(QuoteOpportunitiesFile quoteOpportunitiesFile)
    {
        return quoteOpportunitiesFileMapper.selectQuoteOpportunitiesFileList(quoteOpportunitiesFile);
    }

    /**
     * 新增商机附件
     * 
     * @param quoteOpportunitiesFile 商机附件
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesFile(QuoteOpportunitiesFile quoteOpportunitiesFile)
    {
        quoteOpportunitiesFile.setCreateTime(DateUtils.getNowDate());
        return quoteOpportunitiesFileMapper.insert(quoteOpportunitiesFile);
    }

    /**
     * 修改商机附件
     * 
     * @param quoteOpportunitiesFile 商机附件
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesFile(QuoteOpportunitiesFile quoteOpportunitiesFile)
    {
        quoteOpportunitiesFile.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesFileMapper.updateById(quoteOpportunitiesFile);
    }

    /**
     * 批量删除商机附件
     * 
     * @param ids 需要删除的商机附件主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesFileByIds(Long[] ids)
    {
        return quoteOpportunitiesFileMapper.deleteQuoteOpportunitiesFileByIds(ids);
    }

    /**
     * 删除商机附件信息
     * 
     * @param id 商机附件主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesFileById(Long id)
    {
        return quoteOpportunitiesFileMapper.deleteQuoteOpportunitiesFileById(id);
    }

    @Override
    public Boolean upLoadFiles(List<QuoteOpportunitiesFile> fileList) {
        for (QuoteOpportunitiesFile file: fileList) {
            quoteOpportunitiesFileMapper.insert(file);
        }
        return true;
    }

    @Override
    public Map<Long, List<QuoteOpportunitiesFile>> selectOpportunitiesFileMap(List<QuoteOpportunitiesVO> voList) {
        if(CollUtil.isEmpty(voList)){
            return null;
        }
        Set<Long> ids = voList.stream().map(x -> x.getId()).collect(Collectors.toSet());
        Map<Long, List<QuoteOpportunitiesFile>> reulstMap = baseMapper.selectList(Wrappers.<QuoteOpportunitiesFile>lambdaQuery()
                .in(QuoteOpportunitiesFile::getOpportunitiesId,ids)).stream().collect(Collectors.groupingBy(QuoteOpportunitiesFile::getOpportunitiesId));
        return reulstMap;
    }
}
