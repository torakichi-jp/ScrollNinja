package org.genshin.scrollninja.render.particle;

import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.render.AbstractRenderObject;
import org.genshin.scrollninja.utils.debug.DebugTool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;

/**
 * パーティクルテスト
 * @author torakichi-jp
 * @since		1.0
 * @version		1.0
 */
public class TestParticle extends AbstractRenderObject {

	private ParticleEffect particleEffect;
	private Vector2 position;

	public TestParticle() {
		super(GlobalDefine.RenderDepth.DEFAULT);
		particleEffect = new ParticleEffect();

		// files.internal loads from the "assets" folder
		particleEffect.load(
				Gdx.files.internal("data/particles/fire.effect"),
				Gdx.files.internal("data/particles"));
		this.position = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
	}

	@Override
	public float getPositionX() {
		return position.x;
	}

	@Override
	public float getPositionY() {
		return position.y;
	}

	@Override
	public float getRotation() {
		return 0;
	}

	@Override
	protected void localRender() {
		particleEffect.setPosition(position.x, position.y);
		particleEffect.update(Gdx.graphics.getDeltaTime());
		particleEffect.draw(Global.spriteBatch);
		DebugTool.logToScreen("Test Position: " + position);
	}

}
