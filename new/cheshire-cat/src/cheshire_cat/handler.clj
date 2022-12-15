(ns cheshire-cat.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

;; defroutes는 app-routes라는 HTTP 라우트를 만든다. -> 클라에서 요청하는 URL의 경로를 처리함
(defroutes app-routes 
  ;; GET은 HTTP get 라우트를 만든다 -> 여기서는 베이스 url이 요청된 경우 루트 경로를 처리 => "Hello World" 문자열을 출력
  (GET "/" [] "Hello World")
  (GET "/cheshire-cat" [] "Smile")
  ;; route/not-found는 기본 404에러를 만든다
  (route/not-found "Not Found"))

;; 이 부분에서 우리가 만든 라우트에 다른 기본적인 미들웨어를 추가하기위해, wrap-defaults를 이용한다.
;; wrap-defaults 는 어떤 미들웨어들로 구성할지 결정하는 옵션을 받는다. -> 이 옵션들은 Ring-Defaults 라이브러리에서 찾을 수 있다.
(def app
  (wrap-defaults app-routes site-defaults))
