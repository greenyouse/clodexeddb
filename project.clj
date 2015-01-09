(defproject com.greenyouse/clodexeddb "0.1.0"
  :description "A ClojureScript wrapper for clientside storage"
  :url "https://github.com/greenyouse/clodexeddb"
  :license {:name "BSD 2-Clause"
            :url "http://www.opensource.org/licenses/BSD-2-Clause"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2657"]
                 [com.ydn/db "1.1.2"]]

  :plugins [[lein-cljsbuild "1.0.4"]]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src" "dev"]
                        :compiler {:output-to "clodexeddb.js"
                                   :optimizations :simple
                                   :source-map "clodexeddb.js.map"
                                   :preamble ["db/ydn-db.js"]}}]})
