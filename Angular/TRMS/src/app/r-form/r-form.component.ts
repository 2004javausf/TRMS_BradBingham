import { Component, OnInit } from "@angular/core";

@Component({
  selector: "r-form",
  templateUrl: "./r-form.component.html",
  styleUrls: ["./r-form.component.css"],
})
export class RFormComponent implements OnInit {
  //TODO: import employee info
  employee = {
    id: 1,
    firstName: "Brad",
    lastName: "Bingham",
    department: "sales",
  };

  //TODO: make this caluculate estimated amount
  total = 1000;

  constructor() {}

  ngOnInit(): void {}
  //TODO: post f.value json object to servlet
  submit(f) {
    //f.value sends json object
    console.log(f);
  }
}
