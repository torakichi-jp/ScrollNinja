package org.genshin.scrollninja.object.effect;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.object.ninja.AbstractNinja;
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
	 * @param owner		所有者
	 */
	public SlashEffect(PostureInterface owner)
	{
		this.owner = (AbstractNinja)owner;
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
	public void render()
	{
		//---- 所有者に追従する。
		for(RenderObjectInterface ro : getRenderObjects())
		{
			ro.setPosition(owner.getPositionX(), owner.getPositionY());
			ro.setRotation(owner.getRotation());
		}
		
		flip(owner.isFlip(), false);
		
		super.render();
	}

	@Override
	protected void initializeSprite()
	{
		RenderObjectInterface renderObject = RenderObjectFactory.getInstance().get("SlashEffect");
		addRenderObject(renderObject);
		renderObject.setAnimation("Slash");
	}

	@Override
	protected int getRenderPriority()
	{
		return 2;
	}
	
	/** 所有者 */
	private final AbstractNinja owner;
}
