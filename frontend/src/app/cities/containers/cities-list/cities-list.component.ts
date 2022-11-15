import {Component, OnInit, ViewChild} from '@angular/core';
import {CitiesService} from "../../../core/services/cities.service";
import {BehaviorSubject, debounceTime, distinctUntilChanged, switchMap, take, tap} from "rxjs";
import {City} from "../../../core/model/city";
import {MatDialog} from "@angular/material/dialog";
import {CityEditDialogComponent} from "../../components/dialog/city-edit-dialog/city-edit-dialog.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatPaginator, PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-cities-list',
  templateUrl: './cities-list.component.html',
  styleUrls: ['./cities-list.component.scss'],
})
export class CitiesListComponent implements OnInit {

  cities: City[] = []
  resultsLength = 0;
  pageSize = 20;
  pageIndex = 0;

  pageEvent!: PageEvent;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  private filtering$ = new BehaviorSubject<string>("");

  constructor(private cityService: CitiesService, public dialog: MatDialog, private _snackBar: MatSnackBar) {
    cityService.getAllWithPageable({size: this.pageSize, index: this.pageIndex})
      .subscribe(cities => this.cities = cities);
    cityService.countAll().pipe(take(1))
      .subscribe(count => this.resultsLength = count)
  }

  ngOnInit(): void {
    this.filtering$
      .pipe(
        debounceTime(150),
        distinctUntilChanged(),
        tap(v => this.cityService.countAll("name=" + v).pipe(take(1))
          .subscribe(next => this.resultsLength = next)),
        switchMap((filter) => {
          this.paginator.pageIndex = 0;
          return this.cityService.getAllWithPageable({size: this.pageSize, index: this.pageIndex + 1},
            "name="+filter);
        })
      ).subscribe(cities => this.cities = cities)
  }

  editCity($event: City) {
    const dialogRef = this.dialog.open(CityEditDialogComponent, {data: $event});
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if ($event.photo !== result.photo || $event.name !== result.name) {
          $event = {...$event, ...result}
          let index = this.cities.findIndex(el => el.id === $event.id)
          this.cities[index] = $event
          this.cityService.update($event).subscribe({
            next: () => this.openSnackBar("City updated"),
            error: () => this.openSnackBar("Something go wrong. City was not updated")
          })
        }
      }
    });
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, "close");
  }

  getServerData($event: PageEvent) {
    this.cityService.getAllWithPageable({size: $event.pageSize, index: $event.pageIndex + 1})
      .subscribe(cities => this.cities = cities);
    return $event
  }

  filterValue($event: Event) {
    this.filtering$.next(($event.target as HTMLTextAreaElement).value)
  }
}
