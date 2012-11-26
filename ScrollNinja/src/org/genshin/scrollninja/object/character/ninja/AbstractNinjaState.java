package org.genshin.scrollninja.object.character.ninja;

import java.util.logging.Logger;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * 忍者の状態の基本クラス。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
abstract class AbstractNinjaState implements NinjaStateInterface
{
	@Override
	public NinjaStateInterface update(PlayerNinja me, float deltaTime)
	{
		//---- 各種処理を実行する。
		updateMove(me, deltaTime);		// 移動
		updateJump(me);					// ジャンプ
		updateKaginawa(me);				// 鉤縄
		updateGravity(me, deltaTime);	// 重力
		
		//---- 次の状態を返す。
		return getNextState(me);
	}

	/**
	 * 180度以下の範囲で回転する。
	 * @param me		自身を示す忍者オブジェクト
	 * @param radian	回転後の角度（ラジアン）
	 * @param time		回転するのにかかる時間（秒）
	 */
	protected final void nearRotate(PlayerNinja me, float radian, float time)
	{
		final Body body = me.getBody();

		//---- 角度計算
		float angularVelocity = radian - body.getAngle();

		// -360～360度の範囲に丸める
		angularVelocity = (float)( Math.abs(angularVelocity) % (Math.PI*2.0f) * Math.signum(angularVelocity) );
		
		// -180～180度の範囲に丸める
		if(Math.abs(angularVelocity) > Math.PI)
			angularVelocity -= (float)( Math.signum(angularVelocity) * Math.PI * 2.0f );
		
		// 回転するのにかかる時間を適用する
		angularVelocity /= time;

		body.setAngularVelocity(angularVelocity);
	}
	
	/**
	 * 移動処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 * @param movePower		移動入力の強さ
	 * @param accel			加速度
	 * @param maxVelocity	制限速度
	 */
	protected final void move(PlayerNinja me, float movePower, float accel, float maxVelocity)
	{
		final Body body = me.getBody();
		final Vector2 oldVelocity = body.getLinearVelocity().tmp();		// 古い速度
		
		//---- 加速度を加える
		body.applyLinearImpulse(accel*movePower*me.frontDirection.x, accel*movePower*me.frontDirection.y, me.getPositionX(), me.getPositionY());
		
		//---- 速度に制限をかける
		final Vector2 newVelocity = body.getLinearVelocity();		// 新しい速度
		final float maxVelocity2 = maxVelocity * maxVelocity;		// maxVelocityの2乗
		final float newVelocityLen2 = newVelocity.len2();
		
		// 制限速度を越えた場合
		if( newVelocityLen2 > maxVelocity2 )
		{
			final float oldVelocityLen2 = oldVelocity.len2();
			// 最初から制限速度を越えていた場合
			if( oldVelocityLen2 > maxVelocity2 )
			{
				// 加速度を加えた結果さらに加速していた場合、この加速はなかったことにする
				if( newVelocityLen2 > oldVelocityLen2 )
				{
					body.setLinearVelocity(oldVelocity);
				}
			}
			// 加速度を加えた結果、制限速度を越えてしまった場合、丸める
			else
			{
				body.setLinearVelocity( newVelocity.nor().mul(maxVelocity) );
			}
		}
	}

	/**
	 * ジャンプ処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 */
	protected final void jump(PlayerNinja me)
	{
		final Body body = me.getBody();
		
		// 一度Y方向の力を殺す
		body.setLinearVelocity(body.getLinearVelocity().x, 0.0f);
		
		// ジャンプ力を与える
		body.applyLinearImpulse(0.0f, NinjaParam.INSTANCE.JUMP_POWER, me.getPositionX(), me.getPositionY());
		
		// 地面との接触判定を消し飛ばす
		me.groundedTimer = 0;
	}
	
	/**
	 * 移動の更新処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 * @param deltaTime		経過時間（秒）
	 */
	protected abstract void updateMove(PlayerNinja me, float deltaTime);
	
	/**
	 * ジャンプの更新処理を実行する。
	 * @param me		自身を示す忍者オブジェクト
	 */
	protected abstract void updateJump(PlayerNinja me);
	
	/**
	 * 鉤縄の更新処理を実行する。
	 * @param me		自身を示す忍者オブジェクト
	 */
	protected abstract void updateKaginawa(PlayerNinja me);
	
	/**
	 * 重力の更新処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 * @param deltaTime		経過時間（秒）
	 */
	protected void updateGravity(PlayerNinja me, float deltaTime)
	{
		me.getBody().applyLinearImpulse(me.frontDirection.y * me.worldGravity * deltaTime, -me.frontDirection.x * me.worldGravity * deltaTime, me.getPositionX(), me.getPositionY());
	}
	
	/**
	 * 現在の状況に合わせた次の状態を取得する。
	 * @param me		自身を示す忍者オブジェクト
	 * @return			次の状態
	 */
	protected abstract NinjaStateInterface getNextState(PlayerNinja me);
}
