package org.genshin.scrollninja.work.render.animation;

import org.genshin.scrollninja.utils.TextureFactory;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * テクスチャアニメーション
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class TextureAnimation implements AnimationInterface
{
	/**
	 * コンストラクタ
	 * @param animationDef		テクスチャアニメーションの初期化用定義
	 */
	public TextureAnimation(TextureAnimationDef animationDef)
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
	
	@Override
	public TextureRegion getKeyFrame(float stateTime)
	{
		return animation.getKeyFrame(stateTime);
	}
	
	@Override
	public boolean isAnimationFinished(float stateTime)
	{
		return animation.isAnimationFinished(stateTime);
	}
	
	@Override
	public boolean isAnimationLooping()
	{
		return looping;
	}
	
	
	/** アニメーションのループフラグ */
	final boolean looping;
	
	/** アニメーションオブジェクト */
	final Animation animation;
}
