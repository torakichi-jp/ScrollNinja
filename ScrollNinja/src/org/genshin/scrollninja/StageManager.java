package org.genshin.scrollninja;

//========================================
//インポート
//========================================
import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.World;

//========================================
// クラス宣言
//========================================
//***** モノステート *****/
public class StageManager {
	private static Stage nowStage;			// 現在のステージ
	private static Stage nextStage;			// 次のステージ
/*
	// コンストラクタ
<<<<<<< HEAD
	private StageManager(){}

	//************************************************************
	// Update
	// 現在のステージの更新
	//************************************************************
	public static void Update(Player player) {
		nowStage.Update(player);
=======
	private StageManager(){
		stageList = new ArrayList<Stage>();
	}

	// 更新
	public static int Update(String Name) {
		if( !stageList.contains(Name) ) {		// 名前が見つからなかった場合はエラー
			return -1;		// エラー処理
		}

		//stageList.get(stageList.indexOf(Name)).moveBackground();

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
>>>>>>> kimura/master
	}

	//************************************************************
	// StageTrance
	// ステージ遷移
	//************************************************************
	public static void StageTrance(Stage stage) {
		nowStage = stage;
	}
	*/
}
