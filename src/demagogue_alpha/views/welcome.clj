(ns demagogue-alpha.views.welcome
  (:require [demagogue-alpha.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to demagogue-alpha"]))
