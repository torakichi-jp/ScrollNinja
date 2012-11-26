package org.genshin.scrollninja.object.character.ninja;

import com.badlogic.gdx.math.Vector2;

/**
 * 鉤縄状態の忍者処理の基本クラス。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
abstract class AbstractKaginawaNinjaState extends AbstractNinjaState
{
	@Override
	public NinjaStateInterface update(PlayerNinja me, float deltaTime)
	{
		//---- 前方ベクトルを強制的にX軸にする。
		me.frontDirection.set(Vector2.X);
		
		//---- 姿勢を起こす
		nearRotate(me, 0.0f, 0.1f);
		
		//---- 地面との接触フラグをへし折っておく
		me.groundedTimer = 0;
		
		//---- あとは基本クラスに任せる。
		return super.update(me, deltaTime);
	}
	
	@Override
	protected void updateMove(PlayerNinja me, float deltaTime)
	{
		// とりあえず　なにもしない
	}

	@Override
	protected void updateJump(PlayerNinja me)
	{
		// とりあえず　なにもしない
	}

	@Override
	protected NinjaStateInterface getNextState(PlayerNinja me)
	{
		//---- 通常の状態へ
		if( me.kaginawa.isReleaseState() )
		{
			// 地上
			if( me.isGrounded() )
			{
				return new GroundedNinjaState();
			}
			// 空中
			else
			{
				return new AerialNinjaState();
			}
		}
		
		//---- どれにも当てはまらなければ現状維持
		return this;
	}
}
