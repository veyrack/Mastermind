(ns app.core)
(require '[app.sources :as src])
(require '[app.user :as u])
(defn -main []
  "I don't do a whole lot."
  ;(let [soluce (src/code-secret taille)]
  ;  (println soluce)
  (u/game))
