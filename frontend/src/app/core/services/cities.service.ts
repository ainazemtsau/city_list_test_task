import { Injectable } from '@angular/core';
import {AbstractCrudHttpServiceService} from "./abstract-crud-http-service.service";
import {City} from "../model/city";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CitiesService extends AbstractCrudHttpServiceService<City>{

  constructor(protected override http: HttpClient) {
    super(http)
  }

  getBaseUrl(): string {
    return `${environment.apiUrl}/city`;
  }
  countAllByName(name: string): Observable<number> {
    return this.http.get<number>(this.getBaseUrl() + "/number/"+name);
  }
}
