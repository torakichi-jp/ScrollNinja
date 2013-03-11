package org.genshin.scrollninja.object.effect;

import java.util.ArrayList;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.object.AbstractObject;
import org.genshin.scrollninja.render.RenderObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractEffect extends AbstractObject
{
	/**
	 * コンストラクタ
	 */
	public AbstractEffect()
	{
		this(0.0f, 0.0f, 0.0f);
	}
	
	/**
	 * コンストラクタ
	 * @param x			X座標
	 * @param y			Y座標
	 * @param degrees	角度（度）
	 */
	public AbstractEffect(float x, float y, float degrees)
	{
		posture = new EffectPosture(x, y, degrees);
	}
	
	/**
	 * コンストラクタ
	 * @param def		初期化用定義
	 */
	public AbstractEffect(EffectDef def)
	{
		this(def, 0.0f, 0.0f, 0.0f);
	}
	
	/**
	 * コンストラクタ
	 * @param def		初期化用定義
	 * @param x			X座標
	 * @param y			Y座標
	 * @param degrees	角度（度）
	 */
	public AbstractEffect(EffectDef def, float x, float y, float degrees)
	{
		this(x, y, degrees);
		initialize(def);
	}
	
	/**
	 * コンストラクタ
	 * @param posture		位置情報オブジェクト
	 */
	public AbstractEffect(PostureInterface posture)
	{
		this.posture = new SlaveEffectPosture(posture);
	}
	
	/**
	 * コンストラクタ
	 * @param def			初期化用定義
	 * @param posture		位置情報オブジェクト
	 */
	public AbstractEffect(EffectDef def, PostureInterface posture)
	{
		this(posture);
		initialize(def);
	}
	
	@Override
	public void dispose()
	{
		for(RenderObject ro : renderObjects)
		{
			ro.dispose();
		}
		renderObjects.clear();
		
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
		posture.translate(
			translate	.set(velocity)
						.mul(2.0f)
						.add(currentAccel)
						.mul(deltaTime)
						.mul(0.5f)
		);
		velocity.add(currentAccel);
		
		//---- 角度
		final float currentAngularVelocityAccel = angularVelocityAccel * deltaTime;
		posture.rotate((angularVelocity * 2.0f + currentAngularVelocityAccel) * deltaTime * 0.5f);
		angularVelocity += currentAngularVelocityAccel;
		
		//---- 色
		color.r += colorVelocity.r * deltaTime;
		color.g += colorVelocity.g * deltaTime;
		color.b += colorVelocity.b * deltaTime;
		color.a += colorVelocity.a * deltaTime;
		color.clamp();
		
		for(RenderObject ro : renderObjects)
		{
			ro.setColor(color);
		}
	}
	
	/**
	 * エフェクトの寿命を設定する。
	 * @param life		寿命（秒）
	 */
	public void setLife(float life)
	{
		this.life = life;
	}
	
	/**
	 * エフェクトの速度を設定する。
	 * @param start		最初の速度（毎秒）
	 * @param end		消滅時の速度（毎秒）
	 */
	public void setVelocity(Vector2 start, Vector2 end)
	{
		setVelocity(start.x, start.y, end.x, end.y);
	}
	
	/**
	 * エフェクトの速度を設定する。
	 * @param startX		最初の速度X（毎秒）
	 * @param startY		最初の速度Y（毎秒）
	 * @param endX			消滅時の速度X（毎秒）
	 * @param endY			消滅時の速度Y（毎秒）
	 */
	public void setVelocity(float startX, float startY, float endX, float endY)
	{
		velocity.set(startX, startY);
		accel.set(endX, endY).sub(velocity).mul(1.0f / life);
	}
	
	/**
	 * エフェクトの角速度を設定する。
	 * @param start		最初の角速度（毎秒）
	 * @param end		消滅時の角速度（毎秒）
	 */
	public void setAngularVelocity(float start, float end)
	{
		angularVelocity = start;
		angularVelocityAccel = (end - start) / life;
	}
	
	/**
	 * エフェクトの色を設定する。
	 * @param start		最初の色
	 * @param end		消滅時の色
	 */
	public void setColor(Color start, Color end)
	{
		setColor(start.r, start.g, start.b, start.a, end.r, end.g, end.b, end.a);
	}
	
	/**
	 * エフェクトの色を設定する。
	 * @param startRed		最初の色の赤成分[0, 255]
	 * @param startGreen	最初の色の緑成分[0, 255]
	 * @param startBlue		最初の色の青成分[0, 255]
	 * @param startAlpha	最初の色の透明度成分[0, 255]
	 * @param endRed		消滅時の色の赤成分[0, 255]
	 * @param endGreen		消滅時の色の緑成分[0, 255]
	 * @param endBlue		消滅時の色の青成分[0, 255]
	 * @param endAlpha		消滅時の色の透明度成分[0, 255]
	 */
	public void setColor(int startRed, int startGreen, int startBlue, int startAlpha, int endRed, int endGreen, int endBlue, int endAlpha)
	{
		final float toFloat = 1.0f / 255.0f;
		setColor(startRed * toFloat, startGreen * toFloat, startBlue * toFloat, startAlpha * toFloat, endRed * toFloat, endGreen * toFloat, endBlue * toFloat, endAlpha * toFloat);
	}
	
	/**
	 * エフェクトの色を設定する。
	 * @param startRed		最初の色の赤成分[0.0, 1.0]
	 * @param startGreen	最初の色の緑成分[0.0, 1.0]
	 * @param startBlue		最初の色の青成分[0.0, 1.0]
	 * @param startAlpha	最初の色の透明度成分[0.0, 1.0]
	 * @param endRed		消滅時の色の赤成分[0.0, 1.0]
	 * @param endGreen		消滅時の色の緑成分[0.0, 1.0]
	 * @param endBlue		消滅時の色の青成分[0.0, 1.0]
	 * @param endAlpha		消滅時の色の透明度成分[0.0, 1.0]
	 */
	public void setColor(float startRed, float startGreen, float startBlue, float startAlpha, float endRed, float endGreen, float endBlue, float endAlpha)
	{
		final float invLife = 1.0f / life;
		color.set(startRed, startGreen, startBlue, startAlpha);
		
		// setメソッドを使うと自動的に 0.0 <= n <= 1.0 に丸められてしまうため、直接代入する。
		colorVelocity.r = (endRed - startRed) * invLife;
		colorVelocity.g = (endGreen - startGreen) * invLife;
		colorVelocity.b = (endBlue - startBlue) * invLife;
		colorVelocity.a = (endAlpha - startAlpha) * invLife;
	}
	
	/**
	 * エフェクトの寿命を取得する。
	 * @return		エフェクトの寿命（秒）
	 */
	public float getLife()
	{
		return life;
	}
	
	/**
	 * 描画オブジェクトの配列を取得する。
	 * @return		描画オブジェクトの配列
	 */
	public ArrayList<RenderObject> getRenderObjects()
	{
		return renderObjects;
	}

	@Override
	public float getPositionX()
	{
		return posture.getPositionX();
	}

	@Override
	public float getPositionY()
	{
		return posture.getPositionY();
	}

	@Override
	public float getRotation()
	{
		return posture.getRotation();
	}
	
	/**
	 * エフェクトが終了しているか調べる。
	 * @return		エフェクトが終了している場合はtrue
	 */
	public boolean isFinished()
	{
		return life <= 0.0f;
	}
	
	/**
	 * 描画オブジェクトを追加する。
	 * @param renderObject		描画オブジェクト
	 */
	protected void addRenderObject(RenderObject renderObject)
	{
		renderObjects.add(renderObject);
	}
	
	/**
	 * 初期化する。
	 * @param def		初期化用定義
	 */
	private void initialize(EffectDef def)
	{
		//---- 各種設定
		setLife(def.life);
		setVelocity(def.startVelocity, def.endVelocity);
		setAngularVelocity(def.startAngularVelocity, def.endAngularVelocity);
		setColor(def.startColor, def.endColor);
	}
	
	
	/** 描画オブジェクト */
	private ArrayList<RenderObject>	renderObjects = new ArrayList<RenderObject>(1);
	
	/** 位置情報 */
	private final EffectPosture posture;
	
	/** 寿命（単位；秒） */
	private float	life = 0.0f;
	
	/** 速度 */
	private final Vector2	velocity	= new Vector2(0.0f, 0.0f);
	
	/** 加速度 */
	private final Vector2	accel	= new Vector2(0.0f, 0.0f);
	
	/** 角速度 */
	private float	angularVelocity = 0.0f;
	
	/** 角加速度 */
	private float	angularVelocityAccel = 0.0f;
	
	/** 色 */
	private final Color	color	= new Color(Color.WHITE);
	
	/** 色の変化量 */
	private final Color	colorVelocity	= new Color(0.0f, 0.0f, 0.0f, 0.0f);
	
	/** 移動量計算に一時的に使用する為のVector2オブジェクト */
	private static final Vector2	translate	= new Vector2();
	
	
	/**
	 * エフェクトの位置情報の基本クラス
	 */
	private class EffectPosture implements PostureInterface
	{
		/**
		 * コンストラクタ
		 * @param x				X座標
		 * @param y				Y座標
		 * @param degrees		角度（度）
		 */
		public EffectPosture(float x, float y, float degrees)
		{
			position.set(x, y);
			angle = degrees;
		}
		
		/**
		 * 移動する。
		 * @param translate		移動量
		 */
		public void translate(Vector2 translate)
		{
			position.add(translate);	
		}
		
		/**
		 * 回転する。
		 * @param degrees		回転量（度）
		 */
		public void rotate(float degrees)
		{
			angle += degrees;
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
		
		
		/** 座標 */
		private final Vector2 position = new Vector2();
		
		/** 角度（単位；度） */
		private float angle;
	}
	
	/**
	 * 何かに追従するエフェクトの位置情報
	 */
	private class SlaveEffectPosture extends EffectPosture
	{
		/**
		 * コンストラクタ
		 * @param posture		追従対象となる位置情報オブジェクト
		 */
		public SlaveEffectPosture(PostureInterface posture)
		{
			super(0.0f, 0.0f, 0.0f);
			
			target = posture;
		}
		
		@Override
		public float getPositionX()
		{
			return target.getPositionX() + super.getPositionX();
		}

		@Override
		public float getPositionY()
		{
			return target.getPositionY() + super.getPositionY();
		}

		@Override
		public float getRotation()
		{
			return target.getRotation() + super.getRotation();
		}
		
		
		/** 追従対象となる位置情報オブジェクト */
		private final PostureInterface target;
	}
}
