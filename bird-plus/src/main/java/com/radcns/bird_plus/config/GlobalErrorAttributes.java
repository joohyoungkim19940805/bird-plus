package com.radcns.bird_plus.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.radcns.bird_plus.util.ExceptionCodeConstant;
import com.radcns.bird_plus.util.exception.BirdPlusException;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);
        map.remove("status");
        map.remove("error");
        Throwable throwable = super.getError(request);
        Class<?> superClass = throwable.getClass().getSuperclass();
        if(superClass.equals(BirdPlusException.class)) {
        	BirdPlusException exception = (BirdPlusException) superClass.cast(throwable);
        	ExceptionCodeConstant.Result error = exception.getResult();
	        map.put("code", error.code());
	        map.put("message", error.message());
	        map.put("summary", error.summary());
	        map.put("resultType", "error");
        }else {
        	map.put("message", ExceptionCodeConstant.Result._999.message());//throwable.getMessage());
        	map.put("code", ExceptionCodeConstant.Result._999.code());
        	map.put("summary", ExceptionCodeConstant.Result._999.summary());
        	map.put("resultType", "error");
        }
        return map;
    }
}