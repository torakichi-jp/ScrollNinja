package org.genshin.scrollninja.stage;

import com.badlogic.gdx.math.Vector2;

/**
 * ステージのインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface StageInterface
{
	/**
	 * ステージ開始時のプレイヤー座標を取得する。
	 * @return		ステージ開始時のプレイヤー座標
	 */
	public Vector2 getStartPosition();
}
