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

	// コンストラクタ
	private StageManager(){}

	//************************************************************
	// Update
	// 現在のステージの更新
	//************************************************************
	public static void Update(Player player) {
		nowStage.Update(player);
	}

	//************************************************************
	// StageTrance
	// ステージ遷移
	//************************************************************
	public static void StageTrance(Stage stage) {
		nowStage = stage;
	}
}
