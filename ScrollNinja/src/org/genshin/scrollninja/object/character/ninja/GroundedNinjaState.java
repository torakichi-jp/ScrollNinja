package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.GlobalParam;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * 地面と接触している時の忍者の処理。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class GroundedNinjaState extends AbstractNormalNinjaState
{
	@Override
	public NinjaStateInterface update(PlayerNinja me, float deltaTime)
	{
		//---- 地面との衝突判定用タイマーを更新する。
		--me.groundedTimer;
		
		//---- あとは基本クラスに任せる。
		return super.update(me, deltaTime);
	}

	@Override
	protected void updateBrake(PlayerNinja me)
	{
		//---- ブレーキをかける。
		// TODO ブレーキはbox2dの摩擦力に任せるべき？
		final Body body = me.getBody();
		final Vector2 velocity = body.getLinearVelocity();
		velocity.mul(0.95f);
		if(velocity.len2() < GlobalParam.INSTANCE.WORLD_SCALE)
		{
			velocity.set(Vector2.Zero);
		}
		body.setLinearVelocity(velocity);
		
		//---- 基本クラスの処理も実行する。
		super.updateBrake(me);
	}

	@Override
	protected void updateJump(PlayerNinja me)
	{
		if( me.controller.isJump() )
		{
			jump(me);
		}
	}

	@Override
	protected NinjaStateInterface getNextState(PlayerNinja me)
	{
		//---- 足が地面から離れていれば空中状態へ
		if( !me.isGrounded() )
		{
			return new AerialNinjaState();
		}
		
		//---- 地上で鉤縄にぶら下がっている状態へ
		if( me.kaginawa.isHangState() )
		{
			return new GroundedKaginawaHangNinjaState();
		}
		
		//---- あとは基本クラスに任せる
		return super.getNextState(me);
	}
}
