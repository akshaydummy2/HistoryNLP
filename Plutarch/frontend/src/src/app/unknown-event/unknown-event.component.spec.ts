import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UnknownEventComponent } from './unknown-event.component';

describe('UnknownEventComponent', () => {
  let component: UnknownEventComponent;
  let fixture: ComponentFixture<UnknownEventComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UnknownEventComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UnknownEventComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
