
import axios from 'axios';
const log = console; 
class WindowUtil{
    constructor(){

    }
    async isLogin(callBack = () => {}){
        return axios.get(__serverApi + '/api/account/search/is-login', {
            headers:{
                'Content-Type': 'application/json'
            }
        })
        .catch(err=>{
            return err.response
        })
        .then(response => {
            if( ! this.responseIsOk(response)){
                return callBack({
                    isLogin: false,
                    status: response?.status,
                    statusText: response?.statusText
                });
            }else{
                if(response.data.code == 0){
                    response.isLogin = true;	
                }else if(response.data.code == 100 || response.data.code == 105 || response.data.code == 106 || response.data.code == 107){
                    response.isLogin = false;
                }else{
                    response.isLogin = false;
                }

                if(! response.isLogin){
                    axios.defaults.headers.common['Authorization'] = '';
                }
                return callBack(response);
            }
        })
    }

    responseIsOk({status = undefined} = {}){
        if( ! status){
            return false;
        }
        return (status == '200' || status == '201') ;
    }

    responseCheck(response){
		let status = response.status;
		let {code, data} = response.data;
        if(status == '200' || status == '201'){
			return response
		}else{
            log.error(response);
			throw new Error(response.message);
		}
	}

}

export const windowUtil = new WindowUtil();