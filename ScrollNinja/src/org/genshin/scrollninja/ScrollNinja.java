package org.genshin.scrollninja;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class ScrollNinja extends Game {

	public static Texture texture;
	public static Sprite stageSpr;
	public static Sprite bgSpr;
	public static Vector2 window;

	// 定数　画面解像度
	public static final int XGA = 0;	// 4:3
	public static final int HD = 1;		// 16:9
	public static final int SXGA = 2;	// 5:4
	public static final int WUXGA = 3;	// 16:10

	public static int aspectRatio;					// アスペクト比
	public static boolean FULL_SCREEN = false;		// フルスクリーンかどうか

	@Override
	public void create() {
		/**
			事前に初期設定が必要ならここで
		*/

		window = new Vector2();
		if (!FULL_SCREEN) {
			window.x = Gdx.graphics.getWidth();
			window.y = Gdx.graphics.getHeight();
		}
		else {
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			window.x = (float)d.width;
			window.y = (float)d.height;
		}

		// アスペクト比計算
		if (window.x * 3 == window.y * 4)
			aspectRatio = XGA;
		else if (window.x * 9 == window.y * 16)
			aspectRatio = HD;
		else if (window.x * 5 == window.y * 4)
			aspectRatio = SXGA;
		else //(window.x * 5 == window.y * 8)
			aspectRatio = WUXGA;

		Background.LoadTexture();		// 背景読み込み
		setScreen(new MainMenu(this));	// メインメニュー読み込み
	}


	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
	}
}