package org.genshin.scrollninja.object.character.ninja;

class GroundedKaginawaHangNinjaState extends GroundedNinjaState
{
	@Override
	protected NinjaStateInterface getNextState(PlayerNinja me)
	{
		//---- 足が地面から離れていればジャンプ状態へ
		if( !me.isGrounded() )
		{
			return new AerialKaginawaHangNinjaState();
		}

		//---- 鉤縄が縮み始めたら、鉤縄が縮んでいる時の状態へ
		if( me.kaginawa.isShrinkState() )
		{
			return new KaginawaShrinkNinjaState();
		}
		
		//---- どれにも当てはまらなければ現状維持
		return this;
	}
}
