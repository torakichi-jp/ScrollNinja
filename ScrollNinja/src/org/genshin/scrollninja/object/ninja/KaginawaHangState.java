package org.genshin.scrollninja.object.ninja;

import org.genshin.scrollninja.object.kaginawa.Kaginawa;
import org.genshin.scrollninja.object.ninja.controller.NinjaControllerInterface;
import org.genshin.scrollninja.work.render.AnimationRenderObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;

/**
 * 鉤縄にぶら下がっている時の忍者の処理。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class KaginawaHangState extends AbstractKaginawaState
{
	@Override
	public StateInterface update(AbstractNinja me, float deltaTime)
	{
		//---- あとは基本クラスに任せる。
		return super.update(me, deltaTime);
	}
	
	@Override
	public void collisionTerrain(AbstractNinja me, Contact contact)
	{
		//---- 衝突したのが下半身でなければ何もしない。
		if( !checkContactIsFoot(me, contact) )
			return;
		
		//---- 下半身が地面に衝突した場合、自動的に鉤縄を切断する。
		me.getKaginawa().release();
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
		// 鉤縄を縮める
		else if( controller.isKaginawaShrink() )
		{
			kaginawa.shrink();
		}
		
		//---- 忍者のアニメーションを設定する。
		// 上半身
		bodyRenderObject.setAnimation("Kaginawa");
		
		// 下半身
		final Vector2 ninjaDirection = controller.getDirection();
		final Vector2 kaginawaDirection = Vector2.tmp.set(kaginawa.getPositionX(), kaginawa.getPositionY()).sub(me.getBody().getPosition());
		if( (Vector2.Y.crs(ninjaDirection) < 0.0f) == (Vector2.Y.crs(kaginawaDirection) < 0.0f) )
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
		//---- 鉤縄が縮んでいる状態へ
		if( me.getKaginawa().isShrinkState() )
		{
			return new KaginawaShrinkState(me);
		}
		
		//---- あとは基本クラスに任せる。
		return super.getNextState(me);
	}

}
