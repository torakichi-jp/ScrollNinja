package org.genshin.scrollninja.object.attack;

import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.effect.AbstractEffect;
import org.genshin.scrollninja.object.effect.FileEffect;
import org.genshin.scrollninja.render.RenderObject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class SlashAttack extends AbstractAttack
{
	/**
	 * コンストラクタ
	 * @param attackFilePath	攻撃の初期化用定義ファイルのパス
	 * @param world				所属する世界
	 * @param owner				攻撃の所有者
	 */
	public SlashAttack(String attackFilePath, World world, AbstractCharacter owner)
	{
		this(AttackDefFactory.getInstance().get(attackFilePath), world, owner);
	}
	
	/**
	 * コンストラクタ
	 * @param def		攻撃の初期化用定義
	 * @param world		所属する世界
	 * @param owner		攻撃の所有者
	 */
	public SlashAttack(AttackDef def, World world, AbstractCharacter owner)
	{
		super(def.collisionFilePath, world, def.power, owner);
		
		final SlashType type = (SlashType)def.type;
		effectFilePath = type.effectFilePath;
	}
	
	@Override
	public void dispose()
	{
		//---- このクラスを破棄する。
		if(effect != null)
		{
			effect.dispose();
			effect = null;
		}
		
		//---- 基本クラスを破棄する。
		super.dispose();
	}

	@Override
	public void update(float deltaTime)
	{
		//---- 待機状態なら何もしない。
		if(isSleep())
			return;
		
		//---- 衝突判定、描画オブジェクトを更新する。
		angle += angularVelocity * deltaTime;
		updateCollisionObject();
		
		final AbstractCharacter owner = getOwner();
		final boolean isFlipX = owner.isFlipX();
		final boolean isFlipY = owner.isFlipY();
		for(RenderObject ro : effect.getRenderObjects())
		{
			ro.flip(isFlipX, isFlipY);
		}
		
		//---- エフェクトが消えたら斬撃終了。
		if(effect.isFinished())
		{
			effect = null;
			toSleep();
		}
	}
	
	@Override
	public void fire()
	{
		//---- エフェクトを発生させる。
		effect = new FileEffect(effectFilePath, this);
		
		//---- 回転の初期設定
		if(angularVelocity == null)
		{
			angularVelocity = new Float((125.0f * MathUtils.degreesToRadians) / effect.getLife());
		}
		angle = 0.0f;
		
		//---- 活動状態へ移行する。
		toActive();
	}

	@Override
	public float getPositionX()
	{
		return getOwner().getPositionX();
	}

	@Override
	public float getPositionY()
	{
		return getOwner().getPositionY();
	}

	@Override
	public float getRotation()
	{
		return getOwner().getRotation();
	}

	@Override
	public boolean isSleep()
	{
		return effect == null;
	}
	
	/**
	 * 衝突判定を更新する。
	 */
	private void updateCollisionObject()
	{
		final Body body = getCollisionObject().getBody();
		final float flipRatio = getOwner().isFlipX() ? -1.0f : 1.0f;
		body.setAngularVelocity(angularVelocity * flipRatio);
		body.setTransform(getPositionX(), getPositionY(), (getRotation() - 90.0f) * MathUtils.degreesToRadians + angle * flipRatio);
	}
	
	
	/** エフェクトの初期化用定義ファイルのパス */
	private final String effectFilePath;
	
	/** エフェクトオブジェクト */
	private AbstractEffect effect;
	
	/** 斬撃の角度 */
	private float angle;
	
	/** 角速度（radian/秒） */
	private static Float angularVelocity;
}
