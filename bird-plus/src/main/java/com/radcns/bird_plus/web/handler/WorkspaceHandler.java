package com.radcns.bird_plus.web.handler;

import static com.radcns.bird_plus.util.Response.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.entity.workspace.WorkspaceEntity;
import com.radcns.bird_plus.entity.workspace.WorkspaceMembersEntity;
import com.radcns.bird_plus.repository.customer.AccountRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceMembersRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.Response;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;

import reactor.core.publisher.Mono;

@Component
public class WorkspaceHandler {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private WorkspaceRepository workspaceRepository;
	
	@Autowired
	private WorkspaceMembersRepository workspaceMembersRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	public Mono<ServerResponse> createWorkspace(ServerRequest request){
		//serverRequest.bodyToMono()
		return accountService.convertJwtToAccount(request)
		.flatMap(account -> request.bodyToMono(WorkspaceEntity.class)
			.flatMap(workspace->{
				//create by 용도를 아직 못정하여 account id를 둘 다 저장
				workspace.setCreateBy(account.getId());
				return workspaceRepository.save(workspace.withOwnerAccountId(account.getId()));
			})
			.doOnSuccess(e->
				workspaceMembersRepository.save(
					WorkspaceMembersEntity.builder()
					.accountId(account.getId())
					.workspaceId(e.getId())
					.build()
				).subscribe()
			)
		)
		.flatMap(workspace -> ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(response(Result._0, workspace)), Response.class)
		)
		;
	}
	
	public Mono<ServerResponse> searchWorkspaceName(ServerRequest request){
		var param = request.queryParams();
		PageRequest pageRequest = PageRequest.of(
			Integer.valueOf(param.getFirst("page")),
			Integer.valueOf(param.getFirst("size"))
			//vo.getPage(), vo.getSize()
		);
		String workspaceName = param.getFirst("workspaceName");
		return ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(
				workspaceRepository.findAllByWorkspaceName(workspaceName, pageRequest)
					.collectList()
					.zipWith(workspaceRepository.countByWorkspaceName(workspaceName))
					.map(tuples -> 
						new PageImpl<>(tuples.getT1(), pageRequest, tuples.getT2())
					)
					.map(list -> response(Result._0, list))
			, Response.class);
	}
	
	public Mono<ServerResponse> isWorkspaceJoined(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(account -> workspaceMembersRepository.existsByAccountId(account.getId()))
			.map(isExists -> response(Result._0, isExists))
		, Response.class)
		;
	}
	

	public Mono<ServerResponse> searchWorkspaceMyJoined(ServerRequest request){
		//workspaceMembersRepository.find
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(e->{
				var param = request.queryParams();
				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))
				);
					//.withSort(Sort.by("create_at").ascending());
				return workspaceMembersRepository.findAllByAccountId(e.getId(), pageRequest)
				.collectList()
	            .zipWith(workspaceMembersRepository.countByAccountId(e.getId()))
	            .map(entityTuples -> 
                	new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
                )
	            ;
			})
			.map(list -> response(Result._0, list))
		, Response.class);
	}
}
