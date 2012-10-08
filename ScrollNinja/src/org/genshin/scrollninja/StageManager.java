package org.genshin.scrollninja;

//========================================
//インポート
//========================================
import java.util.ArrayList;

//========================================
// クラス宣言
//========================================
//***** モノステート *****/
public class StageManager {
	private static ArrayList<Stage> stageList;				// ステージリスト

	// コンストラクタ
	private StageManager(){
		stageList = new ArrayList<Stage>();
	}

	// 更新
	public static int Update(String Name) {
		if( !stageList.contains(Name) ) {		// 名前が見つからなかった場合はエラー
			return -1;		// エラー処理
		}

		stageList.get(stageList.indexOf(Name)).moveBackground();

		return 1;
	}

	// ステージの生成
	public static int CreateStage(String Name) {
		if( stageList.contains(Name) ) {		// 既にその名前が作られている場合はエラー
			return -1;		// エラー処理
		}

		Stage pStage = new Stage(Name);			// オブジェクトを生成（&初期化）して
		stageList.add(pStage);					// リストに追加

		return 1;
	}

	// ステージの削除
	public static int DeleteStage(String Name) {
		if( !stageList.contains(Name) ) {		// 名前が見つからなかった場合はエラー
			return -1;		// エラー処理
		}

		stageList.remove(stageList.indexOf(Name));		// 引数で渡されたオブジェクトを削除
		return 1;
	}

	// 参照
	public static Stage GetStage(String Name) {
		return stageList.get(stageList.indexOf(Name));				// 引数で渡されたオブジェクトのポインタを返す
	}
}
