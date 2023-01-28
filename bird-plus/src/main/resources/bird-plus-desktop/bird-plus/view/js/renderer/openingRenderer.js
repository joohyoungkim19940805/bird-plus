
/*
document.getElementById('openFileBtn').onclick = async () => {
	const filePath = await window.myAPI.openFile();
	console.log(filePath);
	document.getElementById('showFilePath').textContent = filePath;
}
*/
import UserDirtoryListDBHandler from "../handler/UserDirtoryListDBHandler"
new class openingRenderer{
	#container = document.getElementById('container');
	#lodingBarWrapper = this.#container.querySelector('.loding_bar_wrapper');
	
	#statusWrapper = this.#lodingBarWrapper.querySelector('.status_wrapper');
	#statusText = this.#statusWrapper.querySelector('.status_text');
	
	#lodingBar = this.#lodingBarWrapper.querySelector('.loding_bar');
	#lodingProgress = this.#lodingBar.querySelector('.progress');

	#desktopLength = 0;

	constructor(){
		
		window.addEventListener('load', () => {
			new Promise(resolve => {
				// 디벨로퍼보다 빨리 실행되어 디버깅이 안되기 때문에 지연을 넣는다.
				setTimeout(()=>{
					let step_1__interval = this.step__scanningProgress()
					window.myAPI.scanningUserDirectory().then(len => {
						clearInterval(step_1__interval);
						step_1__interval = null;
						this.#statusText.textContent = `파일 ${len} 건 스캔 완료`
						this.#desktopLength = len;
						//document.querySelector('a#move_page_test').click();
						resolve('done');
					});
				}, 1500)	
			}).then(()=>{
				return //this.step__insertUserDirtoryList();
			}).then(()=>{
				console.log('#desktopLength>>> ',this.#desktopLength)
			})
		});

	}

	step__scanningProgress(){
		return setInterval(()=>{
			window.myAPI.getScanningProgress().then(len=>{
				this.#statusText.textContent = `파일 ${len} 건 스캔 진행 중`
			})
		},50);
	}

	async step__insertUserDirtoryList(){
		const dbHandler = new UserDirtoryListDBHandler();
		let i = 0;
		return dbHandler.openPromise.then( () =>{
			let insert = () => {
				console.log('index >>>', i)
				return window.myAPI.getUserDirtoryList().then(userDirtoryList => {
					return new Promise(resolve => {
						if( ! userDirtoryList || userDirtoryList.length == 0){
							resolve('done')
						}
						let promise = Promise.all(userDirtoryList.map(async e=>{
							return await new Promise(res => {
								let column = dbHandler.columnContainer
								column.path = e;
								res( dbHandler.addItem(column).then(()=>{
									i += 1;
									if(i%10000 == 0){
										console.log(i);
									}
									return Promise.resolve();
								}) );
							})
						}))
						resolve(promise)
					})
				}).then(result=>{
					console.log(result);
					return new Promise(resolve => {
						if(result !== 'done'){
							return resolve(insert());
						}else{
							return resolve(result);
						}
					})
				})
			}
			return insert();
		})
	}



}();
