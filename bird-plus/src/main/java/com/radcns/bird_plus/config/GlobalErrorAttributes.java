package com.radcns.bird_plus.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.radcns.bird_plus.util.exception.BirdPlusException;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);
        Throwable throwable = super.getError(request);
        BirdPlusException exception = (BirdPlusException) throwable.getClass().getSuperclass().cast(throwable);
        map.put("code", exception.getStatusCode());
        map.put("message", exception.getMessage());
        map.put("type", exception.getError().type());
        return map;
    }

}