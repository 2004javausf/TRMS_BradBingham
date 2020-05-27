import { RformService } from "./../rform.service";
import { Component, OnInit, Input } from "@angular/core";
import { Employee } from "../templates/employee";

@Component({
  selector: "r-form",
  templateUrl: "./r-form.component.html",
  styleUrls: ["./r-form.component.css"],
})
export class RFormComponent implements OnInit {
  @Input("user") user: Employee;
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
    let num = this.cost / percent[this.eventType];
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

  constructor(private rformService: RformService) {}

  ngOnInit(): void {
    this.pendingAmount = 0;
    this.available = this.user.availableAmount;
  }
  submit(f) {
    //f.value sends json object
    f.value.empID = this.user.id;
    f.value.pendingRe = this.pendingAmount;
    console.log(f.value);
    this.rformService.postNewForm(f.value).subscribe((res) => console.log(res));
  }
}
