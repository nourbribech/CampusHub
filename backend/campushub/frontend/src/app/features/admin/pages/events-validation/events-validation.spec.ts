import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventsValidation } from './events-validation';

describe('EventsValidation', () => {
  let component: EventsValidation;
  let fixture: ComponentFixture<EventsValidation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventsValidation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventsValidation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
