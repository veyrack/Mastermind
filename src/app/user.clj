(ns app.user)
(require '[clojure.string :as str])
(require '[app.sources :as src])
(require '[app.core :as c])

(declare entrer-code)
(declare compatible?)
(declare isColor?)
(declare str-to-key)
(declare check-code)
(declare end-game)
(declare getGood)
(declare getColor)
(declare getBad)
(declare containsV?)
(declare frequences)
(declare indications)
(declare filtre-indications)

(def colors #{:rouge, :bleu, :jaune, :vert, :noir, :blanc})
(def g :good)
(def b :bad)
(def c :color)
(def taille 4)
(def nbTenta 5) ;Nombre de tentative pour le joueur


(declare code-secret)
(defn code-secret [n]
  (loop [i 1,res []]
    (if (> i n)
      res
      (recur (inc i) (conj res (rand-nth [:rouge :bleu :vert :jaune :noir :blanc]))))))


(declare game)
(defn game []
  (println "0 pour jouer ou 1 pour faire deviner au solver")
  (let [x (read-line)]
    (if (= (Integer/parseInt x) 0)
      (let [soluce (code-secret taille)]
        (entrer-code soluce 0))
      (println "en construction"))))


(defn entrer-code [sol cpt]
  "Boucle pour entrer un code compatible et verifie ce code"
  (if (= cpt nbTenta) ;check si le nombre de tentative est fini
    (end-game)
    ;else on continue la partie
    (or (println "Entrez un code du type ':couleur :couleur :couleur :couleur'")
      (let [input (str/trim (read-line))] ;recupere l'entree
        (if (compatible? (str-to-key input)) ;Parse le string en vector et check si la forme est compatible
          (check-code sol (str-to-key input) cpt) ;check les couleurs
          (or (println "pas compatible") (entrer-code sol cpt)))))));si pas compatible entrer une nouvelle combinaison


(defn compatible? [vec]
  "check si l'input est bien de la forme du code"
  (loop [s vec,cpt 0]
    (if (seq s)
      (if (isColor? (first s)) ;Check si first s est une couleur
        (recur (rest s) (inc cpt))
        false)
      (if (not= cpt taille) ;Check si la longueur du code est bien 4
        false
        true))))

(defn isColor? [str]
  (if (contains? colors str)
    true
    false))

(defn str-to-key [str]
  "transforme un string en vecteur de keywords"
  (let [vec (str/split str #" ")]
    (loop [s vec,res []]
      (if (seq s)
        (recur (rest s) (conj res (keyword (second (str/split (first s) #":")))))
        res))))

(defn check-code [soluce input cpt]
  "check si le code est bon"
  (let [check (filtre-indications soluce input (indications soluce input))]
    (if (= check [g g g g])
      (println "Tu es une personne formidable")
      (or (println check) (entrer-code soluce (inc cpt))))))


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



(defn end-game [i]
  (if (= i 1)
    (or (println "Tu es une personne formidable") (game))
    (or (println "Perdu, fin du jeu") (game))))
