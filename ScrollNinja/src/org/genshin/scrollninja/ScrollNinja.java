package org.genshin.scrollninja;

import org.genshin.old.scrollninja.GameMain;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;

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
		//---- 画面のクリアカラーを設定する。
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		//---- ウィンドウサイズを設定する。
		Gdx.graphics.setDisplayMode(GlobalParam.INSTANCE.CLIENT_WIDTH, GlobalParam.INSTANCE.CLIENT_HEIGHT, false);
		
		//---- 初期スクリーンを設定する。
		setScreen(new GameMain(this, 0));
	}

	@Override
	public void render()
	{
		//---- 画面をクリアする
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//---- 基本クラスの処理を実行する。
		super.render();
		
		//---- ゲーム内時間をカウントする。
		GlobalParam.INSTANCE.frameCount++;
		GlobalParam.INSTANCE.gameTime = Gdx.graphics.getDeltaTime();
		
		//---- [Esc] 入力でプログラムを終了する。
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
		{
			Gdx.app.exit();
			return;
		}
	}
}