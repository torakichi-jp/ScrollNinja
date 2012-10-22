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
	// 定数宣言
	public static final int KATANA		= 0;
	public static final int SHURIKEN	= 1;
	
	// 変数宣言
	public static ArrayList<Katana> katanaList		= new ArrayList<Katana>();
	public static ArrayList<Weapon> weaponList 		= new ArrayList<Weapon>();

	/**
	 * コンストラクタ
	 */
	private WeaponManager(){}
	
	public static void Update() {
		for( int i = 0; i < katanaList.size(); i ++ ) {
			katanaList.get(i).Update();
		}
	}
	
	/**
	 * 武器生成
	 * @param owner			使用者			
	 * @param type			種類
	 */
	public static void CreateWeapon(CharacterBase owner, int type) {
		switch(type) {
		case KATANA:
			Katana pKatana = new Katana(owner, katanaList.size() + 1);
			katanaList.add(pKatana);
			 break;
		case SHURIKEN:
			break;
		}
	}

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