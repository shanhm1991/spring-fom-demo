package com.fom.demo.schedules.simple;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.fom.annotation.FomSchedule;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 
 * <p>简单的定时任务场景
 * 
 * <p>对定时计划的获取：如果 @Scheduled 中指定了，则以  @Scheduled 的定时计划为准；否则从 @Scheduled 中获取
 * 
 * <p>对于 @FomSchedule 标识的类中，使用 @Value 标识的属性配置，会同步到任务的配置集中，可以在界面实时更新生效
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(remark = "简单定时任务")
public class SimpleSchedule {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleSchedule.class);

	private final Random random = new Random();

	@Value("${email.user:shanhm1991}@${email.address:163.com}")
	private String email;

	@Scheduled(cron = "0 */1 * * * ?")
	public long exec() throws InterruptedException{ 
		long sleep = random.nextInt(5000);
		LOG.info("task executing ..., email={}", email);
		Thread.sleep(sleep);
		return sleep;
	}
}
