// time-ago.pipe.ts
import { Pipe, PipeTransform } from '@angular/core';
import { formatDistanceToNow, parseISO } from 'date-fns';

@Pipe({
  name: 'timeAgo',
  standalone: true,
})
export class TimeAgoPipe implements PipeTransform {
  transform(value: string | Date | number): string {
    if (!value) return '';

    // Ensure value is treated as a UTC date
    const date = typeof value === 'string' ? parseISO(value) : new Date(value);

    // Format the distance from the date to now
    return formatDistanceToNow(date, { addSuffix: true });
  }
}
