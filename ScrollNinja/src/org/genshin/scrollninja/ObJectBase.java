package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

//========================================
// クラス宣言
//========================================
public class ObJectBase {
	protected ArrayList<Sprite> 	sprite;			// スプライト
	protected Body 					body;			// 当たり判定用BOX
	protected ArrayList<Fixture> 	sensor;			// センサー
	
	// コンストラクタ
	ObJectBase(){}
	
	//************************************************************
	// HitTest
	// 当たり判定まとめ
	//************************************************************
	
	
	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
/*	public Sprite GetSprite() { return sprite.get(1); }
	public Body GetBody() { return body; }
	public Fixture GetSensor() { return sensor.get(1); }
	*/
	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
/*	public void SetSprite( Sprite sp) { sprite.get(1).set(sp); }
	public void SetBody(Body bd) { body = bd; }*/
}
