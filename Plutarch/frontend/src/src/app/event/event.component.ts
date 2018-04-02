import {Component, NgZone, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {HistoryEvent} from '../_models/history_event';
import {HistoryEventService} from '../_services/history_event.service';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {

  event: HistoryEvent;
  message: String = '';

  constructor(private hEventService: HistoryEventService, private _ngZone: NgZone,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this._ngZone.run( () => {
      this.route.params.subscribe( params => this.loadEvent(+params['id']));
    });
  }

  loadEvent(eventId) {
    this.hEventService.getEvent(eventId).subscribe(
      (event) => {
        this.event = event;
      });
  }
}
