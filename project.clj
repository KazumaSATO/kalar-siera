(defproject kalar-siera "0.1.0-SNAPSHOT"
  :description "A theme of kalar"
  :url "https://github.com/KazumaSATO/kalar-siera"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/mit-license.php"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [hiccup "1.0.5"]
                 [kalar-core "0.1.2"]
                 [kalar-plugins "0.1.2"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler kalar-core.server/app
         :init kalar-core.server/init
         :auto-reload? true})
