package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;

//========================================
// クラス宣言
//========================================
public class StageObjectManager {
	private ArrayList<StageObject> stageObjectList;			// ステージオブジェクトリスト
	
	// コンストラクタ
	StageObjectManager() {
		stageObjectList = new ArrayList<StageObject>();
	}
	
	// ステージオブジェクトの生成
	public int CreateStageObject(String Name) {
		if( stageObjectList.contains(Name) ) {		// 既にその名前が作られている場合はエラー
			return -1;		// エラー処理
		}
		
		StageObject pStageObject = new StageObject(Name);		// オブジェクトを生成（&初期化）して
		stageObjectList.add(pStageObject);						// リストに追加
		
		return 1;
	}
	
	// ステージオブジェクトの削除
	public int DeleteStage(String Name) {
		if( !stageObjectList.contains(Name) ) {		// 名前が見つからなかった場合はエラー
			return -1;		// エラー処理
		}
		
		stageObjectList.remove(stageObjectList.indexOf(Name));		// 引数で渡されたオブジェクトを削除
		return 1;
	}
	
	// 参照
	public StageObject GetStage(String Name) {
		return stageObjectList.get(stageObjectList.indexOf(Name));	// 引数で渡されたオブジェクトのポインタを返す
	}
}
