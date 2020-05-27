import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Rform } from "./templates/rform";
import { Observable } from "rxjs";
import { Employee } from "./templates/employee";

@Injectable({
  providedIn: "root",
})
export class RformService {
  private url = "http://localhost:8080/TRMS/rform/";

  constructor(private http: HttpClient) {}
  getForms(): Observable<Rform[]> {
    return this.http.get<Rform[]>(this.url);
  }
  getByUserId(id): Observable<Rform[]> {
    return this.http.get<Rform[]>(this.url + id);
  }
  postNewForm(form: Rform): Observable<Rform> {
    return this.http.post<Rform>(this.url, JSON.stringify(form));
  }
}
