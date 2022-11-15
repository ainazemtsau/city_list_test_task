import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {catchError, Observable, take, throwError} from 'rxjs';
import {AuthenticationService} from "../services/authentication.service";
import {MatDialog} from "@angular/material/dialog";
import {LoginComponent} from "../component/login/login.component";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthenticationService, public dialog: MatDialog) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => {
      if (err.status === 401) {
        this.authenticationService.logout();
        this.openDialog();
      }
      const error = err.error.message || err.statusText;
      return throwError(error);
    }))
  }

  openDialog() {
    const dialogRef = this.dialog.open(LoginComponent, {data: {additionalMessage: 'You have to login as administrator'}});
    dialogRef.afterClosed().pipe(take(1)).subscribe(result => {
      if(result){
        this.authenticationService.login(result.username, result.password)
      }
    });
  }
}
