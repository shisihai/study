package com.traveler.print.core.configs;

import javax.xml.ws.Endpoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.traveler.print.core.service.SoapServiceImpl;
import com.traveler.print.utils.SysConstans;
@Configuration
public class SoapServerConfig {
	@Bean
	public Endpoint endpoint() {
		return Endpoint.publish(SysConstans.PRINTSOAPURL, new SoapServiceImpl());
	}
}
