package org.genshin.scrollninja;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public interface StageBase {
	
	// 更新関数
	public void Update();
	
	// 描画関数
	public void Draw();
	
	// 初期化関数
	public void Init();
	
	// 開放関数
	public void Release();
	
}
