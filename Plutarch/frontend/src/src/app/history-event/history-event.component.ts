import {Component, EventEmitter, Input, Output} from '@angular/core';
import {VALIDITY, HistoryEvent} from '../_models/history_event';
import {HistoryEventService} from '../_services/history_event.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-history-event',
  templateUrl: './history-event.component.html',
  styleUrls: ['./history-event.component.css']
})
export class HistoryEventComponent {

  @Input('type') type: string;
  @Input('historyEvent') historyEvent: HistoryEvent;
  @Output() updated: EventEmitter<any> = new EventEmitter();
  VALIDITY;
  alerts: any[] = [];

  constructor(private hEventService: HistoryEventService,
              private route: ActivatedRoute) {
    this.VALIDITY = VALIDITY;
  }

  updateEvent(): void {
    this.hEventService.updateValidity(this.historyEvent)
      .subscribe((data: any) => {
        this.alerts.push({
          type: 'success',
          msg: data,
          timeout: 5000
        });
        this.updated.emit();
      }, (error) => {
        this.alerts.push({
          type: 'danger',
          msg: 'Error while updating event validity',
          timeout: 5000
        });
      });
  }
}
