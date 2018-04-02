export enum VALIDITY {
  Unknown = 'Unknown',
  Valid = 'Valid',
  Invalid = 'Invalid',
  Duplicate = 'Duplicate'
}

export enum DateType {
  Full = 'Full',
  Month = 'Month',
  Year = 'Year',
  Empty = 'Empty'
}

export class Resource {
  id?: number;
  url: string;
  topicName: string;
  topicNER: string;
  processDate: number;
}

export class Topic {
  id?: number;
  word: string;
  ner: string;
  location: Location;
}

export class HistoryDate {
  date: number;
  format: string;
  dateType: DateType;
  formattedDate: string;
}

export class Location {
  id?: number;
  latitude: number;
  longitude: number;
}

export class HistoryEvent {
  id: number;
  phrase: string;
  topics: Array<Topic>;
  hDate: HistoryDate;
  historyEventType: string;
  source: string;
  validity: VALIDITY;
  resource: Resource;
}
