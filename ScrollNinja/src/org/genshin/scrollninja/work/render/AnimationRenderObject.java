package org.genshin.scrollninja.work.render;

import org.genshin.engine.system.PostureInterface;
import org.genshin.engine.system.Updatable;

public class AnimationRenderObject extends RenderObject implements Updatable
{
	/**
	 * コンストラクタ
	 * @param spriteName		スプライト名
	 * @param animationSetName	アニメーションセット名
	 * @param posture			位置情報
	 */
	public AnimationRenderObject(String spriteName, String animationSetName, PostureInterface posture)
	{
		super(spriteName, posture);
		
		animationSet = null;	// AnimationSetFactory.get(animationSetName);
	}

	@Override
	public void dispose()
	{
		//---- 自身の破棄
		animationSet = null;
		
		//---- 基本クラスの破棄
		super.dispose();
	}

	@Override
	public void update(float deltaTime)
	{
		if(!paused)
			timer += deltaTime * speedRatio;
	}

	@Override
	public void render()
	{
		//---- アニメーションを反映する。
		// currentAnimation.apply(getSprite(), timer);
		
		//---- 描画処理
		super.render();
	}
	
	/**
	 * アニメーションを一時停止する。
	 */
	public void pauseAnimation()
	{
		paused = true;
	}
	
	/**
	 * アニメーションの一時停止を解除する。
	 */
	public void resumeAnimation()
	{
		paused = false;
	}
	
	/**
	 * アニメーションを設定する。
	 * @param animationName		アニメーション名
	 */
	public void setAnimation(String animationName)
	{
		// currentAnimation = animationSet.getAnimation(animationName);
		timer = 0.0f;
	}
	
	/**
	 * アニメーションの時間を設定する。
	 * @param time		アニメーションの時間
	 */
	public void setAnimationTime(float time)
	{
		timer = time;
	}
	
	/**
	 * 再生速度の倍率を設定する。
	 * @param speedRatio	再生速度の倍率（初期値：1.0）
	 */
	public void setSpeedRatio(float speedRatio)
	{
		this.speedRatio = speedRatio;
	}
	
	/**
	 * アニメーションの時間を取得する。
	 * @return	アニメーションの時間
	 */
	public float getAnimationTime()
	{
		return timer;
	}

	/**
	 * 再生速度の倍率を取得する。
	 * @return	再生速度の倍率
	 */
	public float getSpeedRatio()
	{
		return speedRatio;
	}
	
	/**
	 * アニメーションが一時停止中か調べる。
	 * @return	アニメーションが一時停止中ならtrue
	 */
	public boolean isAnimationPaused()
	{
		return paused;
	}
	
	/**
	 * アニメーションが終了しているか調べる。
	 * @return	アニメーションが終了している場合はtrue
	 */
	public boolean isAnimationFinished()
	{
		return false;	// currentAnimation.isFinished();
	}
	
	/**
	 * ループするアニメーションか調べる。
	 * @return	ループするアニメーションならtrue
	 */
	public boolean isAnimationLooping()
	{
		return false;	// currentAnimation.isLooping();
	}
	
	
	/** アニメーションセット */
	private Object animationSet;
	
	/** 現在のアニメーション */
	private Object currentAnimation;
	
	/** アニメーションタイマー */
	private float timer = 0.0f;
	
	/** 再生速度の倍率 */
	private float speedRatio = 1.0f;
	
	/** ポーズフラグ */
	private boolean paused = false;
}
