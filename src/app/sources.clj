(ns app.sources)
(require '[app.user :as u])
(def taille u/taille)

;; ## tirage aleatoire du code secret


;;(fact "Le `code-secret` est bien composé de couleurs."
;;    (every? #{:rouge :bleu :vert :jaune :noir :blanc
;;              (code-secret 4))
;;      => true)))

;;(fact "Le `code-secret` a l'air aléatoire."
;      (> (count (filter true? (map not=
;                                   (repeatedly 20 #(code-secret 4))
;                                   (repeatedly 20 #(code-secret 4))))
;         0)
;      => true)))


;; ## Indication si les couleurs sont :good :bad ou :color



;(fact "`indications` sont les bonnes."
;      (indications [:rouge :rouge :vert :bleu]
;                   [:vert :rouge :bleu :jaune]
;      => [:color :good :color :bad]

;      (indications [:rouge :rouge :vert :bleu]
;                   [:bleu :rouge :vert :jaune]
;      => [:color :good :good :bad]

;      (indications [:rouge :rouge :vert :bleu]
;                   [:rouge :rouge :vert :bleu]
;      => [:good :good :good :good]

;      (indications [:rouge :rouge :vert :vert]
;                   [:vert :bleu :rouge :jaune]
;      => [:color :bad :color :bad])))

;; ## Donne les frequences dans un vecteur

(declare frequences)
(defn frequences [v]
  (loop [s v,res {}]
    (if (seq s)
      (recur (rest s)
        (assoc res (first s) (inc (get res (first s) 0))))
      res)))

;(fact "les `frequences` suivantes sont correctes."
;      (frequences [:rouge :rouge :vert :bleu :vert :rouge])
;      => {:rouge 3 :vert 2 :bleu 1}
;
;      (frequences [:rouge :vert :bleu])
;      => {:rouge 1 :vert 1 :bleu 1}
;
;      (frequences [1 2 3 2 1 4]) => {1 2, 2 2, 3 1, 4 1})))

;; ## frequences des couleurs en fct des dispo

(declare freqs-dispo)
(defn freqs-dispo [v1 v2]
  (loop [s v1, res [], m v2, nul {}]
    (if (seq s)
      (if (= (first m) :good)
          (recur (rest s) res (rest m) (if (contains? nul (first s))
                                         nul
                                         (assoc nul (first s) 0)))
          (recur (rest s) (conj res (first s)) (rest m) nul))
      (conj nul (frequences res)))))

;(fact "Les fréquences disponibles de `freqs-dispo` sont correctes."
;      (freqs-dispo [:rouge :rouge :bleu :vert :rouge]
;                   [:good :color :bad :good :color]))
;      => {:bleu 1, :rouge 2, :vert 0})))

;; ## Question 5 : filtrer par cadinalité (+ diffic
;(filtre-indications [:rouge :vert :bleu :rouge] [:rouge :rouge :rouge :bleu] [:good :color :color :color])
