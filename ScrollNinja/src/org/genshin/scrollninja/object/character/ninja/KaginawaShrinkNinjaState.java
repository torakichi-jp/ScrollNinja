package org.genshin.scrollninja.object.character.ninja;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * 鉤縄を縮めている時の忍者の処理。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class KaginawaShrinkNinjaState extends AbstractKaginawaNinjaState
{
	@Override
	public void collisionTerrain(PlayerNinja me, Contact contact)
	{
		//---- まだ地上にいる時はスルー
		if( me.isGrounded() )
			return;
		
		//---- 地面との接触フラグを立てる
		collisionTerrain = true;
	}
	
	@Override
	protected void updateKaginawa(PlayerNinja me)
	{
		//---- 操作状態に合わせて各種処理を実行する。
		// 鉤縄を離し、同時にジャンプする。
		if( me.controller.isAerialJump() )
		{
			me.kaginawa.release();
			jump(me);
			if( me.restAerialJumpCount > 0 )
			{
				me.restAerialJumpCount--;
			}
		}
		// 鉤縄を離す
		else if( me.controller.isKaginawaRelease() )
		{
			me.kaginawa.release();
		}
		// 鉤縄にぶら下がる
		else if( me.controller.isKaginawaHang() )
		{
			me.kaginawa.hang();
		}
		
		//---- 忍者のアニメーションを設定する。
		// 上半身
		me.setBodyAnimation("Kaginawa");
		
		// 下半身
		final Vector2 direction = me.controller.getDirection();
		final Vector2 velocity = me.getBody().getLinearVelocity();
		if( (Vector2.Y.crs(direction) < 0.0f) == (Vector2.Y.crs(velocity) < 0.0f) )
		{
			me.setFootAnimation("KaginawaFront");
		}
		else
		{
			me.setFootAnimation("KaginawaBack");
		}
		
		// アニメーション時間の同期
		me.getFootRenderObject().setAnimationTime( me.getBodyRenderObject().getAnimationTime() );
	}

	@Override
	protected NinjaStateInterface getNextState(PlayerNinja me)
	{
		//---- 地形に吸着する状態へ
		if( collisionTerrain )
		{
			return new SnapTerrainNinjaState();
		}
		
		//---- 鉤縄にぶら下がっている状態へ
		if( me.kaginawa.isHangState() )
		{
			return new KaginawaHangNinjaState();
		}
		
		//---- あとは基本クラスに任せる。
		return super.getNextState(me);
	}
	
	/** 地形と衝突したフラグ */
	boolean collisionTerrain = false;
}
