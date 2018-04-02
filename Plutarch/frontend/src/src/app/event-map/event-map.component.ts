import {Component, OnInit} from '@angular/core';
import {MouseEvent} from '@agm/core';

import {Marker} from '../_models/event_model';
import {HistoryEventService} from '../_services';
import {HistoryEvent} from '../_models/history_event';

const ICON_BASE = '/assets/icons/';
const ICONS = {
  Birth: {
    icon: {
      url: ICON_BASE + 'baby-800.png',
      scaledSize: {
        height: 50,
        width: 50
      }
    }
  },
  Death: {
    icon: {
      url: ICON_BASE + 'Death-512.png',
      scaledSize: {
        height: 50,
        width: 50
      }
    }
  }
};

@Component({
  selector: 'app-event-map',
  templateUrl: './event-map.component.html',
  styleUrls: ['./event-map.component.css']
})
export class EventMapComponent implements OnInit {

  zoom: number;
  lat: number;
  lng: number;
  markers: Marker[] = [];

  constructor(private historyEventService: HistoryEventService) {
    // initial center position for the map
    this.lat = 37.0902;
    this.lng = -95.7129;

    // google maps zoom level
    this.zoom = 4;

    this.loadHistoryEvents();
  }

  ngOnInit() {
  }

  clickedMarker(label: string, index: number) {
    console.log(`clicked the marker: ${label || index}`)
  }

  markerDragEnd(m: Marker, $event: MouseEvent) {
    console.log('dragEnd', m, $event);
  }

  loadHistoryEvents() {
    this.historyEventService.getValidHistoryEvents()
      .subscribe((historyEvents: HistoryEvent[] ) => {
        // iterate the events to add markers
        historyEvents.forEach(function (event) {
          event.topics.forEach(function (topic) {
            if (topic.ner === 'LOCATION') {
              this.markers.push(
                {
                  lat: topic.location.latitude,
                  lng: topic.location.longitude,
                  description: event.phrase,
                  icon: ICONS[event.historyEventType].icon
                });
            }
          }, this);
        }, this);
      });
  }
}
