import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Rform } from "./templates/rform";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class RformService {
  private url = "http://localhost:8080/TRMS/rform";
  constructor(private http: HttpClient) {}
  getForms(): Observable<Rform[]> {
    return this.http.get<Rform[]>(this.url);
  }
}