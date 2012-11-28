package org.genshin.scrollninja.object.character.ninja;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * 通常状態の忍者処理の基本クラス。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
abstract class AbstractNormalState extends AbstractState
{
	@Override
	public void collisionTerrain(PlayerNinja me, Contact contact)
	{
		//---- 衝突したのが下半身でなければ何もしない。
		final Fixture footFixture = me.getFootFixture();
		if(contact.getFixtureA() != footFixture && contact.getFixtureB() != footFixture)
			return;
		
		//---- 衝突したのが下半身なら地面に立っている時の処理を加える。
		// 空中ジャンプのカウント初期化
		me.restAerialJumpCount = NinjaParam.INSTANCE.AERIAL_JUMP_COUNT;
		
		// 地面との衝突判定用タイマー初期化
		me.groundedTimer = NinjaParam.INSTANCE.GROUNDED_JUDGE_TIME;
		
		// 前方ベクトルを設定
		final Vector2 normal = contact.getWorldManifold().getNormal();
		me.frontDirection.set(normal.y, -normal.x);
		
		// ジャンプ方向ベクトルを設定
		if( Math.abs(me.frontDirection.x) > Math.abs(me.frontDirection.y) )
		{
			me.jumpDirection.set(0.0f, me.frontDirection.x<0.0f?-1.0f:1.0f);
		}
		else
		{
			me.jumpDirection.set(me.frontDirection.y<0.0f?1.0f:-1.0f, 0.0f);
		}

		// キャラの角度を補正
		// TODO なだらかな坂に真っ直ぐ立つ処理（仮）　現在は発動しないようにしてある。
		final float normalAngle = normal.angle();
		final float verticalAngleZone = 0.0f;
		final float rotateTime = 0.1f;
		if( Math.abs(normalAngle % 180.0f - 90.0f) < verticalAngleZone )
		{
			nearRotate( me, (float)Math.toRadians(me.jumpDirection.angle()-90.0f), rotateTime );
		}
		else
		{
			nearRotate( me, (float)Math.toRadians(normalAngle-90.0f), rotateTime );
		}
	}
	
	@Override
	protected void updateMove(PlayerNinja me, float deltaTime)
	{
		if(me.controller.isMoveStart())
		{
			me.updateMoveDirection();
		}
		
		//---- 移動入力があれば移動する。
		final float movePower = me.controller.getMovePower() * me.moveDirection * deltaTime;
		if(movePower != 0.0f)
		{
			// ダッシュ
			if( me.controller.isDash() )
			{
				move(me, movePower, NinjaParam.INSTANCE.DASH_ACCEL, NinjaParam.INSTANCE.DASH_MAX_VELOCITY);
				me.setAnimation("Dash");
			}
			// 走り
			else
			{
				move(me, movePower, NinjaParam.INSTANCE.RUN_ACCEL, NinjaParam.INSTANCE.RUN_MAX_VELOCITY);
				me.setAnimation("Run");
			}
		}
		//---- 移動入力がなければアニメーションを待機状態に変更する。
		else
		{
			me.setAnimation("Stay");
		}
	}
	
	@Override
	protected void updateKaginawa(PlayerNinja me)
	{
		if( me.controller.isKaginawaSlack() )
		{
			me.kaginawa.slack(me.controller.getDirection());
		}
		else if( me.controller.isKaginawaShrink() )
		{
			me.kaginawa.shrink();
		}
	}

	@Override
	protected StateInterface getNextState(PlayerNinja me)
	{
		//---- 鉤縄が縮み始めたら、鉤縄が縮んでいる時の状態へ
		if( me.kaginawa.isShrinkState() )
		{
			return new KaginawaShrinkState();
		}
		
		//---- どれにも当てはまらなければ現状維持
		return this;
	}
}
