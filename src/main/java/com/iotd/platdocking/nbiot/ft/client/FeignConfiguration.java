package com.iotd.platdocking.nbiot.ft.client;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Licensed to CMIM,Inc.under the terms of the CMIM
 * Software License version 1.0.
 * <p>
 * See the NOTICE filed is tributed with this work for additional *
 * information regarding copyright ownership.
 * ---------------------------------------------------------------------------
 * Date Author Version Comments
 * 2019/7/22 ericer 1.0 InitialVersion
 **/
@Configuration
public class FeignConfiguration {
	@Bean
	Logger.Level feignLoggerLevel() {
		//这里记录所有，根据实际情况选择合适的日志level
		return Logger.Level.HEADERS;
	}
}
