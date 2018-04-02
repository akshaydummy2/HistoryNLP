import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {TopicService} from '../_services/topic.service';
import {Topic} from '../_models/topic';

@Component({
  selector: 'app-update-topic-location',
  templateUrl: './update-topic-location.component.html',
  styleUrls: ['./update-topic-location.component.css'],
  providers: [TopicService]
})
export class UpdateTopicLocationComponent implements OnInit {

  topic: Topic = new Topic();
  response: String = '';
  alerts: any[] = [];

  constructor(private topicService: TopicService) {
  }

  ngOnInit() {
    this.getNextEmptyTopicLocation();
  }

  getNextEmptyTopicLocation(): void {
    this.topicService.getNextEmptyTopicLocation()
      .subscribe((data: any) => {
        // update topic with loaded topic
        this.topic = data;
      });
  }

  submitForm(myForm: NgForm): void {
    this.topicService.updateLocation(this.topic)
      .subscribe((data: any) => {
        // show popup of the data returned
        this.response = data;

        this.alerts.push({
          type: 'success',
          msg: 'Success!! Topic Location Updated...',
          timeout: 5000
        });

        this.getNextEmptyTopicLocation();
      }, (error) => {
        // TODO : As the return type was string success call coming to error part because of JSON parsing error. Need to handle it
        if (error.status === 200) {
          this.alerts.push({
            type: 'success',
            msg: 'Success!! Topic Location Updated...',
            timeout: 5000
          });
        } else {
          this.alerts.push({
            type: 'danger',
            msg: error.error.text,
            timeout: 5000
          });
        }
      });
  }
}
