(ns audio-book-renamer.core
  (:gen-class))

(require '[clojure.java.io :as io])

(def test-path "C:\\Users\\asmith\\Desktop\\New Folder")
(def output-path "C:\\Users\\asmith\\Desktop\\Renamer")
(def book-name "The Help")
(def book-author "Kathryn Stockett")

(def oldest-first
  (comparator #(< (.lastModified %1) (.lastModified %2))))

(defn is-m4b-file [file] (and
                           (.isFile file)
                           (.endsWith (.getName file) ".m4b")))

(defn get-audio-files [path] 
  (let [x (io/file path)]
    (sort oldest-first
          (filter is-m4b-file (file-seq x)))))

(defn generate-name [n f]
  (io/file output-path (format "%02d - %s - %s.m4b" n book-author book-name)))

(defn new-files [xs]
  (map-indexed #(generate-name (inc %1) %2) xs))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (.mkdirs (io/file output-path))
  (let [xs (get-audio-files test-path)]
    (doseq [[f1 f2] (map vector xs (new-files xs))]
      (io/copy f1 f2))))
