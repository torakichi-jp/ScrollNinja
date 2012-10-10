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
	private static StageBase	nowStage;			// 現在のステージ
	private static StageBase	nextStage;			// 次のステージ

	// コンストラクタ
	private StageManager(){}

	//************************************************************
	// Update
	// 現在のステージの更新処理
	//************************************************************
	public static void Update() {
		nowStage.Update();
	}
	
	//************************************************************
	// Draw
	// 現在のステージの描画処理
	//************************************************************
	public static void Draw() {
		nowStage.Draw();
	}

	//************************************************************
	// StageTrance
	// ステージ遷移
	//************************************************************
	public static void StageTrance(Stage stage) {
		nowStage = stage;
	}
}