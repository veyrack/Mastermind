(ns app.core)
(require '[app.sources :as src])
(require '[app.user :as u])
(require '[app.bot :as bot])

(def taille 4)

(defn -main []
  "I don't do a whole lot."
  (println "Entrez [0] pour jouer ou [1] pour utiliser le solver")
  (let [x (read-line)]
      (if (= (Integer/parseInt x) 0)
        (u/game)
        (bot/solve))))
