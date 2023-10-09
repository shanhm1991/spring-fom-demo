package org.springframework.fom.demo.other;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.fom.Result;
import org.springframework.fom.annotation.Fom;
import org.springframework.fom.proxy.CompleteHandler;
import org.springframework.fom.proxy.ResultHandler;
import org.springframework.fom.proxy.TaskCancelHandler;

/**
 *
 * <p><b>@Fom</b>不要求必须要有定时计划，如果没有定时计划，也可以当成一个线程池来使用，同样支持一些接口功能，并对外提供提交任务的接口
 *
 * @author shanhm1991@163.com
 *
 */
@Fom(threadCore = 4, taskOverTime = 5000, enableTaskConflict = true, detectTimeoutOnEachTask = true)
public class FomExecutor implements ResultHandler<Long>, TaskCancelHandler, CompleteHandler<Long> {

	private static final Logger LOG = LoggerFactory.getLogger(FomExecutor.class);

	@Override
	public void handleResult(Result<Long> result) throws Exception {
		LOG.info("handleResult：统计任务[{}]的结果：{}", result.getTaskId(), result.getContent());
	}

	@Override
	public void handleCancel(String taskId, long costTime) throws Exception {
		LOG.info("handleCancel：取消任务[{}]，耗时={}ms", taskId, costTime);
	}

	@Override
	public void onComplete(long times, long lastTime, List<Result<Long>> results) throws Exception {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastTime);
		LOG.info("onComplete： 第{}次在{}提交的任务全部执行结束，结果数：{}", times, date, results.size());
	}
}
