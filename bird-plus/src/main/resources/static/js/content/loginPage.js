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
	
	#isWorkspaceJoinedPromise = common.loginSuccessPromise.then(async ()=>{
		return fetch('/api/chatting/is-workspace-joined', {
			method: 'GET',
			headers: {
				'Content-Type' : 'application/json'
			}
		}).then(response => {
			if( ! response.ok){
				console.error(response);
				throw new Error('response is not ok');
			}
			return response.json();
		}).then(result => {
			console.log(result)
			if(result.data){
				this.showStartMenuPage();
			}else{
				this.showCreateWorkspacePage();
			}
			return result.data;
		}).catch(error => {
			console.error(error);
			return false;
		})
	});

	#loginPage = Object.assign(document.createElement('div'), {
		className: 'login_page',
		innerHTML: `
			<form id="get_start_login_form" class="login_form">
				<div>
					<div>
						<label for="get_start_login_id">ID</label>
					</div>
					<input type="text" name="account_name" id="get_start_login_id" class="account_name" placeholder="Please enter your ID" autocomplete="username"/>
				</div>
				<div>
					<div>
						<label for="get_start_login_password">Password</label>
					</div>
					<input type="password" id="get_start_login_password" name="password" class="account_password" placeholder="Please enter your password" autocomplete="current-password"/>
				</div>
				<div class="find_wrapper">
					<a href="javascript:void(0);" data-page="forgot_password_page">Forgot password?</a>
					<a href="javascript:void(0);" data-page="sign_up_page">Sign up</a>
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
				<div class="find_wrapper">
					<a href="javascript:void(0);" data-page="login_page">Sign in</a>
					<a href="javascript:void(0);" data-page="sign_up_page">Sign up</a>
				</div>
				<div>
					<button type="submit">Send recovery email</button>
				</div>
			<form>
		`
	});
	
	#forgotPasswordSendEmailEndPage = Object.assign(document.createElement('div') ,{
		className: 'forgot_password_send_email_end_page',
		innerHTML: `
			<form id="forgot_password_send_email_end_form">
				<div>
					<p>send complate your email.</p>
					<p>plase recover try your account.</p>
				</div>
				<div class="find_wrapper">
					<a href="javascript:void(0);" data-page="login_page">Sign in</a>
					<a href="javascript:void(0);" data-page="sign_up_page">Sign up</a>
				</div>
			</form>
		`		
	})

	#signUpPage = Object.assign(document.createElement('div'),{
		className: 'sign_up_page',
		innerHTML: `
			<form id="sign_up_form">
				<div>
					<div>
						<label for="sign_up_account_name">your account id</label>
					</div>
					<input type="text" name="account_name" id="sign_up_account_name" class="account_name" placeholder="Please enter your ID" autocomplete="username" required/>
				</div>
				<div>
					<div>
						<label for="sign_up_email">your email</label>
					</div>
					<input type="email" name="email" id="sign_up_email" placeholder="Please enter your Email" required/>
				</div>
				<div>
					<div>
						<label for="sign_up_name">your full name</label>
					</div>
					<input type="text" name="full_name" id="sign_up_email" placeholder="Please enter your Email" required/>
				</div>
				<div>
					<div>
						<label for="sign_up_password">your password</label>
					</div>
					<input type="password" id="sign_up_password" name="password" class="account_password" placeholder="Please enter your password" autocomplete="current-password" required/>
				</div>
				<div>
					<div>
						<label for="sign_up_password_again">your password again</label>
					</div>
					<input type="password" id="sign_up_password_again" name="password_again" class="account_password" placeholder="Please enter your password (again)" autocomplete="current-password" required/>
				</div>
				<div class="find_wrapper">
					<a href="javascript:void(0);" data-page="login_page">Sign in</a>
					<a href="javascript:void(0);" data-page="forgot_password_page">Forgot password?</a>
				</div>
				<div>
					<button type="submit" class="sign_up_button">sign up</button>
				</div>
			</form>
		`
	});
	
	#createWorkspacePage = Object.assign(document.createElement('div'), {
		className: 'create_workspace_page',
		innerHTML: `
			<form id="create_workspace_form">
				<div> 
					<div>
						<label for="workspace_name">your workspace name</label>
					</div>
					<input type="text" id="workspace_name" name="workspace_name"/>
				</div>
				<div>
					<div>
						<label for="workspace_filter">access email filter <p style="margin:0">(if nothing is selected anyone can access)</p></label>
						<p style="color:red;" class="description_validation"></p>
					</div>
					<input type="email" name="validation_email" hidden value="email" data-default_value="email"/>
					<div class="space_between">
						<input type="email" list="workspace_filter_list" id="workspace_filter" name="workspace_filter" autocomplete="off"/>
						<button type="button" name="workspace_filter_add" id="workspace_filter_add">+</button>
					</div>
					<datalist id="workspace_filter_list" name="workspace_filter_list">
					</datalist>
					<div>
						<select name="workspace_filter_history">
						</select>
					</div>
				</div>
				<div>
					<label for="workspace_finally_only_permit">access is ultimately do permit by admin
						<input type="checkbox" id="workspace_finally_only_permit" name="workspace_finally_only_permit"/>
					</label>
				</div>
				<div>
					<button type="button" name="create_new_workspace_button" class="create_new_workspace_button">create new workspace</button>
				</div>
			</form>
		`
	});

	#startMenuPage = Object.assign(document.createElement('div'), {
		className: 'start_menu_page',
		innerHTML: `
			<form id="startMenu">
				<div>
					<label>
						see your workspace now
					</label>
				</div>
				<div class="find_wrapper">
					<a href="javascript:void(0);" data-page="download_page">bird plus download</a>
					<a href="javascript:void(0);" data-page="web_version_page">i want see web page version</a>
				</div>
				<div class="find_wrapper">
					<a href="javascript:void(0);" data-page="create_workspace_page">create new workspace</a>
				</div>
			<//form>
		`
	})

	constructor(){
		new Promise(resolve => {
			
			this.addOpeningEvent();
			
			/**
			 * login
			 */
			headerController.loginEvent(this.#loginPage, {isContainerLayer: false});
			let [forgotPassword, signUp] = this.#loginPage.querySelectorAll('[data-page="forgot_password_page"], [data-page="sign_up_page"]');
			forgotPassword.onclick = () => this.showForgotPasswordPage();
			signUp.onclick = () => this.showSignUpPage();
			/**
			 * forgot password
			 */
			this.forgotPasswordPageEvent(this.#forgotPasswordPage);
			/**
			 * forgor password send email end after
			 */
			this.forgotPasswordSendEmailEndPageEvent(this.#forgotPasswordSendEmailEndPage);
			/**
			 * sign up
			 */
			this.signUpPageEvent(this.#signUpPage);
			/**
			 * create workspace
			 */
			this.createWorkspaceEvent(this.#createWorkspacePage);
			/**
			 * menu page
			 */
			this.createSstartMenuEvent(this.#startMenuPage);
			
			resolve();
		})
	}
	
	async addOpeningEvent(){

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

		return padeEndProise.then(()=>{
			//this.createRoomContainer.replaceChildren(this.#createWorkspacePage);
			//this.createRoomContainer.replaceChildren(this.#loginPage);
			common.isLogin(async result => {
				if(result.isLogin){
					let isWorkspaceJoined = await fetch('/api/chatting/is-workspace-joined', {
						method: 'GET',
						headers: {
							'Content-Type' : 'application/json'
						}
					}).then(response => {
						if( ! response.ok){
							console.error(response);
							throw new Error('response is not ok');
						}
						return response.json();
					}).then(result => {
						console.log(result)
						return result.data;
					}).catch(error => {
						console.error(error);
						return false;
					})
					console.log(isWorkspaceJoined)
					if(isWorkspaceJoined){
						this.showStartMenuPage();
					}else{
						this.showCreateWorkspacePage();
					}
				}else {
					this.createRoomContainer.replaceChildren(this.#loginPage);
				}
			});
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

	async showLoginPage(){
		return this.#pageChange(this.#loginPage).then(() => {
			//... page change end callback
		});
	}
	async showForgotPasswordPage(){
		return this.#pageChange(this.#forgotPasswordPage).then(() => {
			//... page change end callback
		});
	}
	async showSignUpPage(){
		return this.#pageChange(this.#signUpPage).then(() => {
			//... page change end callback
		});
	}
	async showForgotPasswordSendEmailEndPage(){
		return this.#pageChange(this.#forgotPasswordSendEmailEndPage).then(() => {
			//... page change end callback
		});
	}
	async showCreateWorkspacePage(){
		return this.#pageChange(this.#createWorkspacePage).then(() => {
			//... page change end callback
		});
	}
	async showStartMenuPage(){
		return this.#pageChange(this.#startMenuPage).then((startMenuPage) => {
			let page = 0;
			let size = 10;
			let createWorkspaceList = () => fetch(`/api/chatting/search-workspace-joined?page=${page}&size=${size}`, {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json'
				}
			}).then(response => {
				if( ! response.ok){
					console.error(response);
					throw new Error('response is not ok')
				}
				return response.json();
			}).then(result => {
				console.log('result ::: ', result);
			})
			createWorkspaceList();
		});
	}

	/**
	 * 
	 * @param {HTMLDivElement} page 
	 */
	#pageChange(page){
		return new Promise(resolve => {
			this.createRoomContainer.classList.remove('start');
			this.createRoomContainer.ontransitionend = (event) => {
				this.createRoomContainer.replaceChildren(page);
				this.createRoomContainer.ontransitionend = '';
				this.createRoomContainer.classList.add('start');
				resolve(page);
			}
		});
	}

	/**
	 * 
	 * @param {HTMLDivElement} forgotPassworPage 
	 */
	forgotPasswordPageEvent(forgotPassworPage){
		/*common.loginSuccessPromise.then(()=>{
			if(this.#loginPage.isConnected){
				this.#pageChange(this.#createWorkspacePage);
			}
		});*/

		let form = forgotPassworPage.querySelector('#forgot_password_form');
		let isClick = false;
		form.onsubmit = (event) => {
			event.preventDefault();
			if(isClick){
				return;
			}
			isClick = true;
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
				isClick = false;
				if(result.code == 0){
					this.showForgotPasswordSendEmailEndPage();
				}else{
					alert(result.message);
					console.error(result);
				}
			}).catch(err => {
				isClick = false;

				console.error(err);
				if(err.message){
					alert(err.message);
				}
			})
		}
		let [loginPage, signUpPage] = form.querySelectorAll('[data-page="login_page"], [data-page="sign_up_page"]');
		loginPage.onclick = () => this.showLoginPage();
		signUpPage.onclick = () => this.showSignUpPage();
	}
	
	forgotPasswordSendEmailEndPageEvent(forgotPasswordSendEmailEndPage){
		common.loginSuccessPromise.then(()=>{
			if(this.#loginPage.isConnected){
				this.#pageChange(this.#createWorkspacePage);
			}
		});

		let [loginPage, signUpPage] = forgotPasswordSendEmailEndPage.querySelectorAll('[data-page="login_page"], [data-page="sign_up_page"]');
		loginPage.onclick = () => this.showLoginPage();
		signUpPage.onclick = () => this.showSignUpPage();
	}
	/**
	 * 
	 * @param {HTMLDivElement} signUpPage 
	 */
	signUpPageEvent(signUpPage){
		/*common.loginSuccessPromise.then(()=>{
			if(this.#loginPage.isConnected){
				this.#pageChange(this.#createWorkspacePage);
			}
		});*/

		let form = signUpPage.querySelector('#sign_up_form');

		let {
			account_name: accountName,
			email,
			password,
			password_again: passwordAgain
		} = form
		let isClick = false;
		form.onsubmit = (event) => {
			event.preventDefault();
			if(isClick){
				return;
			}
			isClick = true;
			fetch('/create', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({
					accountName: accountName.value,
					email: email.value,
					password: password.value,
					passwordAgain: passwordAgain.value,
				})
			}).then(response => {
				if( ! response.ok){
					console.error(response);
					throw new Error('response is not ok');
				}
				return response.json();
			}).then(result=>{
				isClick = false;

				alert(result.message);
			}).catch(err => {
				isClick = false;

				console.error(err);
				if(err.message){
					alert(err.message);
				}
			})
		}
		let [loginPage, forgotPassword] = form.querySelectorAll('[data-page="login_page"], [data-page="forgot_password_page"]');
		loginPage.onclick = () => this.showLoginPage();
		forgotPassword.onclick = () => this.showForgotPasswordPage();
	}

	createWorkspaceEvent(createWorkspacePage){
		let form = createWorkspacePage.querySelector('#create_workspace_form');
		let {
			workspace_name: workspaceName,
			validation_email: validationEmail,
			workspace_filter: workspaceFilter,
			workspace_filter_add: workspaceFilterAdd,
			workspace_filter_history: workspaceFilterHistory,
			workspace_finally_only_permit: workspaceFinallyOnlyPermit,
			create_new_workspace_button: createNewWorkspaceButton
		} = form;
		let descriptionValidation = form.querySelector('.description_validation');

		//console.log(workspaceName, workspaceFilter, workspaceFilterAdd, workspaceFilterHistory, workspaceFinallyOnlyPermit, validationEmail);
		//let isValidationOk = false;
		let isOneClick = false;

		workspaceFilter.oninput = () => {
			if(workspaceFilter.value == ''){
				descriptionValidation.textContent = '';
				return;	
			}
			let domainSeparatorIndex = workspaceFilter.value.indexOf('@');
			if(domainSeparatorIndex == 0){
				validationEmail.value = validationEmail.dataset.default_value + workspaceFilter.value.substring(workspaceFilter.value.indexOf('@'))	
			}else if(domainSeparatorIndex > 0){
				validationEmail.value = workspaceFilter.value
			}else{
				validationEmail.value = validationEmail.dataset.default_value;
			}
			
			descriptionValidation.textContent = validationEmail.validationMessage;
		}

		workspaceFilter.onblur = () => {
			if(workspaceFilter.value == ''){
				descriptionValidation.textContent = '';
			}
		}

		workspaceFilterAdd.onclick = () => {
			if(workspaceFilter.value == ''){
				workspaceFilterAdd.classList.add('shake');
				setTimeout(()=>{workspaceFilterAdd.classList.remove('shake')},150)
				return;
			}else if( ! validationEmail.validity.valid){
				descriptionValidation.classList.add('shake');
				setTimeout(()=>{descriptionValidation.classList.remove('shake')},150)
				workspaceFilter.classList.add('shake');
				setTimeout(()=>{workspaceFilter.classList.remove('shake')},150)
				return;
			}
			let clone = workspaceFilter.cloneNode(true);
			clone.value = clone.value.substring(clone.value.indexOf('@'))
			workspaceFilter.value = '';
			let originRect = workspaceFilter.getBoundingClientRect();
			Object.assign(clone.style,{
				position: 'fixed',
				width: originRect.width + 'px', height: originRect.height + 'px',
				top: originRect.top + 'px', bottom: originRect.bottom + 'px',
				left: originRect.left + 'px', right: originRect.right + 'px',
				transition: 'all 0.5s'
			});
			workspaceFilter.parentElement.append(clone);
			let intervar = setInterval(()=>{
				if( ! clone.isConnected){
					return;
				}
				clone.style.top = workspaceFilterHistory.getBoundingClientRect().top + 'px';
				clone.ontransitionend = () => {
					let option = Object.assign(document.createElement('option'),{
						value: clone.value 
					})
					new Promise(resolve => {
						if([...workspaceFilterHistory.options].some(e=> e.value == clone.value) == false){
							workspaceFilterHistory.prepend(option);
						}
						if([...workspaceFilter.list.options].some(e=> e.value == clone.value) == false){
							workspaceFilter.list.prepend(option.cloneNode(true));
						}
						option.textContent = option.value;
						option.selected = true;
						option.disabled = true;
						resolve();
					});
					clone.remove();
				}
				clearInterval(intervar);					
			}, 100);
		}

		workspaceFilter.onkeyup = (event) => {
			if(event.key == 'Enter'){
				workspaceFilterAdd.click();
			}
		}

		form.onsubmit = (event) => {
			event.preventDefault();
		}
		createNewWorkspaceButton.onclick = (event) => {
			if(/* ! isValidationOk ||*/ isOneClick){
				return;
			}else if( ! workspaceName.value || workspaceName.value == ''){
				workspaceName.classList.add('shake');
				setTimeout(()=>{workspaceName.classList.remove('shake')},150)
				workspaceName.labels[0].classList.add('shake');
				setTimeout(()=>{workspaceName.labels[0].classList.remove('shake')},150)
				return;
			}
			isOneClick = true;
			fetch('/api/chatting/create-workspace', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({
					workspaceName: workspaceName.value,
					accessFilter: [...workspaceFilterHistory.options].map(e=>e.value),
					isFinallyPermit: workspaceFinallyOnlyPermit.checked
				})
			}).then(response => {
				if( ! response.ok){
					console.error(response);
					throw new Error('response is not ok');
				}
				return response.json();
			}).then(result => {
				if(result.code == 0){
					this.showStartMenuPage().then(() => {
						workspaceName.value = '';
						isOneClick = true;
					});
				}else{
					alert(result.message);
					isOneClick = false;
				}
				console.log(result);
			}).catch(err=>{
				console.error(err);
				isOneClick = false;
			})
		}

		common.loginSuccessPromise.then(()=>{
			//<a href="javascript:void(0);" data-page="start_menu_page">
			let aTagStartMenuPage = Object.assign(document.createElement('a'), {
				href: 'javascript:void(0);',
				textContent: 'See the workspaces i have joined'
			})
			let div = Object.assign(document.createElement('div'), {
				className: 'find_wrapper',
			});
			div.prepend(aTagStartMenuPage)
			form.append(div);
			aTagStartMenuPage.onclick = () => this.showStartMenuPage();
		});
	}

	createSstartMenuEvent(startMenuPage){
		let [downloadPage, webVersionPage, createWorkspacePage]	= startMenuPage.querySelectorAll('[data-page="download_page"], [data-page="web_version_page"], [data-page="create_workspace_page"]')
		downloadPage.onclick = () => {}
		webVersionPage.onclick = () => {}
		createWorkspacePage.onclick = () => this.showCreateWorkspacePage();

	}
	
}();