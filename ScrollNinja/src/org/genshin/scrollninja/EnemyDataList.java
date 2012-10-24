package org.genshin.scrollninja;

import java.util.ArrayList;

/**
 *
 */
public class EnemyDataList {
	// TODO 敵種類とりあえず適当

	public final static int NON_ACTIVE = 0;
	public final static int ACTIVE = 1;

	public final static ArrayList<EnemyData> list = new ArrayList<EnemyData>();

	public EnemyDataList() {}

	public static EnemyData lead(int id) {
		EnemyData data = new EnemyDataList().new EnemyData();

		// 敵１設定
		if (id == 0) {
			data.enemyFileName = "data/enemy.png";
			data.maxHp = 100;
			data.speed = 1;
			data.enemyMode = NON_ACTIVE;
			data.haveWeapon.add(0);			// TODO とりあえず手裏剣は０

			//list.add(data);
		}
		// 敵２設定
		if (id == 1) {
			//data = new EnemyDataList().new EnemyData();
			data.enemyFileName = "data/enemy.png";
			data.maxHp = 150;
			data.speed = 1;
			data.enemyMode = ACTIVE;
			data.haveWeapon.add(1);			// TODO とりあえず刀は１

			//list.add(data);
		}

		// 敵３設定
		//data = new EnemyDataList().new EnemyData();

		return data;
	}

	public class EnemyData {
		//public int enemyID;												// 種類
		public String enemyFileName;										// テクスチャファイル名
		public int maxHp;													// 最大HP
		public float speed;													// 速度（基準から掛けて使う）
		public int enemyMode;												// アクティブかノンアクティブか
		public ArrayList<Integer> haveWeapon = new ArrayList<Integer>();	// 所持武器
		// TODO
		// 落とすアイテムのIDと落とす確率も必要か
	}
}