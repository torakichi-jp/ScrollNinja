package org.genshin.scrollninja.render.particle;

import org.genshin.scrollninja.render.AbstractRenderObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * パーティクルテスト
 * @author torakichi-jp
 * @since		1.0
 * @version		1.0
 */
public class TestParticle extends AbstractRenderObject {
	private ParticleEffect particleEffect;
	public TestParticle(int depth) {
		super(depth);
		particleEffect = new ParticleEffect();

		// files.internal loads from the "assets" folder
		particleEffect.load(Gdx.files.internal("particle/fire.effect"),
				Gdx.files.internal("particle"));
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		particleEffect.draw(batch);
	}

	public void update() {
		particleEffect.update(0.0f);
	}

	@Override
	public float getPositionX() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getPositionY() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getRotation() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	protected void localRender() {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
