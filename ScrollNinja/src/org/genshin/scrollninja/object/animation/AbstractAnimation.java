package org.genshin.scrollninja.object.animation;

public abstract class AbstractAnimation implements AnimationInterface
{
	@Override
	public final void update()
	{
		final float delta = 1.0f/60.0f;
		currentTime += delta;
	}
	
	@Override
	public final void setCurrentTime(float time)
	{
		currentTime = time;
	}
	
	/**
	 * アニメーションの経過時間を取得する。
	 * @return		アニメーションの経過時間
	 */
	protected float getCurrentTime()
	{
		return currentTime;
	}


	/** アニメーションタイマ */
	private float currentTime;
}
