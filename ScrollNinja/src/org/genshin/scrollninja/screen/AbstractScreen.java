package org.genshin.scrollninja.screen;

import org.genshin.engine.system.RenderableManager;
import org.genshin.engine.system.UpdatableManager;
import org.genshin.old.scrollninja.GameMain;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.object.gui.Cursor;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * スクリーンの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractScreen implements Screen
{
	/**
	 * コンストラクタ
	 */
	AbstractScreen()
	{
		//---- カレントスクリーンに設定する。
		setCurrentScreen();
		
		//---- カーソルオブジェクトを生成する。
		cursor = createCursor();
	}
	
	@Override
	public void render(float delta)
	{
		//---- 更新処理を実行する。
		if( !isPaused() )
		{
			// オブジェクトの更新処理
			updatableManager.update(delta);
			
			// 世界の更新処理
			world.step(delta, 20, 20);
		}
		
		//---- 描画処理を実行する。
		GameMain.spriteBatch = spriteBatch;
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		renderableManager.render();
		spriteBatch.end();
	}

	@Override
	public final void resize(int width, int height)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public final void show()
	{
		//---- カレントスクリーンに設定する。
		setCurrentScreen();
	}

	@Override
	public final void hide()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public final void pause()
	{
		paused = true;
	}

	@Override
	public final void resume()
	{
		paused = false;
	}

	@Override
	public void dispose()
	{
		//---- 更新管理オブジェクトを空にする。
		updatableManager.clear();
		
		//---- 描画管理オブジェクトを空にする。
		renderableManager.clear();
		
		//---- 世界オブジェクトを破棄する。
		world.dispose();
	}
	
	/**
	 * 一時停止中か調べる。
	 * @return		一時停止中ならtrue
	 */
	public final boolean isPaused()
	{
		return paused;
	}
	
	/**
	 * カーソルを生成する。
	 * @return		カーソルオブジェクト
	 */
	protected abstract Cursor createCursor();
	
	/**
	 * 世界オブジェクトを取得する。
	 * @return		世界オブジェクト
	 */
	protected final World getWorld()
	{
		return world;
	}
	
	/**
	 * カメラオブジェクトを取得する。
	 * @return		カメラオブジェクト
	 */
	protected final Camera getCamera()
	{
		return camera;
	}
	
	/**
	 * カーソルを取得する。
	 * @return		カーソルオブジェクト
	 */
	protected final Cursor getCursor()
	{
		return cursor;
	}
	
	/**
	 * このスクリーンをカレントスクリーンに設定する。
	 */
	private final void setCurrentScreen()
	{
		Global.currentUpdatableManager = updatableManager;
		Global.currentRenderableManager = renderableManager;
	}
	
	
	// FIXME 仮
	private final SpriteBatch spriteBatch = new SpriteBatch();
	
	/** 世界オブジェクト */
	private final World world = new World(new Vector2(0, GlobalDefine.INSTANCE.GRAVITY), true);
	
	/** カメラオブジェクト */
	private final Camera camera = new OrthographicCamera(GlobalDefine.INSTANCE.CLIENT_WIDTH * GlobalDefine.INSTANCE.WORLD_SCALE, GlobalDefine.INSTANCE.CLIENT_HEIGHT * GlobalDefine.INSTANCE.WORLD_SCALE);
	
	/** 更新管理オブジェクト */
	private final UpdatableManager updatableManager = new UpdatableManager();
	
	/** 描画管理オブジェクト */
	private final RenderableManager renderableManager = new RenderableManager();
	
	/** カーソルオブジェクト */
	private final Cursor cursor;
	
	/** 一時停止フラグ */
	private boolean paused = false;
}
