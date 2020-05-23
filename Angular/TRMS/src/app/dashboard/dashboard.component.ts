import { Message } from "./../templates/message";
import { Component, OnInit } from "@angular/core";
import { MessageService } from "../message.service";

@Component({
  selector: "dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.css"],
})
export class DashboardComponent implements OnInit {
  private url = "http://localhost:8080/TRMS/message";
  messages: Message[];

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

  constructor(private messageService: MessageService) {}

  ngOnInit(): void {
    this.messageService.getMessages().subscribe((res) => (this.messages = res));
  }
}
