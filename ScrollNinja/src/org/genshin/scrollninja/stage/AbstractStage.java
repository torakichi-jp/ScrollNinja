package org.genshin.scrollninja.stage;

import com.badlogic.gdx.math.Vector2;

/**
 * ステージの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractStage implements StageInterface
{
	@Override
	public Vector2 getStartPosition()
	{
		return startPosition;
	}
	
	/**
	 * ステージ開始時のプレイヤー座標を設定する。
	 * @param x		X座標
	 * @param y		Y座標
	 */
	protected void setStartPosition(float x, float y)
	{
		startPosition.set(x, y);
	}

	/** ステージ開始時のプレイヤー座標 */
	private final Vector2 startPosition = new Vector2();
}
