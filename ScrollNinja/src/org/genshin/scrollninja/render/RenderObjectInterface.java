package org.genshin.scrollninja.render;

import org.genshin.engine.system.Renderable;
import org.genshin.engine.system.Updatable;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface RenderObjectInterface extends Updatable, Renderable
{
	/**
	 * アニメーションを設定する。
	 * @param name		アニメーションの名前
	 */
	public void setAnimation(String name);

	/**
	 * アニメーションの時間を設定する。
	 * @param time		アニメーションの時間（秒）
	 */
	public void setAnimationTime(float time);

	/**
	 * アニメーションを持つか調べる。
	 * @return		アニメーションを持つ場合はtrue
	 */
	public boolean hasAnimation();

	/**
	 * アニメーションがループするか調べる。
	 * @return		ループする場合はtrue
	 */
	public boolean isAnimationLooping();

	/**
	 * アニメーションが終了しているか調べる。
	 * @return		終了している場合はtrue
	 */
	public boolean isAnimationFinished();

	/**
	 * 描画スプライトを取得する。
	 * @return		描画スプライト
	 */
	public abstract Sprite getSprite();
	
	/**
	 * アニメーションの時間を取得する。
	 * @return		アニメーションの時間
	 */
	public float getAnimationTime();

}
