package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class PlayerManager {
	private static ArrayList<Player> playerList = new ArrayList<Player>();

	private PlayerManager(){}

	/**
	 * 更新
	 */
	public static void Update() {
		for( int i = 0; i < playerList.size(); i ++) {
			playerList.get(i).Update();
		}
	}

	/**
	 * スプライト描画
	 */
	public static void Draw() {
		for( int i = 0; i < playerList.size(); i ++) {
			playerList.get(i).Draw();
		}
	}

	/**
	 * プレイヤー生成
	 * @param x		初期座標X
	 * @param y		初期座標Y
	 */
	public static void CreatePlayer(Vector2 position) {
		// 生成した順に管理番号付与
		Player pPlayer = new Player(playerList.size() + 1);
		playerList.add(pPlayer);						// 追加
	}

	/**
	 * プレイヤー参照
	 * @param i		管理番号
	 * @return
	 */
	public static Player GetPlayer( int i) {
		return playerList.get(i);
	}
}
