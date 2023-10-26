package com.radcns.bird_plus.web.handler;

import static com.radcns.bird_plus.util.ResponseWrapper.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.entity.workspace.WorkspaceEntity;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity.WorkspaceMembersDomain;
import com.radcns.bird_plus.repository.account.AccountRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceInAccountRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ResponseWrapper;
import com.radcns.bird_plus.util.exception.WorkspaceException;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WorkspaceHandler {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private WorkspaceRepository workspaceRepository;
	
	@Autowired
	private WorkspaceInAccountRepository workspaceInAccountRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	public Mono<ServerResponse> createWorkspace(ServerRequest request){
		//serverRequest.bodyToMono()
		return accountService.convertRequestToAccount(request)
		.flatMap(account -> request.bodyToMono(WorkspaceEntity.class)
			.flatMap(workspace->{
				//create by 용도를 아직 못정하여 account id를 둘 다 저장
				workspace.setCreateBy(account.getId());
				return workspaceRepository.save(workspace.withOwnerAccountId(account.getId()));
			})
			.doOnSuccess(e->
			workspaceInAccountRepository.save(
					WorkspaceInAccountEntity.builder()
					.accountId(account.getId())
					.workspaceId(e.getId())
					.build()
				).subscribe()
			)
		)
		.flatMap(workspace -> ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(response(Result._0, workspace), ResponseWrapper.class)
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
					.flatMap(list -> response(Result._0, list))
			, ResponseWrapper.class);
	}
	
	public Mono<ServerResponse> isWorkspaceJoined(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.flatMap(account -> workspaceInAccountRepository.existsByAccountId(account.getId()))
			.flatMap(isExists -> response(Result._0, isExists))
		, ResponseWrapper.class)
		;
	}
	

	public Mono<ServerResponse> searchWorkspaceMyJoined(ServerRequest request){
		//workspaceMembersRepository.find
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.flatMap(e->{
				var param = request.queryParams();
				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))
				);
					//.withSort(Sort.by("create_at").ascending());
				return workspaceInAccountRepository.findAllByAccountId(e.getId(), pageRequest)
				.collectList()
	            .zipWith(workspaceInAccountRepository.countByAccountId(e.getId()))
	            .map(entityTuples -> 
                	new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
                )
	            ;
			})
			.flatMap(list -> response(Result._0, list))
		, ResponseWrapper.class);
	}
	
	public Mono<ServerResponse> searchWorkspaceInAccount(ServerRequest request){
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.filterWhen(e -> workspaceInAccountRepository.existsByWorkspaceIdAndAccountId(workspaceId, e.getId()))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.flatMap(account -> {
				var param = request.queryParams();
				String fullName = param.getFirst("fullName");
				
				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
				);
				
				Flux<WorkspaceMembersDomain.WorkspaceInAccountListResponse> workspaceInAccountFlux;
				Mono<Long> workspaceInAccountCountMono;
				if(fullName != null && ! fullName.isBlank()) {
					workspaceInAccountFlux = workspaceInAccountRepository.findAllJoinAccountByWorkspaceIdAndNotAccountIdAndFullName(workspaceId, account.getId(), fullName, pageRequest);
					workspaceInAccountCountMono = workspaceInAccountRepository.countJoinAccountByWorkspaceIdAndNotAccountIdAndFullName(workspaceId, account.getId(), fullName);
				}else {
					workspaceInAccountFlux = workspaceInAccountRepository.findAllJoinAccountByWorkspaceIdAndNotAccountId(workspaceId, account.getId(), pageRequest);
					workspaceInAccountCountMono = workspaceInAccountRepository.countJoinAccountByWorkspaceIdAndNotAccountId(workspaceId, account.getId());
				}
				
				return workspaceInAccountFlux
				.collectList()
				.zipWith(workspaceInAccountCountMono)
				.map(tuples -> 
					new PageImpl<>(tuples.getT1(), pageRequest, tuples.getT2())
				);
			})
			.flatMap(list -> response(Result._0, list))
		, ResponseWrapper.class)
		;
		/*
		accountService.convertJwtToAccount(request)
		.filterWhen(e -> workspaceInAccountRepository.existsByWorkspaceIdAndAccountId(workspaceId, e.getId()))
		.flatMap(account -> {
			return workspaceInAccountRepository.findAllJoinAccountByWorkspaceIdAndNotAccountId(workspaceId, account.getId())
			;
		})
		*/
	}
	
	public Mono<ServerResponse> getWorkspaceDetail(ServerRequest request){
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			workspaceRepository.findById(workspaceId)
			.flatMap(e-> response(Result._0, e.withOwnerAccountId(null).withCreateBy(null)))
		, ResponseWrapper.class)
		;
	}
}
