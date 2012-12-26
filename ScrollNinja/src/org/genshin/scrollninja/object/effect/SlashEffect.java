package org.genshin.scrollninja.object.effect;

import org.genshin.scrollninja.render.RenderObjectFactory;
import org.genshin.scrollninja.render.RenderObjectInterface;

/**
 * 斬撃エフェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class SlashEffect extends AbstractEffect
{
	/**
	 * コンストラクタ
	 * @param positionX		X座標
	 * @param positionY		Y座標
	 * @param degrees		回転（度）
	 */
	public SlashEffect(float positionX, float positionY, float degrees)
	{
		//---- 座標設定
		for(RenderObjectInterface ro : getRenderObjects())
		{
			ro.setPosition(positionX, positionY);
			ro.setRotation(degrees);
		}
	}
	
	@Override
	public void update(float deltaTime)
	{
		//---- アニメーションが終わったら消滅する。
		if( getRenderObject(0).isAnimationFinished() )
		{
			dispose();
		}
	}

	@Override
	protected void initializeSprite()
	{
		RenderObjectInterface renderObject = RenderObjectFactory.getInstance().get("SlashEffect");
		addRenderObject(renderObject);
		renderObject.setAnimation("Slash");
	}
}
