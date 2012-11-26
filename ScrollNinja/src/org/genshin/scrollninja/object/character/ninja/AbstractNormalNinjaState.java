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
abstract class AbstractNormalNinjaState extends AbstractNinjaState
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
		final boolean useWorldManifold = true;
		if(useWorldManifold)
		{
			final Vector2 normal = contact.getWorldManifold().getNormal();
			me.frontDirection.set(normal.y, -normal.x);

			// キャラの角度を補正
			nearRotate( me, (float)Math.toRadians(normal.angle() - 90.0f), 0.1f );
		}
		else
		{
			final float angle = me.getBody().getAngle();
			me.frontDirection.set( (float)Math.cos(angle), (float)Math.sin(angle) );
			
			// キャラの角度を補正
			nearRotate( me, (float)Math.toRadians(contact.getWorldManifold().getNormal().angle() - 90.0f), 0.1f );
		}
	}
	
	@Override
	protected void updateMove(PlayerNinja me, float deltaTime)
	{
		//---- 移動入力があれば移動する。
		final float movePower = me.controller.getMovePower() * deltaTime;
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
		//---- 移動入力がなければブレーキをかける。
		else
		{
			updateBrake(me);
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
	
	/**
	 * ブレーキの更新処理を実行する。
	 * @param me		自身を示す忍者オブジェクト
	 */
	protected void updateBrake(PlayerNinja me)
	{
		me.setAnimation("Stay");
	}

	@Override
	protected NinjaStateInterface getNextState(PlayerNinja me)
	{
		//---- 鉤縄が縮み始めたら、鉤縄が縮んでいる時の状態へ
		if( me.kaginawa.isShrinkState() )
		{
			return new KaginawaShrinkNinjaState();
		}
		
		//---- どれにも当てはまらなければ現状維持
		return this;
	}
}
