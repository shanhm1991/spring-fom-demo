package com.fom.demo.schedules.timeout;

import java.util.ArrayList;
import java.util.List;

import org.springframework.fom.annotation.FomSchedule;
import org.springframework.fom.interceptor.ScheduleFactory;

/**
 * 
 * <p>可以给任务指定超时时间，并在任务超时后进行中断
 * 
 * <p>通过<b>taskOverTime</b>可以给任务指定超时时间，在任务超时会对其进行中断
 * 
 * <p>默认是对每个任务进行超时检测，通过<b>detectTimeoutOnEachTask = false</b>可以设置成对整体任务进行检测，即从第一个任务执行就计算超时时间
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(cron = "0 0/7 * * * ?", threadCore = 4, taskOverTime = 3500, remark = "中断超时任务")
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
