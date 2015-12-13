(ns kalar-siera.core
  (:require
    [kalar-core.config :as kconfig]
    [kalar-plugins.templates.page :as kpage]
    [hiccup.page :as hpage])
  (:import (java.text SimpleDateFormat)
           (java.util Locale)))

(def ^{:private true} config (kconfig/read-config))


(def ^:private date-formatter (SimpleDateFormat. "MMMMM dd, yyyy" Locale/US))
(defn- format-date [date] (.format date-formatter date))

(def ^:private plain-date-formatter (SimpleDateFormat. "yyyy-MM-dd" ))
(defn- plain-format-date [date] (.format plain-date-formatter date))

(def ^:private head
  (list
    [:header {:id "header"}
     [:h1 [:a {:href (:url config)} (:title config)]]
     [:nav {:class "links"}]
     [:nav {:class "main"}
      [:ul
       [:li {:class "menu"} [:a {:class "fa-bars" :href "#menu"} "Menu"]]]]]
    [:section {:id "menu"}
     [:section
      [:ul {:class "links"}
       [:li
        [:a {:href "/tsundoku/index.html"}
         [:h3 "Tsundoku"]
         [:p "Tsundoku"]]]
       [:li
        [:a {:href "/acknowledgement/index.html"}
         [:h3 "Acknowledgement"]
         [:p "Acknowledgement"]]]]]]))

(def ^:private header
  [:head
   [:title (:title config)]
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   (hpage/include-css "/css/siera.css")
   (hpage/include-css "/css/main.css")
   ]
  )

(def ^:private intro
  [:section {:id "intro"}
   [:a {:href (:url config) :class "logo"}
    [:img {:src (:avatar config) :alt ""}]]
   [:header
    [:h2 (:title config)]]])

(defn- display-topic-img [post-url img]
  [:a {:href post-url :class "image featured"} [:img {:src img}]])

(defn recent-posts []
  (let [num 3
        recent-posts (kpage/load-recent-posts num)]
    [:section
     [:div {:class "mini-posts"}
      (for [post recent-posts]
        [:article {:class "mini-post"}
         [:header
          [:h3 [:a {:href (:link post)} (-> post :title first)]]
          [:time {:class "published" :datetime (plain-format-date (:date post))} (format-date (:date post))]
          ]
         (if (-> post :image)
           [:a {:href (:url post)  :class "image"} [:img {:src (-> post :image first)}]])
         ])
      ]]))

(def ^:private about
  [:section {:class "blurb"}
   [:h2 "About"]
   [:p (:site-description config)]
   [:ul {:class "actions"}
    [:li [:a {:href "/about/index.html" :class "button"} "Learn More"]]]])


(def ^:private footer
  [:section {:id "footer"}
   [:ul {:class "icons"}
    [:li
     [:a {:href (str "https://twitter.com/" (:twitter config)) :class "fa-twitter"}
      [:span {:class "label"} "Twitter"]]]
    [:li
     [:a {:href (str "https://github.com/" (:github config)) :class "fa-github"}
      [:span {:class "label"} "Github"]]]]
   [:p {:class "copyright"} (str "Copyright &copy" (:author config) ". All Right Reserved.")]])

(def ^:private scripts
  (for [js '("jquery.min" "skel.min" "util" "main")]
    (hpage/include-js (str "/js/" js ".js"))))

(defn single-column-page [md]
  (hpage/html5
    header
    [:body
     [:div {:id "wrapper"}
      head
      [:div {:id "main"}
       [:article {:class "post"}
        [:header
         [:div {:class "title"} [:h2 [:a {:href (:url md)} (-> md :title first)]]]
         [:div {:class "meta"}
          [:time {:class "published" :datetime (plain-format-date (:date md))} (format-date (:date md))]
          [:a {:href "#"} (-> md :category first)]]
         ]
        (if (-> md :image)
          [:a {:href (:url md)  :class "image featured"} [:img {:src (-> md :image first)}]])
        (:body md)]]
      [:section {:id "sidebar"} intro (recent-posts) about footer]]
     scripts]))

(defn paginate
  [mds]
  (hpage/html5
    header
    [:body
     [:div {:id "wrapper"}
      head
      [:div {:id "main"}
       (for [md (-> mds :posts)]
         [:article {:class "post"}
          [:header
           [:div {:class "title"} [:h2 [:a {:href (:url md)} (-> md :title first)]]]
           [:div {:class "meta"}
            [:time {:class "published" :datetime (plain-format-date (:date md))} (format-date (:date md))]
            [:a {:href "#"} (-> md :category first)]]]
          (if (-> md :image)
            [:a {:href (:url md)  :class "image featured"} [:img {:src (-> md :image first)}]])
          [:p (:excerpt md)]
          [:footer
           [:ul {:class "actions"} [:li [:a {:href (:url md) :class "button big"} "Continue Reading"]]]]])
       [:ul {:class "actions pagination"}
        (if (-> mds :previous-page nil?)
          [:li [:a {:href "#" :class "disabled button big previous"} "Previous Page"]]
          [:li [:a {:href (-> mds :previous-page) :class "button big previous"} "Previous Page"]])
        (if (-> mds :next-page nil?)
          [:li [:a {:href "#" :class "disabled button big next"} "Next Page"]]
          [:li [:a {:href (-> mds :next-page) :class "button big next"} "Next Page"]])]]
     [:section {:id "sidebar"}
      intro
      ( recent-posts) about footer]
      ]
     scripts
     ]))

(defn diary [md]
  (hpage/html5
    header
    [:body
     [:div {:id "wrapper"}
      head
      [:div {:id "main"}
       [:article {:class "post"}
        [:header
         [:div {:class "title"} [:h2 [:a {:href (:url md)} (-> md :title first)]]]
         [:div {:class "meta"}
          [:time {:class "published" :datetime (plain-format-date (:date md))} (format-date (:date md))]
          [:a {:href "#"} (-> md :category first)]]]
        (if (-> md :image)
          [:a {:href (:url md)  :class "image featured"} [:img {:src (-> md :image first)}]])
        (:body md)
        ]
       [:ul {:class "actions pagination"}
        (if (-> md :previous-page nil?)
          [:li [:a {:href "#" :class "disabled button big previous"} "Previous Page"]]
          [:li [:a {:href (-> md :previous-page) :class "button big previous"} "Previous Page"]])
        (if (-> md :next-page nil?)
          [:li [:a {:href "#" :class "disabled button big next"} "Next Page"]]
          [:li [:a {:href (-> md :next-page) :class "button big next"} "Next Page"]])]]
      [:section {:id "sidebar"} intro (recent-posts) about footer]]
     scripts]))

