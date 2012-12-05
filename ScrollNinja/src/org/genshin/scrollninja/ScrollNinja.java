package org.genshin.scrollninja;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Logger;

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
		
		//---- [Alt] + [Enter] 入力でフルスクリーン切り替え（仮）
		final boolean input = Gdx.input.isKeyPressed(Keys.ENTER);
		if( Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.ALT_RIGHT) )
		{
			if(!prevInput && input)
			{
				int newWidth = GlobalParam.INSTANCE.CLIENT_WIDTH;
				int newHeight = GlobalParam.INSTANCE.CLIENT_HEIGHT;
				final boolean newFullscreen = !Gdx.graphics.isFullscreen();
				
				if( newFullscreen )
				{
					final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
					newWidth = d.width;
					newHeight = d.height;
				}
				
				Gdx.graphics.setDisplayMode(newWidth, newHeight, newFullscreen);
				getScreen().resize(newWidth, newHeight);

				if(Gdx.input.isCursorCatched())
				{
					Gdx.input.setCursorCatched(false);
					Gdx.input.setCursorCatched(true);
				}
			}
		}
		prevInput = input;
		
		//---- [Esc] 入力でプログラムを終了する。
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
		{
			Gdx.app.exit();
			return;
		}
	}
	
	/** 仮。 */
	private boolean prevInput = false;
}