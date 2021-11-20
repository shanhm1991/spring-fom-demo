package com.fom.demo.schedules.complete;

import java.util.Random;

import org.springframework.fom.Task;

/**
 * 
 * @author shanhm1991@163.com
 *
 */
public class CompleteTask extends Task<Long> {

	@Override
	public Long exec() throws InterruptedException { 
		long sleep = new Random().nextInt(10000);
		logger.info("task executing ...");
		Thread.sleep(sleep);
		return sleep;
	}
}
