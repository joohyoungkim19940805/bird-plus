/**
 * layout 블럭  
 */
class ContentBlockLayout extends HTMLDivElement {
	#isLoaded = false;
	constructor(){
		super();
	}

	connectedCallback(e){
        if( ! this.#isLoaded){

            // 타이틀바 등록 = 누른상태로 창 이동, 최대화, 최소화 등
            
            this.#isLoaded = true;
        }
    }
    disconnectedCallback(){
        this.#isLoaded = false;
    }
}

window.customElements.define('content-block', ContentBlockLayout, {extends : 'div'});
