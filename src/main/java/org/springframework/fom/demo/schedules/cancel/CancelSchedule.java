package org.springframework.fom.demo.schedules.cancel;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.fom.annotation.Fom;
import org.springframework.fom.proxy.TerminateHandler;
import org.springframework.fom.proxy.TaskCancelHandler;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * <p>实现接口<b>TaskCancelHandler</b>可以自定义任务的取消策略，对于取消策略的调用有两个时机：检测到任务超时，或者被外部shutdown关闭。
 * 如果想自定义shutdown之后的处理，比如释放一些资源，也可以实现接口<b>TerminateHandler</b>
 *
 * <p>有的场景中，可能要取消的任务无法响应中断，比如等待套接字返回，或者无限循环，此时只能通过关闭连接，或增加循环标志等办法来结束
 *
 * @author shanhm1991@163.com
 *
 */
@Fom(cron = "0 0/11 * * * ?", execOnLoad = true, taskOverTime = 300000, remark = "自定义任务取消事件")
public class CancelSchedule implements TaskCancelHandler, TerminateHandler {

	private static final Logger LOG = LoggerFactory.getLogger(CancelSchedule.class);

	private volatile boolean off = false;

	@Scheduled
	public void exec() {
		LOG.info("task executing ...");
		while(!off){
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				//ignore
			}
		}
	}

	@Override
	public void handleCancel(String taskId, long costTime) {
		off = true;
		LOG.info("handleCancel：任务被取消{}，已经耗时{}ms", taskId, costTime);

	}

	@Override
	public void onTerminate(long execTimes, long lastExecTime) {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastExecTime);
		LOG.info("onTerminate：任务关闭，共执行{}次任务，最后一次执行时间为{}", execTimes, date);

	}
}
