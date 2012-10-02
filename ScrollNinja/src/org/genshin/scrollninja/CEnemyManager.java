package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;

public class CEnemyManager {
	// 変数宣言
	private ArrayList<CEnemy> EnemyList;		// 敵リスト
	
	// コンストラクタ
	CEnemyManager(){
		EnemyList = new ArrayList<CEnemy>();
	}
	
	// 敵の生成
	public void CreateEnemy() {
		CEnemy pEnemy = new CEnemy();			// オブジェクトを生成（&初期化）して
		EnemyList.add(pEnemy);					// リストに追加
	}
	
	// 敵の削除
	public void DeleteEnemy(int i) {
		EnemyList.remove(i);					// 引数で渡されたオブジェクトを削除
	}
	
	// 参照
	public CEnemy GetEnemy(int i) {
		return EnemyList.get(i);				// 引数で渡されたオブジェクトのポインタを返す
	}
}
