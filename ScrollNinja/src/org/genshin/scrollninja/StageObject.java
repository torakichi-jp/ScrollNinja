package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

//========================================
// クラス宣言
//========================================
public class StageObject extends ObJectBase {
	private String		name;			// 名前
	private Vector2		position;		// 座標
	
	// コンストラクタ
	StageObject(String Name) {
		name		= new String(Name);
		position	= new Vector2(0,0);
	}
	
	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public StageObject GetStageObject() { return this; }
	public String GetName(){ return name; }
	
	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
}
