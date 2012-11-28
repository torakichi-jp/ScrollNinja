package org.genshin.scrollninja.object.character.ninja;

import com.badlogic.gdx.math.Vector2;


/**
 * 空中にいる（地面と接触していない）時の忍者の処理。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class AerialState extends AbstractNormalState
{
	@Override
	public StateInterface update(PlayerNinja me, float deltaTime)
	{
		//---- 初回フレームに限り、地上フラグが残っているのでへし折る
		if(firstFrame)
		{
			me.groundedTimer = 0;
			firstFrame = false;
		}
		
		//---- 前方ベクトルを強制的にX軸にする。
		me.frontDirection.set(Vector2.X);
		
		//---- ジャンプ方向ベクトルを強制的にY軸にする。
		me.jumpDirection.set(Vector2.Y);
		
		//---- 姿勢を起こす
		nearRotate(me, 0.0f, 0.1f);
		
		//---- 連続ジャンプ時は摩擦を無視する
		me.getFootFixture().setFriction(me.controller.isJump() ? 0.0f : me.defaultFriction);
		
		//---- あとは基本クラスに任せる。
		return super.update(me, deltaTime);
	}
	
	@Override
	protected void updateJump(PlayerNinja me)
	{
		if( me.controller.isAerialJump() && me.restAerialJumpCount > 0 )
		{
			jump(me);
			me.restAerialJumpCount--;
		}
	}

	@Override
	protected StateInterface getNextState(PlayerNinja me)
	{
		//---- 地面と接触したら地上状態へ
		if( me.isGrounded() )
		{
			me.getFootFixture().setFriction(me.defaultFriction);
			return new GroundedState();
		}
		
		//---- 鉤縄にぶら下がっている状態へ
		if( me.kaginawa.isHangState() )
		{
			return new KaginawaHangState();
		}
		
		//---- あとは基本クラスに任せる
		return super.getNextState(me);
	}
	
	/** 初回フレームフラグ */
	boolean firstFrame = true;
}
