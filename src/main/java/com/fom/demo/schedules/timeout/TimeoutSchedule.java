package com.fom.demo.schedules.timeout;

import java.util.ArrayList;
import java.util.List;

import org.springframework.fom.annotation.FomSchedule;
import org.springframework.fom.proxy.ScheduleFactory;

/**
 * 
 * <p>如果想给任务指定超时时间，并在超时后进行取消，那么可以通过<b>taskOverTime</b>给任务指定超时时间，这样当任务超时后会尝试对其进行中断取消。
 * 
 * <p>默认会对每个任务单独进行超时检测，如果希望对整体任务进行超时检测，可以通过<b>detectTimeoutOnEachTask = false</b>进行设置。
 * 这样将从第一个任务开始执行时计算超时时间，等到超时后就取消还没结束的任务， 而不管这个任务实际耗时多久，就算它还没开始执行（耗时 0ms）也会被取消。
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(cron = "0 0/7 * * * ?", threadCore = 4, taskOverTime = 3500, remark = "任务超时检测")
public class TimeoutSchedule implements ScheduleFactory<Long>{
	
	@Override
	public List<TimeoutTask> newSchedulTasks() throws Exception {
		List<TimeoutTask> list = new ArrayList<>();
		for(int i = 1; i <= 15; i++){
			list.add(new TimeoutTask());
		}
		return list;
	}
}
