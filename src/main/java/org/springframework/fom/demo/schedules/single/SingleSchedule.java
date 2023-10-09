package org.springframework.fom.demo.schedules.single;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.fom.annotation.Fom;
import org.springframework.fom.annotation.Schedule;
import org.springframework.fom.support.service.ScheduleService;

/**
 *
 * <p>单个定时任务比较简单，这里演示一下对于<b>ScheduleService</b>的使用，实现在任务过程中对配置的获取和修改
 *
 * <p>示例中，配置项<b>config.email</b>可以在界面进行修改和注入，在任务的执行过程中，
 * 也可以手动新增修改配置项<b>config.sleep.time</b>，然后在管理界面上同样可以查看和修改。
 *
 * @author shanhm1991@163.com
 *
 */
@Fom(cron = "0 */1 * * * ?", remark = "定时单任务")
public class SingleSchedule {

	private static final Logger LOG = LoggerFactory.getLogger(SingleSchedule.class);

	private final Random random = new Random();

	@Value("${config.email:shanhm1991@163.com}")
	private String email;

	@Autowired
	private ScheduleService scheduleService;

	@Schedule
	public long exec() throws InterruptedException{
		Integer sleepTime = scheduleService.getCurrentConfig("config.sleep.time");
		if(sleepTime != null){
			LOG.info("executing sleep..., email={}", email);
			Thread.sleep(sleepTime);
		}

		sleepTime = random.nextInt(5000);
		scheduleService.putCurrentConfig("config.sleep.time", sleepTime);
		scheduleService.serializeCurrent();
		return sleepTime;
	}
}
