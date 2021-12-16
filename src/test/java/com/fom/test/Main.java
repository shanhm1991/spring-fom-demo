package com.fom.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.fom.annotation.EnableFom;

/**
 * 
 * @author shanhm1991@163.com
 *
 */
@EnableFom(enableFomView=false)
@Configuration
public class Main {
	@SuppressWarnings("resource")
	public static void main(String[] args) { 
		new AnnotationConfigApplicationContext("com.fom.test");
	}
}
