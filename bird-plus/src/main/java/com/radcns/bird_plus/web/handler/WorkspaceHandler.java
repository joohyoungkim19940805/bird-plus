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

import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.workspace.WorkspaceEntity;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity;
import com.radcns.bird_plus.entity.workspace.WorkspaceEntity.WorkspaceDomain.JoinedWorkspaceRequest;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity.WorkspaceMembersDomain;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity.WorkspaceMembersDomain.WokrspaceInAccountPermitListResponse;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity.WorkspaceMembersDomain.WorkspaceInAccountGiveAdmin;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity.WorkspaceMembersDomain.WorkspaceInAccountPermitRequest;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity.WorkspaceMembersDomain.WorkspaceInAccountPermitRequest.PermitType;
import com.radcns.bird_plus.repository.account.AccountRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceInAccountRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ResponseWrapper;
import com.radcns.bird_plus.util.exception.WorkspaceException;
import com.radcns.bird_plus.util.exception.BirdPlusException.Result;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;
import com.radcns.bird_plus.util.stream.WorkspaceBroker;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate.ServerSentStreamType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

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
	
	@Autowired
	private WorkspaceBroker workspaceBroker;
	
	
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
					.isEnabled(true)
					.isAdmin(true)
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
			.flatMap(account -> workspaceInAccountRepository.existsByAccountIdAndIsEnabled(account.getId(), true))
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
			.filterWhen(e -> workspaceInAccountRepository.existsByWorkspaceIdAndAccountIdAndIsEnabled(workspaceId, e.getId(), true))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._201)))
			.flatMap(account -> {
				var param = request.queryParams();
				String fullName = param.getFirst("fullName");
				Long roomId = Long.valueOf(param.getOrDefault("roomId", List.of("0")).get(0));
				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
				);
				
				Flux<WorkspaceMembersDomain.WorkspaceInAccountListResponse> workspaceInAccountFlux;
				Mono<Long> workspaceInAccountCountMono;

				if(fullName != null && ! fullName.isBlank()) {
					workspaceInAccountFlux = workspaceInAccountRepository.findAllJoinAccountByWorkspaceIdAndNotAccountIdAndFullName(workspaceId, roomId, account.getId(), fullName, pageRequest);
					workspaceInAccountCountMono = workspaceInAccountRepository.countJoinAccountByWorkspaceIdAndNotAccountIdAndFullName(workspaceId, roomId, account.getId(), fullName);
				}else {
					workspaceInAccountFlux = workspaceInAccountRepository.findAllJoinAccountByWorkspaceIdAndNotAccountId(workspaceId, roomId, account.getId(), pageRequest);
					workspaceInAccountCountMono = workspaceInAccountRepository.countJoinAccountByWorkspaceIdAndNotAccountId(workspaceId, roomId, account.getId());
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
	
	public Mono<ServerResponse> createJoinedWorkspace(ServerRequest request) {
		
		return accountService.convertRequestToAccount(request)
		.flatMap(account-> 
			request.bodyToMono(JoinedWorkspaceRequest.class).flatMap(joinedWorkspaceRequest -> 
				workspaceRepository.findById(joinedWorkspaceRequest.getId())
				.filterWhen(workspaceEntity -> workspaceInAccountRepository.existsByWorkspaceIdAndAccountIdAndIsEnabled(workspaceEntity.getId(), account.getId(), true).map(e->! e))
				.switchIfEmpty(Mono.error(new WorkspaceException(Result._203)))
				.filterWhen(workspaceEntity -> workspaceInAccountRepository.existsByWorkspaceIdAndAccountId(workspaceEntity.getId(), account.getId()).map(e->! e))
				.switchIfEmpty(Mono.error(new WorkspaceException(Result._206)))
				.filterWhen(workspaceEntity -> {
					if(workspaceEntity.getAccessFilter().size() == 0) {
						return Mono.just(Boolean.TRUE);
					}					
					String joinedEmail = account.getEmail().substring( account.getEmail().indexOf("@") );
					
					return Flux.fromIterable(workspaceEntity.getAccessFilter()).any(e->e.equals(joinedEmail));
				})
				.switchIfEmpty(Mono.error(new WorkspaceException(Result._202)))
				.flatMap(workspaceEntity -> {
					WorkspaceInAccountEntity workspaceInAccountEntity = WorkspaceInAccountEntity.builder()
					.accountId(account.getId())
					.workspaceId(workspaceEntity.getId())
					.isEnabled( ! workspaceEntity.getIsFinallyPermit())
					.isAdmin(false)
					.build();
					return workspaceInAccountRepository.save(workspaceInAccountEntity).map(e->e.withAccountId(null))
					.doOnSuccess(e->{
						workspaceBroker.send(
							new ServerSentStreamTemplate<WokrspaceInAccountPermitListResponse>(
								e.getWorkspaceId(),
								(long)0,
								WokrspaceInAccountPermitListResponse.builder()
									.id(e.getId())
									.workspaceId(e.getWorkspaceId())
									.accountName(account.getAccountName())
									.email(account.getEmail())
									.fullName(account.getFullName())
									.jobGrade(account.getJobGrade())
									.department(account.getDepartment())
								.build(),
								ServerSentStreamType.WORKSPACE_PERMIT_REQUEST_ACCEPT
							) {}
						);
					});
				})
			)
		).flatMap(e->{
			 return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response(Result._0, e), ResponseWrapper.class)
				;
		});

	}
	public Mono<ServerResponse> createPermitWokrspaceInAccount(ServerRequest request){
		
		return accountService.convertRequestToAccount(request)
		.flatMap(account -> 
			request.bodyToMono(WorkspaceInAccountPermitRequest.class)
			.filterWhen(e->workspaceInAccountRepository.existsByWorkspaceIdAndAccountIdAndIsAdmin(e.getWorkspaceId(), account.getId(), true))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._203)))
			.flatMap(workspaceInAccountPermitRequest-> {
				return workspaceInAccountRepository.findById(workspaceInAccountPermitRequest.getId()).flatMap(workspaceInAccountEntity -> {
					Mono<?> saveOrDelete;
					if(workspaceInAccountPermitRequest.getPermitType() == null ) {
						throw new WorkspaceException(Result._205);
					}else if(workspaceInAccountPermitRequest.getPermitType().equals(PermitType.PERMIT)) {
						saveOrDelete = workspaceInAccountRepository.save(workspaceInAccountEntity.withIsEnabled(true));
					}else{//(workspaceInAccountPermitRequest.getPermitType().equals(PermitType.REJECT)) {
						saveOrDelete = workspaceInAccountRepository.delete(workspaceInAccountEntity);
					}
					saveOrDelete.doOnSuccess(e->{
						workspaceBroker.send(
							new ServerSentStreamTemplate<WorkspaceInAccountPermitRequest>(
								workspaceInAccountEntity.getWorkspaceId(),
								(long)0,
								WorkspaceInAccountPermitRequest.builder()
									.id(workspaceInAccountEntity.getId())
									.workspaceId(workspaceInAccountEntity.getWorkspaceId())
									.permitType(workspaceInAccountPermitRequest.getPermitType())
									.accountName(workspaceInAccountPermitRequest.getAccountName())
								.build(),
								ServerSentStreamType.WORKSPACE_PERMIT_RESULT_ACCEPT
							) {}
						);
					}).subscribe();
					
					return Mono.just(workspaceInAccountEntity);
				});
			})
			.flatMap(e->ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response(Result._0, e.withAccountId(null)), ResponseWrapper.class)
			)			
		);
	}
	
	public Mono<ServerResponse> giveAdmin(ServerRequest request){
		return accountService.convertRequestToAccount(request)
			.flatMap(account -> 
			request.bodyToMono(WorkspaceInAccountGiveAdmin.class)
			.filterWhen(e->workspaceInAccountRepository.existsByWorkspaceIdAndAccountIdAndIsAdmin(e.getWorkspaceId(), account.getId(), true))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._204)))
			.flatMap(e->workspaceInAccountRepository.findById(e.getId()))
			.filterWhen(workspaceInAccount ->workspaceInAccountRepository.existsByAccountIdAndIsEnabled(workspaceInAccount.getAccountId(), true))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._206)))
			.flatMap(e-> workspaceInAccountRepository.save(e.withIsAdmin(true)))
			.flatMap(e->ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response(Result._0, e.withAccountId(null)), ResponseWrapper.class)
			)
		);
	}
	
	public Mono<ServerResponse> searchPermitRequestList(ServerRequest request){
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));
		
		return accountService.convertRequestToAccount(request)
		.filterWhen(e-> workspaceInAccountRepository.existsByWorkspaceIdAndAccountIdAndIsEnabled(workspaceId, e.getId(), true))
		.switchIfEmpty(Mono.error(new WorkspaceException(Result._201)))
		.flatMap(account -> {
			Sinks.Many<WokrspaceInAccountPermitListResponse> sinks = Sinks.many().unicast().onBackpressureBuffer();
			
			workspaceInAccountRepository.findAllPermitList(workspaceId)
			.doOnNext(e->{
				sinks.tryEmitNext(e);
			})
			.doFinally(f->{
				sinks.tryEmitComplete();
			})
			.subscribe();

			return ok()
			.contentType(MediaType.TEXT_EVENT_STREAM)
			.body(sinks.asFlux(), WokrspaceInAccountPermitListResponse.class);
		});
		
	}
	
	public Mono<ServerResponse> getIsAdmin(ServerRequest request){
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));
		return accountService.convertRequestToAccount(request)
			.flatMap(account -> {
				return workspaceInAccountRepository.existsByWorkspaceIdAndAccountIdAndIsAdmin(workspaceId, account.getId(), true);
			}).flatMap(e->
				ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response(Result._0, e), ResponseWrapper.class)
			);
	}
	
}
