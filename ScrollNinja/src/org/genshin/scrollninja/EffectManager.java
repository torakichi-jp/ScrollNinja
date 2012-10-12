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
	private static ArrayList<Effect> effectList = new ArrayList<Effect>();;
	
	// コンストラクタ
	private EffectManager(){}
	
	// エフェクトの生成
	public static void CreateEffect(int Type) {
		Effect pEffect = new Effect(Type);		// オブジェクトを生成（&初期化）して
		effectList.add(pEffect);				// リストに追加
	}
	
	// エフェクトの削除
	public static void DeleteEffect(int Type) {
		effectList.remove(effectList.indexOf(Type));		// 引数で渡されたオブジェクトを削除
	}
	
	// 参照
	public static Effect GetEffect(int Type) {
		for(int i = 0; i < effectList.size(); i ++) {
			if( effectList.get(i).GetType() == Type ) {
				return effectList.get(i);			// 引数で渡されたオブジェクトのポインタを返す
			}
		}
		return null;
	}
	
	public static int GetSize() {
		return effectList.size();
	}
}
