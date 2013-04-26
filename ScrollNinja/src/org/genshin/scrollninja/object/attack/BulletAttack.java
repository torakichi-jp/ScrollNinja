package org.genshin.scrollninja.object.attack;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.render.AnimationRenderObject;
import org.genshin.scrollninja.render.RenderObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class BulletAttack extends AbstractAttack
{
	/**
	 * コンストラクタ
	 * @param attackFilePath		初期化用定義ファイルのパス
	 * @param world					所属する世界
	 * @param owner					攻撃の所有者
	 */
	public BulletAttack(String attackFilePath, World world, AbstractCharacter owner)
	{
		this(AttackDefFactory.getInstance().get(attackFilePath), world, owner);
	}
	
	/**
	 * コンストラクタ
	 * @param def		初期化用定義
	 * @param world		所属する世界
	 * @param owner		攻撃の所有者
	 */
	public BulletAttack(AttackDef def, World world, AbstractCharacter owner)
	{
		super(def.collisionFilePath, world, def.power, owner);
		
		//---- 描画オブジェクトを生成する。
		final BulletType type = (BulletType)def.type;
		final int renderDepth = GlobalDefine.RenderDepth.EFFECT;
		
		// アニメーションなし
		if(type.animationFilePath == null || type.animationFilePath.isEmpty() || type.animationName == null || type.animationName.isEmpty())
		{
			renderObject = new RenderObject(type.spriteFilePath, this, renderDepth);
		}
		
		// アニメーションあり
		else
		{
			final AnimationRenderObject aro = new AnimationRenderObject(type.spriteFilePath, type.animationFilePath, this, renderDepth);
			aro.setAnimation(type.animationName);
			renderObject = aro;
		}
		
		// 最初は非表示にしておく。
		renderObject.setRenderEnabled(false);
		
		//---- 衝突まわり
		velocity = type.velocity;
		angularVelocity = type.angularVelocity;
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
		/* 何もしない */
	}
	
	@Override
	public void fire()
	{
		final Body body = getCollisionObject().getBody();
		final AbstractCharacter owner = getOwner();
		
		//---- 座標を設定する。
		body.setTransform(owner.getPositionX(), owner.getPositionY(), 0.0f);
		
		//---- 速度を設定する。
		final Vector2 direction = owner.getLookAtDirection().tmp();
		body.setLinearVelocity(direction.mul(velocity));
		body.setAngularVelocity(angularVelocity);
		
		//---- 活動状態へ移行する。
		toActive();
	}
	
	
	@Override
	protected void toActive()
	{
		renderObject.setRenderEnabled(true);
		
		super.toActive();
	}

	@Override
	protected void toSleep()
	{
		renderObject.setRenderEnabled(false);
		
		super.toSleep();
	}


	/** 描画オブジェクト */
	private final RenderObject renderObject;
	
	/** 速度 */
	private final float velocity;
	
	/** 回転速度 */
	private final float angularVelocity;
}
