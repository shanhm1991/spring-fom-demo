package com.fom.demo.schedules.batch;

import java.util.Random;

import org.springframework.fom.Task;

/**
 * 
 * <p>作为任务的统一抽象，需要继承Task，建议指定任务id，但不作要求
 * 
 * <p>如果给每个任务指定一个唯一id，那么可以通过 enableTaskConflict 在全局检测任务冲突，这样如果在提交任务时发现该任务的id已经存在，
 * 并且还在运行，那么会忽略本次提交，这在一些文件处理的场景中会比较有用
 * 
 * @author shanhm1991@163.com
 *
 */
public class BatchTask extends Task<Long> {

	public BatchTask(int i) {
		super("BatchTask-" + i);
	} 

	@Override
	public Long exec() throws InterruptedException { 
		long sleep = new Random().nextInt(10000);
		logger.info("task executing ...");
		Thread.sleep(sleep);
		return sleep;
	}
}
