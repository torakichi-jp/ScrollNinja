package org.genshin.scrollninja.utils.debug;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.World;



/**
 * デバッグ関連の操作をまとめたクラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public final class DebugTool
{
	/**
	 * 初期化する。
	 * @param debug		デバッグを有効にする場合はtrue
	 */
	public static void initialize(boolean debug)
	{
		if(impl == null)
			impl = debug ? new EnabledDebugImpl() : new DisabledDebugImpl();
	}
	
	/**
	 * デバッグコマンドの更新処理を実行する。
	 */
	public static void updateDebugCommand()
	{
		impl.updateDebugCommand();
	}

	/**
	 * 衝突判定を描画する。
	 * @param world		世界オブジェクト
	 * @param camera	カメラオブジェクト
	 */
	public static void renderCollison(World world, Camera camera)
	{
		impl.renderCollision(world, camera);
	}
	
	/**
	 * ログを描画する。
	 */
	public static void renderLog()
	{
		impl.renderLog();
	}

	/**
	 * 画面上にログを出力する。
	 * @param message		出力するメッセージ
	 */
	public static void logToScreen(String message)
	{
		impl.logToScreen(message);
	}

	/**
	 * コンソールにログを出力する。
	 * @param message		出力するメッセージ
	 */
	public static void logToConsole(String message)
	{
		impl.logToConsole(message);
	}
	
	/**
	 * コンソールにログを出力する。
	 * @param object		出力するオブジェクト
	 */
	public static void logToConsole(Object object)
	{
		impl.logToConsole(object);
	}


	/** デバッグ処理の実装オブジェクト */
	private static DebugImplInterface impl = null;
}
