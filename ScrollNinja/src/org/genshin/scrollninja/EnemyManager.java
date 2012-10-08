package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;

//========================================
// クラス宣言	
//========================================
//***** モノステート *****/
public class EnemyManager {
	// 変数宣言
	private static ArrayList<Enemy> enemyList;		// 敵リスト
	
	// コンストラクタ
	private EnemyManager() {
		enemyList = new ArrayList<Enemy>();
	}
	
	// 敵の生成
	public static int CreateEnemy(String Name) {
		if( enemyList.contains(Name) ) {		// 既にその名前が作られている場合はエラー
			return -1;		// エラー処理
		}
		
		Enemy pEnemy = new Enemy(Name);		// オブジェクトを生成（&初期化）して
		enemyList.add(pEnemy);				// リストに追加
		
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
		return enemyList.get(enemyList.indexOf(Name));				// 引数で渡されたオブジェクトのポインタを返す
	}
}
