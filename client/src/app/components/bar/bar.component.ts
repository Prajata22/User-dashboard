import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-bar',
  templateUrl: './bar.component.html',
  styleUrls: ['./bar.component.css']
})
export class BarComponent implements OnInit {

  title = "User-Dashboard"

  constructor(private userService: UserService) { }

  ngOnInit(): void {
  }

  checkLogin(){
    return this.userService.checkLogin()
  }

}
