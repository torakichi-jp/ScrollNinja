package org.genshin.engine.system;

/**
 * 座標と回転を持つインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface PostureInterface
{
	/**
	 * X座標を取得する。
	 * @return		X座標
	 */
	public float getPositionX();

	/**
	 * Y座標を取得する。
	 * @return		Y座標
	 */
	public float getPositionY();

	/**
	 * 角度（単位：度）を取得する。
	 * @return		角度（単位：度）
	 */
	public float getRotation();
}
