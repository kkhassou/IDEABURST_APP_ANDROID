package com.example.ideamemo

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_idea_burst.*
import pj.ekak.ideamemo.R

class HintAddActivity : AppCompatActivity() {

    // 動的に書き換える
    val hint_category_items = ArrayList<String>()

    var m_spinner_index = 0
    var m_spinner_max = 0
//        arrayOf(
//        "シンプル_1",
//        "オズボーン_1",
//        "関係ないモノ_1"
//    )

    lateinit var realm: Realm
    var hint_list_items = ArrayList<String>()
    var hint_list_adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hint_add)

        Realm.init(this);
        val realmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        realm = Realm.getInstance(realmConfiguration)

        // リストの表示
        //////////////////////////////////////////////////////////////////////
        val hint_list = findViewById<ListView>(R.id.hint_list)

        hint_list_adapter = ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1,
            hint_list_items);
        hint_list.adapter = hint_list_adapter

        val hint_list_db = realm.where(HintStock::class.java).equalTo("category","シンプル_1").findAll()
        hint_list_db.forEach { obj ->
            hint_list_items.add(obj.hint)
        }
        var hint_list_items_temp = ArrayList<String>()
        hint_list_items_temp.addAll(hint_list_items.distinct())
        hint_list_items.clear()
        hint_list_items.addAll(hint_list_items_temp)
        //////////////////////////////////////////////////////////////////////
        // スピナーの表示
        //////////////////////////////////////////////////////////////////////
        val hint_category_db = realm.where(HintStock::class.java).findAll()
        hint_category_db.forEach { obj ->
            hint_category_items.add(obj.category)
        }
        var hint_category_items_temp = ArrayList<String>()
        hint_category_items_temp.addAll(hint_category_items.distinct())
        hint_category_items.clear()
        m_spinner_max = hint_category_items_temp.size
        hint_category_items.addAll(hint_category_items_temp)
        var hint_category_spinner_adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            hint_category_items
        )
        hint_category_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_hint_category.adapter = hint_category_spinner_adapter
        //////////////////////////////////////////////////////////////////////
        // リスナーを登録
        spinner_hint_category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            //　アイテムが選択された時
            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?, position: Int, id: Long) {
                val spinnerParent = parent as Spinner
                val item = spinnerParent.selectedItem as String
                spinner_hint_category_text.text = item
                m_spinner_index = position
                // リストの表示
                //////////////////////////////////////////////////////////////////////
                hint_list.adapter = hint_list_adapter

                hint_list_items.clear()

                val all = realm.where(HintStock::class.java).equalTo("category",item).findAll()
                all.forEach { obj ->
                    hint_list_items.add(obj.hint)
                }
                var hint_list_items_temp = ArrayList<String>()
                hint_list_items_temp.addAll(hint_list_items.distinct())
                hint_list_items.clear()
                hint_list_items.addAll(hint_list_items_temp)
                //////////////////////////////////////////////////////////////////////
            }

            //　アイテムが選択されなかった
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }
        val button_hint_add = findViewById<Button>(R.id.button_hint_add)
        button_hint_add.setOnClickListener {
            // ヒントを1つ1つ追加する
            // 今、表示されているものに追加する
            // ポップアップしてテキストを入力
            // 入力したものが重複していないかだけ確認してリストに追加
            val myedit = EditText(this)
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("追加のヒントを入力してください")
            dialog.setView(myedit)
            dialog.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                if(!myedit.text.equals("")){
                    var add_hint_category = spinner_hint_category_text.text.toString()
                    var add_hint = myedit.text.toString()
                    var sameExistFlag = false
                    val all = realm.where(HintStock::class.java).equalTo("category",add_hint_category).findAll()
                    all.forEach { obj ->
                        if (add_hint.equals(obj.hint)) {
                            sameExistFlag = true
                        }
                    }
                    if(sameExistFlag == false){
                        realm.executeTransaction {
                            var new_hint =  HintStock()
                            new_hint.category = add_hint_category
                            new_hint.hint = add_hint
                            realm.insert(new_hint)
                        }
                        // リストの表示
                        //////////////////////////////////////////////////////////////////////
                        hint_list.adapter = hint_list_adapter

                        val all = realm.where(HintStock::class.java).equalTo("category",add_hint_category).findAll()
                        all.forEach { obj ->
                            hint_list_items.add(obj.hint)
                        }
                        var hint_list_items_temp = ArrayList<String>()
                        hint_list_items_temp.addAll(hint_list_items.distinct())
                        hint_list_items.clear()
                        hint_list_items.addAll(hint_list_items_temp)
                        //////////////////////////////////////////////////////////////////////
                    }
                }
            })
            dialog.setNegativeButton("キャンセル", null)
            dialog.show()
        }
        val button_hint_category_add = findViewById<Button>(R.id.button_hint_category_add)
        button_hint_category_add.setOnClickListener {
            // ヒントカテゴリを追加する
            // ポップアップしてテキストを入力
            // 入力したものが重複していないかだけ確認してリストに追加
            val myedit = EditText(this)
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("追加のヒントカテゴリを入力してください")
            dialog.setView(myedit)
            dialog.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                if(!myedit.text.equals("")){
                    var add_hint_category = myedit.text.toString()
                    var sameExistFlag = false
                    val all = realm.where(HintStock::class.java).findAll()
                    all.forEach { obj ->
                        if (add_hint_category.equals(obj.category)) {
                            sameExistFlag = true
                        }
                    }
                    if(sameExistFlag == false){
                        realm.executeTransaction {
                            // ここでは、一旦、ヒントはなしでカテゴリだけで格納する
                            var new_hint =  HintStock()
                            new_hint.category = add_hint_category
                            realm.insert(new_hint)
                        }
                        // この下かなり難易度高い。初めて、スピナーの更新を行う
                        // スピナーの表示
                        //////////////////////////////////////////////////////////////////////

                        val hint_category_db = realm.where(HintStock::class.java).findAll()
                        hint_category_db.forEach { obj ->
                            hint_category_items.add(obj.category)
                        }
                        var hint_category_items_temp = ArrayList<String>()
                        hint_category_items_temp.addAll(hint_category_items.distinct())
                        hint_category_items.clear()
                        hint_category_items.addAll(hint_category_items_temp)
                        var hint_category_spinner_adapter = ArrayAdapter(
                            applicationContext,
                            android.R.layout.simple_spinner_item,
                            hint_category_items
                        )
                        hint_category_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner_hint_category.adapter = hint_category_spinner_adapter

                        val spinner_hint_category = findViewById<Spinner>(R.id.spinner_hint_category)
                        spinner_hint_category.setSelection(m_spinner_max);
                    }
                }
            })
            dialog.setNegativeButton("キャンセル", null)
            dialog.show()
        }
        val button_retrun = findViewById<Button>(R.id.button_return)
        button_retrun.setOnClickListener {
            val intent = Intent(this@HintAddActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}