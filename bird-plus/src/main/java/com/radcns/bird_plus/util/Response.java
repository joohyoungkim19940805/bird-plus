package com.radcns.bird_plus.util;

import com.radcns.bird_plus.entity.room.RoomEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;

import lombok.Getter;
import reactor.core.publisher.Flux;

@Getter
public class Response{
	private Integer code;// = 00;
	private String message;// = "처리에 성공하였습니다.";
	private String summary;// = "SUCCESS";
	private Object data;
	
	private Response() {

	}
	private Response(Object data) {
		this.data = data;
	}
	
	private Response setResult(Result result) {
		this.code = result.code();
		this.message = result.message();
		this.summary = result.summary();
		return this;
	}
	private Response setResult(Result result, Object data) {
		this.code = result.code();
		this.message = result.message();
		this.summary = result.summary();
		this.data = data;
		if(data != null) {
			Flux.just(data.getClass().getDeclaredFields())
			.filter(e->e.getName().equals("accountId") || e.getName().equals("updateBy") || e.getName().equals("createBy"))
			.doOnNext(e->{
				e.setAccessible(true);
				try {
					e.set(data, null);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					e1.printStackTrace();
				}
				e.setAccessible(false);
			}).subscribe();
		}
		
		return this;
	}
	public static Response response(Result result) {
		return new Response().setResult(result);
	}
	public static Response response(Result result, Object data) {
		return new Response().setResult(result, data);
	}
	public Response data(Object data) {
		this.data = data;
		return this;
	}
}
