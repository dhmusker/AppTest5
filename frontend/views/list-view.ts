import { html, LitElement, customElement } from 'lit-element';
import '@vaadin/vaadin-combo-box';
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-grid/all-imports';
import '@vaadin/vaadin-grid';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-text-field';



@customElement('list-view')
export class ListView extends LitElement {
    createRenderRoot() {
        // Do not use a shadow root
        return this;
    }

    render() {
        return html`<vaadin-grid id="mygrid" theme="no-border column-borders"> </vaadin-grid>`;
    }
}
