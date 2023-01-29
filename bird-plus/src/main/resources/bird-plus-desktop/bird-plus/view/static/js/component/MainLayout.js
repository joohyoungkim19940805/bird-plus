/**
 * 커스텀 메인 레이아웃
 */

class MainLayout extends HTMLElement {
	#isLoaded = false;
	#isAddEventDone = false;
	constructor(){
		super();
	}

	connectedCallback(){
        if( ! this.#isLoaded){
			this.#isLoaded = true;
        }
		if( ! this.#isAddEventDone ){
			this.addMainEvent();
			this.#isAddEventDone = true;
		}
    }
	disconnectedCallback(){
        this.#isLoaded = false;
    }

	addMainEvent(){
		console.log(window.myAPI);
		window.myAPI.electronEventTrigger.addElectronWindowEventListener('resized', ( [width, height] ) => {
			
		});
	}
}

window.customElements.define('main-layout', MainLayout);