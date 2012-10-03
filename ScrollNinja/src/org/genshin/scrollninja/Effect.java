//******************************
//	Effect.java 
//******************************

package org.genshin.scrollninja;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Effect extends Weapon{
	
	//変数宣言
	private String 	Name;		// 名前
	private int 		EffectTime;	// 効果時間
	private Sprite 	Sprite;		// スプライト
	//private Vector2 	m_Position;	// 座標
	
	
	//コンストラクタ
	public Effect()
	{
		this.Name       = "";
		this.EffectTime = 5;
	}
		
	

	//アニメーション
	public void EffectAnimation()
	{
		//-------------------------
		// 画像切り替え
		//-------------------------
		
	}
	
	public int GetEffectTime()
	{
		return this.EffectTime;
	}
	
	// 
	public void play()
	{
		
	}
	
}
