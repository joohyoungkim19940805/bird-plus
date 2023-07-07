/**
 * 
 */
const getStart = new class GetStart{
	contentWrappper = document.querySelector('.content_wrapper');
	contentContainer = this.contentWrappper.querySelector('.content_container');
	
	getStartWrapper = this.contentWrappper.querySelector('.get_start_wrapper');
	getStartContainer = this.getStartWrapper.querySelector('.get_start_container');
	getStartButton = this.getStartContainer.querySelector('.get_start');
	constructor(){
		this.getStartContainer.style.width = '1px';
		this.getStartContainer.style.height = '20px';
		
		let styleMap = window.getComputedStyle(this.getStartContainer); 
		//this.getStartContainer.style.height = styleMap.height;
		//this.getStartContainer.style.maxHeight = styleMap.height;
		this.addEvent();
		

	}
	
	addEvent(){

		let widthTransitionEndResolve;
		let widthTransitionEndPromise = new Promise(resolve=>{
			widthTransitionEndResolve = resolve;
		})
		let padeEndPromiseList = [];
		
		let padeEndResolve;
		let padeEndProise = new Promise(resolve => {
			padeEndResolve = resolve
		})
		
		//this.getStartButton.remove();
		this.getStartContainer.classList.add('start');
		setTimeout(()=>{
			this.getStartContainer.style.width = '100vw';
		}, 1)
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
		
		padeEndProise.then(()=>{
			Promise.all(padeEndPromiseList).then(()=>{
				let delay = 100;
				[...this.contentContainer.children].reverse().forEach(item=>{
					[...item.children].forEach((e, i)=>{
						setTimeout(()=>{
							e.style.color = 'aliceblue';
						}, i * delay)
					});
				});
			});
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
	}
	
}();