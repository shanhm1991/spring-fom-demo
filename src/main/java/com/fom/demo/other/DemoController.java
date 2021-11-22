package com.fom.demo.other;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.fom.Result;
import org.springframework.fom.ScheduleContext;
import org.springframework.fom.ScheduleInfo;
import org.springframework.fom.Task;
import org.springframework.fom.support.Response;
import org.springframework.fom.support.service.FomService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * <p>fom.html界面上的所有功能完全基于<b>FomService</b>提供的接口，如果想自己重新实现一个管理界面，
 * 或者在自己的界面中添加一些任务管理操作，则可以直接通过注入<b>FomService</b>来实现
 * 
 * @author shanhm1991@163.com
 *
 */
@Controller
@RequestMapping("/other")
public class DemoController {

	@Autowired
	private FomService fomService;

	@Autowired
	private ScheduleContext<Long> taskExecutor;

	// 如果@FomSchedule标识的类已经继承了ScheduleContext，则在注入的时候与beanName一致，比如上面的taskExecutor
	// 否则，注入时需要在目标beanName加一个$符，表示获取注入其对应ScheduleContext
	//@Autowired
	//private ScheduleContext<Long> $simpleSchedule;

	// 可以通过容器注入所有的 ScheduleContext
	//@Autowired
	//private List<ScheduleContext<?>> schedules;

	/**
	 * 获取所有的任务信息
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Response<List<ScheduleInfo>> list(){
		return new Response<>(Response.SUCCESS, "", fomService.list());
	}

	@RequestMapping("/submit")
	@ResponseBody
	public Response<List<Result<Long>>> submit() throws InterruptedException, ExecutionException{
		List<Task<Long>> tasks = new ArrayList<>();

		Random random = new Random();
		for(int i = 0; i < 10; i++){ 
			tasks.add(new Task<Long>(){
				@Override
				public Long exec() throws Exception {
					long sleep = random.nextInt(3000);
					logger.info("task executing ...");
					Thread.sleep(sleep);
					return sleep;
				}
			});
		}

		taskExecutor.submitBatch(tasks); 
		return new Response<>(Response.SUCCESS, "");
	}
}
