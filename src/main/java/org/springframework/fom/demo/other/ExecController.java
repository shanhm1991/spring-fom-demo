package org.springframework.fom.demo.other;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.fom.ScheduleContext;
import org.springframework.fom.ScheduleInfo;
import org.springframework.fom.Task;
import org.springframework.fom.support.service.FomService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * <p>在使用时要注意实际注入类型是ScheduleContext
 *
 * @author shanhm1991@163.com
 *
 */
@RestController
@RequestMapping("/exec")
public class ExecController {

	@Autowired
	private FomService fomService;

	@Autowired
	private List<ScheduleContext<?>> schedules;

	@Autowired
	private ScheduleContext<Long> $fomExecutor;

	/**
	 * 获取任务列表
	 */
	@RequestMapping("/list")
	public List<ScheduleInfo> list(){
		return fomService.list();
	}

	/**
	 * 获取任务计数
	 */
	@RequestMapping("/count")
	public Integer count(){
		return schedules.size();
	}

	/**
	 * 提交任务
	 */
	@RequestMapping("/submit")
	public void submit(String tag) {
		List<Task<Long>> tasks = new ArrayList<>();
		for(int i = 0; i < 10; i++){
			tasks.add(new FomTask(tag + i));
		}
		$fomExecutor.submitBatch(tasks);
	}
}
