package com.example.ideamemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import pj.ekak.ideamemo.R


class MainActivity : AppCompatActivity() {

    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {

        Realm.init(this);
        val realmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        realm = Realm.getInstance(realmConfiguration)

        // 一旦全部DBを消す
        //　リリースのためfalse
        if(false) {
            realm.executeTransaction {
                realm.delete(IdeaStock::class.java)
                realm.delete(HintStock::class.java)
            }
        }
        // DBへの入力はここで行う
        // シンプル
        // オズボーンのチェックリスト
        // 関係ないモノ
        // の3つは、デフォルトで入れる
        // true　flaseで切り替えるのではなく、
        // DBがNULLだったら追加にしよう
        val all = realm.where(HintStock::class.java).findAll()
        if(all.size == 0){
            // シンプル
            val array_simple = arrayOf("＋", "－", "×", "÷", "逆", "分岐" )
            array_simple.forEach { obj ->
                realm.executeTransaction {
                    // ここでは、一旦、ヒントとアイデアはなしで格納する
                    // アイデアのところで、入力しなくても、テーマが消えないため
                    // アイデア表示の際には、空を表示しないようにする
                    var new_hint =  HintStock()
                    new_hint.category = "シンプル_1"
                    new_hint.hint = obj
                    realm.insert(new_hint)
                }
            }
            // オズボーンのチェックリスト
            val array_oz_check = arrayOf("他に使い道は？", "応用できないか？", "修正したら？", "拡大したら？", "縮小したら？", "代用したら？", "アレンジし直したら？", "逆にしたら？", "組み合わせたら？")
            array_oz_check.forEach { obj ->
                realm.executeTransaction {
                    // ここでは、一旦、ヒントとアイデアはなしで格納する
                    // アイデアのところで、入力しなくても、テーマが消えないため
                    // アイデア表示の際には、空を表示しないようにする
                    var new_hint =  HintStock()
                    new_hint.category = "オズボーン_1"
                    new_hint.hint = obj
                    realm.insert(new_hint)
                }
            }
            // 関係ないモノ
            val array_Irrelevant_obj = arrayOf("パンチ", "住居", "スタジアム", "交差点", "義手", "レジ", "風呂", "機関車", "ランチ", "土偶", "畑", "石器", "おにぎり", "昆虫", "ドリル", "ボトル", "フロント", "服装", "たんぽぽ", "ひとで", "スープ", "屋根裏", "手袋", "プリン", "シェア", "タイタニック", "動物園", "らくだ", "税金", "税務署", "モーター", "ミニ四駆", "ガンディスキー", "紅の豚", "ヴァイオリン", "アンモニア", "書籍", "フットサル", "怪物", "抽象画", "円", "鍬", "キャンディ", "切手", "灰皿", "バンジージャンプ", "犬と猫", "砂遊び", "ミシン", "ネズミ", "樋", "仮面ライダー", "ライター", "海", "野原", "メニュー", "敷物", "中華鍋", "太陽のコロナ", "ヒップアタック", "サンショウウオ", "銃", "インデックス", "上流", "エンディミオンシリーズ", "宇宙船", "季節", "ペスト", "バルブ", "SF", "水槽", "かき氷", "ポスター", "三角形", "辞書", "スーツケース", "金銭", "電話", "接着剤", "埃", "通路", "サーモスタット", "クリアファイル", "シナリオ", "魚拓", "かき氷", "おいしい水", "ナポレオン", "ミルクボーイ", "漫才", "ロビー", "ランダム", "とびうお", "鐘の音", "ビン", "和太鼓", "競馬", "大王イカ", "雲", "図書館", "ビデオデッキ", "ノート", "ネオン", "兆候", "潮流", "煙", "火山", "大学", "ステレオ", "サバの缶詰", "粘土", "かまいたち", "ファン", "裁判所", "板チョコ", "マッチングサイト", "恐竜", "クジラ", "ハンドル", "チップキック", "100M走", "ティーバック", "チョコレート", "ピラミッド", "十字路", "蒸気", "ヌードル", "外套", "カートリッジ", "カボチャ", "恐怖", "ネットワーク", "わさび", "イースター", "大根", "葉巻", "妖精", "配管工", "ハンバーガー", "リフティング", "傷跡", "動画編集", "装飾品", "シャトル", "方程式", "社会福祉", "錨", "ダンサー", "頭皮", "病気", "小型ジェット", "ワセリン", "カリフラワー", "ほうれん草", "掲示版", "卵の殻", "迷路", "ネズミ", "配当", "野火", "肝試し", "予報", "プラム", "平和部隊", "笑い", "病院", "漫才", "硫黄", "格子", "放物線", "鹿児島", "脇役", "透明", "ワニ皮", "牧場", "ボードゲーム", "底", "かくれんぼ", "ロボット工学", "掃除機", "ブレーキ", "コブラ", "戦士", "国土航空局", "ハワイ", "接触", "エンジニア", "いわし", "空洞", "かくれんぼ", "くまもん", "縞模様", "ゆりかご", "オリンピック", "レバー", "インコ", "カエデ", "かさぶた", "埋め立て地", "大牧場", "アルファベット", "お寿司", "盾", "沖縄", "教室", "刑事ドラマ", "賃金アップ", "アニメーター", "レタスの千切り", "ハサミ", "燃料", "排泄物", "EU", "休暇", "エメラルド", "トナカイ", "砂丘", "日本", "蔓", "統計学者", "だんご", "ダイヤル", "ダンボール", "針金工作", "エクセル", "スケート", "双眼鏡" )
            array_Irrelevant_obj.forEach { obj ->
                realm.executeTransaction {
                    // ここでは、一旦、ヒントとアイデアはなしで格納する
                    // アイデアのところで、入力しなくても、テーマが消えないため
                    // アイデア表示の際には、空を表示しないようにする
                    var new_hint =  HintStock()
                    new_hint.category = "関係ないモノ_1"
                    new_hint.hint = obj
                    realm.insert(new_hint)
                }
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button_idea_output = findViewById<Button>(R.id.button_idea_output)
        button_idea_output.setOnClickListener {

            val intent = Intent(this@MainActivity, IdeaListActivity::class.java)
            intent.putExtra("m_big_theme", "")
            intent.putExtra("m_big_theme_id", "")
            startActivity(intent)
        }
        val button_hint_add = findViewById<Button>(R.id.button_hint_add)
        button_hint_add.setOnClickListener {
            val intent = Intent(this@MainActivity, HintAddActivity::class.java)
            startActivity(intent)
        }
    }
}