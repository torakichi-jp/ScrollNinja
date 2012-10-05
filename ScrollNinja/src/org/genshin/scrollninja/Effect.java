//******************************
//	Effect.java 
//******************************

package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

//========================================
// クラス宣言
//========================================
public class Effect {
	// 定数宣言

	//変数宣言
	private String		name;		// 名前
	private int 		effectTime;	// 効果時間

	//コンストラクタ
	public Effect(String Name) {
		name		= new String(Name);
		effectTime	= 0;
	}
		
	

	//************************************************************
	// animation
	// アニメーション処理
	//************************************************************
	public void animation() {
	}
	
	public int GetEffectTime() {
		return effectTime;
	}
	
	// 
	public void play() {
		
	}
	
}

