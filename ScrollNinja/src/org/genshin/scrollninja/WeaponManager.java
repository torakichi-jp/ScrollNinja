package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;

//========================================
// クラス宣言
//========================================
//***** モノステート *****/
public class WeaponManager {
	// 変数宣言
	public static ArrayList<Weapon> weaponList 		= new ArrayList<Weapon>();

	// コンストラクタ
	private WeaponManager(){}

	/**************************************************
	* CreateWeapon
	* 武器の生成
	**************************************************/
	// プレイヤーの武器
	public static int CreateWeapon(String Name, Player player, int type) {
		if( weaponList.contains(Name) ) {		// 既にその名前が作られている場合はエラー
			return -1;		// エラー処理
		}

		Weapon pWeapon = new Weapon(Name, player, type);	// オブジェクトを生成（&初期化）して
		weaponList.add(pWeapon);							// リストに追加

		return 1;
	}

	/**************************************************
	* DeleteWeapon DeleteEnemyWeapon
	* 武器の削除
	**************************************************/
	// プレイヤー
	public static int DeleteWeapon(String Name) {
		for(int i = 0; i < weaponList.size(); i ++) {
			if( weaponList.get(i).GetName().equals(Name) ) {
				weaponList.get(i).Release();
				weaponList.remove(i);		// 引数で渡されたオブジェクトを削除
				return 1;
			}
		}
		// 見つからなかったらエラーを返す
		return -1;
	}

	/**************************************************
	* GetWeapon
	* 参照
	**************************************************/
	public static Weapon GetWeapon(String Name) {
		for(int i = 0; i < weaponList.size(); i ++) {
			if( weaponList.get(i).GetName().equals(Name) ) {
				return weaponList.get(i);		// 引数で渡されたオブジェクトのポインタを返す
			}
		}
		return null;
	}
}