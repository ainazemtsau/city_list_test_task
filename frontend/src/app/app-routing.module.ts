import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'cities',
    loadChildren: () => import('./cities/cities.module').then(m => m.CitiesModule),
  },
  { path: '',   redirectTo: '/cities', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
