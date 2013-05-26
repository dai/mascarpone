(defproject clj-native "0.9.3-SNAPSHOT"
  :description "Simplify usage of native libs from Clojure. Uses JNA."
  :jvm-opts ["-Djna.library.path=src/examples"]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [net.java.dev.jna/jna "3.4.0"]])
