import {Location} from "./location";
export class Topic {
  id: number;
  word: string;
  ner: string;
  location: Location;

  constructor() {
    this.location = new Location();
  }
}
