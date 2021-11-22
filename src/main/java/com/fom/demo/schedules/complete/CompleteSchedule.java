package com.fom.demo.schedules.complete;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.fom.Result;
import org.springframework.fom.annotation.FomSchedule;
import org.springframework.fom.interceptor.ScheduleCompleter;
import org.springframework.fom.interceptor.ScheduleFactory;

/**
 * 
 * <p>如果想在 每次定时任务全部结束时 执行一些自定义操作，可以实现接口<b>ScheduleCompleter</b>
 * 
 * <p>这在multi多任务或者batch批任务场景中比较有用，可以在最后一个任务结束时执行指定的操作，
 * 并且在执行中可以拿到所有任务的结果，定时执行的次数和时间信息
 * 
 * <p>下面以batch为例
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(cron = "0 0/5 * * * ?", threadCore = 4, remark = "定时任务全部结束时执行自定义操作")
public class CompleteSchedule implements ScheduleFactory<Long>, ScheduleCompleter<Long> {
	
	private static final Logger LOG = LoggerFactory.getLogger(CompleteSchedule.class);

	@Override
	public List<CompleteTask> newSchedulTasks() throws Exception {
		List<CompleteTask> list = new ArrayList<>();
		for(int i = 1; i <= 10; i++){
			list.add(new CompleteTask());
		}
		return list;
	}
	
	@Override
	public void onScheduleComplete(long execTimes, long lastExecTime, List<Result<Long>> results) throws Exception {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastExecTime);
		LOG.info( "第{}次在{}提交的任务全部完成，结果省略...", execTimes, date);
	}

}
