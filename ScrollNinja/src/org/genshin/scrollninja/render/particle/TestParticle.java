package org.genshin.scrollninja.render.particle;

import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.render.AbstractRenderObject;
import org.genshin.scrollninja.utils.debug.DebugTool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
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
		//ParticleEmitter emitter = particleEffect.getEmitters().get(0);
		//ScaledNumericValue value = emitter.getScale();
		//float[] scaling = new float[1];
		//scaling[0] = GlobalDefine.INSTANCE.WORLD_SCALE;
		//value.setScaling(scaling);
		//value.setHighMax(value.getHighMax() * scaling[0]);
		//value.setHighMin(value.getHighMin() * scaling[0]);

		float scale = GlobalDefine.INSTANCE.WORLD_SCALE;
		ParticleEmitter emitter = particleEffect.getEmitters().get(0);

		DebugTool.logToConsole(""+emitter.getVelocity().getHighMax());
		DebugTool.logToConsole(""+emitter.getVelocity().getHighMin());
		DebugTool.logToConsole(""+emitter.getVelocity().getLowMax());
		DebugTool.logToConsole(""+emitter.getVelocity().getLowMin());
		
		float scalingMax = emitter.getScale().getHighMax();
		float scalingMin = emitter.getScale().getHighMin();
		emitter.getScale().setHigh(scalingMin * scale, scalingMax * scale);
		scalingMax = emitter.getScale().getLowMax();
		scalingMin = emitter.getScale().getLowMin();
		emitter.getScale().setLow(scalingMin * scale, scalingMax * scale);
		scalingMax = emitter.getVelocity().getHighMax();
		scalingMin = emitter.getVelocity().getHighMin();
		emitter.getVelocity().setHigh(scalingMin * scale, scalingMax * scale);
		scalingMax = emitter.getVelocity().getLowMax();
		scalingMin = emitter.getVelocity().getLowMin();
		emitter.getVelocity().setLow(scalingMin * scale, scalingMax * scale);
		
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
