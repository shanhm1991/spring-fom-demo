package com.fom.demo.schedules.simple;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.fom.annotation.FomSchedule;
import org.springframework.fom.support.service.ScheduleService;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 
 * <p>简单的定时任务场景
 * 
 * <p>对定时计划的获取：如果<b>@FomSchedule</b>中指定了，则以<b>@FomSchedule</b>的定时计划为准；否则从<b>@Scheduled</b>中获取
 * 
 * <p>在<b>@FomSchedule</b>标识的类中，对于使用<b>@Value</b>标识的属性，会将其对应的配置同步到任务的配置集中，然后可以通过接口实时更新
 * 
 * <p>通过注入的<b>scheduleService</b>，可以在任务执行期间，实时向配置集中添加或修改配置，并支持持久化
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(remark = "简单定时任务")
public class SimpleSchedule {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleSchedule.class);

	private final Random random = new Random();

	@Value("${email.user:shanhm1991}@${email.address:163.com}")
	private String email;

	@Autowired
	private ScheduleService scheduleService;

	@Scheduled(cron = "0 */1 * * * ?")
	public long exec() throws InterruptedException{ 
		Long lastSleep = scheduleService.getCurrentConfig("dynamic.sleep.time");
		if(lastSleep != null){
			LOG.info("last sleep time: {}", lastSleep); 
		}

		long sleep = random.nextInt(5000);
		LOG.info("task executing ..., email={}", email);
		Thread.sleep(sleep);

		scheduleService.putCurrentConfig("dynamic.sleep.time", sleep);
		scheduleService.serializeCurrent();
		return sleep;
	}
}
