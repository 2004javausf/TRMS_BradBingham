import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { testForm } from "./test-form/test";
import { of, Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class TestFormService {
  constructor() {}
}
