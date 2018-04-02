import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe, HashLocationStrategy, LocationStrategy } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { MatTableModule } from '@angular/material';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { AlertModule } from 'ngx-bootstrap/alert';
import { AgmCoreModule } from '@agm/core';

import { AppComponent } from './app.component';
import { HelloComponent } from './hello/hello.component';
import { AddResourceComponent } from './add-resource/add-resource.component';
import { DefaultComponent } from './default';
import { CuratorComponent } from './curator';

import { AlertComponent } from './_directives/alert.directive';
import { LoginComponent } from './login';
import { RegisterComponent } from './register';
import {RouterModule} from '@angular/router';
import {CookieModule} from 'ngx-cookie';
import { EventComponent } from './event/event.component';
import { UpdateTopicLocationComponent } from './update-topic-location/update-topic-location.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HistoryEventComponent } from './history-event';
import { VALIDITY } from './_models/history_event';
import { UnknownEventComponent } from './unknown-event/unknown-event.component';
import { EventSearchComponent } from './event-search/event-search.component';
import { EventMapComponent } from './event-map/event-map.component';

import {
  HistoryEventService,
  AuthService,
  HealthService,
  AlertService,
  ResourceService,
  TopicService
} from './_services/';

@NgModule({
  declarations: [
    AppComponent,
    HelloComponent,
    AddResourceComponent,
    DefaultComponent,
    CuratorComponent,
    AlertComponent,
    LoginComponent,
    RegisterComponent,
    EventComponent,
    UpdateTopicLocationComponent,
    DashboardComponent,
    HistoryEventComponent,
    UnknownEventComponent,
    EventSearchComponent,
    EventMapComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyAbdx-vKRndUtd_HtGT8QuzDL0TLj4fkIM'
    }),
    RouterModule.forRoot([
      { path: '', component: DefaultComponent },
      { path: 'login', component: LoginComponent },
      { path: 'event-map', component: EventMapComponent },
      { path: 'register', component: RegisterComponent },
      { path: 'curator', component: CuratorComponent,
        children: [
          { path: 'dashboard', component: DashboardComponent },
          { path: 'addResource', component: AddResourceComponent },
          { path: 'updateTopicLocation', component: UpdateTopicLocationComponent },
          { path: 'events/unknown', component: UnknownEventComponent, data: { validity: VALIDITY.Unknown } },
          { path: 'eventSearch', component: EventSearchComponent },
          { path: 'event/:id', component: EventComponent }
        ]
      }
    ]),
    CookieModule.forRoot(),
    MatTableModule,
    BsDropdownModule.forRoot(),
    AlertModule.forRoot()
  ],
  providers: [
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    AuthService,
    HealthService,
    HistoryEventService,
    DatePipe,
    AlertService,
    ResourceService,
    TopicService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
