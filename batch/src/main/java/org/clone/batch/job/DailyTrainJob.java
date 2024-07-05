package org.clone.batch.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import org.clone.batch.feign.BusinessFeign;
import org.clone.common.response.CommonResponse;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Date;

@DisallowConcurrentExecution
public class DailyTrainJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainJob.class);

    @Resource
    private BusinessFeign businessFeign;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        MDC.put("LOG_ID", System.currentTimeMillis() + RandomUtil.randomString(3));
        LOG.info("Start to generate train data 15 days later");
        Date date = new Date();
        DateTime dateTime = DateUtil.offsetDay(date, 14);
        Date offsetDate = dateTime.toJdkDate();
        CommonResponse<Object> commonResponse = businessFeign.genDaily(offsetDate);
        LOG.info("Generation finished, {}", commonResponse);
    }
}
