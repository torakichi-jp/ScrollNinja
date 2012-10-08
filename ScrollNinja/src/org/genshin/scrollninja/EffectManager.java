package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;

//========================================
// クラス宣言	
//========================================
// ***** モノステート *****/
public class EffectManager {	
	// 変数宣言
	private static ArrayList<Effect> effectList;
	
	// コンストラクタ
	private EffectManager(){
		effectList = new ArrayList<Effect>();
	}
	
	// エフェクトの生成 
	public static int CreateEffect(String Name) {
		if( effectList.contains(Name) ) {		// 既にその名前が作られている場合はエラー
			return -1;		// エラー処理
		}
		
		Effect pEffect = new Effect(Name);		// オブジェクトを生成（&初期化）して
		effectList.add(pEffect);				// リストに追加
		
		return 1;
	}
	
	// エフェクトの削除
	public static int DeleteEnemy(String Name) {
		if( !effectList.contains(Name) ) {		// 名前が見つからなかった場合はエラー
			return -1;		// エラー処理
		}
		
		effectList.remove(effectList.indexOf(Name));		// 引数で渡されたオブジェクトを削除
		return 1;
	}
	
	// 参照
	public static Effect GetEnemy(String Name) {
		return effectList.get(effectList.indexOf(Name));	// 引数で渡されたオブジェクトのポインタを返す
	}
	
}
