package org.genshin.scrollninja;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenu implements Screen {
	Game scrollNinja;

	OrthographicCamera camera;
	SpriteBatch batch;

	// コンストラクタ
	public MainMenu(Game game) {
		this.scrollNinja = game;
	}

	// 最初から
	public void startGame() {

	}

	// 途中から
	public void loadGame() {

	}

	// オプション
	public void optionMenu() {

	}

	// 記録閲覧
	public void showRecord() {

	}

	// マルチプレイヤー
	public void multiPlayer() {

	}

	// 描画
	@Override
	public void render(float delta) {

	}

	// サイズ変更
	@Override
	public void resize(int width, int height) {

	}

	// 表示
	@Override
	public void show() {

	}

	// 隠れた時
	@Override
	public void hide() {

	}

	// 一時中断
	@Override
	public void pause() {

	}


	// 再開
	@Override
	public void resume() {

	}

	// 終了前
	@Override
	public void dispose() {

	}
}
