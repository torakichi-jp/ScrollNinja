package org.genshin.scrollninja.object.ninja;

import org.genshin.scrollninja.object.ninja.controller.NinjaControllerInterface;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;


/**
 * 地面と接触している時の忍者の処理。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class GroundedState extends AbstractNormalState
{
	/**
	 * コンストラクタ
	 * @param me		自身を示す忍者オブジェクト
	 */
	GroundedState(AbstractNinja me)
	{
		//---- 鉤縄のロープジョイントフラグを立てておく
		me.getKaginawa().setUseRopeJoint(true);
		
		//---- 摩擦を有効にする。
		me.setFrictionEnabled(true);
	}
	
	@Override
	public void collisionTerrain(AbstractNinja me, Contact contact)
	{
		//---- 衝突したのが下半身でなければ何もしない。
		if( !checkContactIsFoot(me, contact) )
			return;
		
		//---- 衝突したのが下半身なら地面に立っている時の処理を加える。
		// 地上状態へ
		me.toGrounded();
		
		// 前方ベクトルを設定
		final Vector2 terrainNormal = contact.getWorldManifold().getNormal();
		me.setFrontDirection(terrainNormal.y, -terrainNormal.x);
		
		// ジャンプ方向ベクトルを設定
		final Vector2 frontDirection = me.getFrontDirection();
		if( Math.abs(frontDirection.x) > Math.abs(frontDirection.y) )
		{
			me.setJumpDirection(0.0f, frontDirection.x<0.0f?-1.0f:1.0f);
		}
		else
		{
			me.setJumpDirection(
				Vector2.tmp
					.set(frontDirection.y<0.0f?1.0f:-1.0f, frontDirection.x<0.0f?-1.0f:1.0f)
					.nor()
			);
		}
	
		// キャラの角度を補正
		// TODO なだらかな坂に真っ直ぐ立つ処理（仮）　現在は発動しないようにしてある。
		final float normalAngle = terrainNormal.angle();
		final float verticalAngleZone = 0.0f;
		final float rotateTime = 0.1f;
		if( Math.abs(normalAngle % 180.0f - 90.0f) < verticalAngleZone )
		{
			nearRotate( me, (me.getJumpDirection().angle()-90.0f) * MathUtils.degreesToRadians, rotateTime );
		}
		else
		{
			nearRotate( me, (normalAngle-90.0f) * MathUtils.degreesToRadians, rotateTime );
		}
	}

	@Override
	protected void updateMove(AbstractNinja me, float deltaTime)
	{
		//---- 基本クラスの処理を実行する。
		super.updateMove(me, deltaTime);
		
		//---- エフェクトを生成する。
		final NinjaControllerInterface controller = me.getController();
		final float movePower = controller.getMovePower() * me.getMoveDirection();
		if(		controller.isDashStart() && movePower != 0.0f
			||	controller.isMoveStart() && controller.isDash()	)
		{
//			if(movePower != 0.0f)
//				new DashSmokeEffect(me.getPositionX(), me.getPositionY(), me.getFrontDirection().angle(), movePower > 0.0f);
		}
	}

	@Override
	protected void updateJump(AbstractNinja me)
	{
		final NinjaControllerInterface controller = me.getController();
		
		if( isSnapCeiling(me) )
		{
			if( controller.isLeaveSnapCeiling() )
			{
				leaveSnapCeiling(me);
			}
		}
		else if( controller.isJump() )
		{
			jump(me);
		}
	}

	@Override
	protected StateInterface getNextState(AbstractNinja me)
	{
		//---- 足が地面から離れていれば空中状態へ
		if( me.isAerial() )
		{
			return new AerialState(me);
		}
		
		//---- 地上で鉤縄にぶら下がっている状態へ
		if( me.getKaginawa().isHangState() )
		{
//			return new GroundedToKaginawaState(me);
		}
		
		//---- あとは基本クラスに任せる
		return super.getNextState(me);
	}
	
	/**
	 * 天井に吸着しているか調べる。
	 * @param me		自身を示す忍者オブジェクト
	 * @return			天井に吸着している場合はtrue
	 */
	private boolean isSnapCeiling(AbstractNinja me)
	{
		return me.getJumpDirection().y < 0.0f;
	}
}
