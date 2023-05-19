package org.springframework.fom.demo.schedules.complete;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.fom.Result;
import org.springframework.fom.annotation.Fom;
import org.springframework.fom.proxy.CompleteHandler;
import org.springframework.fom.proxy.ScheduleFactory;

/**
 * 
 * <p>如果希望在任务全部结束时，执行一些自定义事件，那么可以实现接口<b>CompleterHandler</b>
 * 
 * <p>这在<b>multi</b>多任务或者<b>batch</b>批任务场景中比较有用， 并且在事件执行中可以拿到一些信息，比如执行的次数、本次执行时间、以及所有任务的结果。
 * 
 * @author shanhm1991@163.com
 *
 */
@Fom(cron = "0 0/5 * * * ?", threadCore = 4, remark = "自定义任务结束处理")
public class CompleteSchedule implements ScheduleFactory<Long>, CompleteHandler<Long> {
	
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
	public void onComplete(long times, long lastTime, List<Result<Long>> results) throws Exception {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastTime);
		LOG.info( "onComplete：第{}次在{}提交的任务全部完成，结果省略...", times, date);
	}

}
