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
import { IfStmt, ThrowStmt } from "@angular/compiler";

@Component({
  selector: "dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.css"],
})
export class DashboardComponent implements OnInit {
  data = null;
  // login
  display = "login";
  loggedIn = false;
  user: Employee;
  availableReinbursement: number;
  options: number = 0;

  onOptionChange($event) {
    this.options = $event.target.value;
  }
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

  updateDaily() {
    this.updateDailyDate();
    this.updateDailyForm();
  }

  updateDailyDate() {
    let d: Date = new Date();

    if (d.getMonth() == 0) {
      this.systemService.resetAllAvailable();
    }
  }

  updateDailyForm() {
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
      //live logic
      this.messageService.getByUserId(this.user.id).subscribe((res) => {
        res.forEach((x) => this.messages.push(x));
        if (this.messages.length == 0) {
          this.myMessagesMessage = "No messages";
        } else {
          this.messages = this.messages.sort((a, b) => {
            return a.id - b.id;
          });
        }
      });
      this.rformService.getByUserId(this.user.id).subscribe((res) => {
        res.forEach((x) => this.forms.push(x));
        this.calculatedAmount();
        if (this.forms.length == 0) {
          this.myFormsMessage = "No Forms";
        } else {
          this.forms = this.forms.sort((a, b) => {
            return a.id - b.id;
          });
        }
      });
      if (
        this.user.title != "Associate" ||
        this.user.department == "Benefits"
      ) {
        this.manageService.getManageForms(this.user).subscribe((res) => {
          this.manageForms = res.filter((x) => x.empID != this.user.id);
          if (this.manageForms.length > 0) {
            this.autoMessageReviewForms();
          }
        });
      }
      this.loggedIn = true;
    }
    if (toThis == "login") {
      this.messages = [];
      this.forms = [];
      this.loggedIn = false;
      this.display = toThis;
      this.manageForms = null;
    }
    this.focus = null;
    this.focusEmployee = null;
    this.display = toThis;
    this.requestAdditional = false;
    this.declineReason = false;
    this.alterForm = false;
    this.options = 0;
  }

  autoMessageReviewForms() {
    let msObj: Message = {
      id: 0,
      submittedOn: new Date().toISOString(),
      sendID: 0,
      recID: 0,
      formID: 0,
      message: "You have unreviewed forms",
    };
    this.messages.push(msObj);
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
  messages: Message[] = [];
  focus: Rform;
  focusEmployee: Employee;
  forms: Rform[] = [];
  manageForms: Rform[];
  myFormsMessage = "loading...";
  myMessagesMessage = "loading...";
  requestAdditional: boolean = false;
  declineReason: boolean = false;
  alterForm: boolean = false;
  isThinking: boolean = false;

  eventType(input) {
    let swap = {
      1: "University Course",
      2: "Seminar",
      3: "Certification Prep Class",
      4: "Certification",
      5: "Technical Training",
      6: "Other",
    };
    return swap[input];
  }

  //TODO: fix focus on focus employee
  focusOn(item: Rform) {
    this.displayChange("focus");
    this.focus = item;
    if (this.user.id == this.focus.empID) {
      this.focusEmployee = this.user;
    } else {
      this.employeeService
        .getEmployee(this.focus.empID)
        .subscribe((res) => (this.focusEmployee = res));
    }
  }

  remove(item: Message) {
    this.isThinking = true;
    this.messages = this.messages.filter((x) => x.id != item.id);
    this.messageService.deleteMessage(item.id).subscribe((res) => {
      this.isThinking = false;
      if (this.messages.length == 0) {
        this.myMessagesMessage = "No messages";
      }
    });
  }
  goBack() {
    //TODO: maybe make back button for forms
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
    this.messageService.sendMessage(msObj).subscribe((res) => {
      window.alert("Successfully Submitted");
      this.displayChange("manage");
    });
  }

  makeStatusChangeObj(input, additional?) {
    let scObj = {
      rfId: this.focus.id,
      empId: this.focus.empID,
      aprId: this.user.id,
      title: this.user.title,
      newStatus: input,
      reason: null,
    };
    if (additional) {
      scObj.reason = String(additional);
    }
    return scObj;
  }

  submitFormElement(input, additional) {
    let x = this.makeStatusChangeObj(input, additional);
    this.isThinking = true;
    console.log(x);
    this.statusService.updateForm(x).subscribe((res) => {
      window.alert("Successfully Submitted");
      this.isThinking = false;
      switch (input) {
        case "SubmitFinalGrade":
          console.log("in SubmitFinalGrade");
          this.focus.finalGrade = additional;
          this.displayChange("forms");
          break;
        case "SubmitFinalPres":
          console.log("in SubmitFinalPres");
          this.focus.finalPres = additional;
          this.displayChange("forms");
          break;
        case "AlterForm":
          console.log("in AlterForm");
          this.focus.isAltered = "True";
          this.focus.pendingRe = additional;
          this.displayChange("manage");
          break;

        default:
          break;
      }
    });
  }
  changeApprovalStatus(input, additional?) {
    let x = this.makeStatusChangeObj(input, additional);
    this.isThinking = true;
    this.statusService.updateForm(x).subscribe((res) => {
      window.alert("Successfully Submitted");
      this.isThinking = false;
      switch (input) {
        case "ApproveForm":
          console.log("in Approve Form");
          this.manageForms = this.manageForms.filter(
            (x) => x.id != this.focus.id
          );
          this.displayChange("manage");
          break;
        case "DenyForm":
          console.log("in Deny Form");
          this.manageForms = this.manageForms.filter(
            (x) => x.id != this.focus.id
          );
          this.displayChange("manage");
          break;
        case "AcceptBenCoOffer":
          console.log("in AcceptBenCoOffer");
          this.focus.isAltered = "Approved";
          this.displayChange("forms");
          break;
        case "DeclineBenCoOffer":
          console.log("in DeclineBenCoOffer");
          this.focus.isAltered = "Declined";
          this.displayChange("forms");
          break;
        case "ConfirmGrade":
          console.log("in ConfirmGrade");
          this.manageForms = this.manageForms.filter(
            (x) => x.id != this.focus.id
          );
          this.displayChange("manage");
          break;
        case "RejectGrade":
          console.log("in RejectGrade");
          this.manageForms = this.manageForms.filter(
            (x) => x.id != this.focus.id
          );
          this.displayChange("manage");
          break;
        case "ConfirmPres":
          console.log("in ConfirmPres");
          this.manageForms = this.manageForms.filter(
            (x) => x.id != this.focus.id
          );
          this.displayChange("manage");
          break;
        case "RejectPres":
          console.log("in RejectPres");
          this.manageForms = this.manageForms.filter(
            (x) => x.id != this.focus.id
          );
          this.displayChange("manage");
          break;
        default:
          break;
      }
    });
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

  ngOnInit(): void {
    // this.updateDailyDate();
  }
}
