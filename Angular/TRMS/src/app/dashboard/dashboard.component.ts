import { Component, OnInit } from "@angular/core";

@Component({
  selector: "dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.css"],
})
export class DashboardComponent implements OnInit {
  user = {
    firstName: "Brad",
    available: 1000,
  };
  list = [
    { id: 1, status: "in-review" },
    { id: 2, status: "in-review" },
    { id: 3, status: "Pending" },
    { id: 4, status: "Accepted" },
  ];
  messages = [
    { id: 1, sender: "Bob", message: "I need you to change this" },
    { id: 2, sender: "Jessica", message: "Please provide more" },
    { id: 3, sender: "Franklin", message: "id 1 approved" },
  ];
  constructor() {}

  ngOnInit(): void {}
}
