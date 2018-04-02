import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';

import {Resource} from '../_models/resource';
import {ResourceService} from '../_services/resource.service';

@Component({
  selector: 'app-add-resource',
  templateUrl: './add-resource.component.html',
  styleUrls: ['./add-resource.component.css'],
  providers: [ResourceService]
})
export class AddResourceComponent implements OnInit {

  resource: Resource = {
    url: 'https://en.wikipedia.org/wiki/Martin_Luther_King_Jr.',
    topicName: 'Martin Luther King Jr.',
    topicNER: 'PERSON'
  };
  resourceMessage: String = '';
  alerts: any[] = [];

  constructor(private resourceService: ResourceService) {
  }

  ngOnInit() {
  }

  submitForm(myForm: NgForm): void {
    this.resourceService.addResource(myForm.value)
      .subscribe((data: any) => {
        // show popup of the data returned
        this.resourceMessage = data;

        // Clear fields if successful
        this.resource.url = '';
        this.resource.topicName = '';

        this.alerts.push({
          type: 'success',
          msg: 'Success!! Resource Added...',
          timeout: 5000
        });

      }, (error) => {
        this.alerts.push({
          type: 'danger',
          msg: error.error.text,
          timeout: 5000
        });
      });
  }
}
