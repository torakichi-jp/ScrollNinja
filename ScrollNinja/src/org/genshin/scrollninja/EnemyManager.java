package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

//========================================
// クラス宣言	
//========================================
//***** モノステート *****/
public class EnemyManager {
	// 変数宣言
	private static ArrayList<Enemy> enemyList		= new ArrayList<Enemy>();		// 敵リスト
	
	// コンストラクタ
	private EnemyManager() {}
	
	// 更新
	public static void Update(World world) {
		for(int i = 0; i < enemyList.size(); i ++) {
			enemyList.get(i).Update(world);
		}
	}
	
	// 敵の生成
	public static int CreateEnemy(String Name, int type, Vector2 pos) {
		if( enemyList.contains(Name) ) {		// 既にその名前が作られている場合はエラー
			return -1;		// エラー処理
		}
		
		Enemy pEnemy = new Enemy(Name, type, pos);	// オブジェクトを生成（&初期化）して
		enemyList.add(pEnemy);						// リストに追加
		
		return 1;
	}
	
	public static int CreateEnemy(String Name, int type, float x, float y) {
		if( enemyList.contains(Name) ) {		// 既にその名前が作られている場合はエラー
			return -1;		// エラー処理
		}
		
		Enemy pEnemy = new Enemy(Name, type, x, y);	// オブジェクトを生成（&初期化）して
		enemyList.add(pEnemy);						// リストに追加
		
		return 1;
	}
	
	// 敵の削除 
	public static int DeleteEnemy(String Name) {
		if( !enemyList.contains(Name) ) {		// 名前が見つからなかった場合はエラー
			return -1;		// エラー処理
		}
		
		enemyList.remove(enemyList.indexOf(Name));		// 引数で渡されたオブジェクトを削除
		return 1;
	}
	
	// 参照
	public static Enemy GetEnemy(String Name) {
		for(int i = 0; i < enemyList.size(); i ++) {
			if( enemyList.get(i).GetName().equals(Name) ) {
				return enemyList.get(i);			// 引数で渡されたオブジェクトのポインタを返す
			}
		}
		return null;
	}
}
