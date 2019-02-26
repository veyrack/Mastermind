(ns app.bot)
(require '[app.sources :as src])
(require '[clojure.string :as str])

(def color [:bleu :rouge :vert :jaune :noir :blanc])
(def indi #{:good :bad :color})
(def tailleM 4)


(declare solveur)
(declare solveurInte)
(declare init)
(declare newTry)

(declare solve)

(defn solve []
  (let [soluce (src/code-secret tailleM)]
    ;(solveur soluce)))
    (solveurInte)))


(defn solveur [soluce]
  "solveur"
  (let [init (init tailleM)]
    (loop [s soluce,res init,cpt 1]
      (let [indic (src/filtre-indications res s (src/indications res s))]
        (if (= indic (src/geneGood tailleM))
          (println "Found: " res)
          (recur s (newTry res indic cpt ) (inc cpt)))))))

(defn solveurInte []
  (let [init (init tailleM)]
    (loop [res init,cpt 1]
      (or (println "   Proposition du bot: "res"\n||        Entrez votre indication de type         ||\n||         ':good ... :good' de taille" tailleM"         ||\n||          parmi:"indi"          ||\n")
        (let [input (str/trim (read-line))]
          (if (src/compatible? (src/str-to-key input))
            (if (= (src/str-to-key input) (src/geneGood tailleM))
              (println "Resultat final : " res)
              (if (> cpt (inc tailleM))
                (println "TRICHEUR")
                (recur (newTry res (src/str-to-key input) cpt ) (inc cpt))))
            (or (println "=> L'indication n'est pas compatible\n") (recur res cpt))))))))


(defn init [taille]
  (loop [taille taille,res []]
    (if (> taille 0)
      (recur (dec taille) (conj res (get color 0)))
      res)))



(defn newTry [last_try indication cpt]
  "Renvoi un essai en gardant les couleurs :good"
  ;(println last_try)
  (loop [s last_try,i indication,res []]
    (if (seq s)
      (if (= :good (first i))
        (recur (rest s) (rest i) (conj res (first s)))
        (recur (rest s) (rest i) (conj res (get color cpt))))
      res)))
