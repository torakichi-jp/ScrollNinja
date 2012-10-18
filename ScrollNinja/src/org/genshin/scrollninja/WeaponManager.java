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
	public static ArrayList<Weapon> weaponList = new ArrayList<Weapon>();

	// コンストラクタ
	private WeaponManager(){
		//weaponList = new ArrayList<Weapon>();
	}

	// 武器の生成
	public static int CreateWeapon(String Name) {
		if( weaponList.contains(Name) ) {		// 既にその名前が作られている場合はエラー
			return -1;		// エラー処理
		}

		Weapon pWeapon = new Weapon(Name);			// オブジェクトを生成（&初期化）して
		weaponList.add(pWeapon);					// リストに追加

		return 1;
	}

	// 武器の削除
	public static int DeleteWeapon(String Name) {
		for(int i = 0; i < weaponList.size(); i ++) {
			if( weaponList.get(i).GetName().equals(Name) ) {
				weaponList.get(i).Release();
				weaponList.remove(i);		// 引数で渡されたオブジェクトを削除
				return 1;
			}
		}
		/*
		if( !weaponList.contains(Name) ) {		// 名前が見つからなかった場合はエラー
			return -1;		// エラー処理
		}
		System.out.println("in");
		weaponList.get(weaponList.indexOf(Name)).Release();
		weaponList.remove(weaponList.indexOf(Name));		// 引数で渡されたオブジェクトを削除
		*/
		return -1;
	}

	// 参照
	public static Weapon GetWeapon(String Name) {


		for(int i = 0; i < weaponList.size(); i ++) {
			if( weaponList.get(i).GetName().equals(Name) ) {
				return weaponList.get(i);			// 引数で渡されたオブジェクトのポインタを返す
			}
		}
		return null;


		//return weaponList.get(weaponList.indexOf(Name));	// 引数で渡されたオブジェクトのポインタを返す
	}
}
