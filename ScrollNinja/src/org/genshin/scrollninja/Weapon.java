//******************************
//	Weapon.java
//******************************

package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Weapon {

<<<<<<< HEAD
	private int			m_WeaponNum;		// 武器番号
	private Sprite		m_Sprite;			// スプライト
	private Vector2 	m_Position;			// 武器座標
	private float 		m_AttackNum;		// 武器威力
	private int 		m_WeaponLevel;		// 武器レベル
	private Boolean 	m_Use;				// 使用フラグ
	
	//コンストラクタ
	public Weapon(int i) {
		this.m_Position = new Vector2(0,0);
		this.m_AttackNum = 1;
		this.m_WeaponLevel = 1;
		m_WeaponNum = i;
=======
	
	private Sprite	sprite;		// スプライト
	private Vector2 	position;		// 武器座標
	private float 	attackNum;		// 武器威力
	private int 		weaponLevel;	// 武器レベル
	private Boolean 	use;			// 使用フラグ
	private Body		body;			// ボディ
	

	//コンストラクタ
	public Weapon() {
		this.position    = new Vector2(0,0);
		this.attackNum   = 0;
		this.weaponLevel = 0;
		this.use         = true;
>>>>>>> kimura/master
	}

	//武器座標ゲット
	public Vector2 GetWeaponPos() {
		return this.position;
	}
	
	//武器威力ゲット
	public float GetAttackNum() {
		return this.attackNum;
	}


	//武器レベルゲット
	public int GetWeaponLv() {
		return this.GetWeaponLv();
	}
	
	
}






