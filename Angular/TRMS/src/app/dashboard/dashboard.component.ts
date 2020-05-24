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
  display = "forms";
  user: any = {
    firstName: "Brad",
  };

  displayChange(toThis) {
    this.display = toThis;
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
  goBack() {}
  //focus form comp
  constructor(
    private messageService: MessageService,
    private employeeService: EmployeeService,
    private rformService: RformService
  ) {}

  ngOnInit(): void {
    this.messageService.getMessages().subscribe((res) => (this.messages = res));
    // this.employeeService
    //   .getEmployee("Brad")
    //   .subscribe((res) => (this.user = res));
    this.rformService.getForms().subscribe((res) => (this.forms = res));
  }
}
