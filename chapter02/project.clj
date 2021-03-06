(defproject chapter02 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [
  [lein-antlr "0.2.0"]
  [lein-protobuf "0.3.1"]
  ]
  ; just for antlr
  :antlr-src-dir "antlr"
  :antlr-dest-dir "gen-src"
  :java-source-paths ["src/java" "gen-src"]

  :dependencies [
    ; clojure
    [org.clojure/clojure "1.5.0-alpha5"]

  	; colors in your REPL
  	[colorize "0.1.1" :exclusions [[org.clojure/clojure]]]

  	; http kit, websockets
  	[me.shenfeng/http-kit "2.0-SNAPSHOT"]

    ; statistics
    [incanter "1.3.0"]

    ; graph 
    [lacij "0.7.1"]

    ; UI
    [seesaw "1.4.2"]
    ; UI customization
    [com.github.insubstantial/substance "7.1"]

    ; xml parsing
    [org.clojars.kyleburton/clj-xpath "1.4.0"]

    ; html parsing
    [clojure-soup "0.0.1" :exclusions [[org.clojure/clojure]]]

    ; templating
    [enlive "1.0.1"]
    
    ; docjure
    [dk.ative/docjure "1.6.0"]

    ; emails
    [com.draines/postal "1.9.2"]

    ; dns
    [com.brweber2/clj-dns "0.0.2"]  

    ; clostache
    [de.ubercode.clostache/clostache "1.3.1"] 

    ; csv
    [clojure-csv "2.0.0-alpha2"]

    ; RSS
    [clj-rss "0.1.2"]

    ; growl
    [clj-growlnotify "0.1.1"]

    ; JSON
    [cheshire "5.0.1"]

    ; digest
    [digest "1.3.0"]

    ; terminal
    [clojure-lanterna "0.9.2"]

    ; time
    [clj-time "0.4.5-SNAPSHOT"]

    ; ssh
    [clj-ssh "0.4.3"]

    ; easy http
    [clj-http "0.3.6"]

    ; serial
    [serial-port "1.1.2"]

    ; large xml
    [xml-picker-seq "0.0.2"]    

    ; opennlp
    [clojure-opennlp "0.2.0"]

    ; conduit processing
    [net.intensivesystems/conduit "0.9.0"]

    ; stream processing and workflow
    [lamina "0.5.0-beta9"]

    ; Alice stuff
    [laczoka/clj-crypto "1.0.2-SNAPSHOT"]

    ; units
    [frinj "0.1.3"]

    ; json querying
    [json-path "0.2.0"]

    ; neural network
    [netz "0.1.1"]

    ; shell commands
    [shake "0.2.1"]

    ; timers
    [factual/timely "0.0.3"]

    ; at at
    [overtone/at-at "1.1.1"]

    ; latest antlr-complete, to test the generate parser from antlr plugin
    [org.antlr/antlr4 "4.0"] 

    ; files
    [clj-glob "1.0.0"]

    ; match and matchure
    [matchure "0.10.1"]
    [org.clojure/core.match "0.2.0-alpha11"]

    ; google protobuffer
    [org.flatland/protobuf "0.7.2"]

    ; lucene
    [clucy "0.3.0"]

    ; solr remote integration
    [org.clojars.mattdeboard/clojure-solr "0.4.0"]

    ; pdf magic
    [com.lowagie/itext "4.2.0"] ; use a more recent itext library
    [clj-pdf "1.0.5-SNAPSHOT" :exclusions [[itext-min]]]

    ; event processing
    [clj-esper "1.0.1"]

    ; ldap
    [org.clojars.pntblnk/clj-ldap "0.0.7"]

    ; parser
    [net.cgrand/parsley "0.9.1"]

  ])