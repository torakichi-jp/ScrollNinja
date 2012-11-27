package org.genshin.scrollninja.object.character.ninja;


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
			return new GroundedToKaginawaNinjaState();
		}
		
		//---- あとは基本クラスに任せる
		return super.getNextState(me);
	}
}
