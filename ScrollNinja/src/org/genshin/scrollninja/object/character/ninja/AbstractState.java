package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.effect.FileEffect;
import org.genshin.scrollninja.render.AnimationRenderObject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * 忍者の状態の基本クラス。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
abstract class AbstractState implements StateInterface
{
// XXX お手軽状態遷移出力（よく使うので残しておく。最終的には消す）
//	static String oldState;
//	AbstractState()
//	{
//		String newState = getClass().getSimpleName();
//		Logger.global.info(GlobalParam.INSTANCE.frameCount + ": " + oldState + " -> " + newState);
//		oldState = newState;
//	}
	
	@Override
	public StateInterface update(AbstractNinja me, float deltaTime)
	{
		//---- 各種処理を実行する。
		updateMove(me, deltaTime);		// 移動
		updateJump(me);					// ジャンプ
		updateAttack(me);				// 攻撃
		updateKaginawa(me);				// 鉤縄
		updateGravity(me, deltaTime);	// 重力
		updateFlip(me);					// 左右反転
		
		//---- 次の状態を返す。
		return getNextState(me);
	}

	/**
	 * 180度以下の範囲で回転する。
	 * @param me		自身を示す忍者オブジェクト
	 * @param radian	回転後の角度（ラジアン）
	 * @param time		回転するのにかかる時間（秒）
	 */
	protected final void nearRotate(AbstractNinja me, float radian, float time)
	{
		final Body body = me.getBody();

		//---- 角度計算
		float angularVelocity = radian - body.getAngle();

		// -360～360度の範囲に丸める
		angularVelocity = Math.abs(angularVelocity) % (MathUtils.PI*2.0f) * Math.signum(angularVelocity);
		
		// -180～180度の範囲に丸める
		if(Math.abs(angularVelocity) > MathUtils.PI)
			angularVelocity -= Math.signum(angularVelocity) * MathUtils.PI * 2.0f;
		
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
	protected final void move(AbstractNinja me, float movePower, float accel, float maxVelocity)
	{
		final Body body = me.getBody();
		final Vector2 frontDirection = me.getFrontDirection();
		final Vector2 oldVelocity = body.getLinearVelocity().tmp();
		final float oldVelocityLenAbs = Math.abs( frontDirection.dot(oldVelocity) );
		
		//---- 加速度を加える
		body.applyLinearImpulse(accel*movePower*frontDirection.x, accel*movePower*frontDirection.y, body.getPosition().x, body.getPosition().y);
		
		//---- 速度に制限をかける
		final Vector2 newVelocity = body.getLinearVelocity();
		final float newVelocityLen = frontDirection.dot(newVelocity);
		final float newVelocityLenAbs = Math.abs(newVelocityLen);
		
		// 制限速度を越えた場合
		if( newVelocityLenAbs > maxVelocity )
		{
			// 最初から制限速度を越えていた場合
			if( oldVelocityLenAbs > maxVelocity )
			{
				// 加速度を加えた結果さらに加速していた場合、この加速はなかったことにする
				if( newVelocityLenAbs > oldVelocityLenAbs )
				{
					body.setLinearVelocity(oldVelocity);
				}
			}
			// 加速度を加えた結果制限速度を越えてしまった場合、前方に働く速度が制限速度内になるように丸める
			else
			{
				body.setLinearVelocity( newVelocity.sub(frontDirection.tmp().mul(maxVelocity/newVelocityLen)) );
			}
		}
	}

	/**
	 * ジャンプ処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 */
	protected final void jump(AbstractNinja me)
	{
		jumpImpl(me, NinjaDefine.INSTANCE.JUMP_POWER);
	}
	
	/**
	 * 空中でのジャンプ処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 */
	protected final void aerialJump(AbstractNinja me)
	{
		jump(me);
		me.decrementAerialJumpCount();
	}
	
	/**
	 * 天井への吸着を解除する処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 */
	protected final void leaveSnapCeiling(AbstractNinja me)
	{
		jumpImpl(me, NinjaDefine.INSTANCE.LEAVE_SNAP_CEILING_POWER);
		me.reverseMoveDirection();
	}
	
	/**
	 * ジャンプ処理の実装メソッド
	 * @param me		自身を示す忍者オブジェクト
	 * @param power		ジャンプ力
	 */
	private final void jumpImpl(AbstractNinja me, float power)
	{
		//setAxisImpulse(me, me.getJumpDirection(), power);
		
		//---- ジャンプする。
		final Body body = me.getBody();
		final Vector2 jumpDirection = me.getJumpDirection();
		
		// 一度ジャンプ方向の力を殺す。
		final Vector2 velocity = body.getLinearVelocity();
		final float jumpDirectionPower = jumpDirection.dot(velocity);
		body.setLinearVelocity(velocity.sub(jumpDirection.tmp().mul(jumpDirectionPower)));
		
		// ジャンプ力を与える。
		body.applyLinearImpulse(jumpDirection.tmp().mul(power), body.getPosition());
		
		// 地面との接触判定を消し飛ばす。
		me.toAerial();
		
		//---- エフェクトを生成する。
		new FileEffect("data/jsons/effect/jump_smoke.json", me.getPositionX(), me.getPositionY(), me.getRotation());
	}
	
	/**
	 * 移動の更新処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 * @param deltaTime		経過時間（秒）
	 */
	protected abstract void updateMove(AbstractNinja me, float deltaTime);
	
	/**
	 * ジャンプの更新処理を実行する。
	 * @param me		自身を示す忍者オブジェクト
	 */
	protected abstract void updateJump(AbstractNinja me);
	
	/**
	 * 攻撃の更新処理を実行する。
	 * @param me		自身を示す忍者オブジェクト
	 */
	protected abstract void updateAttack(AbstractCharacter me);
	
	/**
	 * 鉤縄の更新処理を実行する。
	 * @param me		自身を示す忍者オブジェクト
	 */
	protected abstract void updateKaginawa(AbstractNinja me);
	
	/**
	 * 重力の更新処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 * @param deltaTime		経過時間（秒）
	 */
	protected void updateGravity(AbstractNinja me, float deltaTime)
	{
		final Body body = me.getBody();
		final Vector2 frontDirection = me.getFrontDirection();
		final Vector2 gravity = Vector2.tmp
									.set(frontDirection.y, -frontDirection.x)
									.mul(me.getGravityPower() * deltaTime);
		
		body.applyLinearImpulse(gravity, body.getPosition());
	}

	/**
	 * 左右反転の更新処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 */
	protected void updateFlip(AbstractNinja me)
	{
		final Vector2 direction = me.getController().getDirection();
		final float degree = me.getRotation();
		final Vector2 upDirection = Vector2.tmp.set(-MathUtils.sinDeg(degree), MathUtils.cosDeg(degree));
		final boolean flipX = upDirection.crs(direction) < 0.0f;
		
		for(AnimationRenderObject ro : me.getRenderObjects())
		{
			ro.flipX(flipX);
		}
	}
	
	/**
	 * 衝突したのが下半身か調べる。
	 * @param me		自身を示す忍者オブジェクト
	 * @param contact	衝突情報
	 * @return
	 */
	protected boolean checkContactIsFoot(AbstractNinja me, Contact contact)
	{
		final Fixture footFixture = me.getFootFixture();
		return contact.getFixtureA() == footFixture || contact.getFixtureB() == footFixture;
	}
	
	/**
	 * 現在の状況に合わせた次の状態を取得する。
	 * @param me		自身を示す忍者オブジェクト
	 * @return			次の状態
	 */
	protected abstract StateInterface getNextState(AbstractNinja me);
}
