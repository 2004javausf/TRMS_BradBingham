import { SystemService } from "./../system.service";
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
  setDate = null;

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
    private manageService: ManageService,
    private systemService: SystemService
  ) {}
  updateDailyDate() {
    let d: Date;
    if (this.setDate) {
      d = this.setDate;
    } else {
      d = new Date();
    }
    if (d.getMonth() == 0) {
      this.systemService.resetAllAvailable();
    }
    console.log(d);
  }
  updateIncomingForm() {
    this.forms.forEach((x) => {
      let sup = x.formSubDate;
      let head = x.supSubDate;
      let coor = x.headSubDate;
      let today = new Date();

      let testDate = new Date(
        +sup.substr(0, 4),
        +sup.substr(5, 2) - 1,
        +sup.substr(8, 2) + 3
      );
      if (today >= testDate && x.supApr != "True") {
        console.log("auto approve Supervisor for " + x.id);
        let scObj = {
          rfId: x.id,
          empId: x.empID,
          aprId: 0,
          title: "Supervisor",
          newStatus: "True",
          reason: null,
        };
        this.statusService.updateForm(scObj).subscribe();
        return;
      }

      if (head != null) {
        let testDate = new Date(
          +head.substr(0, 4),
          +head.substr(5, 2) - 1,
          +head.substr(8, 2) + 3
        );
        if (today >= testDate && x.headApr != "True") {
          console.log("auto approve Head for " + x.id);
          let scObj = {
            rfId: x.id,
            empId: x.empID,
            aprId: 0,
            title: "Head",
            newStatus: "True",
            reason: null,
          };
          this.statusService.updateForm(scObj).subscribe();
          return;
        }
      }

      if (coor != null) {
        let testDate = new Date(
          +head.substr(0, 4),
          +head.substr(5, 2) - 1,
          +head.substr(8, 2) + 3
        );
        if (today >= testDate && x.coorApr != "True") {
          console.log("BenCo needs to hurry up with " + x.id);
          return;
        }
      }
    });
  }
  onSubmitted(test) {
    this.forms.push(test);
    this.displayChange("forms");
  }
  displayChange(toThis) {
    if (!this.loggedIn) {
      //TODO: remove this top if clause when ready to go live, tested
      if (this.data == "sample") {
        this.forms = sampleForms;
        this.messages = sampleMessages;
        this.calculatedAmount();
        if (
          this.user.title != "Associate" ||
          this.user.department == "Benefits"
        ) {
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
          this.updateIncomingForm();
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
  alterForm: boolean = false;

  //TODO: fix focus on focus employee
  focusOn(item: Rform) {
    console.log(item);
    this.displayChange("focus");
    this.focus = item;
    if (item.finalGrade || item.finalPres) {
      this.alreadySubmitted = true;
    }
    if (this.user.id == this.focus.empID) {
      this.focusEmployee = this.user;
    } else {
      if (this.data == "sample") {
        this.focusEmployee = this.user;
      } else {
        this.employeeService
          .getEmployee(this.focus.empID)
          .subscribe((res) => (this.focusEmployee = res));
      }
    }
  }
  remove(item: Rform) {
    console.log("remove " + item.id);
    this.messages = this.messages.filter((x) => x.id != item.id);
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
  onReviewChanges(input, additional?) {
    let scObj = {
      rfId: this.focus.id,
      empId: this.focus.empID,
      aprId: this.user.id,
      title: this.user.title,
      newStatus: input,
      reason: null,
    };
    if (additional) {
      scObj.reason = additional;
    }
    console.log(scObj);
    if (input == "Canceled") {
      this.statusService.updateForm(scObj).subscribe((res) => {});
      this.focus.status = "Canceled";
      this.focus.isAltered = "Canceled";
    } else if (input == "Confirm" || input == "Deny") {
      this.statusService.updateForm(scObj).subscribe((res) => {});
      this.focus.status = input;
      if (scObj.reason == "Grade") {
        this.focus.gradeApr = input;
      } else {
        this.focus.presApr = input;
      }
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
  ngOnInit(): void {
    this.updateDailyDate();
  }
}
let sampleEmployee: Employee = {
  id: 3,
  firstName: "FrstNm",
  lastName: "LstNM",
  username: "User1",
  password: "Pass1",
  availableAmount: 1000,
  title: "Supervisor",
  department: "IT",
  officeLoc: "Main",
};
let sampleForms: Rform[] = [
  {
    id: 1,
    empID: 3,
    status: "Pending",
    isUrgent: null,
    supApr: "True",
    supSubDate: null,
    headApr: "True",
    headSubDate: null,
    coorApr: "True",
    coorSubDate: null,
    isAltered: "False",
    rejectMessage: null,
    formSubDate: "2021-05-21",
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
    finalPres: "wwfi.fijdf.efi",
    presApr: "False",
  },
  {
    id: 6,
    empID: 3,
    status: "Pending",
    isUrgent: "True",
    supApr: "False",
    supSubDate: null,
    headApr: "False",
    headSubDate: null,
    coorApr: "False",
    coorSubDate: null,
    isAltered: "True",
    rejectMessage: null,
    formSubDate: "2021-05-21",
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
    submittedOn: "2021-05-21",
    sendID: 2,
    recID: 1,
    formID: 1,
    message: "message one",
  },
  {
    id: 2,
    submittedOn: "2021-05-21",
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
