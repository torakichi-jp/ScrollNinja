package org.genshin.scrollninja.object.character.ninja;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

class SnapTerrainNinjaState extends AbstractNinjaState
{
	@Override
	public void collisionTerrain(PlayerNinja me, Contact contact)
	{
		//---- 衝突したのが下半身なら吸着完了！
		final Fixture footFixture = me.getFootFixture();
		if(contact.getFixtureA() == footFixture || contact.getFixtureB() == footFixture)
		{
			snapComplete = true;
			me.kaginawa.release();
			me.getBody().setLinearVelocity(Vector2.Zero);
		}
		
		//---- 衝突したのが下半身なら地面に立っている時の処理を加える。
		// 空中ジャンプのカウント初期化
		me.restAerialJumpCount = NinjaParam.INSTANCE.AERIAL_JUMP_COUNT;
		
		// 地面との衝突判定用タイマー初期化
		me.groundedTimer = NinjaParam.INSTANCE.GROUNDED_JUDGE_TIME;
		
		// 前方ベクトルを設定
		final Vector2 normal = contact.getWorldManifold().getNormal();
		me.frontDirection.set(normal.y, -normal.x);

		// キャラの角度を補正
		nearRotate( me, (float)Math.toRadians(normal.angle() - 90.0f), 0.1f );
	}

	@Override
	protected void updateMove(PlayerNinja me, float deltaTime)
	{
		// なにもしない
	}

	@Override
	protected void updateJump(PlayerNinja me)
	{
		// なにもしない
	}

	@Override
	protected void updateKaginawa(PlayerNinja me)
	{
		// なにもしない
	}

	@Override
	protected NinjaStateInterface getNextState(PlayerNinja me)
	{
		//---- 吸着処理が完了したら地上状態へ
		if(snapComplete)
		{
			return new GroundedNinjaState();
		}
		
		//---- どれにも当てはまらなければ現状維持
		return this;
	}
	
	/** 吸着完了したよフラグ */
	boolean snapComplete = false;
}
