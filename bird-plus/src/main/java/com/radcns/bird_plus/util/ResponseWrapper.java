package com.radcns.bird_plus.util;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;

import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.exception.ApiException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
/**
 * 해당 클래스는 hanlder가 여러개인 functional endpoint 방식에서는 동작을 안하는 걸로 추측됨
 * (현재 이게 나온지 얼마 안되서 어째서 매핑이 안되는 건지 정확한 정보는 없음)
 * @author oozu1
 *
 */
/*
public class ResponseWrapper extends ResponseBodyResultHandler {

	// Method 메소드 매개 변수 의 사양을 캡슐화하는 헬퍼 클래스입니다 Constructor. 전달할 사양 개체로 유용합니다.
	// 4.2부터 SynthesizingMethodParameter 속성 별칭으로 주석을 합성하는 하위 클래스가 있습니다. 이 하위 클래스는 특히 웹 및 메시지 끝점 처리에 사용됩니다.
	private static MethodParameter param;

    static {
        try {
            //get new params
            param = new MethodParameter(ResponseWrapper.class
                    .getDeclaredMethod("methodForParams"), -1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    private static Mono<Response> methodForParams() {
        return null;
    }
	
	public ResponseWrapper(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver) {
		super(writers, resolver);
		// TODO Auto-generated constructor stub
	}
	
	public ResponseWrapper(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver,
			ReactiveAdapterRegistry registry) {
		super(writers, resolver, registry);
		// TODO Auto-generated constructor stub
	}

	// HandlerResult를 지원할 수 있는지 여부를 리턴합니다.
	@Override
    public boolean supports(HandlerResult result) {
        boolean isMonoFlux = (result.getReturnType().resolve() == Mono.class || result.getReturnType().resolve() == Flux.class);
        boolean isAlreadyResponse = result.getReturnType().resolveGeneric(0) == Response.class;
        System.out.println("kjh test<<<");
        System.out.println(isMonoFlux);
        System.out.println(isAlreadyResponse);
        return isMonoFlux && !isAlreadyResponse;
    }
	
    @Override
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        System.out.println("kjh ???");
    	if(result.getReturnValue() == null) {
        	throw new ApiException(Result._01);
        }
    	//result.getReturnValue()
        // modify the result as you want
        @SuppressWarnings("unchecked")
		Mono<Response> body = ((Mono<Object>) result.getReturnValue()).map(e-> Response.response(Result._00, e))
                .defaultIfEmpty(Response.response(Result._00));
        return writeBody(body, param, exchange);
    }
}
*/
