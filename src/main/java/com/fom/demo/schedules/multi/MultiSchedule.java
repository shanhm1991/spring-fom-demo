package com.fom.demo.schedules.multi;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.fom.annotation.FomSchedule;
import org.springframework.fom.support.service.ScheduleService;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 
 * <p>如果存在多个异构任务，但是定时计划相同，可以使用多个 @Scheduled 进行任务标识
 * 
 * <p>对定时计划的获取：如果 @Scheduled 中指定了，则统一以  @Scheduled 的定时计划为准；
 * 否则从下面的 @Scheduled 中获取定时计划，以获取到的第一个为准
 * 
 * <p>通过 scheduleService，可以在任务执行期间实时向配置集中添加或修改配置，并可以进行持久化，
 * 然后在界面也能实时修改，并可以在任务中获取到修改后的新值
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(fixedRate = 120000, threadCore = 2, remark = "定时多任务测试")
public class MultiSchedule {

	private static final Logger LOG = LoggerFactory.getLogger(MultiSchedule.class);

	private final Random random = new Random();

	@Autowired
	private ScheduleService scheduleService;

	@Scheduled
	public long task1() throws InterruptedException{
		Long lastSleep = scheduleService.getCurrentConfig("dynamic.sleep.time");
		if(lastSleep != null){
			LOG.info("last sleep time: {}", lastSleep); 
		}

		long sleep = random.nextInt(5000);
		LOG.info("task executing ...");
		Thread.sleep(sleep);

		scheduleService.putCurrentConfig("dynamic.sleep.time", sleep);
		scheduleService.serializeCurrent();
		return sleep; 
	}

	@Scheduled
	public String task2() throws InterruptedException{
		LOG.info("task executing ...");
		Thread.sleep(random.nextInt(5000));
		return "test";
	}
}
