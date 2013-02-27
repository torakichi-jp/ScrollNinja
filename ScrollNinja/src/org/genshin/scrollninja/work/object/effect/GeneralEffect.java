package org.genshin.scrollninja.work.object.effect;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.work.render.AnimationRenderObject;
import org.genshin.scrollninja.work.render.RenderObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * 汎用エフェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class GeneralEffect extends AbstractEffect
{
	public GeneralEffect(String effectFilePath, float x, float y, float degrees)
	{
		this(effectFilePath, x, y, degrees, false, false);
	}
	
	public GeneralEffect(String effectFilePath, float x, float y, float degrees, boolean flipX, boolean flipY)
	{
		this(effectFilePath, x, y, degrees, flipX, flipY, GlobalDefine.RenderDepth.EFFECT);
	}
	
	public GeneralEffect(String effectFilePath, float x, float y, float degrees, boolean flipX, boolean flipY, int depth)
	{
		final EffectDef def = new EffectDef(effectFilePath);
		
		//---- フィールドの初期化
		// 寿命
		life = def.life;
		
		// 座標、角度
		position.set(x, y);
		angle = degrees;
		
		// 速度
		final Vector2 velocityFlip = Vector2.tmp.set(flipX ? -1.0f : 1.0f, flipY ? -1.0f : 1.0f);
		velocity.set(def.startVelocity.x * velocityFlip.x, def.startVelocity.y * velocityFlip.y).rotate(angle);
		accel.set(def.velocityAccel.x * velocityFlip.x, def.velocityAccel.y * velocityFlip.y).rotate(angle);
		
		// 角速度
		angularVelocity = def.startAngularVelocity;
		angularVelocityAccel = def.angularVelocityAccel;
		
		// 色
		color.set(def.startColor);
		colorVelocity.r = def.colorVelocity.r;
		colorVelocity.g = def.colorVelocity.g;
		colorVelocity.b = def.colorVelocity.b;
		colorVelocity.a = def.colorVelocity.a;
		
		//---- 描画オブジェクト生成
		// アニメーションなし
		if(def.animationFilePath == null || def.animationFilePath.isEmpty() || def.animationName == null || def.animationName.isEmpty())
		{
			renderObject = new RenderObject(def.spriteFilePath, this, depth);
		}
		// アニメーションあり
		else
		{
			final AnimationRenderObject aro = new AnimationRenderObject(def.spriteFilePath, def.animationFilePath, this, depth);
			aro.setAnimation(def.animationName);
			renderObject = aro;
		}
		
		renderObject.setColor(color);
		renderObject.setScale(flipX ? -1.0f : 1.0f, flipY ? -1.0f : 1.0f);
	}

	@Override
	public void dispose()
	{
		renderObject.dispose();
		
		super.dispose();
	}

	@Override
	public void update(float deltaTime)
	{
		//---- 寿命
		if( (life -= deltaTime) <= 0.0f )
		{
			dispose();
			return;
		}
		
		//---- 補完計算メモ
		// ans = (v1 + v2) * (t2 - t1) / 2
		//  v2 = v1 + a
		// ans = (v1 * 2 + a) * (t2 - t1) * 0.5
		
		//---- 座標
		final Vector2 currentAccel = accel.tmp().mul(deltaTime);
		position.add(
			translate	.set(velocity)
						.mul(2.0f)
						.add(currentAccel)
						.mul(deltaTime)
						.mul(0.5f)
		);
		velocity.add(currentAccel);
		
		//---- 角度
		final float currentAngularVelocityAccel = angularVelocityAccel * deltaTime;
		angle += (angularVelocity * 2.0f + currentAngularVelocityAccel) * deltaTime * 0.5f;
		angularVelocity += currentAngularVelocityAccel;
		
		//---- 色
		color.r += colorVelocity.r * deltaTime;
		color.g += colorVelocity.g * deltaTime;
		color.b += colorVelocity.b * deltaTime;
		color.a += colorVelocity.a * deltaTime;
		color.clamp();
		renderObject.setColor(color);
	}

	@Override
	public float getPositionX()
	{
		return position.x;
	}

	@Override
	public float getPositionY()
	{
		return position.y;
	}

	@Override
	public float getRotation()
	{
		return angle;
	}
	
	
	/** 描画オブジェクト */
	private final RenderObject renderObject;
	
	/** 寿命（単位；秒） */
	private float life;
	
	/** 座標 */
	private final Vector2 position = new Vector2();
	
	/** 角度（単位：度） */
	private float angle;
	
	/** 速度 */
	private final Vector2 velocity = new Vector2();
	
	/** 加速度 */
	private final Vector2 accel = new Vector2();
	
	/** 角速度 */
	private float angularVelocity;
	
	/** 角加速度 */
	private final float angularVelocityAccel;
	
	/** 色 */
	private final Color color = new Color();
	
	/** 色の変化量 */
	private final Color colorVelocity = new Color();
	
	/** 移動量計算に一時的に使用する為のVector2オブジェクト */
	private static final Vector2 translate = new Vector2();
}
