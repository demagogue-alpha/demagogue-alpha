(ns demagogue-alpha
  (:use clojure.pprint))

(defrecord Player [name influence played hand])

(def costs {:force 2, :rhetoric 3, :wealth 1})

(declare play-turn take-action)

(defn remove-first [x s]
  (let [[pre post] (split-with #(not= x %) s)]
    (concat pre (rest post))))

(def words {0 "Zero", 1 "One", 2 "Two", 3 "Three", 4 "Four", 5 "Five", 6 "Six"})

(defn make-players [n]
  (map #(Player. (words %) 5 [] []) (range n)))

(defn choose-card [p choice]
  "A player may spend influence to add a card to their hand."
  (assoc p :influence (- (:influence p) (costs choice))
           :hand      (conj (:hand p) choice)))

(defn play-card [p choice]
  "Take a card from the hand and play it."
  (assoc p :played (conj (:played p) choice)
           :hand   (remove-first choice (:hand p))))

(defn add-influence [p ps n]
  "Give n influence to player p"
  (assoc p :influence (+ (:influence p) (min n (remaining-influence ps)))))

(defn rhetoric-influence-owed [p]
   (count (filter #(= % :rhetoric) (:played p)))) 

(defn remaining-influence [ps]
  (- (* 50 (count ps)) (reduce + (map :influence ps))))

(defn any-winner? [ps total]
  (some #(= (:influence %) total) ps))

(defn play-game [n]
  (let [total-influence (* 50 n)]
    (take-while (complement #(any-winner? % total-influence)) 
      (iterate play-turn (make-players n)))))

(defn play-turn [ps]
  (let [[p & others] ps]
    (take-action 
      (add-influence p ps (rhetoric-influence-owed p)) 
      others)))

(defn take-action [p others]
  (let [choice (rand-nth (keys costs))]   
    (conj (into [] others) 
    (if (< (costs choice) (:influence p))
        (choose-card p choice)
        (if (not (empty? (:hand p))) 
           (play-card p (rand-nth (:hand p))) 
           p)))))

(defn run []
  (let [batch (take 15 (play-game 3))]
    (pprint batch)))

(run)
