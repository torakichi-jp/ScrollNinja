package org.genshin.scrollninja.object.ninja;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.object.effect.JumpSmokeEffect;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

class SnapTerrainState extends AbstractState
{
	@Override
	public void collisionTerrain(AbstractNinja me, Contact contact)
	{
		final Vector2 normal = contact.getWorldManifold().getNormal();
		
		//---- 衝突したのが下半身なら吸着完了！
		if( checkContactIsFoot(me, contact) )
		{
			final Body body = me.getBody();
			final float safeVelocity = 200.0f * GlobalDefine.INSTANCE.WORLD_SCALE;
			
			if(body.getLinearVelocity().len2() < safeVelocity * safeVelocity)
			{
				snapComplete = true;
				me.kaginawa.release();
				body.setLinearVelocity(Vector2.Zero);
				
				//---- 地上フラグを立てておく。
				me.groundedTimer = NinjaDefine.INSTANCE.GROUNDED_JUDGE_TIME;
				
				//---- 前方ベクトルを設定しておく。
				me.frontDirection.set(normal.y, -normal.x);
			}
		}

		//---- キャラの角度を補正
		nearRotate( me, (normal.angle() - 90.0f) * MathUtils.degreesToRadians, 0.1f );
	}

	@Override
	protected void updateMove(AbstractNinja me, float deltaTime)
	{
		// なにもしない
	}

	@Override
	protected void updateJump(AbstractNinja me)
	{
		// なにもしない
	}

	@Override
	protected void updateKaginawa(AbstractNinja me)
	{
		// なにもしない
	}

	@Override
	protected StateInterface getNextState(AbstractNinja me)
	{
		//---- 吸着処理が完了したら地上状態へ
		if(snapComplete)
		{
			if(me.controller.getMovePower() != 0.0f)
			{
				me.updateMoveDirection();
			}
			new JumpSmokeEffect(me.getPositionX(), me.getPositionY(), me.frontDirection.angle());
			return new GroundedState(me);
		}
		
		//---- どれにも当てはまらなければ現状維持
		return this;
	}
	
	/** 吸着完了したよフラグ */
	boolean snapComplete = false;
}
