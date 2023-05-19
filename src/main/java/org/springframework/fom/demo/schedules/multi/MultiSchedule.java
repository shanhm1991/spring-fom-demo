package org.springframework.fom.demo.schedules.multi;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.fom.annotation.Fom;
import org.springframework.fom.annotation.Schedule;

/**
 * 
 * <p>如果存在多个异构任务，但定时计划相同，那么可以使用多个<b>@Scheduled</b>进行任务标识
 * 
 * <p>对于定时计划的获取，同样先以<b>@FomSchedule</b>中定义的为准；如果没有定义再从下面的<b>@FomSchedule</b>中获取，以获取到的第一个为准
 * 
 * @author shanhm1991@163.com
 *
 */
@Fom(fixedRate = 120000, threadCore = 2, remark = "定时多任务")
public class MultiSchedule {

	private static final Logger LOG = LoggerFactory.getLogger(MultiSchedule.class);

	private final Random random = new Random();

	@Schedule
	public long task1() throws InterruptedException{
		long sleep = random.nextInt(5000);
		LOG.info("task executing ...");
		Thread.sleep(sleep);
		return sleep; 
	}

	@Schedule
	public String task2() throws InterruptedException{
		LOG.info("task executing ...");
		Thread.sleep(random.nextInt(5000));
		return "test";
	}
}
