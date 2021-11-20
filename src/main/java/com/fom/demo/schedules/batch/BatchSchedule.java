package com.fom.demo.schedules.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.fom.annotation.FomSchedule;
import org.springframework.fom.interceptor.ScheduleFactory;

/**
 * 
 * <p>如果希望定时执行一批任务，并且这些任务可以有统一的抽象
 * 
 * <p>那么可以实现接口 ScheduleFactory，以集合的方式提交一批任务，并可以通过 threadCore 指定线程数来并行执行
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(fixedDelay = 180000, threadCore = 4, remark = "批量定时任务")
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
