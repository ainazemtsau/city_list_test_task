import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {City} from "../../../../core/model/city";

@Component({
  selector: 'app-city-edit-dialog',
  templateUrl: './city-edit-dialog.component.html',
  styleUrls: ['./city-edit-dialog.component.scss']
})
export class CityEditDialogComponent implements OnInit {

  cityEdit: FormGroup;
  constructor(private fb: FormBuilder,  @Inject(MAT_DIALOG_DATA) public data: City) {
    this.cityEdit = fb.group({
      photo: this.fb.control(data?.photo || ""),
      name: this.fb.control(data?.name || "")
    })
  }

  ngOnInit(): void {
  }

}
