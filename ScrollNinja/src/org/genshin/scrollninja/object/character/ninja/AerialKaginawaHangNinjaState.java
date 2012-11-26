package org.genshin.scrollninja.object.character.ninja;

class AerialKaginawaHangNinjaState extends AerialNinjaState
{
	@Override
	protected NinjaStateInterface getNextState(PlayerNinja me)
	{
		//---- 地面と接触したら通常状態へ
		if( me.isGrounded() )
		{
			return new GroundedKaginawaHangNinjaState();
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
