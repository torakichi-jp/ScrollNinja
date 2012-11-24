package org.genshin.scrollninja.object.character.ninja;

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
	
	/**
	 * ブレーキの更新処理を実行する。
	 * @param me		自身を示す忍者オブジェクト
	 */
	protected void updateBrake(PlayerNinja me)
	{
		me.setAnimation("Stay");
	}
}
