(ns app.sources)
(require '[clojure.string :as str])


(def tailleM 4)
(def indi #{:good :bad :color})

(declare code-secret)
(declare frequences)
(declare filtre-indications)
(declare indications)
(declare containsV?)
(declare geneGood)
(declare str-to-key)
(declare compatible?)
(declare isIndic?)

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
  "Check si i est contenu dans le vecteur v"
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


(defn compatible? [vec]
  "check si l'input est bien de la forme [:good...]"
  (loop [s vec,cpt 0]
    (if (seq s)
      (if (isIndic? (first s)) ;Check si first s est une couleur
        (recur (rest s) (inc cpt))
        false)
      (if (not= cpt tailleM) ;Check si la longueur du code est bien 4
        false
        true))))

(defn isIndic? [e]
  (if (contains? indi e)
    true
    false))

(defn str-to-key [str]
  "transforme un string en vecteur de keywords"
  (let [vec (str/split str #" ")]
    (loop [s vec,res []]
      (if (seq s)
        (recur (rest s) (conj res (keyword (second (str/split (first s) #":")))))
        res))))
