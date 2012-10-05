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
		Texture texture = new Texture(Gdx.files.internal("data/stage_near_test.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// テクスチャ範囲
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 0, 2048, 2048);
		// 背景スプライトにセット
		sprite = new Sprite(tmpRegion);
		// 中心
		//stageSpr.setOrigin(stageSpr.getWidth() / 2, stageSpr.getHeight() / 2);
		// 0,0 だと画面の中央に背景画像の左下が設置されるため調整
		// 画面下の方が空白なので高さ位置はどう出したものかと…
		sprite.setPosition(-(Gdx.graphics.getWidth() / 2), -1024);

		// 背景（奥）テクスチャ読み込み
		texture = new Texture(Gdx.files.internal("data/stage_far_test.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmpRegion = new TextureRegion(texture, 0, 0, 1024, 1024);
	}

}