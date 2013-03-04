package org.genshin.scrollninja.render.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * アニメーションのインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface AnimationInterface
{
	/**
	 * キーフレームを取得する。
	 * @param stateTime		時間
	 * @return		指定した時間のキーフレーム
	 */
	public TextureRegion getKeyFrame(float stateTime);
	
	/**
	 * アニメーションが終了しているか調べる。
	 * @param stateTime		時間
	 * @return		指定した時間の時点でアニメーションが終了している場合はtrue
	 */
	public boolean isAnimationFinished(float stateTime);
	
	/**
	 * アニメーションがループするか調べる。
	 * @return		アニメーションがループする場合はtrue
	 */
	public boolean isAnimationLooping();
}