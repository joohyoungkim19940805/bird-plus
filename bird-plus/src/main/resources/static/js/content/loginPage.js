/**
 * 
 */
const getStart = new class GetStart{
	contentWrappper = document.querySelector('.content_wrapper');
	//contentContainer = this.contentWrappper('.content_container');
	
	getStartWrapper = this.contentWrappper.querySelector('.get_start_wrapper');
	getStartContainer = this.getStartWrapper.querySelector('.get_start_container');
	getStartButton = this.getStartContainer.querySelector('.get_start');
	constructor(){
		let styleMap = window.getComputedStyle(this.getStartButton); 
		this.getStartContainer.style.width = styleMap.width;
		this.getStartContainer.style.height = styleMap.height;
		
		this.addEvent();
	}
	
	addEvent(){
		this.getStartButton.onclick = (event) => {
			this.getStartButton.remove();
			this.getStartContainer.classList.add('start');
			this.getStartContainer.style.width = '100vw';
			this.getStartContainer.ontransitionend = () => {
				this.getStartContainer.style.height = '100vh';
			}
		}
	}
	
}();