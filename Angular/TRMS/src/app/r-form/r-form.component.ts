import { Component, OnInit } from "@angular/core";

@Component({
  selector: "r-form",
  templateUrl: "./r-form.component.html",
  styleUrls: ["./r-form.component.css"],
})
export class RFormComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
  submit(f) {
    //f.value sends json object
    console.log(f.value);
  }
}
