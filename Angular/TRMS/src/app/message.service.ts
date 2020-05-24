import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Message } from "./templates/message";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class MessageService {
  private url = "http://localhost:8080/TRMS/message/";
  constructor(private http: HttpClient) {}
  getMessages(): Observable<Message[]> {
    return this.http.get<Message[]>(this.url);
  }
  getByUserId(id): Observable<Message[]> {
    return this.http.get<Message[]>(this.url + id);
  }
}
