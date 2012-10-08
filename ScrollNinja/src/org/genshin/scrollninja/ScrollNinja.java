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
		Background.LoadTexture();		// 背景読み込み
		setScreen(new MainMenu(this));	// メインメニュー読み込み
	}
	

	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
	}
}