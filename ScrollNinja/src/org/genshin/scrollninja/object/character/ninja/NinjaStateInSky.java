package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.object.character.ninja.controller.NinjaControllerInterface;

/**
 * 空中にいる状態。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class NinjaStateInSky extends AbstractNinjaState
{
	/**
	 * コンストラクタ
	 * @param me	自身となる忍者オブジェクト
	 */
	public NinjaStateInSky(PlayerNinja me)
	{
		super(me);
	}

	@Override
	AbstractNinjaState update()
	{
		PlayerNinja me = getMe();
		NinjaControllerInterface controller = me.getNinjaController();
		
		me.move();
		me.jump();
		me.kaginawa();
		
		if(me.IsGrounded())
		{
			return new NinjaStateOnGround(me);
		}
		else
		{
			me.nearRotate(0.0f);
		}
		
		return this;
	}
}
