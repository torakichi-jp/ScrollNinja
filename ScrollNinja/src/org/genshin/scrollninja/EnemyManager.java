package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.awt.dnd.Autoscroll;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

//========================================
// クラス宣言
//========================================
//***** モノステート *****/
public class EnemyManager {
	// 変数宣言
	private static ArrayList<Integer>	enemyList		= new ArrayList<Integer>();		// 敵リスト

	// デバッグのために公開にしてあります
	public static ArrayList<Enemy>		normalEnemyList	= new ArrayList<Enemy>();
	public static ArrayList<Enemy>		rareEnemyList	= new ArrayList<Enemy>();
	public static ArrayList<Enemy>		autoEnemyList	= new ArrayList<Enemy>();

	// コンストラクタ
	private EnemyManager() {}

	/**
	 * 更新
	 */
	public static void Update() {
		for(int i = 0; i < normalEnemyList.size(); i ++) {
			normalEnemyList.get(i).Update();
		}
	}

	/**
	 * スプライト描画
	 */
	public static void Draw() {
		for(int i = 0; i < normalEnemyList.size(); i ++) {
			normalEnemyList.get(i).Draw();
		}
	}

	/**
	 * 敵生成
	 * 同じ種類が既にある場合は最後に追加
	 * まだその種類がリストにない場合は新規で追加
	 */
	public static void CreateEnemy(int Type, float x, float y) {
		// 同じ種類の敵がないか探す
		for(int i = 0; i < enemyList.size(); i ++ ) {
			// 同じ種類発見
			if( enemyList.get(i).equals(Type) ) {

				switch( Type ) {
				case Enemy.NORMAL:
					int j = normalEnemyList.size() + 1;
					Enemy pEnemy = new Enemy(Type, j, x, y);	// 最後の番号を管理番号に
					normalEnemyList.add(pEnemy);				// 追加
				break;
				}

				return;
				}
			}
		// なかった
		enemyList.add(Type);						// この種類の項目を増やす
		Enemy pEnemy = new Enemy(Type, 1, x, y);	// 最初の一つ目なので管理番号は１

		switch( Type ) {
		case Enemy.NORMAL:
			normalEnemyList.add(pEnemy);					// 追加
			break;
		case Enemy.RARE:
			rareEnemyList.add(pEnemy);
			break;
		case Enemy.AUTO:
			autoEnemyList.add(pEnemy);
			break;
		}
	}

	/**
	 * 削除とソート
	 * @param Type		敵の種類
	 * @param Num		管理番号
	 */
	public static void DeleteEnemy(int Type, int Num) {
		for(int i = 0; i < enemyList.size(); i ++ ) {

			// 発見
			if( enemyList.get(i).equals(Type) ) {

				switch( Type ) {
				case Enemy.NORMAL:
					for( int j = 0; j < normalEnemyList.size(); j ++ ) {
						if( normalEnemyList.get(j).GetNum() == Num ) {
							normalEnemyList.get(j).Release();
							normalEnemyList.remove(j);					// 削除！
						}
					}

					// 削除に合わせて管理番号変更。とりあえず全部
					for( int j = 0; j < normalEnemyList.size(); j ++ ) {
						normalEnemyList.get(j).SetNum(j + 1);
					}
					break;

				case Enemy.RARE:
					break;
				case Enemy.AUTO:
					break;
				}
			}
		}
	}

	/**
	 * 削除とソート
	 * @param enemy		削除する敵のポインタ
	 */
	public static void Deleteenemy(Enemy enemy) {
		for(int i = 0; i < enemyList.size(); i ++ ) {

			// 発見
			if( enemyList.get(i).equals(enemy.GetType()) ) {

				switch( enemy.GetType() ) {
				case Enemy.NORMAL:
					for( int j = 0; j < normalEnemyList.size(); j ++ ) {
						if( normalEnemyList.get(j).GetNum() == enemy.GetNum() ) {
							normalEnemyList.get(j).Release();
							normalEnemyList.remove(j);					// 削除！
						}
					}

					// 削除に合わせて管理番号変更。とりあえず全部
					for( int j = 0; j < normalEnemyList.size(); j ++ ) {
						normalEnemyList.get(j).SetNum(j + 1);
					}
					break;
				case Enemy.RARE:
					break;
				case Enemy.AUTO:
					break;
				}
			}
		}
	}
}
