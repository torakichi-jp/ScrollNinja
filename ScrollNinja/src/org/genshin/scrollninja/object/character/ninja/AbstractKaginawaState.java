package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.object.character.AbstractCharacter;

import com.badlogic.gdx.math.Vector2;

/**
 * 鉤縄状態の忍者処理の基本クラス。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
abstract class AbstractKaginawaState extends AbstractState
{
	@Override
	public StateInterface update(AbstractNinja me, float deltaTime)
	{
		//---- 前方ベクトルを強制的にX軸にする。
		me.setFrontDirection(Vector2.X);

		//---- 姿勢を起こす
		nearRotate(me, 0.0f, 0.1f);
		
		//---- あとは基本クラスに任せる。
		return super.update(me, deltaTime);
	}
	
	@Override
	protected void updateMove(AbstractNinja me, float deltaTime)
	{
		// とりあえず　なにもしない
	}

	@Override
	protected void updateJump(AbstractNinja me)
	{
		// とりあえず　なにもしない
	}

	@Override
	protected void updateAttack(AbstractCharacter me)
	{
		// とりあえず　なにもしない
	}

	@Override
	protected StateInterface getNextState(AbstractNinja me)
	{
		//---- 通常の状態へ
		if( me.getKaginawa().isReleaseState() )
		{
			// 地上
			if( me.isGrounded() )
			{
				return new GroundedState(me);
			}
			// 空中
			else
			{
				me.updateMoveDirection();
				return new AerialState(me);
			}
		}
		
		//---- どれにも当てはまらなければ現状維持
		return this;
	}
}
