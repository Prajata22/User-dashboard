import { Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';
import { checkEmailValidator } from 'src/app/utils/checkEmail.directive';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user ?: User
  heroForm!:FormGroup
  isLoggedIn?: Boolean

  constructor(
    private userService: UserService,
    private router: Router,
    private renderer: Renderer2,
    private elementRef: ElementRef
  ) { }

  ngOnInit(): void {
    this.isLoggedIn = false,
    this.heroForm = new FormGroup({
      email: new FormControl(this.user?.email, [Validators.required,Validators.email]),
      password: new FormControl(this.user?.password, [Validators.required, Validators.minLength(8)])
    })
  }

  goDashboard(): void{
    this.router.navigate(['/dashboard'])
  }

  retryLogin(): void{
    const div = this.renderer.createElement('div')
    const text = this.renderer.createText("Invalid Email or Password.")
    const text2 = this.renderer.createText(" Please Try Again.")

    const loginErrorMsg = this.elementRef.nativeElement.querySelector("#loginErrorMsg")
    console.log(loginErrorMsg)
    if(this.elementRef.nativeElement.querySelector("#loginErrorBox") !== null) return

    this.renderer.addClass(div, "alert")
    this.renderer.addClass(div, "alert-danger")
    this.renderer.setAttribute(div, "id", "loginErrorBox")
    this.renderer.appendChild(div, text)
    this.renderer.appendChild(div, text2)
    this.renderer.appendChild(loginErrorMsg, div)
  }

  login(email: string, password: string, check: string): void {
    const checkBool = (check==null) ? false : true
    email=email.trim()
    password=password.trim()
    this.userService.loginUser({email, password} as User)
      .subscribe(res => {
        if(res === undefined) {
          this.userService.logout()
          this.retryLogin()
        }
        else{
          this.user = res
          localStorage.setItem('token', res.token)
          this.userService.login()
          this.goDashboard()
        }
      })
  }

  get email() {return this.heroForm.get('email')}

  get password() {return this.heroForm.get('Pass')}
}
