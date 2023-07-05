const common = new class{
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
}();