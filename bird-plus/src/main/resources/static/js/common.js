const common = new class{
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
}();