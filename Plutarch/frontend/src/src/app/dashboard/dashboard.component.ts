import { Component, OnInit } from '@angular/core';
import {HistoryEventService, ResourceService} from '../_services';
import {TopicService} from '../_services/topic.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  unprocessedResourceCount: number;
  emptyTopicLocationCount: number;
  unknownEventCount: number;

  constructor(private resourceService: ResourceService,
              private historyEventService: HistoryEventService,
              private topicService: TopicService) {
    this.unprocessedResourceCount = 0;
    this.emptyTopicLocationCount = 0;
    this.unknownEventCount = 0;
  }

  ngOnInit() {
    this.updateUnprocessedResourceCount();
    this.updateEmptyTopicLocationCount();
    this.updateUnknownEventCount();
  }

  updateUnprocessedResourceCount(): void {
    this.resourceService.getUnprocessedResourceCount()
      .subscribe((data: any) => {
        // update topic with loaded topic
        this.unprocessedResourceCount = data;
      });
  }

  updateEmptyTopicLocationCount(): void {
    this.topicService.getEmptyTopicLocationCount()
      .subscribe((data: any) => {
        // update topic with loaded topic
        this.emptyTopicLocationCount = data;
      });
  }

  updateUnknownEventCount(): void {
    this.historyEventService.getUnknownEventCount()
      .subscribe((data: any) => {
        // update topic with loaded topic
        this.unknownEventCount = data;
      });
  }
}
