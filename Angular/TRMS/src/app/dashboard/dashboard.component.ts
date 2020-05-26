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
      //TODO: switch when ready to present, tested
      // this.messageService
      //   .getByUserId(this.user.id)
      //   .subscribe((res) => (this.messages = res));
      // this.rformService
      //   .getByUserId(this.user.id)
      //   .subscribe((res) => (this.forms = res));
      // this.loggedIn = true;
      this.forms = sampleForms;
      this.messages = sampleMessages;
    }
    if (toThis == "login") {
      this.messages = null;
      this.forms = null;
      this.loggedIn = false;
      this.display = toThis;
    }
    this.focus = null;
    this.finalType = null;
    this.alreadySubmitted = null;
    this.focusEmployee = null;
    this.display = toThis;
  }
  //login component
  submit(f) {
    //TODO: switch when ready to present, tested
    // this.employeeService.validateUser(f.value).subscribe(
    //   (res) => {
    //     this.user = res;
    //     this.displayChange("forms");
    //   },
    //   (error) => window.alert("incorrect username/password")
    // );
    this.user = sampleEmployee;
    this.displayChange("forms");
  }
  //myforms comoponent
  messages: Message[];
  focus: Rform;
  focusEmployee: Employee;
  forms: Rform[];

  focusOn(item: Rform) {
    console.log(item);
    this.displayChange("focus");
    this.focus = item;
    if (item.finalGrade || item.finalPres) {
      this.alreadySubmitted = true;
    }
    if (this.user.id == this.focus.id) {
      this.focusEmployee = this.user;
    } else {
      //TODO: test employee services and switch
      this.focusEmployee = this.user;
      // this.employeeService
      //   .getEmployee(this.focus.id)
      //   .subscribe((res) => (this.focusEmployee = res));
    }
  }
  //TODO: make back button for forms
  goBack() {}
  //focus form comp
  gradingFormat(format) {
    let swap = {
      1: "Pass/Fail",
      2: ">80%",
      3: ">70%",
      4: "Presentation",
      5: "In Description",
    };
    return swap[format];
  }
  finalType;
  alreadySubmitted;
  finalGradePost(input) {
    console.log(input.value);
  }
  ngOnInit(): void {}
}
let sampleEmployee: Employee = {
  id: 1,
  firstName: "FrstNm",
  lastName: "LstNM",
  username: "User1",
  password: "Pass1",
  availableAmount: 1000,
  title: "Associate",
  department: "Sales",
  officeLoc: "Main",
};
let sampleForms: Rform[] = [
  {
    id: 1,
    empID: 1,
    status: "In-reveiw",
    supApr: "False",
    supSubDate: null,
    headApr: "False",
    headSubDate: null,
    coorApr: "False",
    coorSubDate: null,
    isAltered: "False",
    rejectMessage: null,
    formSubDate: null,
    startDate: "20-MAY-2021",
    startTime: "1 AM",
    location: "Here",
    cost: 300,
    pendingRe: 200,
    description: "description",
    justification: "justifucation",
    gradeFormatID: 2,
    eventType: "College Course",
    onSubmit: null,
    finalGrade: 40,
    gradeApr: "False",
    finalPres: null,
    presApr: "False",
  },
  {
    id: 2,
    empID: 1,
    status: "In-reveiw",
    supApr: "False",
    supSubDate: null,
    headApr: "False",
    headSubDate: null,
    coorApr: "False",
    coorSubDate: null,
    isAltered: "False",
    rejectMessage: null,
    formSubDate: null,
    startDate: "20-MAY-2021",
    startTime: "1 AM",
    location: "Here",
    cost: 400,
    pendingRe: 300,
    description: "description",
    justification: "justifucation",
    gradeFormatID: 3,
    eventType: "College Course",
    onSubmit: null,
    finalGrade: null,
    gradeApr: "False",
    finalPres: null,
    presApr: "False",
  },
];
let sampleMessages: Message[] = [
  {
    id: 1,
    submittedOn: "20-MAY-2021",
    sendID: 2,
    recID: 1,
    formID: 1,
    message: "message one",
  },
  {
    id: 2,
    submittedOn: "20-MAY-2021",
    sendID: 2,
    recID: 1,
    formID: 1,
    message: "message two",
  },
];
