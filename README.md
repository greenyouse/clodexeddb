# clodexeddb

This is a wrapper around [ydn-db](http://dev.yathit.com/) to make
clientside storage a little easier to do in ClojureScript. It is a
polyfill that targets IndexedDB, WebSQL, LocalStorage, and UserData (for
IE6) in that order.

WARNING: This is a wip that I mostly did for
[pldb-cache](https://github.com/greenyouse/pldb-cache). I'm not sure how
much time I can commit to this project, so it may not get finished
unless there's demand for it. Any pull requests are welcome!

## Installation

Add this dependency to your project.clj:
```clj
[com.greenyouse/clodexeddb "0.1.0-SNAPSHOT"]
```

## Usage

Most of the functions match 1-to-1 with
[ydn-db](http://dev.yathit.com/). Take a look at the source code for how
to use each one. 

## License

Copyright © 2015 greenyouse

Distributed under the [BSD 2-Clause License](http://www.opensource.org/licenses/BSD-2-Clause).
