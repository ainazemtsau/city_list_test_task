import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CitiesListComponent} from "./containers/cities-list/cities-list.component";

const routes: Routes = [
  {
    path: '',
    component: CitiesListComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CitiesRoutingModule { }
