import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateTopicLocationComponent } from './update-topic-location.component';

describe('UpdateTopicLocationComponent', () => {
  let component: UpdateTopicLocationComponent;
  let fixture: ComponentFixture<UpdateTopicLocationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UpdateTopicLocationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateTopicLocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
