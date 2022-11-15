import {NgModule} from '@angular/core';
import {CitiesListComponent} from './containers/cities-list/cities-list.component';
import {CitiesRoutingModule} from "./cities-routing.module";
import {CommonModule} from "@angular/common";
import { CityCardComponent } from './components/city-card/city-card.component';
import {MatCardModule} from "@angular/material/card";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import { CityEditDialogComponent } from './components/dialog/city-edit-dialog/city-edit-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import {ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {MatPaginatorModule} from "@angular/material/paginator";


@NgModule({
  declarations: [
    CitiesListComponent,
    CityCardComponent,
    CityEditDialogComponent
  ],
    imports: [
        CitiesRoutingModule,
        CommonModule,
        FlexLayoutModule,
        MatCardModule,
        MatIconModule,
        MatButtonModule,
        MatDialogModule,
        ReactiveFormsModule,
        MatInputModule,
        MatPaginatorModule
    ]
})
export class CitiesModule { }
