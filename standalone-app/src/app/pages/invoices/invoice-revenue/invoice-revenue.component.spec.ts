import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvoiceRevenueComponent } from './invoice-revenue.component';

describe('InvoiceRevenueComponent', () => {
  let component: InvoiceRevenueComponent;
  let fixture: ComponentFixture<InvoiceRevenueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InvoiceRevenueComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InvoiceRevenueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
