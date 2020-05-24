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
    console.log(f.value);
    //TODO: fix CORS post problem
    // this.employeeService.validateUser(f.value).subscribe((res) => {
    //   if (res == "Incorrect Username or Password") {
    //     //TODO: reset the username/password feilds and display
    //     //TODO: "Incorrect Username or Password"
    //   } else {
    //     this.user = res as Employee;
    //     this.displayChange("forms");
    //   }
    // });
    //TODO: replace this with the functions above when working
    this.employeeService.getEmployee(f.value.username).subscribe((res) => {
      if (res == null) {
        window.alert("incorrect username");
      } else {
        this.user = res;
        this.displayChange("forms");
      }
    });
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
  gradingFormat(format) {
    switch (format) {
      case 1:
        return "Pass/Fail";
        break;
      case 2:
        return ">80%";
        break;
      case 3:
        return ">70%";
        break;
      case 4:
        return "Presentation";
        break;
      case 5:
        return "In Description";
        break;
      default:
        break;
    }
  }
  ngOnInit(): void {}
}

//sample data for HttpMethod tests
// this.user = {
//   ID: 5,
//   firstName: "Brad ",
//   lastName: "Bingham",
//   username: "User8",
//   password: "Password8",
//   availableAmount: 1000,
//   title: "Associate",
//   department: "Sales",
//   officeLoc: "Tucson",
// };
// this.forms = [
//   {
//     ID: 1,
//     empID: 1,
//     status: "In-review",
//     supApr: "false",
//     supSubDate: null,
//     headApr: "false",
//     headSubDate: null,
//     coorApr: "false",
//     coorSubDate: null,
//     isAltered: "false",
//     rejectMessage: null,
//     formSubDate: "May 23 2020",
//     startDate: "June 23 2020",
//     startTime: "10 Am",
//     location: "School",
//     cost: 2000,
//     description: "A class for learning things",
//     justification: "I can learn things",
//     gradeFormatID: 1,
//     eventType: "College",
//     onSubmit: null,
//     finalGrade: null,
//     gradeApr: "false",
//     finalPres: null,
//     presApr: "false",
//   },
//   {
//     ID: 2,
//     empID: 1,
//     status: "In-review",
//     supApr: "false",
//     supSubDate: null,
//     headApr: "false",
//     headSubDate: null,
//     coorApr: "false",
//     coorSubDate: null,
//     isAltered: "false",
//     rejectMessage: null,
//     formSubDate: "May 24 2020",
//     startDate: "June 24 2020",
//     startTime: "11 Am",
//     location: "Learning Annex",
//     cost: 1000,
//     description: "A person will teach me",
//     justification: "I can learn skills",
//     gradeFormatID: 2,
//     eventType: "Prep course",
//     onSubmit: null,
//     finalGrade: null,
//     gradeApr: "false",
//     finalPres: null,
//     presApr: "false",
//   },
// ];
// this.messages = [
//   {
//     id: 1,
//     submittedOn: "May 22, 2020",
//     sendID: 2,
//     recID: 1,
//     formID: 1,
//     message: "This form will work",
//   },
//   {
//     id: 2,
//     submittedOn: "May 21, 2020",
//     sendID: 3,
//     recID: 1,
//     formID: 1,
//     message: "I need for info",
//   },
// ];
