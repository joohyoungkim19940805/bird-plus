package com.radcns.bird_plus.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.radcns.bird_plus.util.ExceptionCodeConstant;
import com.radcns.bird_plus.util.exception.BirdPlusException;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Component
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);
        Throwable throwable = super.getError(request);
        try {
	        BirdPlusException exception = (BirdPlusException) throwable.getClass().getSuperclass().cast(throwable);
	        ExceptionCodeConstant.Error error = exception.getError();
	        map.put("code", error.status());
	        map.put("message", error.message());
	        map.put("summary", error.summary());
        }catch(Exception e) {
        	log.error(e.getMessage(), e);
        	map.put("message", ExceptionCodeConstant.Error._999.message());//throwable.getMessage());
        	map.put("code", ExceptionCodeConstant.Error._999.status());
        	map.put("summary", ExceptionCodeConstant.Error._999.summary());
        }
        return map;
    }

}