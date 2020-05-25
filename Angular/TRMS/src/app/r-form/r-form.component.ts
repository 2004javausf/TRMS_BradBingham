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
  total;

  constructor(private rformService: RformService) {}

  ngOnInit(): void {
    this.total = this.user.availableAmount;
  }
  submit(f) {
    //f.value sends json object
    f.value.empID = this.user.id;
    console.log(f.value);
    this.rformService.postNewForm(f.value).subscribe((res) => console.log(res));
  }
}
