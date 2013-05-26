;;   Copyright (c) Markus Gustavsson. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file epl-v10.html at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.

;;; ***************************************************************************
;;;
;;; -----===== JNA Direct Mapping =====-----
;;;
;;; ***************************************************************************

(ns clj-native.direct
  (:use [clj-native direct-util functions callbacks structs unions])
  (:import [java.util UUID]
           [com.sun.jna Native Library]))

(defn parse-lib
  "Parses input to defclib and returns a library specification map"
  [name & body]
  (when-not (symbol? name)
    (throw (Exception. "name must be a symbol"))) ;; overkill?
  (let [when-key (fn [k f ut]
                   (when-let [stuff (some #(when (= k (first %))
                                             (next %))
                                          body)]
                     (f stuff ut)))
        user-types (atom {})
        libname (or (when-key :libname (fn [x & _] (str (first x))) nil)
                    (str name))
        structs (when-key :structs parse-structs user-types)
        unions (when-key :unions parse-unions user-types)
        callbacks (when-key :callbacks parse-callbacks user-types)
        functions (when-key :functions parse-functions user-types)]
    {:name name
     :lib libname
     :cbs callbacks
     :fns functions
     :structs structs
     :unions unions
     :pkg (symbol (.replaceAll (str (ns-name *ns*)) "-" "_"))
     :classname (symbol (.replaceAll
                         (str "lib_" (UUID/randomUUID)) "-" "_"))}))

(defn loadlib-fn
  "Creates a function that will load a native library
  and replace all the mapped function stubs with real versions."
  [lib]
  (let [clsname (str (:pkg lib) \. (:classname lib))]
    `(fn []
       ;; Try to load the native library before creating and
       ;; loading the JNA class because we want to discover the
       ;; error of not finding the library file here rather than
       ;; in the static class initializer.
       (let [libobject# (Native/loadLibrary ~(:lib lib) Library)]
         ;; Structs
         ~@(for [sspec (:structs lib)]
             `(let [spec# '~(update-in
                             sspec [:fields]
                             #(doall
                               (for [f %]
                                 (update-in f [:type] force))))
                    [main# val# ref#] (make-native-struct spec#)]
                (load-code ~(:classname sspec) main# spec#)
                (load-code ~(str (:classname sspec) "$ByValue") val# spec#)
                (load-code ~(str (:classname sspec) "$ByReference") ref# spec#)
                ~(make-struct-constructors (list 'quote (ns-name *ns*)) sspec)))
         ;; Unions
         ~@(for [uspec (:unions lib)]
             `(let [spec# '~(update-in
                             uspec [:fields]
                             #(doall
                               (for [f %]
                                 (update-in f [:type] force))))
                    [main# val# ref#] (make-native-union spec#)]
                (load-code ~(:classname uspec) main# spec#)
                (load-code ~(str (:classname uspec) "$ByValue") val# spec#)
                (load-code ~(str (:classname uspec) "$ByReference") ref# spec#)
                ~(make-union-constructors (list 'quote (ns-name *ns*)) uspec)))
         ;; Callback interfaces and constructors
         ~@(for [cbspec (:cbs lib)]
             `(let [spec# '~(assoc cbspec
                              :rettype (force (:rettype cbspec))
                              :argtypes (force (:argtypes cbspec)))]
                (load-code ~(:classname cbspec)
                           (make-callback-interface spec#) spec#)
                ~(make-callback-constructor (list 'quote (ns-name *ns*))
                                            cbspec)))
         ;; Main glue class
         (let [spec# '~(doall
                        (for [fdef (:fns lib)]
                          (-> (update-in fdef [:argtypes] force)
                              (update-in [:rettype] force))))]
           (load-code ~clsname
                      (make-native-lib-stub
                       ~(:lib lib)
                       ~(str (:name lib))
                       spec#
                       :name '~(:classname lib)
                       :pkg '~(:pkg lib))
                      spec#))
         ;; Rebinding of function var roots
         ;; TODO: move to own function like the others
         ~@(for [fdef (:fns lib)]
             (let [native (:name fdef)
                   name (:cljname fdef)
                   args (vec (argnames (force (:argtypes fdef))))
                   ns (ns-name *ns*)
                   v (gensym)]
               `(eval
                 ~(list 'quote
                        (list 'let [v `(ns-resolve '~ns '~name)]
                              `(.bindRoot
                                ~v
                                ~(list `fn args
                                       (list* (symbol (str clsname \/ native))
                                              args))))))))
         libobject#))))

;;; ***************************************************************************
;;;
;;; -----===== Public Interface =====-----
;;;
;;; ***************************************************************************

;; TODO: new doc string
(defmacro defclib
  "Create C library bindings."
  [lib & body]
  (let [lib (apply parse-lib lib body)]
    `(do
       (def ~(:name lib) {:loadfn ~(loadlib-fn lib)})
       ;; TODO: Remove the stubs for structs and callbacks?
       ;; They are not strictly needed. Could just require quoted symbols
       ;; as input to constructor functions.
       ~@(make-struct-stubs (list 'quote (ns-name *ns*)) lib)
       ~@(make-union-stubs (list 'quote (ns-name *ns*)) lib)
       ~@(make-callback-stubs (list 'quote (ns-name *ns*)) lib)
       ~@(make-function-stubs lib))))

(defn loadlib
  "Loads a native library.
  DO NOT call this on the top level, should be called at runtime only."
  [libdef]
  ((:loadfn libdef)))

(defn typeof
  "Returns the java class type of a callback, struct or union.
  Use the second parameter with :val or :ref to get the java class
  for a structure or union that is passed by value or reference respectively."
  ([x] (typeof x nil))
  ([x pass-style]
     (condp = pass-style
       :val (:valclass x)
       :ref (:refclass x)
       (:class x))))
