package org.genshin.scrollninja;

import java.util.ArrayList;

import org.genshin.scrollninja.EnemyDataList.EnemyData;
import org.genshin.scrollninja.EnemyDataList.EnemyType;

import com.badlogic.gdx.math.Vector2;

public class StageDataList {
	public final static ArrayList<StageData> list = new ArrayList<StageData>();

	public StageDataList() {}

	public static void lead() {
		StageData data = new StageDataList().new StageData();

		// ステージ０設定
		data.playerPosition.set(0, 0);
		data.enemyType.add(0);
		data.enemyNum.add(1);
		//data.enemyType.add(1);
		//data.enemyNum.add(1);
		data.enemyPosition.add(new Vector2(20, 0));
		data.backgroundFileName.add("data/stage/00_far.png");
		data.backgroundFileName.add("data/stage/00_main.png");
		data.backgroundFileName.add("data/stage/00_near.png");
		data.backgroundBodyFileName = "data/stage/00.json";

		// data.musicFileName.add("data/");
		// data.goalPosition.set(0, 0);
		list.add(data);

		// ステージ１設定
		// data = new StageDataList().new StageData();

		data = null;
	}
	public class StageData {
		public Vector2 playerPosition = new Vector2();							// プレイヤーの出現位置
		public ArrayList<Integer> enemyType = new ArrayList<Integer>();			// 出現する敵の種類
		public ArrayList<Integer> enemyNum = new ArrayList<Integer>();			// 各種敵の数
		public ArrayList<Vector2> enemyPosition = new ArrayList<Vector2>();		// 各種敵の出現位置
		public ArrayList<String> backgroundFileName = new ArrayList<String>();	// 使用する背景のファイル名
		public String backgroundBodyFileName = "";								// 背景当たり判定ファイル名
		public ArrayList<Integer> objectType = new ArrayList<Integer>();		// 出現するオブジェクトの種類
		public ArrayList<Integer> objectNum = new ArrayList<Integer>();			// 各種オブジェクトの数
		public ArrayList<Vector2> objectPosition = new ArrayList<Vector2>();	// 各種オブジェクトの位置
		public ArrayList<String> musicFileName = new ArrayList<String>();		// 使用するBGMのファイル名
		public Vector2 goalPosition = new Vector2();							// ゴールの位置
		// public final ArrayList<Integer> dropItem = new ArrayList<Integer>();	// 落ちているアイテムの種類
		// public final ArrayList<Vector2> itemPosition = ArrayList<Vector2>();	// アイテムの出現位置
		// public final int eventNo;											// 発生イベントNo
	}
}