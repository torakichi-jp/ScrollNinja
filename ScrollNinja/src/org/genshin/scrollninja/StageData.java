package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class StageData {
	private final int stageNum = 1;		// ステージ数
	public final ArrayList<StageData> StageDatas = new ArrayList<StageData>(stageNum);	// 各ステージのデータ

	private final Vector2 playerPosition = new Vector2();							// プレイヤーの出現位置
	private final ArrayList<Integer> enemyType = new ArrayList<Integer>();			// 出現する敵の種類
	private final ArrayList<Integer> enemyNum = new ArrayList<Integer>();			// 出現する敵の数
	private final ArrayList<Vector2> enemyPosition = new ArrayList<Vector2>();		// 敵の出現位置
	private final ArrayList<String> backgroundFileName = new ArrayList<String>();	// 使用する背景のファイル名
	private final ArrayList<String> musicFileName = new ArrayList<String>();		// 使用するBGMのファイル名
	private final Vector2 goalPosition = new Vector2();								// ゴールの位置
	// private final ArrayList<Integer> dropItem = new ArrayList<Integer>();		// 落ちているアイテムの種類
	// private final ArrayList<Vector2> itemPosition = new ArrayList<Vector2>();	// アイテムの出現位置
	// private final int eventNo;													// 発生イベントNo

	public StageData() {
		StageDatas.get(0).playerPosition.x = 0;
		StageDatas.get(0).playerPosition.y = 0;

	}



}