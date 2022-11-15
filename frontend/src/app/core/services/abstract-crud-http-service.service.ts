import {Injectable} from "@angular/core";
import {CrudHttpService} from "./crud-http-service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Pageable} from "../model/pageable";

@Injectable({
  providedIn: 'root'
})
export abstract class AbstractCrudHttpServiceService<T> implements CrudHttpService<T> {

  protected constructor(protected http: HttpClient) {
  }

  abstract getBaseUrl(): string;

  getAll(): Observable<T[]> {
    return this.http.get<T[]>(this.getBaseUrl());
  }

  getAllWithPageable(pageable: Pageable, predicate?: string): Observable<T[]> {
    return this.http.get<T[]>(`${this.getBaseUrl()}?size=${pageable.size}&page=${pageable.index}&${predicate}`);
  }

  update(obj: T): Observable<T> {
    return this.http.put<T>(this.getBaseUrl(), obj);
  }

  countAll(predicate?: string): Observable<number> {
    return this.http.get<number>(this.getBaseUrl() + "/number/?"+predicate);
  }

}
