import {Component, NgZone, OnInit} from '@angular/core';

import {HistoryEvent} from '../_models/history_event';
import {HistoryEventService} from '../_services/history_event.service';

@Component({
  selector: 'app-unknown-event',
  templateUrl: './unknown-event.component.html',
  styleUrls: ['./unknown-event.component.css'],
  providers: [HistoryEventService]
})
export class UnknownEventComponent implements OnInit {

  event: HistoryEvent;

  constructor(private hEventService: HistoryEventService, private _ngZone: NgZone) {
  }

  ngOnInit() {
    this._ngZone.run(() => {
      this.loadUnknownEvent();
    });
  }

  loadUnknownEvent() {
    this.hEventService.getUnknownEvent().subscribe(
      (event) => {
        this.event = event;
      });
  }
}
