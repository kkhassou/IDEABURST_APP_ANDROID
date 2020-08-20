package com.example.ideamemo

import io.realm.RealmObject

class RealmModel {
}
public open  class IdeaStock : RealmObject() {
    var big_theme_id = ""
    var big_theme = ""
    var theme_id = ""
    var theme = ""
    var hint = ""
    var idea_id = ""
    var idea = ""
}
public open  class HintStock : RealmObject() {
    var category = ""
    var hint : String = ""
}
//public open  class HierarchyMemory : RealmObject() {
//    var big_theme_id = ""
//    var big_theme = ""
//    var theme_id = ""
//    var theme = ""
//}
