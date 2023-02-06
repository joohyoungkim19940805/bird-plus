package com.radcns.bird_plus.scheduler;

import com.radcns.bird_plus.util.CreateRandomCodeUtil;

public class CreateKeyScheduler implements Runnable {
	private final CreateRandomCodeUtil createRandomCodeUtil;
	public CreateKeyScheduler(CreateRandomCodeUtil createRandomCodeUtil) {
		this.createRandomCodeUtil = createRandomCodeUtil;
	}
	
	@Override
	public void run() {
		System.setProperty("springbootwebfluxjjwt.password.encoder.secret", createRandomCodeUtil.createCode(new byte[16]));
	}
	
}
