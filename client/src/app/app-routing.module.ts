import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LoginComponent } from './components/login/login.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { LoginGuard } from './guards/login.guard';

const routes: Routes = [
  {path:'login', component: LoginComponent},
  {path:'register', component: RegistrationComponent, canActivate:[LoginGuard]},
  {path:'userList', component:UserListComponent, canActivate:[LoginGuard]},
  {path:'dashboard', component:DashboardComponent, canActivate:[LoginGuard]},
  {path:'', redirectTo:'/login', pathMatch:'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
