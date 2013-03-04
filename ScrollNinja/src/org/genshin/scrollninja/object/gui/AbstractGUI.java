package org.genshin.scrollninja.object.gui;

import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.object.AbstractObject;

import com.badlogic.gdx.math.Vector2;

/**
 * GUIの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractGUI extends AbstractObject
{
	@Override
	public float getPositionX()
	{
		return screenPosition.x * GlobalDefine.INSTANCE.WORLD_SCALE - Global.camera.viewportWidth * 0.5f + Global.camera.position.x;
	}

	@Override
	public float getPositionY()
	{
		return Global.camera.viewportHeight * 0.5f - screenPosition.y * GlobalDefine.INSTANCE.WORLD_SCALE + Global.camera.position.y;
	}

	@Override
	public float getRotation()
	{
		return 0.0f;
	}
	
	/**
	 * スクリーン座標を設定する。
	 * @param x		X座標
	 * @param y		Y座標
	 */
	protected void setScreenPosition(float x, float y)
	{
		screenPosition.set(x, y);
	}
	
	/**
	 * スクリーン座標でのX座標を取得する。
	 * @return		スクリーン座標でのX座標
	 */
	protected float getScreenPositionX()
	{
		return screenPosition.x;
	}
	
	/**
	 * スクリーン座標でのY座標を取得する。
	 * @return		スクリーン座標でのY座標
	 */
	protected float getScreenPositionY()
	{
		return screenPosition.y;
	}
	
	/**
	 * スクリーンの横幅を計算し、取得する。
	 * @return		スクリーンの横幅
	 */
	protected float getScreenWidth()
	{
		return Global.camera.viewportWidth * GlobalDefine.INSTANCE.INV_WORLD_SCALE;
	}
	
	/**
	 * スクリーンの縦幅を計算し、取得する。
	 * @return		スクリーンの縦幅
	 */
	protected float getScreenHeight()
	{
		return Global.camera.viewportHeight * GlobalDefine.INSTANCE.INV_WORLD_SCALE;
	}
	
	
	/** スクリーン座標 */
	private final Vector2 screenPosition = new Vector2();
}
