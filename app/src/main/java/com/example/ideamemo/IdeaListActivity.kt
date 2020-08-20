package com.example.ideamemo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import pj.ekak.ideamemo.R
import java.util.*
import kotlin.collections.ArrayList


class IdeaListActivity : AppCompatActivity() {

    lateinit var realm: Realm
    var m_big_theme = ""
    var m_big_theme_id = ""
    var m_theme = ""
    var m_theme_id = ""

    var theme_list_items = ArrayList<String>()

    var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idea_list)
        Realm.init(this);
        val realmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        realm = Realm.getInstance(realmConfiguration)

        val idea_list = findViewById<ListView>(R.id.idea_list)

        adapter = ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1,
            theme_list_items);
        idea_list.adapter = adapter

        // 一番最初以外は、アイデア出しから戻る時
        val intent = getIntent()
        m_big_theme = intent.getStringExtra("m_big_theme")
        m_big_theme_id = intent.getStringExtra("m_big_theme_id")
        val big_theme_content = findViewById<TextView>(R.id.big_theme_content)
        big_theme_content.setText(m_big_theme)
        val big_theme_to_thme = realm.where(IdeaStock::class.java).equalTo("big_theme_id",m_big_theme_id).findAll()
        big_theme_to_thme.forEach { obj ->
            if(obj.theme != "") {
                theme_list_items.add(obj.theme)
            }
        }
        val theme_to_idea = realm.where(IdeaStock::class.java).equalTo("theme_id",m_big_theme_id).findAll()
        theme_to_idea.forEach { obj ->
            if(obj.idea != "") {
                theme_list_items.add(obj.idea)
            }
        }
        var listItems_temp = ArrayList<String>()
        listItems_temp.addAll(theme_list_items.distinct())
        theme_list_items.clear()
        theme_list_items.addAll(listItems_temp)

        val button_retrun = findViewById<Button>(R.id.button_return)
        button_retrun.setOnClickListener {
            val intent = Intent(this@IdeaListActivity, MainActivity::class.java)
            startActivity(intent)
        }
        val button_up = findViewById<Button>(R.id.button_up)
        button_up.setOnClickListener {
            // 階層が一番上だったら、それ以上、上にいけない
            if(m_big_theme != ""){
                val idea_list = findViewById<ListView>(R.id.idea_list)
                adapter = ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    theme_list_items);
                idea_list.adapter = adapter
                val serch = realm.where(IdeaStock::class.java).equalTo("theme_id",m_big_theme_id).findFirst()
                m_big_theme_id = serch?.big_theme_id.toString()
                m_big_theme = serch?.big_theme.toString()
                val big_theme_content = findViewById<TextView>(R.id.big_theme_content)
                big_theme_content.setText(m_big_theme)
                theme_list_items.clear()
                val big_theme_to_thme = realm.where(IdeaStock::class.java).equalTo("big_theme_id",m_big_theme_id).findAll()
                big_theme_to_thme.forEach { obj ->
                    if(obj.theme != "") {
                        theme_list_items.add(obj.theme)
                    }
                }
                val theme_to_idea = realm.where(IdeaStock::class.java).equalTo("theme_id",m_big_theme_id).findAll()
                theme_to_idea.forEach { obj ->
                    if(obj.idea != "") {
                        theme_list_items.add(obj.idea)
                    }
                }
                var listItems_temp = ArrayList<String>()
                listItems_temp.addAll(theme_list_items.distinct())
                theme_list_items.clear()
                theme_list_items.addAll(listItems_temp)

                m_theme = ""
            }
        }
        val button_down = findViewById<Button>(R.id.button_down)
        button_down.setOnClickListener {
            if(m_theme != "") {
                val idea_list = findViewById<ListView>(R.id.idea_list)
                adapter = ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    theme_list_items);
                idea_list.adapter = adapter

                val all = realm.where(IdeaStock::class.java).equalTo("theme",m_theme).findAll()
                if(all.size != 0) {
                    val big_theme_content = findViewById<TextView>(R.id.big_theme_content)
                    big_theme_content.setText(m_theme)

                    // ここは、下があるという事は、テーマとして登録されているという事なので、分岐は必要ない
                    val selected_theme_id = realm.where(IdeaStock::class.java).equalTo("big_theme_id",m_big_theme_id).equalTo("theme",m_theme).findFirst()
                    m_big_theme_id = selected_theme_id?.theme_id.toString()
                    m_big_theme = m_theme

                    theme_list_items.clear()

                    // テーマとしてだけ登録されている場合もあるので、このブロックは必要
                    val big_theme_to_thme = realm.where(IdeaStock::class.java).equalTo("big_theme_id",m_big_theme_id).findAll()
                    big_theme_to_thme.forEach { obj ->
                        if(obj.theme != "") {
                            theme_list_items.add(obj.theme)
                        }
                    }
                    val theme_to_idea = realm.where(IdeaStock::class.java).equalTo("theme_id",m_big_theme_id).findAll()
                    theme_to_idea.forEach { obj ->
                        if(obj.idea != "") {
                            theme_list_items.add(obj.idea)
                        }
                    }
                    var listItems_temp = ArrayList<String>()
                    listItems_temp.addAll(theme_list_items.distinct())
                    theme_list_items.clear()
                    theme_list_items.addAll(listItems_temp)

                    m_theme = ""
                }
            }
        }
        val button_theme_select = findViewById<Button>(R.id.button_theme_select)
        button_theme_select.setOnClickListener {
            if(m_theme != "") {
                val intent = Intent(this@IdeaListActivity, IdeaBurstActivity::class.java)
                intent.putExtra("m_big_theme", m_big_theme)
                intent.putExtra("m_big_theme_id", m_big_theme_id)
                intent.putExtra("m_theme", m_theme)
                // m_theme_idを設定しなければならない
                val selected_theme_id = realm.where(IdeaStock::class.java).equalTo("big_theme_id",m_big_theme_id).equalTo("theme",m_theme).findAll()
                if(selected_theme_id.size != 0){
                    m_theme_id = selected_theme_id?.first()?.theme_id.toString()
                }else{
                    val selected_theme_id = realm.where(IdeaStock::class.java).equalTo("theme_id",m_big_theme_id).equalTo("idea",m_theme).findFirst()
                    m_theme_id = selected_theme_id?.idea_id.toString()
                }
                intent.putExtra("m_theme_id", m_theme_id)
                startActivity(intent)
            }
        }
        val button_new = findViewById<Button>(R.id.button_new)
        button_new.setOnClickListener {
            val myedit = EditText(this)
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("テーマを入力してください")
            dialog.setView(myedit)
            dialog.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                if(!myedit.text.equals("")){
                    m_theme = myedit.text.toString()
                    var sameExistFlag = false
                    val big_theme_to_thme = realm.where(IdeaStock::class.java).equalTo("big_theme_id",m_big_theme_id).findAll()
                    big_theme_to_thme.forEach { obj ->
                        if (myedit.text.equals(obj.theme)) {
                            sameExistFlag = true
                        }
                    }
                    val theme_to_idea = realm.where(IdeaStock::class.java).equalTo("theme_id",m_big_theme_id).findAll()
                    theme_to_idea.forEach { obj ->
                        if (myedit.text.equals(obj.idea)) {
                            sameExistFlag = true
                        }
                    }
                    if(sameExistFlag == false){
                        var theme_id = UUID.randomUUID().toString()
                        realm.executeTransaction {
                            // ここでは、一旦、ヒントとアイデアはなしで格納する
                            // アイデア表示の際には、空を表示しないようにする
                            var new_theme =  IdeaStock()
                            new_theme.big_theme_id = m_big_theme_id
                            new_theme.big_theme = m_big_theme
                            new_theme.theme_id = theme_id
                            new_theme.theme = m_theme
                            realm.insert(new_theme)
                        }
                        val intent = Intent(this, IdeaBurstActivity::class.java)
                        intent.putExtra("m_big_theme", m_big_theme)
                        intent.putExtra("m_big_theme_id", m_big_theme_id)
                        intent.putExtra("m_theme",  m_theme)
                        intent.putExtra("m_theme_id",  theme_id)
                        startActivity(intent)
                    }
                }
            })
            dialog.setNegativeButton("キャンセル", null)
            dialog.show()

        }
        idea_list.setOnItemClickListener(OnItemClickListener { parent, view, position, id -> // クリックされたアイテムを取得する。
            m_theme = theme_list_items[id.toInt()]
        })
    }
    fun onResume(savedInstanceState: Bundle?) {

    }
}