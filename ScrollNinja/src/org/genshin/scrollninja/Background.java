package org.genshin.scrollninja;

import java.awt.Point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Background extends ObJectBase{
	
	private static final Background Instance = new Background();			// このクラスの唯一のインスタンスを作ります
	
	// インスタンスを返す
	public static Background GetInstace() {
		return Instance;
	}
	
	private float zIndex;		// Zインデックス
	
	// コンストラクタ
	public Background() {
		zIndex = 0.0f;
	}
	
	public void LoadTexture() {
		
		float w = Gdx.graphics.getWidth();
		
		Texture texture = new Texture(Gdx.files.internal("data/stage_near_test.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// テクスチャ範囲
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 0, 2048, 2048);
		// 背景スプライトにセット
		sprite = new Sprite(tmpRegion);
		sprite.setPosition(-(w / 2), -1024);
		
		// 背景（奥）テクスチャ読み込み
		texture = new Texture(Gdx.files.internal("data/stage_far_test.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmpRegion = new TextureRegion(texture, 0, 0, 1024, 1024);
		
	}
	
}