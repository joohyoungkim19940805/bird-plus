/**
 * 
 */
const getStart = new class GetStart{
	contentWrappper = document.querySelector('.content_wrapper');
	contentContainer = this.contentWrappper.querySelector('.content_container');
	
	getStartWrapper = this.contentWrappper.querySelector('.get_start_wrapper');
	getStartContainer = this.getStartWrapper.querySelector('.get_start_container');
	getStartButton = this.getStartContainer.querySelector('.get_start');
	
	createRoomWrapper = this.getStartContainer.querySelector('.create_room_wrapper');
	createRoomContainer = this.createRoomWrapper.querySelector('.create_room_container');
	
	#loginPage = Object.assign(document.createElement('div'), {
		className: 'login_page',
		innerHTML: `
			<form id="get_start_login_form" class="login_form">
				<div>
					<div>
						<label for="get_start_login_id">ID</label>
					</div>
					<input type="text" name="id" id="get_start_login_id" class="account_id" placeholder="Please enter your ID" autocomplete="username"/>
				</div>
				<div>
					<div>
						<label for="get_start_login_password">Password</label>
					</div>
					<input type="password" id="get_start_login_password" name="password" class="account_password" placeholder="Please enter your password" autocomplete="current-password"/>
				</div>
				<div class="find_wrapper">
					<a href="/" id="get_start_forgot_password">Forgot password?</a>
					<a href="/" id="get_start_sign_up">sign up</a>
				</div>
				<div>
					<button type="button" class="login_send">login</button>
				</div>
				<div class='status_text'>
				</div>
			</form>
		`
	});
	
	#forgotPasswordPage = Object.assign(document.createElement('div'), {
		className: 'forgot_password_page',
		innerHTML: `
			<form id="forgot_password_form" class="">
				<div>
					<label for="get_start_forgot_password_email">Enter your email address and we’ll send you a recovery link.</label>
				</div>
				<div>
					<input type="email" name="email" id="get_start_forgot_password_email" placeholder="Please enter your Email" required/>
				</div>
				<div>
					<button type="submit">Send recovery email</button>
				</div>
			<form>
		`
	})
	
	constructor(){
		
		this.addOpeningEvent();
		console.log(headerController);
		headerController.loginEvent(this.#loginPage, {isContainerLayer: false});
		this.forgotPasswordPageEvent(this.#forgotPasswordPage)
	}
	
	addOpeningEvent(){

		let widthTransitionEndResolve;
		let widthTransitionEndPromise = new Promise(resolve=>{
			widthTransitionEndResolve = resolve;
		})
		let padeEndPromiseList = [];
		
		let padeEndResolve;
		let padeEndProise = new Promise(resolve => {
			padeEndResolve = resolve
		});

		this.getStartContainer.classList.add('start');

		let lodingDelay = setInterval(()=>{
			if(this.getStartContainer.isConnected){
				this.getStartContainer.style.width = '100vw';
				clearInterval(lodingDelay);
			}
		},50)

		this.getStartContainer.ontransitionend = (event) => {
			if(event.propertyName == 'height'){
				padeEndResolve();
			}
			widthTransitionEndResolve();
			this.getStartContainer.style.maxHeight = ''
			this.getStartContainer.style.height = '100vh';
		}

		let intersectionObserver = new IntersectionObserver((entries, observer) => {
			entries.forEach(entry =>{
				if (entry.isIntersecting) {
						widthTransitionEndPromise.then(() => {
							entry.target.style.visibility = 'visible';
							entry.target.style.opacity = '1';
							let delay = 100;
							[...entry.target.children].forEach((e,i)=>{
								padeEndPromiseList.push(new Promise(resolve=>{
									setTimeout(()=>{
										e.style.visibility = 'visible';
										e.style.opacity = '1';
										resolve()
									}, i * delay)
								}))
							})
						});
				}else{
					entry.target.style.visibility = 'hidden';
					entry.target.style.opacity = '0';
					[...entry.target.children].forEach((e,i)=>{
						e.style.visibility = 'hidden';
						e.style.opacity = '0';
					})
				}
			})
		}, {
			threshold: 0.1, 
			root: this.getStartContainer
		});
		new Promise(res=>{
			[...this.contentContainer.children].reverse().forEach((item, i)=>{
				intersectionObserver.observe(item);
				let textSplit = item.textContent.trim().split('').map(text=>{
					let span = Object.assign(document.createElement('span'), {
						textContent: text
					});
					return span;
				});
				item.replaceChildren(...textSplit);
				
				item.style.marginLeft = i * 5 + 'vw';
			});
			res();
		})

		padeEndProise.then(()=>{
			//this.showLoginPage();
			this.showForgotPasswordPage();
			return Promise.all(padeEndPromiseList).then(()=>{
				let delay = 100;
				
				let colorChangePromiseList = [...this.contentContainer.children].reverse().map( item=>{
					return Promise.all([...item.children].map( async (e, i)=>{
						return new Promise(res=>{
							setTimeout(()=>{
								e.style.color = 'rgb(240 248 255 / 0.95)';
								res();
							}, i * delay)
						});
					}));
				});
				return Promise.all(colorChangePromiseList);
			});
		}).then(() => {
			this.createRoomContainer.classList.add('start');
		});
		
	}
	
	showContainer(){

	}

	showLoginPage(){
		this.createRoomContainer.replaceChildren(this.#loginPage);
	}
	
	showForgotPasswordPage(){
		this.createRoomContainer.replaceChildren(this.#forgotPasswordPage);
	}
	
	forgotPasswordPageEvent(forgotPassworPage){
		let form = forgotPassworPage.querySelector('#forgot_password_form');
		form.onsubmit = (event) => {
			event.preventDefault();
			fetch('/forgot-password-send-email', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({email: form.email.value})
			}).then(response => {
				if( ! response.ok){
					console.log(response);	
				}
				return response.json();
			}).then(result=>{
				console.log(result);
			})
		}
	}
}();