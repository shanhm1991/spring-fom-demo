package org.springframework.fom.demo.schedules.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.fom.annotation.Fom;
import org.springframework.fom.proxy.ScheduleFactory;

/**
 * <p>实现接口<b>ScheduleFactory</b>，可以用集合的方式来提交一批任务，但是对于任务的抽象需要继承给定的<b>Task</b>。
 *
 * <p>有的场景中，可能希望定时执行一批任务，并且这些任务有统一的抽象
 *
 * @author shanhm1991@163.com
 *
 */
@Fom(fixedDelay = 180000, threadCore = 4, remark = "定时批任务")
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
