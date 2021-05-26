import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators'
import { User } from '../models/user';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  private loggedIn = new BehaviorSubject<boolean>(false); // {1}

  get isLoggedIn() {
    return this.loggedIn.asObservable(); // {2}
  }

  private baseUrl = "http://localhost:8080/api"
  private httpOptions = {
    headers: new HttpHeaders({"Content-Type":"application/json"})
  }

  constructor(
    private http: HttpClient,
  ) { }

  private HandleError<T>(operation = "operation", result?: T){
    return (error: any) : Observable<T> => {
      console.log(operation)
      console.log(error)
      return of(result as T)
    }
  }

  loginUser(user: User): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, user, this.httpOptions).pipe(
      catchError(this.HandleError<any>('loginUser'))
    )
  }

  registerUser(user: User): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/user`, user, this.httpOptions).pipe(
      catchError(this.HandleError<User>('registerUser'))
    )
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/user/all`).pipe(
      catchError(this.HandleError<User[]>('getUsers', []))
    )
  }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/user/${id}`).pipe(
      catchError(this.HandleError<User>('getUser'))
    )
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/delete/${id}`, this.httpOptions).pipe(
      catchError(this.HandleError<any>('deleteUser'))
    )
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/user`, user, this.httpOptions).pipe(
      catchError(this.HandleError<User>('updateUser'))
    )
  }


  login(){
    this.loggedIn.next(true)
  }

  logout(){
    this.loggedIn.next(false)
  }

  checkLogin(): Observable<boolean> {
    return this.isLoggedIn.pipe(
      take(1),
      map((isLoggedIn: boolean) => {
        if (!isLoggedIn){
          return false;
        }
        return true;
      })
    )
  }
}
