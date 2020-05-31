import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { StatusChange } from "./templates/statusChange";

@Injectable({
  providedIn: "root",
})
export class StatusService {
  private url = "http://localhost:8080/TRMS/status";
  constructor(private http: HttpClient) {}

  updateForm(sc: StatusChange): Observable<any> {
    return this.http.post<StatusChange>(this.url, JSON.stringify(sc));
  }

  getNextFormId(): Observable<any> {
    return this.http.get(this.url);
  }
}
