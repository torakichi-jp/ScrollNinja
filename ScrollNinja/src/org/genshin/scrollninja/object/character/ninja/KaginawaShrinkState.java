package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.object.character.ninja.controller.NinjaControllerInterface;
import org.genshin.scrollninja.object.kaginawa.Kaginawa;
import org.genshin.scrollninja.render.AnimationRenderObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;

/**
 * 鉤縄を縮めている時の忍者の処理。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class KaginawaShrinkState extends AbstractKaginawaState
{
	/**
	 * コンストラクタ
	 * @param me		自身を示す忍者オブジェクト
	 */
	KaginawaShrinkState(AbstractNinja me)
	{
		//---- 鉤縄のロープジョイントフラグを叩き折っておく
		me.getKaginawa().setUseRopeJoint(false);
	}
	
	@Override
	public StateInterface update(AbstractNinja me, float deltaTime)
	{
		//---- 地面との接触フラグをへし折っておく
		me.toAerial();
		
		//---- あとは基本クラスに任せる
		return super.update(me, deltaTime);
	}

	@Override
	public void collisionTerrain(AbstractNinja me, Contact contact)
	{
		//---- まだ地上にいる時はスルー
		if( me.isGrounded() )
			return;
		
		//---- 地面との接触フラグを立てる
		collisionTerrain = true;
	}
	
	@Override
	protected void updateKaginawa(AbstractNinja me)
	{
		final NinjaControllerInterface controller = me.getController();
		final Kaginawa kaginawa = me.getKaginawa();
		final AnimationRenderObject bodyRenderObject = me.getBodyRenderObject();
		final AnimationRenderObject footRenderObject = me.getFootRenderObject();
		
		//---- 操作状態に合わせて各種処理を実行する。
		// 鉤縄を離し、同時にジャンプする。
		if( controller.isAerialJump() )
		{
			kaginawa.release();
			aerialJump(me);
		}
		// 鉤縄を離す
		else if( controller.isKaginawaRelease() )
		{
			kaginawa.release();
		}
		// 鉤縄にぶら下がる
		else if( controller.isKaginawaHang() )
		{
			kaginawa.hang();
		}
		
		//---- 忍者のアニメーションを設定する。
		// 上半身
		bodyRenderObject.setAnimation("Kaginawa");
		
		// 下半身
		final Vector2 direction = controller.getDirection();
		final Vector2 velocity = me.getBody().getLinearVelocity();
		if( (Vector2.Y.crs(direction) < 0.0f) == (Vector2.Y.crs(velocity) < 0.0f) )
		{
			footRenderObject.setAnimation("KaginawaFront");
		}
		else
		{
			footRenderObject.setAnimation("KaginawaBack");
		}
		
		// アニメーション時間の同期
		footRenderObject.setAnimationTime( bodyRenderObject.getAnimationTime() );
	}

	@Override
	protected StateInterface getNextState(AbstractNinja me)
	{
		//---- 地形に吸着する状態へ
		if( collisionTerrain )
		{
			return new SnapTerrainState();
		}
		
		//---- 鉤縄にぶら下がっている状態へ
		if( me.getKaginawa().isHangState() )
		{
			return new KaginawaHangState();
		}
		
		//---- あとは基本クラスに任せる。
		return super.getNextState(me);
	}
	
	/** 地形と衝突したフラグ */
	boolean collisionTerrain = false;
}
