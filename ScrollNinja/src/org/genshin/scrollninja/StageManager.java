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
	private static StageBase	currentStage;		// 現在のステージ
	private static StageBase	nextStage;			// 次のステージ

	// コンストラクタ
	private StageManager(){}

	//************************************************************
	// Update
	// 現在のステージの更新処理
	//************************************************************
	public static void Update() {
		currentStage.Update();
	}
	
	//************************************************************
	// Draw
	// 現在のステージの描画処理
	//************************************************************
	public static void Draw() {
		currentStage.Draw();
	}

	//************************************************************
	// ChangeStage
	// ステージ遷移
	//************************************************************
	public static void ChangeStage(StageBase stage) {
		currentStage = stage;
	}
	
	public static StageBase GetNowStage() {
		return currentStage;
	}
}