package org.genshin.scrollninja.object.effect;

import java.util.ArrayList;

import org.genshin.scrollninja.render.RenderObjectFactory;
import org.genshin.scrollninja.render.RenderObjectInterface;

/**
 * 鉤縄を離した時のエフェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class KaginawaReleaseEffect extends AbstractEffect
{
	public KaginawaReleaseEffect(ArrayList<RenderObjectInterface> renderObjects)
	{
		
	}

	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
	}

	@Override
	protected void initializeSprite()
	{
		addRenderObject(RenderObjectFactory.getInstance().get(""));
	}
}
