import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ModelModule } from "./model/model.module";
import { CoreModule } from "./core/core.module";
import { TableComponent } from "./core/table.component";
import { FormComponent } from "./core/form.component";
import { TasklistComponent } from './tasklist.component';
import { routing } from "./tasklist.routing";

@NgModule({
    imports: [BrowserModule, ModelModule, CoreModule, routing],
    declarations: [TasklistComponent],
    bootstrap: [TasklistComponent]
})
export class TasklistModule { }
