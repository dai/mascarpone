(defproject clojuroid/clojuroid "0.0.1-SNAPSHOT"
  :description "FIXME: Android project description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"

  :warn-on-reflection true

  :source-paths ["src/clojure"]
  :java-source-paths ["src/java" "gen"]
  ;; The following two definitions are optional. The default
  ;; target-path is "target", but you can change it to whatever you like.
  ;; :target-path "bin"
  ;; :compile-path "bin/classes"

  ;; Uncomment this line if your project doesn't use Clojure. Also
  ;; don't forget to remove respective dependencies.
  ;; java-only true

  :dependencies [[android/clojure "1.4.0"]
                 [neko/neko "2.0.0-snapshot"]]
  :profiles {:dev {:dependencies [[android/tools.nrepl "0.2.0-bigstack"]]
                   :android {:aot :all-with-unused}}
             :release {:android
                       {;; Specify the path to your private
                        ;; keystore and the the alias of the
                        ;; key you want to sign APKs with.
                        ;; :keystore-path "/home/user/.android/private.keystore"
                        ;; :key-alias "mykeyalias"
                        :aot :all}}}

  :android {;; Specify the path to the Android SDK directory either
            ;; here or in your ~/.lein/profiles.clj file.
            ;; :sdk-path "/home/user/path/to/android-sdk/"
            :sdk-path "/Users/niko/Downloads/adt-bundle-mac-x86_64-20130514/sdk"

            ;; Uncomment this if dexer fails with OutOfMemoryException
            ;; :force-dex-optimize true

            :target-version "11"
            :aot-exclude-ns ["clojure.parallel"]})