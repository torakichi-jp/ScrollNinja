package org.genshin.scrollninja.object.animation;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * テクスチャアニメーションを管理する
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public final class TextureAnimation extends AbstractAnimation
{
	/**
	 * コンストラクタ
	 * @param tad	テクスチャアニメーションの定義
	 */
	public TextureAnimation(TextureAnimationDef tad)
	{ 
		TextureRegion textureRegions[] = new TextureRegion[tad.frameCount];
		for(int i = 0;  i < tad.frameCount;  ++i)
		{
			// FIXME アニメーションが2行以上続いている場合にメモリぶっ壊すよ。
			textureRegions[i] = new TextureRegion(
				tad.texture,
				(tad.startIndex.x+i)*tad.size.x, tad.startIndex.y*tad.size.y,
				tad.size.x, tad.size.y
			);
		}
		looping = tad.looping;
		animation = new Animation(tad.time, textureRegions);
	}
	
	@Override
	public void apply(Sprite sprite)
	{
		sprite.setRegion( animation.getKeyFrame(getCurrentTime(), isLooping()) );
	}

	@Override
	public boolean isLooping()
	{
		return looping;
	}

	@Override
	public boolean isFinished()
	{
		return animation.isAnimationFinished(getCurrentTime());
	}
	
	/** アニメーションオブジェクト */
	private final Animation animation;
	
	/** ループフラグ */
	private final boolean looping;
}
