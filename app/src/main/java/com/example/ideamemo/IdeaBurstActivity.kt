package com.example.ideamemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_idea_burst.*
import pj.ekak.ideamemo.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class IdeaBurstActivity : AppCompatActivity() {

    lateinit var realm: Realm
    var m_big_theme = ""
    var m_big_theme_id = ""
    var m_theme = ""
    var m_theme_id = ""
    val hint_category_items = arrayOf(
        "シンプル_1",
        "オズボーン_1",
        "関係ないモノ_1"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idea_burst)
        Realm.init(this);
        val realmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        realm = Realm.getInstance(realmConfiguration)

        val intent = getIntent()
        m_big_theme = intent.getStringExtra("m_big_theme")
        m_big_theme_id = intent.getStringExtra("m_big_theme_id")
        m_theme = intent.getStringExtra("m_theme")
        m_theme_id = intent.getStringExtra("m_theme_id")
        println("---------------------------kake↓")
        println(m_big_theme)
        println(m_big_theme_id)
        println(m_theme)
        println(m_theme_id)
        println("---------------------------kake↑")
        val theme_content = findViewById<TextView>(R.id.theme_content)
        theme_content.setText(m_theme)

        var adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            hint_category_items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_hint_category.adapter = adapter
        var hint_list = arrayOf("➕", "➖", "✕","➗","分岐","逆")
        // ここが動的になる
        var num = Random.nextInt(hint_list.size)
        hint_content.setText(hint_list[num])

        // リスナーを登録
        spinner_hint_category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            //　アイテムが選択された時
            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?, position: Int, id: Long) {
                val spinnerParent = parent as Spinner
                val item = spinnerParent.selectedItem as String
                spinner_hint_category_text.text = item
            }

            //　アイテムが選択されなかった
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

        val button_input_and_random = findViewById<Button>(R.id.button_input_and_random)
        button_input_and_random.setOnClickListener {

            val input_text = findViewById<EditText>(R.id.input_text)
            val hint_text = findViewById<TextView>(R.id.hint_content)
            realm.executeTransaction {
                // ここでは、一旦、ヒントとアイデアはなしで格納する
                // アイデアのところで、入力しなくても、テーマが消えないため
                // アイデア表示の際には、空を表示しないようにする
                var new_idea =  IdeaStock()
                new_idea.big_theme_id = m_big_theme_id
                new_idea.big_theme = m_big_theme
                new_idea.theme_id = m_theme_id
                new_idea.theme = m_theme
                new_idea.hint = hint_text.text.toString()
                new_idea.idea = input_text.text.toString()
                new_idea.idea_id = UUID.randomUUID().toString()
                realm.insert(new_idea)
            }
            var temp = realm.where(HintStock::class.java).equalTo("category",spinner_hint_category_text.text.toString()).findAll()
            var hint_list_temp = ArrayList<String>()
            temp.forEach { obj ->
                hint_list_temp.add(obj.hint)
            }
            var num = Random.nextInt(hint_list_temp.size)
            hint_content.setText(hint_list_temp[num])

            // 入力された文字を消す
            input_text.setText("")
        }
        val button_return = findViewById<Button>(R.id.button_return)
        button_return.setOnClickListener {
            val intent = Intent(this@IdeaBurstActivity,IdeaListActivity::class.java)
            intent.putExtra("m_big_theme", m_big_theme)
            intent.putExtra("m_big_theme_id", m_big_theme_id)
            intent.putExtra("m_theme", m_theme)
            intent.putExtra("m_theme_id", m_theme_id)
            startActivity(intent)
        }
    }
}