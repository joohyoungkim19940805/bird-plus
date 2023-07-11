const common = new class Common{
	
	globalMouseEvent = undefined;
	lastClickElementPath = undefined;
	globalClickEventPromiseResolve;
	globalClickEventPromise = new Promise(resolve=>{
		this.globalClickEventPromiseResolve = resolve;
	});
	
	constructor(){
		document.addEventListener('mousemove', (event) => {
			//mousePos = { x: event.clientX, y: event.clientY };
			//mousePosText.textContent = `(${mousePos.x}, ${mousePos.y})`;
			this.globalMouseEvent = event;
		});
		document.addEventListener('mousedown', (event) => {
			this.lastClickElementPath = event.composedPath();
			this.globalClickEventPromiseResolve(event)
			this.globalClickEventPromise = new Promise(resolve => {
				this.globalClickEventPromiseResolve = resolve;
			})
		})
	}
	
	processingElementPosition(element, target){
		let {x, y, height, width} = target.getBoundingClientRect();
		
		let elementHeightPx = element.clientHeight;
		let elementWidthPx = element.clientWidth;
		let elementTop = (y - elementHeightPx);
		let elementLeft = (x - elementWidthPx);
		if(elementTop > 0){
			element.style.top = elementTop + 'px';
		}else{
			element.style.top = y + height + 'px';
		}
		if(elementLeft > 0){
			element.style.left = elementLeft + 'px';
		}else{
			element.style.left = x + width + 'px'; 
		}
		//element.style.left = x + 'px';
	}
	
	isMouseInnerElement(element){
		if( ! this.globalMouseEvent) return;
		let {clientX, clientY} = this.globalMouseEvent;
		let {x, y, width, height} = element.getBoundingClientRect();
		let isMouseInnerX = ((x + width) >= clientX && x <= clientX);
		let isMouseInnerY = ((y + height) >= clientY && y <= clientY);
		return (isMouseInnerX && isMouseInnerY);
	}
	
	outClickElementListener(element, callBack = ({oldEvent, newEvent})=>{}){

		if(element == undefined || element?.nodeType != Node.ELEMENT_NODE){
			throw new Error('element is not Element');
		}

		let oldEvent = undefined;
		let newEvent = undefined;
		const simpleObserver = () => {
			this.globalClickEventPromise.then((event)=>{
				let isMouseOut = ! this.isMouseInnerElement(element);
				newEvent = event;
				callBack({oldEvent, newEvent, isMouseOut});
				oldEvent = event;
				simpleObserver();
			})
		}
		simpleObserver();
	}
	
	createLoadingRotate(width = 15, height = 15){
		let div = Object.assign(document.createElement('div'),{
			className: 'loading_rotate'
		});
		div.style.width = 15 + 'px';
		div.style.height = 15 + 'px';
		return div;
	}
	
	isLogin(callBack = () => {}){
		return fetch('/api/login/isLogin', {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			}
		}).then(response => {
			if( ! response.ok){
				return callBack({
					isLogin: false,
					status: response.status,
					statusText: response.statusText
				});
			}else{
				return response.json().then(json=>{
					if(json.code == 0){
						json.isLogin = true;	
					}else{
						json.isLogin = false;
					}
					return callBack(json);
				});
			}
		}).catch(error=>{
			return callBack({
				isLogin: false,
				message: error.message,
				stack: error.stack
			});
		});
	}
}();