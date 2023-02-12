package com.radcns.bird_plus.util;

import java.util.function.Supplier;

import lombok.Getter;

@Getter
public class ResponseSuccess<T> {
	private Integer code = 00;
	private String resultType = "success";
	private T data;
	
	public ResponseSuccess<T> withData(T data) {
		this.data = data;
		return this;
	}
	public ResponseSuccess<T> withData(Supplier<T> data) {
		this.data = data.get();
		return this;
	}
}
