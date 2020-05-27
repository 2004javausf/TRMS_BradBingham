import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Employee } from "./templates/employee";
import { Rform } from "./templates/rform";

@Injectable({
  providedIn: "root",
})
export class ManageService {
  private url = "http://localhost:8080/TRMS/manage";
  constructor(private http: HttpClient) {}
  getManageForms(em: Employee): Observable<Rform[]> {
    return this.http.post<Rform[]>(this.url, JSON.stringify(em));
  }
}
