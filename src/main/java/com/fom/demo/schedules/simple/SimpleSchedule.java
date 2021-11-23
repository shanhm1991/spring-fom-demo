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
 * <p>对于简单的定时任务场景，没什么好讲的，所以这里顺便演示了一下，对于任务配置实时修改的支持。
 * 
 * <p>对于<b>@Value</b>中的配置项<b>email.user</b>和<b>email.address</b>可以在界面实时修改合注入，
 * 而且在任务的执行过程中，还可以手动新增配置项<b>dynamic.sleep.time</b>，同样也能在界面查看和实时修改。
 * 
 * <p>另外，对于定时计划的获取有一个优先级，
 * 即如果<b>@FomSchedule</b>中指定了，则以<b>@FomSchedule</b>中的定时计划为准，否则再尝试从<b>@Scheduled</b>中获取。
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(remark = "定时单任务")
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
