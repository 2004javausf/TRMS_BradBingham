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
  validateUser(userPass): Observable<Employee> {
    return this.http.post<Employee>(this.url, JSON.stringify(userPass));
  }
  getEmployee(id: number): Observable<Employee> {
    return this.http.get<Employee>(this.url + id);
  }
}
