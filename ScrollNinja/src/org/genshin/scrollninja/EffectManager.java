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
	private static ArrayList<Effect> effectList = new ArrayList<Effect>();

	// コンストラクタ
	private EffectManager(){}

	public static void Draw() {
		for( int i = 0; i < effectList.size(); i ++ ) {
			effectList.get(i).Draw();
		}
	}

	// エフェクトの生成
	public static void CreateEffect(int Type) {
		if (effectList == null)
			effectList = new ArrayList<Effect>();

 		Effect pEffect = new Effect(Type);	// オブジェクトを生成（&初期化）して
		effectList.add(pEffect);					// リストに追加
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

	public static Effect GetEffect(Effect current) {
		for( int i = 0; i < effectList.size(); i ++ ) {
			if( effectList.get(i).equals(current) )
				return effectList.get(i);
		}
		return null;
	}

	public static Effect GetEffectForLoop(int i) {
		return effectList.get(i);
	}

	public static int GetListSize() {
		return effectList.size();
	}

	public static void dispose() {
		if (effectList != null) {
			for (int i = 0; i < effectList.size(); i++) {
				effectList.get(i).Release();
			}
		}
		effectList = null;
	}
}