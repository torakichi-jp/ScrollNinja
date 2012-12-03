package org.genshin.scrollninja.object.effect;

import java.util.ArrayList;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.render.RenderObjectFactory;
import org.genshin.scrollninja.render.RenderObjectInterface;

import com.badlogic.gdx.graphics.Texture.TextureWrap;

/**
 * 鉤縄を離した時のエフェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class KaginawaReleaseEffect extends AbstractEffect
{
	public KaginawaReleaseEffect(ArrayList<RenderObjectInterface> renderObjects, float positionX, float positionY, float degrees)
	{
		//---- 描画オブジェクトの状態をコピーする。
		final int count = renderObjects.size();
		for(int i = 0;  i < count;  ++i)
		{
			final RenderObjectInterface src = renderObjects.get(i);
			final RenderObjectInterface dest = getRenderObject(i);
			
			dest.getSprite().set(src.getSprite());
			dest.translate(positionX, positionY);
			dest.rotate(degrees);
		}

		//---- 各種パラメータ設定
		setVelocityInterpolation(0.0f, -100.0f*GlobalParam.INSTANCE.WORLD_SCALE, 0.0f, 0.0f);
		setColorInterpolation(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f);
		
	}

	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
	}

	@Override
	public void render()
	{
		// TODO Auto-generated method stub
		super.render();
	}

	@Override
	protected void initializeSprite()
	{
		final RenderObjectInterface ropeRenderObject = RenderObjectFactory.getInstance().get("KaginawaRope");
		ropeRenderObject.getSprite().getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		addRenderObject( ropeRenderObject );
		addRenderObject( RenderObjectFactory.getInstance().get("KaginawaAnchor") );
	}

	@Override
	protected float getLife()
	{
		return 0.5f;
	}
}
