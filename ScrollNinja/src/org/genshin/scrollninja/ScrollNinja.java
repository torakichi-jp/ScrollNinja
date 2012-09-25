package org.genshin.scrollninja;

import com.badlogic.gdx.Game;

public class ScrollNinja extends Game {

	@Override
	public void create() {

		/** 設定の事前ロードとかあればここで実行 */

		// メインメニュー画面セット
		setScreen(new MainMenu(this));
	}

	@Override
	public void dispose() {
		// 最後の処理
		super.dispose();
		getScreen().dispose();
	}
}