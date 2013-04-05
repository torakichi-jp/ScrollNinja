package org.genshin.scrollninja.utils.debug;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.World;

/**
 * デバッグ処理の実装オブジェクトのインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
interface DebugImplInterface
{
	/**
	 * デバッグコマンドを更新する。
	 */
	public void updateDebugCommand();
	
	/**
	 * 衝突判定を描画する。
	 * @param world		世界オブジェクト
	 * @param camera	カメラオブジェクト
	 */
	public void renderCollision(World world, Camera camera);
	
	/**
	 * ログを描画する。
	 */
	public void renderLog();
	
	/**
	 * 画面上にログを出力する。
	 * @param message		出力するメッセージ
	 */
	public void logToScreen(String message);
	
	/**
	 * コンソールにログを出力する。
	 * @param message		出力するメッセージ
	 */
	public void logToConsole(String message);
	
	/**
	 * コンソールにログを出力する。
	 * @param object		出力するオブジェクト
	 */
	public void logToConsole(Object object);
}
