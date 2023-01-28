//const { contextBridge, ipcRenderer } = require('electron')
const { contextBridge, ipcRenderer } = require('electron')
//console.log('test<<<', __project_path);
contextBridge.exposeInMainWorld('myAPI', {
	setTitle1 : (title) => {
		console.log('bridge <<< ', title)
		/**
		 * 단방향 ipc 통신
		 * 보안상의 이유로ipcRenderer.send 전체 API를 직접 노출하지 않습니다. 
		 * Electron API에 대한 렌더러의 액세스를 가능한 한 많이 제한해야 합니다.
		 */
		ipcRenderer.send('test', title)
	},
	/**
	 * dialog:IPC 채널 이름 의 접두사는 코드에 영향을 미치지 않습니다. 
	 * 코드 가독성에 도움이 되는 네임스페이스 역할만 합니다.
	 * 보안상의 이유로ipcRenderer.invoke 전체 API를 직접 노출하지 않습니다. 
	 * Electron API에 대한 렌더러의 액세스를 가능한 한 많이 제한해야 합니다.
	 * @returns 
	 */
	//openFile : () => ipcRenderer.invoke('dialog:openFile'),
	
	scanningUserDirectory : () => ipcRenderer.invoke('scanningUserDirectory'),

	getUserDirtoryList : () => ipcRenderer.invoke('getUserDirtoryList'),
	getUserDirtoryMapper : () => ipcRenderer.invoke('getUserDirtoryMapper'),
	getUserFileMapper : () => ipcRenderer.invoke('getUserFileMapper'),
	getUserFileExtensionMapper : () => ipcRenderer.invoke('getUserFileExtensionMapper'),
	getStatsMapper : () => ipcRenderer.invoke('getStatsMapper'),

	getScanningProgress : () => ipcRenderer.invoke('getScanningProgress')
})

/** 
 * 최초에 화면이 열릴시 document가 준비되었을 때의 이벤트를 정의한다.
 * node js의 process versions 개체에 엑세스하여 html 문서에 버전 번호를 추가하게 한다.
 */
window.addEventListener('DOMContentLoaded', () => {
	
})