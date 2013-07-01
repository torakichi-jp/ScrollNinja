package org.genshin.scrollninja.render.particle;

import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.render.AbstractRenderObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
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
		
		// サイズ変更
		ScaledNumericValue value = particleEffect.getEmitters().get(0).getScale();
		float[] scaling = new float[1];
		scaling[0] = GlobalDefine.INSTANCE.WORLD_SCALE;
		value.setScaling(scaling);
		//value = particleEffect.getEmitters().get(0).getSpawnHeight();
		//value.setScaling(scaling);
		//value = particleEffect.getEmitters().get(0).getSpawnWidth();
		//value.setScaling(scaling);
		
		// 位置設定
		this.position = new Vector2(0, -5.0f);
		
		// 継続するかどうか
		particleEffect.getEmitters().get(0).setContinuous(true);
		
		// パーティクル開始
		particleEffect.start();
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
	}

}
