import { HttpClient, HttpClientModule } from "@angular/common/http";
import { RouterModule } from "@angular/router";
import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { RFormComponent } from "./r-form/r-form.component";
import { LoginComponent } from "./login/login.component";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { FormDetailsComponent } from "./form-details/form-details.component";

@NgModule({
  declarations: [
    AppComponent,
    RFormComponent,
    LoginComponent,
    DashboardComponent,
    FormDetailsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot([]),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
