import { EmployeeService } from "./../employee.service";
import { Component, OnInit } from "@angular/core";
import { Employee } from "../templates/employee";

@Component({
  selector: "login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.css"],
})
export class LoginComponent implements OnInit {
  constructor(private employeeServices: EmployeeService) {}

  ngOnInit(): void {}

  submit(f) {
    console.log(f.value);
    this.employeeServices
      .validateUser(f.value)
      .subscribe((res) => console.log(res));
  }
}
