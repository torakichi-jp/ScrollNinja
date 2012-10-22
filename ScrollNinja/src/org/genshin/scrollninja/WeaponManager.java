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
//	public static ArrayList<>

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
	public static WeaponBase CreateWeapon(CharacterBase owner, int type) {
		switch(type) {
		case KATANA:
			Katana pKatana = new Katana(owner, katanaList.size() + 1);
			katanaList.add(pKatana);
			return pKatana;
		case SHURIKEN:
			break;
		}
		
		return null;
	}
}