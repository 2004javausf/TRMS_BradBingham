import { ManageService } from "./../manage.service";
import { RformService } from "./../rform.service";
import { EmployeeService } from "./../employee.service";
import { Employee } from "./../templates/employee";
import { Message } from "./../templates/message";
import { Component, OnInit, Input } from "@angular/core";
import { MessageService } from "../message.service";
import { Rform } from "../templates/rform";
import { StatusService } from "../status.service";

@Component({
  selector: "dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.css"],
})
export class DashboardComponent implements OnInit {
  data = "sample";
  display = "login";
  loggedIn = false;
  user: Employee;
  availableReinbursement: number;

  calculatedAmount() {
    let pendingAmount = 0;
    this.forms
      .filter((x) => x.status == "Pending")
      .forEach((x) => (pendingAmount += x.pendingRe));
    this.availableReinbursement = this.user.availableAmount - pendingAmount;
  }

  constructor(
    private messageService: MessageService,
    private employeeService: EmployeeService,
    private rformService: RformService,
    private statusService: StatusService,
    private manageService: ManageService
  ) {}
  displayChange(toThis) {
    if (!this.loggedIn) {
      //TODO: remove this top if clause when ready to go live, tested
      if (this.data == "sample") {
        this.forms = sampleForms;
        this.messages = sampleMessages;
        this.calculatedAmount();
        if (this.user.title != "Associate") {
          this.manageService
            .getManageForms(this.user)
            .subscribe((res) => (this.manageForms = res));
        }
        this.loggedIn = true;
      } else {
        this.messageService
          .getByUserId(this.user.id)
          .subscribe((res) => (this.messages = res));
        this.rformService.getByUserId(this.user.id).subscribe((res) => {
          this.forms = res;
          this.calculatedAmount();
        });
        if (this.user.title != "Associate") {
          this.manageService
            .getManageForms(this.user)
            .subscribe((res) => (this.manageForms = res));
        }
        this.loggedIn = true;
      }
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
    console.log(f.value);
    //TODO: switch when ready to present, tested
    if (this.data == "sample") {
      this.user = sampleEmployee;
      this.displayChange("forms");
    } else {
      this.employeeService.validateUser(f.value).subscribe(
        (res) => {
          this.user = res;
          this.displayChange("forms");
        },
        (error) => window.alert("incorrect username/password")
      );
    }
  }
  //myforms comoponent
  messages: Message[];
  focus: Rform;
  focusEmployee: Employee;
  forms: Rform[];
  manageForms: Rform[];
  requestAdditional: boolean = false;
  declineReason: boolean = false;

  //TODO: fix focus on focus employee
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
      if (this.data == "sample") {
        this.focusEmployee = this.user;
      } else {
        this.employeeService
          .getEmployee(this.focus.id)
          .subscribe((res) => (this.focusEmployee = res));
      }
    }
  }
  goBack() {
    //TODO: make back button for forms
  }
  //focus form comp
  sendMessage(input) {
    let msObj = {
      id: null,
      submittedOn: null,
      sendID: this.user.id,
      recID: this.focus.empID,
      formID: this.focus.id,
      message: input.value.requestMessage,
    };
    this.messageService.sendMessage(msObj).subscribe((res) => console.log(res));
  }
  onReviewChanges(input, reason?) {
    let scObj = {
      rfId: this.focus.id,
      empId: this.focus.empID,
      aprId: this.user.id,
      title: this.user.title,
      newStatus: input,
      reason: reason.value.declineReason,
    };
    console.log(scObj);
    if (input == "Canceled") {
      this.statusService.updateForm(scObj).subscribe((res) => {});
      this.focus.status = "Canceled";
      this.focus.isAltered = "Canceled";
    } else {
      this.statusService.updateForm(scObj).subscribe((res) => {});
      this.focus.isAltered = input;
    }
  }
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
  id: 3,
  firstName: "FrstNm",
  lastName: "LstNM",
  username: "User1",
  password: "Pass1",
  availableAmount: 1000,
  title: "Supervisor",
  department: "Sales",
  officeLoc: "Main",
};
let sampleForms: Rform[] = [
  {
    id: 1,
    empID: 1,
    status: "Pending",
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
    finalGrade: null,
    gradeApr: "False",
    finalPres: "www.someplace.fjio",
    presApr: "False",
  },
  {
    id: 6,
    empID: 3,
    status: "Pending",
    supApr: "False",
    supSubDate: null,
    headApr: "False",
    headSubDate: null,
    coorApr: "False",
    coorSubDate: null,
    isAltered: "True",
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
let sampleChangeStatus = {
  rfId: 5,
  empId: 3,
  aprId: 3,
  title: "Associate",
  newStatus: "Canceled",
  reason: "here is my reason",
};
// {	"rfId":5,
// 	"empId":3,
// 	"aprId":3,
// 	"title":"Associate",
// 	"newStatus":"Accept",
// 	"reason": "here is my reason"}
