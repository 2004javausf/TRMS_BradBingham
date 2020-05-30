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
  @Input("setDate") setDate: Date;
  @Output() submitted = new EventEmitter();
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
    this.pendingAmount = num.toFixed(2);
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
    console.log(this.setDate);
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
    if (this.setDate) {
      d1 = this.setDate;
    } else {
      d1 = new Date(
        new Date().getFullYear(),
        new Date().getMonth(),
        new Date().getDate() + 7
      );
    }
    if (d1 >= d2) {
      alert("this cannot be submitted");
    } else {
      if (this.user.title == "Supervisor" || this.user.title == "Head") {
        f.value.supApr = "True";
        f.value.supSubDate = new Date();
      }
      if (this.user.title == "Head") {
        f.value.headApr = "True";
        f.value.headSubDate = new Date();
      }
      f.value.empID = this.user.id;
      f.value.pendingRe = this.pendingAmount;
      f.value.formSubDate = new Date().toISOString();
      f.value.status = "In-review";
      //TODO: test if this works live

      if (d3 >= d2) {
        //TODO:notify submitted as urgent
        f.value.isUrgent = "True";
      } else {
        f.value.isUrgent = "False";
      }
      this.statusService.getNextFormId().subscribe((res) => {
        if (res > 0) {
          console.log("trying to get currval");
          f.value.id = res;
        }
        window.alert("Succesfully submitted");
        console.log(f.value);
        this.submitted.emit(f.value);
        this.rformService.postNewForm(f.value).subscribe();
      });
    }
  }
}
