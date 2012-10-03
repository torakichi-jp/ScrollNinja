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

	
	private Sprite	Sprite;		// スプライト
	private Vector2 	Position;		// 武器座標
	private float 	AttackNum;		// 武器威力
	private int 		WeaponLevel;	// 武器レベル
	private Boolean 	Use;			// 使用フラグ
	private Body		Body;			// ボディ
	
	//コンストラクタ
	public Weapon()
	{
		this.Position    = new Vector2(0,0);
		this.AttackNum   = 1;
		this.WeaponLevel = 1;
		this.Use         = true;
	}
	
	//武器座標ゲット
	public Vector2 GetWeaponPos()
	{
		return this.Position;
	}
	
	//武器威力ゲット
	public float GetAttackNum()
	{
		return this.AttackNum;
	}

	//武器レベルゲット
	public int GetWeaponLv()
	{
		return this.GetWeaponLv();
	}
	
	
}






