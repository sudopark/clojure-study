(ns serpent-talk.talk
  (:require [camel-snake-kebab.core :as csk]))
;; 해당 파일의 이름공간 -> namespace는 프로젝트이름.파일

(defn serpent-talk [input]
  (str "Serpent! You said: " (csk/->snake_case input)))

(defn -main [& args]
  (println (serpent-talk (first args))))
