package org.genshin.scrollninja.work.render.animation;

import org.genshin.scrollninja.utils.TextureFactory;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * アニメーションのラッパークラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class AnimationWrapper
{
	/**
	 * コンストラクタ
	 * @param animationDef		アニメーションの初期化用定義
	 */
	public AnimationWrapper(AnimationDef animationDef)
	{
		//---- ループフラグ設定
		looping = animationDef.looping;
		
		//---- アニメーション生成
		final int frameCount = animationDef.frames.length;
		final TextureRegion[] textureRegions = new TextureRegion[frameCount];
		
		for(int i = 0;  i < frameCount;  ++i)
		{
			textureRegions[i] = new TextureRegion(
				TextureFactory.getInstance().get(animationDef.texture),
				(animationDef.start.x + animationDef.frames[i]) * animationDef.uvSize.x, animationDef.start.y * animationDef.uvSize.y,
				animationDef.uvSize.x, animationDef.uvSize.y
			);
		}
		
		animation = new Animation(animationDef.time / 60.0f, textureRegions);
		animation.setPlayMode(looping ? Animation.LOOP : Animation.NORMAL);
	}
	
	/**
	 * キーフレームを取得する。
	 * @param stateTime		時間
	 * @return		指定した時間のキーフレーム
	 */
	public TextureRegion getKeyFrame(float stateTime)
	{
		return animation.getKeyFrame(stateTime);
	}
	
	/**
	 * アニメーションが終了しているか調べる。
	 * @param stateTime		時間
	 * @return		指定した時間の時点でアニメーションが終了している場合はtrue
	 */
	public boolean isAnimationFinished(float stateTime)
	{
		return animation.isAnimationFinished(stateTime);
	}
	
	/**
	 * アニメーションがループするか調べる。
	 * @return		アニメーションがループする場合はtrue
	 */
	public boolean isAnimationLooping()
	{
		return looping;
	}
	
	
	/** アニメーションのループフラグ */
	final boolean looping;
	
	/** アニメーションオブジェクト */
	final Animation animation;
}
