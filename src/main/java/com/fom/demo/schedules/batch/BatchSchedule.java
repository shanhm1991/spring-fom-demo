package com.fom.demo.schedules.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.fom.annotation.FomSchedule;
import org.springframework.fom.proxy.ScheduleFactory;

/**
 * 
 * <p>如果希望定时执行一批任务，并且这些任务可以进行统一抽象，那么可以实现接口<b>ScheduleFactory</b>，以集合的方式提交一批任务
 * 但是对于任务的抽象需要继承给定的<b>Task</b>。
 * 
 * <p>另外，通过<b>threadCore</b>可以指定线程数来并行执行。
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(fixedDelay = 180000, threadCore = 4, remark = "定时批任务")
public class BatchSchedule implements ScheduleFactory<Long> {

	@Override
	public List<BatchTask> newSchedulTasks() throws Exception {
		List<BatchTask> list = new ArrayList<>();
		for(int i = 1; i <= 10; i++){
			list.add(new BatchTask(i));
		}
		return list;
	}
}
