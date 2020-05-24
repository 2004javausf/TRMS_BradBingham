import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Employee } from "./templates/employee";

@Injectable({
  providedIn: "root",
})
export class EmployeeService {
  private url = "http://localhost:8080/TRMS/employee/";
  constructor(private http: HttpClient) {}
  validateUser(userPass) {
    return this.http.post(this.url, userPass);
  }
  getEmployee(name): Observable<Employee> {
    return this.http.get<Employee>(this.url + name);
  }
}
