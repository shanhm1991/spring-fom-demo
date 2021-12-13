package com.fom.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.fom.annotation.EnableFom;

/**
 * 
 * @author shanhm1991@163.com
 *
 */
@EnableFom
@SpringBootApplication
public class FomDemoBoot {
	public static void main( String[] args ) {
		SpringApplication.run(FomDemoBoot.class, args);
    }
}
