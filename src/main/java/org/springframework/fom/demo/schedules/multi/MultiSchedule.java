package org.springframework.fom.demo.schedules.multi;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.fom.annotation.Fom;
import org.springframework.fom.annotation.Schedule;

/**
 *
 * <p>使用<b>@Fom</b>标识一个类，表示启用一个调度计划，使用<b>@Schedule</b>标识其中的方法，则表示以这个方法作为任务来执行。可以标识多个方法，然后这些任务共享同一个调度计划
 *
 * <p>在有的场景中，可能存在不同的任务，但他们的定时计划相同
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
