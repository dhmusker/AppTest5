import { html, LitElement, customElement } from 'lit-element';
import '@vaadin/vaadin-form-layout';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-combo-box';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-checkbox';



@customElement('project-dialog1-view')
export class ProjectDialog1View extends LitElement {
    createRenderRoot() {
        // Do not use a shadow root
        return this;
    }

    render() {
        return html`<h3>Project Dialog 1</h3>
      <vaadin-form-layout style="width: 50%;">
        <vaadin-integer-field label="Id" id="id"></vaadin-integer-field>
        <vaadin-text-field label="Project Manager" id="manager"></vaadin-text-field>
        <vaadin-text-field label="Title" id="title" colspan="2"></vaadin-text-field>
        <vaadin-text-field label="Business Case" id="businessCase"></vaadin-text-field>
        <vaadin-date-picker label="Start Date" id="dateStarted"></vaadin-date-picker>
        <vaadin-date-picker label="End Date" id="dateEnded"></vaadin-date-picker>
        <vaadin-checkbox label="Is Active" id="isActive"></vaadin-checkbox>
      </vaadin-form-layout>
      <vaadin-horizontal-layout
        style="margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-l);"
        theme="spacing"
      >
        <vaadin-button theme="primary" id="save"> Save </vaadin-button>
        <vaadin-button id="delete"> Delete </vaadin-button>
        <vaadin-button id="cancel"> Cancel </vaadin-button>
      </vaadin-horizontal-layout>`;
    }
}
