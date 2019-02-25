(ns app.bot)
(require '[app.sources :as src])


(def color [:bleu :rouge :vert :jaune :noir :blanc])
(def tailleM 4)


(declare solveur)
(declare init)
(declare newTry)

(declare solve)
(defn solve []
  (let [soluce (src/code-secret tailleM)]
    (solveur soluce)))

(defn solveur [soluce]
  "solveur"
  (let [init (init tailleM)]
    (loop [s soluce,res init,cpt 1]
      (let [indic (src/filtre-indications res s (src/indications res s))]
        (if (= indic (src/geneGood tailleM))
          (println "Found: " res)
          (recur s (newTry res indic cpt ) (inc cpt)))))))


(defn init [taille]
  (loop [taille taille,res []]
    (if (> taille 0)
      (recur (dec taille) (conj res (get color 0)))
      res)))



(defn newTry [last_try indication cpt]
  "Renvoi un essai en gardant les couleurs :good"
  (println last_try)
  (loop [s last_try,i indication,res []]
    (if (seq s)
      (if (= :good (first i))
        (recur (rest s) (rest i) (conj res (first s)))
        (recur (rest s) (rest i) (conj res (get color cpt))))
      res)))
