import { windowUtil } from "../window/WindowUtil"; 
import axios from 'axios';
const log = console;

class ApiS3Controller {
	constructor() {
	}
	generatePutObjectPresignedUrl(param){
		return windowUtil.isLogin( result => {
			param = Object.entries(param).reduce((total, [k,v]) => {
				if(v != undefined && v != ''){
					total[k] = v;
				}
				return total;
			},{});
			if(result.isLogin){
				return axios.post(`${__serverApi}/api/generate-presigned-url/create/`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => {
					return response.data;
				}).catch(err=>{
					log.error('IPC sendChatting error', err);
					return err.response.data;
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			log.error('sendChatting login error ::: ', error.message);
			log.error('sendChatting login error stack ::: ', error.stack);
			return undefined;
		});
	}
	generateGetObjectPresignedUrl(param){
		return windowUtil.isLogin( result => {
			param = Object.entries(param).reduce((total, [k,v]) => {
				if(v != undefined && v != ''){
					total[k] = v;
				}
				return total;
			},{});
			if(result.isLogin){
				return axios.post(`${__serverApi}/api/generate-presigned-url/search/`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => {
					return response.data;
				}).catch(err=>{
					log.error('IPC sendChatting error', err);
					return err.response.data;
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			log.error('sendChatting login error ::: ', error.message);
			log.error('sendChatting login error stack ::: ', error.stack);
			return undefined;
		});
	}
}
export const apiS3Controller = new ApiS3Controller();