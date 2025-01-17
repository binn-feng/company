package com.internal.quartz.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.system.domain.SystemEmail;
import com.internal.system.mapper.SystemEmailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author every
 */
@Component("EmailClearTask")
@Slf4j
public class EmailClearTask {

    @Autowired
    private SystemEmailMapper systemEmailMapper;

    public void clear() {
        log.info("----------开始执行邮件发送次数重置任务----------");
        systemEmailMapper.update(null, Wrappers.<SystemEmail>lambdaUpdate().set(SystemEmail::getSendNum, 0).isNotNull(SystemEmail::getId));
        log.info("----------执行邮件发送次数重置任务完成----------");
    }
}
