# streaming-mysql-results

Since version 1.8, Spring data project includes an interesting feature - through a simple API call developer can request database query results to be returned as Java 8 stream. This technique can be particularly useful when processing large datasets (for example, exporting larger amounts of database data in a specific format).

<p align="center">
  <img src="https://github.com/uuhnaut69/streaming-mysql-results/blob/master/Screen%20Shot%202019-10-15%20at%205.36.52%20PM.png"/>
</p>

when using MySQL in order to really stream the results we need to satisfy three conditions:

- Forward-only resultset
- Read-only statement
- Fetch-size set to Integer.MIN_VALUE
# Summary
- We've seen significant performance improvements when using streaming (via scrollable resultsets) vs paging, admittedly in a very specific task of exporting data.

- Spring Data's new features give really convenient access to scrollable resultsets via streams.

- There are gotchas to get it working with MySQL, but they are manageable.
There are further restrictions when reading scrollable result sets in MySQL - no statement may be issued through the same database connection until the resultset is fully read.

- The export works fine because we are writing results directly to HttpServletResponse. If we were using default Spring's message converters (e.g. returning stream from the controller method ) then there's a good chance this would not work as expected.
