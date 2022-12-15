(ns cheshire-cat.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as ring-json]
            [ring.util.response :as rr]))

;; defroutes는 app-routes라는 HTTP 라우트를 만든다. -> 클라에서 요청하는 URL의 경로를 처리함
(defroutes app-routes
  ;; GET은 HTTP get 라우트를 만든다 -> 여기서는 베이스 url이 요청된 경우 루트 경로를 처리 => "Hello World" 문자열을 출력
  (GET "/" [] "Hello World")
  ;;  ring은 요청과 응답을 모두 map으로 처리한다. -> 그래서 핸들러 함수에서 HTTP 응답을 맵의 형식으로 반환해야 한다.
  ;; 위의 베이스 라우팅 처리시 string을 넣어도 작동하는것은 ring이 알아서 표준 응답으로 바꿔주기 때문이다
  ;; {:status 200 :headers {"ContentType" "text/html; charset=uyf-8"} :body "Hello world"}
  (GET "/cheshire-cat" [] 
    (rr/response {:name "Cheshire Cat" :status "grinning!"}))
  ;; route/not-found는 기본 404에러를 만든다
  (route/not-found "Not Found"))

;; 이 부분에서 우리가 만든 라우트에 다른 기본적인 미들웨어를 추가하기위해, wrap-defaults를 이용한다.
;; wrap-defaults 는 어떤 미들웨어들로 구성할지 결정하는 옵션을 받는다. -> 이 옵션들은 Ring-Defaults 라이브러리에서 찾을 수 있다.
(def app
  (-> app-routes 
      (ring-json/wrap-json-response)
      (wrap-defaults site-defaults)))
;; 위는 app-routes를 받아서 site-default로 래핑하고 다시 그것을 자동화된 json 응답으로 래핑한다.
;; json 래핑은 체셔 라이브러리를 이용해서 응답의 본문에 있는 모든 컬렉션들을 자동으로 json으로 변환한다.

;; ## cheshire 설명

;; json으로 인코딩: generate-string
;; (json/generate-string {:name "c" :state :grinning})

;; json으로 부터 디코딩: parse-string -> 기본적으로 키가 문자열인 맵을 반환함
;; (json/parse-string "{\"name\":\"c\"}") => {"name" "c"}
;; 두번째 인수를 true로 하면 parse-string은 키가 키워드인 맵을 반환한다.
;; (json/parse-string "{\"name\":\"c\"}" true) => {:name "c"}
