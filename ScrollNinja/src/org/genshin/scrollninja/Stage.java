

package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Stage {

	private String							name;
	private Vector2							size;
	private Sprite 							sprite;

	// コンストラクタ
	public Stage(String Name){
		name = new String(Name);
	}
	
	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void Update(Player player) {
		PopEnemy(player);
		
		
	}

	//************************************************************
	// PopEnemy
	// 敵の出現タイミングの設定
	//************************************************************
	public void PopEnemy(Player player) {
		if( player.GetPosition().x > 200 ) {
			EnemyManager.CreateEnemy("ざこ", 0, 200.0f, 300.0f);
		}
	}

	// アイテムポップ
	public void popItem() {

	}

	public void moveBackground() {
		
	}

	public Player spawnPlayer(Player player) {
		return player;
	}

	public int timeCount(int nowTime) {
		// 制限時間 or 経過時間加算
		nowTime += 1;

		return nowTime;
	}
	
	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public Stage GetStage() { return this; }
	public String GetName(){ return name; }
	
	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************

}
