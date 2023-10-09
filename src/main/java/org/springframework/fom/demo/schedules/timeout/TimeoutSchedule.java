package org.springframework.fom.demo.schedules.timeout;

import java.util.ArrayList;
import java.util.List;

import org.springframework.fom.annotation.Fom;
import org.springframework.fom.proxy.ScheduleFactory;

/**
 *
 * <p>通过<b>taskOverTime</b>可以给任务指定超时时间，这样当任务超时后调度线程会尝试对其进行中断取消
 *
 * <p>默认是对每个任务单独进行超时检测，如果想对整体任务进行超时检测，可以设置<b>detectTimeoutOnEachTask</b>为false。
 * 这样将从第一个任务开始执行时计算时间，等到超时后就取消还没结束的任务，不管这个任务实际耗时多久，就算它还没有开始执行也会被取消。
 *
 * @author shanhm1991@163.com
 *
 */
@Fom(cron = "0 0/7 * * * ?", threadCore = 4, taskOverTime = 3500, remark = "任务超时检测")
public class TimeoutSchedule implements ScheduleFactory<Long>{

	@Override
	public List<TimeoutTask> newSchedulTasks() {
		List<TimeoutTask> list = new ArrayList<>();
		for(int i = 1; i <= 15; i++){
			list.add(new TimeoutTask());
		}
		return list;
	}
}
