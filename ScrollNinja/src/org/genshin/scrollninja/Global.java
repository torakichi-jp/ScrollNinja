package org.genshin.scrollninja;

import org.genshin.engine.system.RenderableManager;
import org.genshin.engine.system.UpdatableManager;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * プロジェクト全体で共有する変数（モノステート）
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public final class Global
{
	/** 現在指定されている更新処理を管理するオブジェクト */
	public static UpdatableManager updatableManager = null;
	
	/** 現在指定されている描画処理を管理するオブジェクト */
	public static RenderableManager renderableManager = null;

	/** 現在指定されているカメラオブジェクト */
	public static Camera camera = null;
	
	/** 現在指定されているスプライトバッチオブジェクト */
	public static SpriteBatch spriteBatch = new SpriteBatch();
	
	/** ゲーム内時間（秒） */
	public static float gameTime = 0.0f;
	
	/** ゲーム内時間（フレーム数） */
	public static int frameCount = 0;
}
