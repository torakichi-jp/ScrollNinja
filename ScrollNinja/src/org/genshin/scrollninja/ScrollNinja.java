package org.genshin.scrollninja;

import org.genshin.old.scrollninja.GameMain;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 * ScrollNinja エントリポイント
 * @author インターン生
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class ScrollNinja extends Game
{
	@Override
	public void create()
	{
		//---- ウィンドウサイズを設定する。
		Gdx.graphics.setDisplayMode(GlobalParam.INSTANCE.CLIENT_WIDTH, GlobalParam.INSTANCE.CLIENT_HEIGHT, false);
		
		//---- 初期スクリーンを設定する。
		setScreen(new GameMain(this, 0));
	}

	@Override
	public void render()
	{
		//---- [Esc] 入力でプログラムを終了する。
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
		{
			Gdx.app.exit();
			return;
		}
		
		//---- あとは基本クラスに任せる。
		super.render();
	}
}