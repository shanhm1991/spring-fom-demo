package org.springframework.fom.demo.other;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.fom.ScheduleContext;
import org.springframework.fom.ScheduleInfo;
import org.springframework.fom.Task;
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
	private List<ScheduleContext<?>> schedules;

	@Autowired
	private ScheduleContext<Long> $demoExecutor;

	/**
	 * 获取任务列表
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<ScheduleInfo> list(){
		return fomService.list();
	}

	/**
	 * 获取任务计数
	 * @return
	 */
	@RequestMapping("/count")
	@ResponseBody
	public Integer count(){
		return schedules.size();
	}

	/**
	 * 提交任务
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public void submit(String tag) {
		List<Task<Long>> tasks = new ArrayList<>();
		for(int i = 0; i < 10; i++){ 
			tasks.add(new DemoTask(tag + i));
		}

		$demoExecutor.submitBatch(tasks);
	}
}