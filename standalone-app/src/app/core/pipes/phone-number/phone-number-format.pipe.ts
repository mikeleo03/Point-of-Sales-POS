import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'phoneNumberFormat',
  standalone: true
})
export class PhoneNumberFormatPipe implements PipeTransform {

  transform(value: string): string {
    const countryCode = value.slice(0, 3);
    const mainNumber = value.slice(3);
    
    return `(${countryCode}) ${mainNumber}`;
  }

}
