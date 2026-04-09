import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationSalle } from './reservation-salle';

describe('ReservationSalle', () => {
  let component: ReservationSalle;
  let fixture: ComponentFixture<ReservationSalle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReservationSalle]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservationSalle);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
