import { TestBed } from '@angular/core/testing';

import { RformService } from './rform.service';

describe('RformService', () => {
  let service: RformService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RformService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
