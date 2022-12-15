(defproject cheshire-cat "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [cheshire "5.11.0"]]
  ;; lein-ring 플러그인은 웹 서버를 시작하는 것과 같은 일반적인 링 작업을 자동화 한다.
  :plugins [[lein-ring "0.12.5"]]
  ;; :ring :handler 키는 웹 서버를 시작할 때 어디서 app 라우트를 찾아야 하는지 알려준다.
  :ring {:handler cheshire-cat.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
