import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: "root",
})
export class SystemService {
  private url = "http://localhost:8080/TRMS/system";
  constructor(private http: HttpClient) {}
  resetAllAvailable() {
    this.http.post(this.url, { req: "ResetAllAvailable" }).subscribe();
  }
}
