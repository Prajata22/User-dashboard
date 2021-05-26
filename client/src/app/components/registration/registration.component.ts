import { Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  constructor(
    private userService: UserService,
    private renderer: Renderer2,
    private elementRef: ElementRef
  ) { }

  ngOnInit(): void {
  }

  retryRegistration(): void{
    const div = this.renderer.createElement('div')
    const text = this.renderer.createText("Invalid Email or Password.")
    const text2 = this.renderer.createText(" Please Try Again.")

    const loginErrorMsg = this.elementRef.nativeElement.querySelector("#regErrorMsg")
    console.log(loginErrorMsg)
    if(this.elementRef.nativeElement.querySelector("#regErrorBox") !== null) return

    this.renderer.addClass(div, "alert")
    this.renderer.addClass(div, "alert-danger")
    this.renderer.setAttribute(div, "id", "regErrorBox")
    this.renderer.appendChild(div, text)
    this.renderer.appendChild(div, text2)
    this.renderer.appendChild(loginErrorMsg, div)
  }

  register(name: string, email: string, password: string, phone: string, address: string, check: string): void{
    console.log(check)
    name=name.trim()
    email=email.trim()
    password=password.trim()
    phone=phone.trim()
    const contact: number = +phone
    address=address.trim()
    this.userService.registerUser({name,email,password,address,contact} as User)
      .subscribe(user => {
        if(user === undefined) this.retryRegistration()
      })
  }
}
