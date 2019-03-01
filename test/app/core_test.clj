(ns app.core-test
  (:require [clojure.test :refer :all]
            [app.core :refer :all]))
(require '[app.sources :as src])
(require '[app.user :as u])
(require '[app.bot :as bot])
(use 'midje.repl)

(deftest a
  (fact "testing"
    (conj [:a] :b)  => [:a :b]))

(deftest testBot
  (fact (bot/solveur [:bleu :vert :rouge :blanc]) => [:bleu :vert :rouge :blanc])
  (fact (bot/init 4) => [:bleu :bleu :bleu :bleu])
  (fact (bot/newTry [:bleu :bleu :bleu :bleu] [:good :bad :good :bad] 1)
    => [:bleu :rouge :bleu :rouge]))

(deftest testUser
  (fact (u/compatible? [:bleu :vert :blanc :noir]) => true)
  (fact (u/compatible? [:abcd 'vert 'efgh :noir]) => false)
  (fact (u/isColor? :bleu) => true)
  (fact (u/isColor? 'bleu) => false)
  (fact (u/check-code [:bleu :jaune :vert :blanc] [:bleu :jaune :vert :blanc] 1) => true)
  (fact (u/end-game 1) => true))

(deftest testSource
  (fact "Le `code-secret` est bien composé de couleurs."
      (every? #{:rouge :bleu :vert :jaune :noir :blanc}
        (src/code-secret 4))  => true)

  (fact "Le `code-secret` a l'air aléatoire."
        (> (count (filter true? (map not=
                                     (repeatedly 20 #(src/code-secret 4))
                                     (repeatedly 20 #(src/code-secret 4)))))
           0)
        => true)
  (fact "les `frequences` suivantes sont correctes."
    (src/frequences [:rouge :rouge :vert :bleu :vert :rouge])
    => {:rouge 3 :vert 2 :bleu 1})

  (fact "Le `filtre-indications` fonctionne bien."
      (src/filtre-indications [:rouge :rouge :vert :bleu]
                          [:vert :rouge :bleu :jaune]
                          [:color :good :color :bad])
      => [:color :good :color :bad])

  (fact "`indications` sont les bonnes."
        (src/indications [:rouge :rouge :vert :bleu]
                     [:vert :rouge :bleu :jaune])
        => [:color :good :color :bad])

  (fact (src/containsV? [:vert :bleu :rouge :blanc] :blanc) => true)
  (fact (src/geneGood 4) => [:good :good :good :good])
  (fact (src/compatible? [:good :bad :good :bad]))
  (fact (src/isIndic? :good) => true)
  (fact (src/isIndic? good) => false)
  (fact (src/str-to-key ":bleu :rouge :vert :jaune") => [:bleu :rouge :vert :jaune]))
