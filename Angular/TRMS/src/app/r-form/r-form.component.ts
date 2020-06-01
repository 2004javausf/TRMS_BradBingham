import { RformService } from "./../rform.service";
import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { Employee } from "../templates/employee";
import { StatusService } from "../status.service";

@Component({
  selector: "r-form",
  templateUrl: "./r-form.component.html",
  styleUrls: ["./r-form.component.css"],
})
export class RFormComponent implements OnInit {
  @Input("user") user: Employee;
  @Output() submitted = new EventEmitter();
  @Input() availableReinbursement: number;
  //TODO: make this caluculate estimated amount
  available;
  pendingAmount;
  cost;
  eventType;
  caluculatePending() {
    let percent = {
      1: 0.8,
      2: 0.6,
      3: 0.75,
      4: 1,
      5: 0.9,
      6: 0.3,
    };
    let num = this.cost * percent[this.eventType];
    if (num > this.availableReinbursement) {
      this.pendingAmount = this.availableReinbursement;
    } else {
      this.pendingAmount = num.toFixed(2);
    }
  }
  setCost(input) {
    this.cost = input;
    if (this.eventType) {
      this.caluculatePending();
    }
  }
  setFormat(input) {
    this.eventType = input;
    if (this.cost) {
      this.caluculatePending();
    }
  }

  constructor(
    private rformService: RformService,
    private statusService: StatusService
  ) {}

  ngOnInit(): void {
    this.pendingAmount = 0;
    this.available = this.user.availableAmount;
  }
  submit(f) {
    let d1: Date;
    let x: string = f.value.startDate;
    let d2: Date = new Date(
      +x.substr(0, 4),
      +x.substr(5, 2) - 1,
      +x.substr(8, 2)
    );
    let d3: Date = new Date(
      new Date().getFullYear(),
      new Date().getMonth(),
      new Date().getDate() + 14
    );

    d1 = new Date(
      new Date().getFullYear(),
      new Date().getMonth(),
      new Date().getDate() + 7
    );

    if (d1 >= d2) {
      alert("Start date must be further than 1 week from today");
    } else {
      if (this.user.title == "Supervisor" || this.user.title == "Head") {
        f.value.supApr = "Approved";
        f.value.supSubDate = new Date();
      }
      if (this.user.title == "Head") {
        f.value.headApr = "Approved";
        f.value.headSubDate = new Date();
      }
      f.value.empID = this.user.id;
      f.value.pendingRe = this.pendingAmount;
      f.value.formSubDate = new Date().toISOString();
      f.value.status = "In-review";
      if (d3 >= d2) {
        f.value.isUrgent = "True";
      } else {
        f.value.isUrgent = "False";
      }
      this.statusService.getNextFormId().subscribe((res) => {
        if (res > 0) {
          f.value.id = +res + 1;
        } else {
          f.value.id = 1;
        }
        if (f.value.isUrgent == "True") {
          window.alert("Sucessfully submitted as urgent");
        } else {
          window.alert("Succesfully submitted");
        }
        console.log(f.value);
        this.submitted.emit(f.value);
        this.rformService.postNewForm(f.value).subscribe();
      });
    }
  }
}
