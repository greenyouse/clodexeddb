(defproject com.greenyouse/clodexeddb "0.1.0"
  :description "A ClojureScript wrapper for clientside storage"
  :url "https://github.com/greenyouse/clodexeddb"
  :license {:name "BSD 2-Clause"
            :url "http://www.opensource.org/licenses/BSD-2-Clause"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2850"]
                 ;[weasel "0.6.0-SNAPSHOT"]
                 [com.greenyouse/ydn-cljs "1.1.2"]]

  :plugins [[lein-cljsbuild "1.0.4"]]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src" "dev"]
                        :compiler {:main clodexeddb.core
                                   :output-to "clodexeddb.js"
                                   :output-dir "out"
                                   :optimizations :none
                                   :source-map true}}]})
