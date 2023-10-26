package com.radcns.bird_plus.web.router.main;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.config.security.Token;
import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.util.ResponseWrapper;
import com.radcns.bird_plus.web.handler.MainHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface IndexRouterSwagger {
    @RouterOperations({
    	@RouterOperation(
    		path = "/login-processing", 
    		produces = {MediaType.APPLICATION_JSON_VALUE },
			beanClass = MainHandler.class,  
			beanMethod = "loginProcessing",
			method = RequestMethod.POST,
            operation = @Operation(operationId = "loginProcessing",
            	/* GET인 경우 사용
            	parameters = {
        			@Parameter(
        				name = "accountName", 
        				description = "account name",
        				in = ParameterIn.QUERY, 
        				schema = @Schema(type = "String"),
        				example = "test"
        			),
        			@Parameter(
        				name = "password", 
        				description = "password",
        				in = ParameterIn.QUERY, 
        				schema = @Schema(type = "String"),
        				example = "rlawngud1"
        			)
            	},
            	*/
	        	responses = {
        			@ApiResponse(
    		                responseCode = "0", 
    		                description = "data is always inside wrappering", 
    		                content = @Content(
    		                	schema = @Schema(
    		                		implementation = ResponseWrapper.class
    		                	),
    		                	mediaType = MediaType.APPLICATION_JSON_VALUE
    		                )
            		),
        			@ApiResponse(
		                responseCode = "200", 
		                description = "is login ok.", 
		                content = @Content(
		                	schema = @Schema(
		                		implementation = Token.class
		                	),
		                	mediaType = MediaType.APPLICATION_JSON_VALUE
		                )
        			)
        		},
    			requestBody = @RequestBody(
    				content = @Content(schema = @Schema(implementation = AccountEntity.class ))
    			)
            )
    	)
    })
    public RouterFunction<ServerResponse> index(MainHandler mainHandler);

    public static void main(String a[]) {
    	String test = "accountEntity";
    	System.out.println(test.endsWith("Entity"));
    	new AccountEntity().getAccountName();
    }
}
