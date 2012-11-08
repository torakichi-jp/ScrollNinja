package org.genshin.scrollninja.object.animation;

import org.genshin.engine.system.Updatable;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * アニメーションを管理するインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface AnimationInterface extends Updatable
{
	/**
	 * アニメーションを適用する。
	 * @param sprite	アニメーションを適用するスプライト
	 */
	public void apply(Sprite sprite);
	
	/**
	 * アニメーションの経過時間を設定する。
	 * @param time		アニメーションの経過時間
	 */
	public void setCurrentTime(float time);
	
	/**
	 * アニメーションがループするか調べる。
	 * @return		ループする場合はtrue
	 */
	public boolean isLooping();
	
	/**
	 * アニメーションが終了しているか調べる。
	 * @return		終了している場合はtrue
	 */
	public boolean isFinished();
}
