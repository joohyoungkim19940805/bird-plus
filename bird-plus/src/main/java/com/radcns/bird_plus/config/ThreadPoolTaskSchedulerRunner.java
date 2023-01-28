package com.radcns.bird_plus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.resolver.DefaultAddressResolverGroup;
import reactor.netty.http.client.HttpClient;

@Component
public class ThreadPoolTaskSchedulerRunner {
	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;
	
	//@Autowired
	//private CommonUtil util;
	
	//@Autowired
    //private StockInfoService stockInfoService;

    public void scheduleRunnableWithCronTrigger() {
		HttpClient httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
		var webClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
		//taskScheduler.schedule(new CompanySummaryInfo(webClient, util, stockInfoService), new CronTrigger("0 44 15 * * ?"));
		//taskScheduler.schedule(new TestCloudSearchScheduler(stockInfoService), new CronTrigger("0 24 16 * * ?")); 
	}
	
}
