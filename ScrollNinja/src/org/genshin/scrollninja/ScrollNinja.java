package org.genshin.scrollninja;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScrollNinja extends Game {

	public static Texture texture;
	public static Sprite stageSpr;
	public static Sprite bgSpr;
	
	
	@Override
	public void create() {
		/**
			事前に初期設定が必要ならここで
		*/
		/*
		 * 背景読み込み
		 * */
		
		//画面サイズ取得
		float w = Gdx.graphics.getWidth();
		
		// 背景（奥）テクスチャ読み込み
		texture = new Texture(Gdx.files.internal("data/stage_far_test.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 0, 1024, 1024);
		// 背景スプライトにセット
		bgSpr = new Sprite(tmpRegion);
		bgSpr.setPosition(-(w / 2), -512);
		
		// 背景(手前)テクスチャ読み込み
		texture = new Texture(Gdx.files.internal("data/stage_near_test.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmpRegion = new TextureRegion(texture, 0, 0, 2048, 2048);
		// 背景スプライトにセット
		stageSpr = new Sprite(tmpRegion);
		stageSpr.setPosition(-(w / 2), -1024);
		
		// メインメニュー読み込み
		setScreen(new MainMenu(this));
	}
	

	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
	}
}