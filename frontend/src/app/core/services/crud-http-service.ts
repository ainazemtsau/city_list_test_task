import {Observable} from "rxjs";

export interface CrudHttpService<T> {
  getAll(): Observable<T[]>

  update(obj: T): Observable<T>

  getBaseUrl(): string
}
