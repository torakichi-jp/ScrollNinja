package org.genshin.scrollninja.object.ninja;

import org.genshin.scrollninja.object.ninja.controller.NinjaControllerInterface;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;


/**
 * 空中にいる（地面と接触していない）時の忍者の処理。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class AerialState extends AbstractNormalState
{
	/**
	 * コンストラクタ
	 * @param me		自身を示す忍者オブジェクト
	 */
	AerialState(AbstractNinja me)
	{
		//---- 鉤縄のロープジョイントフラグを叩き折っておく
		me.getKaginawa().setUseRopeJoint(false);
		
		//---- 地上フラグをへし折る
		me.toAerial();
		
		//---- 前方ベクトルを強制的にX軸にする。
		me.setFrontDirection(Vector2.X);
		
		//---- ジャンプ方向ベクトルを強制的にY軸にする。
		me.setJumpDirection(Vector2.Y);
	}
	
	@Override
	public StateInterface update(AbstractNinja me, float deltaTime)
	{
		//---- 初回フレーム判定用
		if(firstFrame)
		{
			firstFrame = false;
		}
		
		//---- 姿勢を起こす
		nearRotate(me, 0.0f, 0.1f);
		
		//---- 連続ジャンプ時は摩擦を無視する
		final NinjaControllerInterface controller = me.getController();
		me.setFrictionEnabled(!controller.isJump());
		
		//---- あとは基本クラスに任せる。
		return super.update(me, deltaTime);
	}
	
	@Override
	public void collisionTerrain(AbstractNinja me, Contact contact)
	{
		//---- 衝突したのが足でなければ何もしない？
		if( !checkContactIsFoot(me, contact) )
			return;
		
		//---- 初回フレームなら何もしない
		if( firstFrame )
			return;
		
		//---- 地面と接触した時の処理を加える。
		me.toGrounded();
	}
	
	@Override
	protected void updateJump(AbstractNinja me)
	{
		final NinjaControllerInterface controller = me.getController();
		
		if( controller.isAerialJump() && me.canAerialJump() )
		{
			aerialJump(me);
		}
	}

	@Override
	protected StateInterface getNextState(AbstractNinja me)
	{
		//---- 地面と接触したら地上状態へ
		if( me.isGrounded() )
		{
//			new JumpSmokeEffect(me.getPositionX(), me.getPositionY(), 0.0f);
			return new GroundedState(me);
		}
		
		//---- 鉤縄にぶら下がっている状態へ
		if( me.getKaginawa().isHangState() )
		{
			return new KaginawaHangState();
		}
		
		//---- あとは基本クラスに任せる
		return super.getNextState(me);
	}

	@Override
	protected String getStayAnimationName()
	{
		return "Jump";
	}

	/** 初回フレームフラグ */
	boolean firstFrame = true;
}
