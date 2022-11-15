import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {City} from "../../../core/model/city";

@Component({
  selector: 'app-city-card',
  templateUrl: './city-card.component.html',
  styleUrls: ['./city-card.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CityCardComponent implements OnInit {

  @Input()
  city!: City;
  @Output()
  onEditCity: EventEmitter<City> = new EventEmitter<City>();
  constructor() { }

  ngOnInit(): void {
  }

  editCity(city: City) {
    this.onEditCity.emit(city)
  }
}
