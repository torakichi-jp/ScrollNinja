package org.genshin.scrollninja.object.character.ninja;

import com.badlogic.gdx.math.Vector2;


/**
 * 空中にいる（地面と接触していない）時の忍者の処理。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class AerialNinjaState extends AbstractNormalNinjaState
{
	@Override
	public NinjaStateInterface update(PlayerNinja me, float deltaTime)
	{
		//---- 前方ベクトルを強制的にX軸にする。
		me.frontDirection.set(Vector2.X);
		
		//---- 姿勢を起こす
		nearRotate(me, 0.0f, 0.1f);
		
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
	protected NinjaStateInterface getNextState(PlayerNinja me)
	{
		//---- 地面と接触したら通常状態へ
		if( me.isGrounded() )
		{
			return new GroundedNinjaState();
		}
		
		//---- どれにも当てはまらなければ現状維持
		return this;
	}
}
