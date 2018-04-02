import { Component, OnInit } from '@angular/core';
import {HistoryEventService} from '../_services';
import {HistoryEvent} from '../_models/history_event';

@Component({
  selector: 'app-event-search',
  templateUrl: './event-search.component.html',
  styleUrls: ['./event-search.component.css'],
  providers: [HistoryEventService]
})
export class EventSearchComponent implements OnInit {

  searchCriteria: string = '';
  searchResults: HistoryEvent[];

  constructor(private historyEventService: HistoryEventService) { }

  ngOnInit() {
  }

  searchEvents(): void {
    this.historyEventService.search(this.searchCriteria)
      .subscribe((data: any) => {
        // show popup of the data returned
        this.searchResults = data;
      });
  }

  viewEvent(event: HistoryEvent) : void {

  }
}
