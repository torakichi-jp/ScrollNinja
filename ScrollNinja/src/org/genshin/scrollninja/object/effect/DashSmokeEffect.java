package org.genshin.scrollninja.object.effect;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.render.RenderObjectFactory;
import org.genshin.scrollninja.render.RenderObjectInterface;

/**
 * ダッシュ時の煙のエフェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class DashSmokeEffect extends AbstractEffect
{
	/**
	 * コンストラクタ
	 * @param positionX		X座標
	 * @param positionY		Y座標
	 */
	public DashSmokeEffect(float positionX, float positionY, boolean flip)
	{
		flip(flip, false);
		
		//---- 座標設定
		for(RenderObjectInterface ro : getRenderObjects())
		{
			ro.setPosition(positionX, positionY);
		}
		
		//---- 各種パラメータ設定
		setVelocityInterpolation(300.0f*(flip?-1.0f:1.0f)*GlobalParam.INSTANCE.WORLD_SCALE, 100.0f*GlobalParam.INSTANCE.WORLD_SCALE, 0.0f, 0.0f);
		setColorInterpolation(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f);
	}

	@Override
	protected float getLife()
	{
		return 0.5f;
	}

	@Override
	protected void initializeSprite()
	{
		addRenderObject(RenderObjectFactory.getInstance().get("DashSmoke"));
	}
}
