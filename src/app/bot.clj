(ns app.bot)
(require '[app.user :as u])

(declare solveur)
(def color [:bleu :rouge :vert :jaune :noir :blanc])

(defn solveur [soluce]
  "solveur"
  (let [init (init 4)]
    (loop [s soluce,res []])))


(let indi (u/indications init s))



(defn init [taille]
  (loop [taille taille,res []]
    (if (> taille 0)
      (recur (dec taille) (conj res (get color 0)))
      res)))
