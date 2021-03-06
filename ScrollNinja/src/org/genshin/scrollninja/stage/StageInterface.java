package org.genshin.scrollninja.stage;

import org.genshin.engine.system.Disposable;

import com.badlogic.gdx.math.Vector2;

/**
 * ステージのインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface StageInterface extends Disposable
{
	/**
	 * ステージの大きさを取得する。
	 * @return		ステージの大きさ
	 */
	public Vector2 getSize();
	
	/**
	 * ステージ開始時のプレイヤー座標を取得する。
	 * @return		ステージ開始時のプレイヤー座標
	 */
	public Vector2 getStartPosition();
}
