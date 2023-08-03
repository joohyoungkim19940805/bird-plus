package com.radcns.bird_plus.web.router.main;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.util.Response;
import com.radcns.bird_plus.web.handler.MainHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface MainRouterSwagger {
    @RouterOperations({
    	@RouterOperation(path = "/login-processing", 
    		produces = {MediaType.APPLICATION_JSON_VALUE },
			beanClass = MainHandler.class,  
			beanMethod = "loginProcessing",
			method = RequestMethod.POST,
            operation = @Operation(operationId = "loginProcessing",
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
	        	responses = {
	        		@ApiResponse(
		                responseCode = "200", 
		                description = "is login ok.", 
		                useReturnTypeSchema = true, 
		                content = @Content(
		                	schema = @Schema(
		                		implementation = Response.class
		                	),
		                	mediaType = MediaType.APPLICATION_JSON_VALUE
		                )
        			)
        		}
            )
    	)
    })
    public RouterFunction<ServerResponse> index(MainHandler mainHandler);
}
