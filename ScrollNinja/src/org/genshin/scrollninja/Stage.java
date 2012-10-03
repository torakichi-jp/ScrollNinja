package org.genshin.scrollninja;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Stage {
	private String name;
	
	// コンストラクタ
	public Stage(String Name){
		name = new String(Name);						// オブジェクト化と同時にステージ番号の代入
	}
	
	// 参照
	public Stage GetStage() {
		return this;
	}
}
