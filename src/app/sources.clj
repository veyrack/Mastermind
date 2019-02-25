(ns app.sources)



(def tailleM 4)

;; ## tirage aleatoire du code secret
(declare code-secret)
(declare filtre-indications)
(declare indications)
(declare frequences)
(declare containsV?)
(declare geneGood)


(defn code-secret [n]
  (loop [i 1,res []]
    (if (> i n)
      res
      (recur (inc i) (conj res (rand-nth [:rouge :bleu :vert :jaune :noir :blanc]))))))

(defn frequences [v]
  (loop [s v,res {}]
    (if (seq s)
      (recur (rest s)
        (assoc res (first s) (inc (get res (first s) 0))))
      res)))


(defn filtre-indications [code, try, indic]
  "renvoi les bonnes indications"
  (loop [index 0, res [], freq (frequences code)]
    (if (< index (count code))
      (let [color (get try index), tag (get indic index)]
        (if (= tag :bad)
          (recur (inc index), (conj res tag), freq)
          (if (> (get freq color) 0)
            (recur (inc index), (conj res tag), (update freq color dec))
            (recur (inc index), (conj res :bad), freq))))
      res)))


(defn containsV? [v i]
  (loop [s v]
    (if (seq s)
      (if (= i (first s))
        true
        (recur (rest s)))
      false)))

(defn indications [sol v]
  "indique les :good | :color | :bad en fonction des positions"
  (loop [s v,res [], m sol]
    (if (seq s)
      (if (= (first s) (first m))
          (recur (rest s) (conj res :good) (rest m))
          (if (containsV? sol (first s))
            (recur (rest s) (conj res :color) (rest m))
            (recur (rest s) (conj res :bad) (rest m))))
      res)))

(defn geneGood [taille]
  "Genere un vecteur de taille taille contenant :good"
  (loop [taille taille,res []]
    (if (= 0 taille)
      res
      (recur (dec taille) (conj res :good)))))
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


;(fact "les `frequences` suivantes sont correctes."
;      (frequences [:rouge :rouge :vert :bleu :vert :rouge])
;      => {:rouge 3 :vert 2 :bleu 1}
;
;      (frequences [:rouge :vert :bleu])
;      => {:rouge 1 :vert 1 :bleu 1}
;
;      (frequences [1 2 3 2 1 4]) => {1 2, 2 2, 3 1, 4 1})))
