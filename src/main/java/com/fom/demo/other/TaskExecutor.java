package com.fom.demo.other;

import org.springframework.fom.ScheduleContext;
import org.springframework.fom.annotation.FomSchedule;

/**
 * 
 * <p>@FomSchedule 并不要求必须要有定时计划，如果没有定时计划，也可以当成一个线程池来使用，
 * 并同样可在界面对其进行监控和控制
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(threadCore = 4, remark = "任务执行器")
public class TaskExecutor extends ScheduleContext<Long> {

}
