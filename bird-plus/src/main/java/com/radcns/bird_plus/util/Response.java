package com.radcns.bird_plus.util;

import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;

import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Getter
public class Response<T>{
	private Integer code;// = 00;
	private String message;// = "처리에 성공하였습니다.";
	private String summary;// = "SUCCESS";
	private T data;
	
	private Response() {

	}
	private Response(T data) {
		this.data = data;
	}
	
	private Mono<Response<T>> setResult(Result result) {
		this.code = result.code();
		this.message = result.message();
		this.summary = result.summary();
		return Mono.just(this);
	}
	private Mono<Response<T>> setResult(Result result, T data) {
		this.code = result.code();
		this.message = result.message();
		this.summary = result.summary();
		this.data = data;
		if(data == null || data.getClass().equals(Response.class)) {
			return deleteData();
		}
		return Flux.fromArray(data.getClass().getDeclaredFields())
		.filter(e->
			e.getName().equals("accountId") ||
			e.getName().equals("updateBy") ||
			e.getName().equals("createBy") || 
			(data.getClass().equals(AccountEntity.class) && e.getName().equals("id"))
		)
		.flatMap(e-> Mono.fromCallable(()->{
			e.setAccessible(true);
			try {
				e.set(data, null);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
			e.setAccessible(false);
			return e;
		}))
		.collectList()
		.map(e->this);
	}
	private Mono<Response<T>> deleteData() {
		this.data = null;
		return Mono.just(this);
	}
	public static <T> Mono<Response<T>> response(Result result) {
		return new Response<T>().setResult(result);
	}
	public static <T> Mono<Response<T>> response(Result result, T data) {
		return new Response<T>().setResult(result, data);
	}
	public Response<T> data(T data) {
		this.data = data;
		return this;
	}
	
}
