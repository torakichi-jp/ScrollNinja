//******************************
//	Weapon.java 
//******************************

package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Weapon {

	
	private Texture 	m_Texture;			// テクスチャー
	private Sprite	m_Sprite;			// スプライト
	private Vector2 	m_Position;		// 武器座標
	private float 	m_AttackNum;		// 武器威力
	private int 		m_WeaponLevel;	// 武器レベル
	private Boolean 	m_Use;				// 使用フラグ
	
	//コンストラクタ
	public Weapon()
	{
		this.m_Position = new Vector2(0,0);
		this.m_AttackNum = 1;
		this.m_WeaponLevel = 1;
	}
	
	//武器座標ゲット
	public Vector2 GetWeaponPos()
	{
		return this.m_Position;
	}
	

}
