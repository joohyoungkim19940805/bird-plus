import { accountController } from "./../controller/AccountController"
import { apiS3Controller } from "./../controller/ApiS3Controller";
import { chattingController } from "./../controller/ChattingController";
import { emoticonController } from "./../controller/EmoticonController";
import { eventStreamController } from "./../controller/EventStreamController";
import { noticeBoardController } from "./../controller/NoticeBoardController";
import { roomController } from "./../controller/RoomController";
import { workspaceController } from "./../controller/WorkspaceController";

import { windowUtil } from "./../window/WindowUtil";

/**
 * @author kimjoohyoung
 * @description 기본적으로 사용 할 preload 정의
 */

export const globalHandler = new class GlobalHandler{
	#workspaceId
	#option;
	constructor(){
		//n : name, v : value, c : create_at, u : update_at
		
		this.#option = JSON.parse(localStorage.getItem('o')).reduce((t,e)=>{
			let firstChar = e.OPTION_VALUE.charAt(0);
			let lastChar = e.OPTION_VALUE.charAt(e.OPTION_VALUE.length - 1);
			let obj = {
				['OPTION_NAME'] : e.n,
				['OPTION_CREATE_AT'] : e.c,
				['OPTION_UPDATE_AT'] : e.u
			};
			if( 
				(firstChar == '{' && lastChar == '}') || 
				(firstChar == '[' && lastChar == ']')
			){
				obj['OPTION_VALUE'] = JSON.parse(e.v)
			}else {
				obj['OPTION_VALUE'] = e.v;
			}
			t[e.n] = obj;
			return t;
		}, {});

	}

	resetWorkspace(){
		this.#workspaceId = undefined;
	}

	set workspaceId(workspaceId){
		if(this.#workspaceId === workspaceId) return;
		this.#workspaceId = workspaceId;
		ipcRenderer.webEventSend('workspaceChange', {workspaceId : this.workspaceId});
	}
	get workspaceId(){
		return this.#workspaceId
	}

	setOption({name, value}){
		this.#option[name]['OPTION_VALUE'] = value;
		localStorage.setItem('o', JSON.stringify( Object.values(this.#option)) );
	}

	getOption(name){
		return this.#option[name];
	}
}

export const ipcRenderer = new class IpcRendererWeb{
	#send = {};
	#invoke = {};
	#connection = {};
	constructor(){
		[
			accountController, apiS3Controller, 
			chattingController, emoticonController, 
			eventStreamController, noticeBoardController,
			roomController, workspaceController
		].flatMap(e=>{
			return Object.getOwnPropertyNames(Object.getPrototypeOf(e)).filter(e=> e != 'constructor').map(name=>{
				return { [name] : e }
			});
		}).forEach( ({name, e})=>{
			this.#send[name] = e[name] 
			this.#invoke[name] = e[name] 
		})
	}
	send(name, param){
		if( ! this.#send[name] || ! this.#send[name] instanceof Function){
			return;
		}
		this.#send[name](param,ipcRenderer);
	}
	invoke(name, param){
		if( ! this.#invoke[name] || ! this.#invoke[name] instanceof Function){
			return;
		}
		return this.#invoke[name](param);
	}
	webEventSend(eventName, data){
		if( ! this.#connection[eventName] || ! this.#connection[eventName] instanceof Function){
			return;
		}
		this.#connection[eventName](undefined, data);
	}
	on(eventName, callback){
		this.#connection[eventName] = callback;
	}
	/*
	send : {},
	invoke : {},
	connection : {},
	eventSend: (eventName, data) => {
		if( ! ipcRenderer.connection[eventName] instanceof Function){
			return;
		}
		ipcRenderer.connection[eventName](undefined, data);
	},
	on : (eventName, callback) => {
		connection[eventName] = callback;
	}*/
};

const electronEventTrigger = {
	objectEventListener : {},
	onEvent : {},
	on : (eventName, callBack) => {
		electronEventTrigger.onEvent[eventName] = callBack;
		ipcRenderer.on(eventName, (event, message) => {
			electronEventTrigger.trigger(eventName, event, message);
		})
	},
	addElectronEventListener : (eventName, callBack) => {
		if(electronEventTrigger.objectEventListener.hasOwnProperty(eventName)){
			electronEventTrigger.objectEventListener[eventName].push({ [callBack.name] : callBack });
		}else{
			electronEventTrigger.objectEventListener[eventName] = [{ [callBack.name] : callBack }];
		}
		ipcRenderer.on(eventName, (event, message) => {
			electronEventTrigger.trigger(eventName, event, message);
		})
	},
	removeElectronEventListener : (eventName, callBack) => {
		if(electronEventTrigger.objectEventListener.hasOwnProperty(eventName)){
			if(callBack){
				electronEventTrigger.objectEventListener[eventName] = electronEventTrigger.objectEventListener[eventName].filter(e=> ! e.hasOwnProperty(callBack.name))
			}else{
				delete electronEventTrigger.objectEventListener[eventName]
			}
		}
	},
	trigger : (eventName, event, message) => {
		new Promise(resolve=> {
			if( ! electronEventTrigger.objectEventListener.hasOwnProperty(eventName)){
				resolve();
				return;
			}
			electronEventTrigger.objectEventListener[eventName].forEach(async obj => {
				Object.values(obj).forEach(async callBack => {
					if( ! callBack || ! callBack instanceof Function){
						return;
					}
					new Promise(res=>{	
						try{
							if(eventName == 'checkForUpdates' || eventName == 'updateAvailable' || eventName == 'updateDownloaded'){
								callBack(event,message);
								return;
							}
			
							callBack(message);
						}catch(err){
							console.error(`${eventName} error message ::: `,err.message);
							console.error(`${eventName} error stack ::: `,err.stack);
							throw new Error('');
						}
						res();
					})
				})
			})
			resolve();
		})
		
		new Promise(resolve => {
			if( ! electronEventTrigger.onEvent[eventName] || ! electronEventTrigger.onEvent[eventName] instanceof Function){
				resolve();
				return;
			}
			try{
				electronEventTrigger.onEvent[eventName](message);
			}catch(err){
				console.error(`${eventName} error message ::: `, err.message);
				console.error(`${eventName} error stack ::: `, err.stack);
			}
			resolve();
		})
		
	}
};

export const myAPI = {
	/**
	 * 단방향 ipc 통신
	 * 보안상의 이유로ipcRenderer.send 전체 API를 직접 노출하지 않습니다. 
	 * Electron API에 대한 렌더러의 액세스를 가능한 한 많이 제한해야 합니다.
	 */
	//setTitle : (param) => ipcRenderer.send('setTitle', param),
	/**
	 * dialog:IPC 채널 이름 의 접두사는 코드에 영향을 미치지 않습니다. 
	 * 코드 가독성에 도움이 되는 네임스페이스 역할만 합니다.
	 * 보안상의 이유로ipcRenderer.invoke 전체 API를 직접 노출하지 않습니다. 
	 * Electron API에 대한 렌더러의 액세스를 가능한 한 많이 제한해야 합니다. 
	 */
	//openFile : () => ipcRenderer.invoke('dialog:openFile'),
	
	/**
	 * key-value가 아닌 함수는 일렉트론에서만쓰고 웹버전에서는 쓰지 않도록 합니다.
	 * event.electronEventTrigger은 일렉트론 + 웹이 함께써야 하는 부분이 있어 예외입니다.
	 */

	getProjectPath : async () => `${location.protocol}//${location.host}${location.pathname}/`,

	createSubWindow : async (param) => {
		//open(url?: string | URL, target?: string, features?: string): WindowProxy | null;
		window.open(`/mobile/create-sub-window/${param.pageName}`, param.pageId)
	},

	resetWorkspaceId: async () => {
		globalHandler.resetWorkspace();
	},

	getWorkspaceId: async () => {
		return globalHandler.workspaceId;
	},

	notifications: async (param) => ipcRenderer.send('notifications', param),

	getOption: async (optionName) => globalHandler.getOption(optionName),
	setOption: async (param) => globalHandler.setOption(param),

	isLogin: async () => {
		return windowUtil.isLogin(result=>{
			return {isLogin : result.isLogin}
		})
	},

	getServerUrl : async () => `${location.origin}${location.pathname}`,

	pageChange : {
		changeLoginPage : async () => ipcRenderer.send('changeLoginPage'),
		changeWokrspacePage : async () => ipcRenderer.send('changeWokrspacePage'),
		changeMainPage : async (workspaceId) => ipcRenderer.send('changeMainPage', workspaceId),
	},
	
	event : {
		electronEventTrigger : electronEventTrigger,
	},

	stream : {
		initWorkspaceStream : async (param) => ipcRenderer.send('initWorkspaceStream', param),
	},



	account : {
		loginProcessing : async (param) => ipcRenderer.invoke('loginProcessing', param),
		getAccountInfo : async () => ipcRenderer.invoke('getAccountInfo'),
		updateSimpleAccountInfo : async (param) => ipcRenderer.invoke('updateSimpleAccountInfo', param)
	},

	chatting : {
		sendChatting : async (param) => ipcRenderer.invoke('sendChatting', param),
		deleteChatting : async (param) => ipcRenderer.invoke('deleteChatting', param),
		searchChattingList : async (param) => ipcRenderer.invoke('searchChattingList', param),
	},
	
	workspace : {
		searchWorkspaceMyJoined : async (param) => ipcRenderer.invoke('searchWorkspaceMyJoined', param),
		searchNameSpecificList: async (param) => ipcRenderer.invoke('searchNameSpecificList', param),
		searchWorkspaceInAccount : async (param) => ipcRenderer.invoke('searchWorkspaceInAccount', param),
		getWorkspaceDetail : async (param) => ipcRenderer.invoke('getWorkspaceDetail', param),
		createPermitWokrspaceInAccount: async (param) => ipcRenderer.invoke('createPermitWokrspaceInAccount', param),
		createGiveAdmin : async (param) => ipcRenderer.invoke('createGiveAdmin', param),
		searchPermitRequestList : async (param) => ipcRenderer.invoke('searchPermitRequestList', param),
		getIsAdmin : async (param) => ipcRenderer.invoke('getIsAdmin', param),
		createWorkspaceJoined: async (param) => ipcRenderer.invoke('createWorkspaceJoined', param),
		createWorkspace : async (param) => ipcRenderer.invoke('createWorkspace', param),
		getWorkspaceInAccountCount : async (param) => ipcRenderer.invoke('getWorkspaceInAccountCount', param),
	},

	room : {
		createRoom : async (param) => ipcRenderer.invoke('createRoom', param),
		createMySelfRoom : async (param)=> ipcRenderer.invoke('createMySelfRoom', param),
		createRoomInAccount : async (param) => ipcRenderer.invoke('createRoomInAccount', param),
		createRoomFavorites : async (param) => ipcRenderer.invoke('createRoomFavorites', param),
		updateRoomInAccoutOrder : async (param) => ipcRenderer.invoke('updateRoomInAccoutOrder', param),
		updateRoomFavorites : async (param) => ipcRenderer.invoke('updateRoomFavorites', param),
		searchRoom : async (param) => ipcRenderer.invoke('searchRoom', param),
		searchMyJoinedRoomList : async (param) => ipcRenderer.invoke('searchMyJoinedRoomList', param),
		searchRoomFavoritesList : async (param) => ipcRenderer.invoke('searchRoomFavoritesList', param),
		searchRoomJoinedAccountList : async (param) => ipcRenderer.invoke('searchRoomJoinedAccountList', param),
		getRoomDetail : async (param) => ipcRenderer.invoke('getRoomDetail', param),
		isRoomFavorites : async (param) => ipcRenderer.invoke('isRoomFavorites', param)
	},

	noticeBoard : {
		createNoticeBoard : async (param) => ipcRenderer.invoke('createNoticeBoard', param),
		createNoticeBoardGroup : async (param) => ipcRenderer.invoke('createNoticeBoardGroup', param),
		createNoticeBoardDetail : async (param) => ipcRenderer.invoke('createNoticeBoardDetail', param),
		deleteNoticeBoard : async (param) => ipcRenderer.invoke('deleteNoticeBoard', param),
		deleteNoticeBoardGroup : async (param) => ipcRenderer.invoke('deleteNoticeBoardGroup', param),
		updateNoticeBoardOrder : async (param) => ipcRenderer.invoke('updateNoticeBoardOrder', param),
		updateNoticeBoardDetailOrder : async (param) => ipcRenderer.invoke('updateNoticeBoardDetailOrder', param),
		searchNoticeBoardList : async (param) => ipcRenderer.invoke('searchNoticeBoardList', param),
		searchNoticeBoardDetailList : async (param) => ipcRenderer.invoke('searchNoticeBoardDetailList', param),
	},
	s3: {
		generatePutObjectPresignedUrl : async (param) => ipcRenderer.invoke('generatePutObjectPresignedUrl', param),
		generateGetObjectPresignedUrl : async (param) => ipcRenderer.invoke('generateGetObjectPresignedUrl', param),
	},
	emoticon: {
		createEmotionReaction : async (param) => ipcRenderer.invoke('createEmotionReaction', param),
		getIsReaction : async (param) => ipcRenderer.invoke('getIsReaction', param)
	}

}
window.myAPI = myAPI;
console.log('????????????????');
console.log(window.myAPI, myAPI)