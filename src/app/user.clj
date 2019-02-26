(ns app.user)
(require '[clojure.string :as str])
(require '[app.sources :as src])

(declare entrer-code)
(declare compatible?)
(declare isColor?)
(declare check-code)
(declare end-game)



(def colors #{:rouge, :bleu, :jaune, :vert, :noir, :blanc})
(def g :good)
(def b :bad)
(def c :color)
(def taille 4)
(def nbTenta 5) ;Nombre de tentative pour le joueur



(declare game)
(defn game []
  (let [soluce (src/code-secret taille)]
    (entrer-code soluce 0)))


(defn entrer-code [sol cpt]
  "Boucle pour entrer un code compatible et verifie ce code"
  (println "||------------ Tentative en cours :" (inc cpt) "-------------||")
  (if (= cpt nbTenta) ;check si le nombre de tentative est fini
    (end-game 0)
    ;else on continue la partie
    (or (println "||            Entrez un code du type :             ||\n||       ':couleur ... :couleur' de taille"taille"      ||\n|| parmi" colors"||\n")
      (let [input (str/trim (read-line))] ;recupere l'entree
        (if (compatible? (src/str-to-key input)) ;Parse le string en vector et check si la forme est compatible
          (check-code sol (src/str-to-key input) cpt) ;check les couleurs
          (or (println "=> Le code n'est pas compatible\n") (entrer-code sol cpt)))))));si pas compatible entrer une nouvelle combinaison


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



(defn check-code [soluce input cpt]
  "check si le code est bon"
  (let [check (src/filtre-indications soluce input (src/indications soluce input))]
    (if (= check [g g g g])
      (end-game 1)
      (or (println "=>"check"\n") (entrer-code soluce (inc cpt))))))



(defn end-game [i]
  (if (= i 1)
    (println "Tu es une personne formidable")
    (println "Perdu, fin du jeu")))
