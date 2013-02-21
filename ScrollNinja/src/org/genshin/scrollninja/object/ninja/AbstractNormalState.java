package org.genshin.scrollninja.object.ninja;

import org.genshin.scrollninja.object.ninja.controller.NinjaControllerInterface;


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
	protected void updateMove(AbstractNinja me, float deltaTime)
	{
		final NinjaControllerInterface controller = me.getController();
		
		//---- 移動初めの検出
		if(controller.isMoveStart())
		{
			me.updateMoveDirection();
		}
		
		//---- 移動入力があれば移動する。
		final float movePower = controller.getMovePower() * me.getMoveDirection() * deltaTime;
		if(movePower != 0.0f)
		{
			// ダッシュ
			if( controller.isDash() )
			{
				move(me, movePower, NinjaDefine.INSTANCE.DASH_ACCEL, NinjaDefine.INSTANCE.DASH_MAX_VELOCITY);
				me.setAnimation("Dash");
			}
			// 走り
			else
			{
				move(me, movePower, NinjaDefine.INSTANCE.RUN_ACCEL, NinjaDefine.INSTANCE.RUN_MAX_VELOCITY);
				me.setAnimation("Run");
			}
		}
		//---- 移動入力がなければアニメーションを待機状態に変更する。
		else
		{
			me.setAnimation(getStayAnimationName());
		}
	}
	
	@Override
	protected void updateAttack(AbstractNinja me)
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void updateKaginawa(AbstractNinja me)
	{
		final NinjaControllerInterface controller = me.getController();
		
		//---- 鉤縄を伸ばす
		if(controller.isKaginawaSlack())
		{
//			me.kaginawa.slack(controller.getDirection());
		}
		//---- 鉤縄を縮める
		else if(controller.isKaginawaShrink())
		{
//			me.kaginawa.shrink();
		}
	}

	@Override
	protected StateInterface getNextState(AbstractNinja me)
	{
		//---- 鉤縄が縮み始めたら、鉤縄が縮んでいる時の状態へ
//		if( me.kaginawa.isShrinkState() )
//		{
//			return new KaginawaShrinkState(me);
//		}
		
		//---- どれにも当てはまらなければ現状維持
		return this;
	}
	
	/**
	 * 待機状態のアニメーション名を取得する。
	 * @return		待機状態のアニメーション名
	 */
	protected String getStayAnimationName()
	{
		return "Stay";
	}
}
