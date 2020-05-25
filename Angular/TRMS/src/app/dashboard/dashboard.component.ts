import { RformService } from "./../rform.service";
import { EmployeeService } from "./../employee.service";
import { Employee } from "./../templates/employee";
import { Message } from "./../templates/message";
import { Component, OnInit, Input } from "@angular/core";
import { MessageService } from "../message.service";
import { Rform } from "../templates/rform";

@Component({
  selector: "dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.css"],
})
export class DashboardComponent implements OnInit {
  display = "login";
  loggedIn = false;
  user: Employee;
  constructor(
    private messageService: MessageService,
    private employeeService: EmployeeService,
    private rformService: RformService
  ) {}
  displayChange(toThis) {
    if (!this.loggedIn) {
      console.log(this.user);
      this.messageService
        .getByUserId(this.user.id)
        .subscribe((res) => (this.messages = res));
      this.rformService
        .getByUserId(this.user.id)
        .subscribe((res) => (this.forms = res));
      this.loggedIn = true;
    }
    if (toThis == "login") {
      this.messages = null;
      this.forms = null;
      this.loggedIn = false;
      this.display = toThis;
    }
    this.display = toThis;
  }
  //login component
  submit(f) {
    this.employeeService.validateUser(f.value).subscribe(
      (res) => {
        this.user = res;
        this.displayChange("forms");
      },
      (error) => window.alert("incorrect username/password")
    );
  }
  //myforms comoponent
  messages: Message[];
  focus: Rform;
  focusEmployee: Employee;
  forms: Rform[];

  focusOn(item) {
    console.log(item);
    this.displayChange("focus");
    this.focus = item;
  }
  //TODO: make back button for forms
  goBack() {}
  //focus form comp
  gradingFormat(format) {
    let switch2 = {
      1: "Pass/Fail",
      2: ">80%",
      3: ">70%",
      4: "Presentation",
      5: "In Description",
    };
    return switch2[format];
  }
  ngOnInit(): void {}
}
