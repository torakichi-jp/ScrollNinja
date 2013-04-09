package org.genshin.scrollninja.object.character;

import java.util.ArrayList;

import org.genshin.scrollninja.collision.AbstractCollisionCallback;
import org.genshin.scrollninja.collision.CollisionObject;
import org.genshin.scrollninja.object.AbstractObject;
import org.genshin.scrollninja.render.RenderObject;
import org.genshin.scrollninja.utils.GeneralPoint;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;


/**
 * キャラクタの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractCharacter extends AbstractObject
{
	/**
	 * コンストラクタ
	 * @param collisionFilePath		衝突判定の定義ファイルのパス
	 * @param world					所属する世界オブジェクト
	 */
	public AbstractCharacter(String collisionFilePath, World world)
	{
		super();
		
		collisionObject = new CollisionObject(collisionFilePath, world, createCollisionCallback());
	}

	@Override
	public void dispose()
	{
		if(collisionObject != null)
		{
			collisionObject.dispose();
			collisionObject = null;
		}

		for(RenderObject ro : renderObjects)
		{
			ro.dispose();
		}
		renderObjects.clear();
		
		super.dispose();
	}
	
	public void respawn(Vector2 position)
	{
		//---- ライフポイント回復
		lifePoint.set(lifePoint.getMax());
		
		//---- 座標初期化
		setTransform(position, 0.0f);
		
		//---- 速度などのパラメータも初期化しておく。
		final Body body = collisionObject.getBody();
		body.setLinearVelocity(Vector2.Zero);
		body.setAngularVelocity(0.0f);
	}

	@Override
	public final float getPositionX()
	{
		return collisionObject.getBody().getPosition().x;
	}

	@Override
	public final float getPositionY()
	{
		return collisionObject.getBody().getPosition().y;
	}

	@Override
	public final float getRotation()
	{
		return collisionObject.getBody().getAngle() * MathUtils.radiansToDegrees;
	}
	
	/**
	 * 死亡フラグを取得する。
	 * @return		死亡フラグ
	 */
	public final boolean isDead()
	{
		return lifePoint.isZero();
	}
	
	/**
	 * X方向の反転フラグを取得する。
	 * @return		X方向の反転フラグ
	 */
	public final boolean isFlipX()
	{
		return renderObjects.get(0).isFlipX();
	}
	
	/**
	 * Y方向の反転フラグを取得する。
	 * @return		Y方向の反転フラグ
	 */
	public final boolean isFlipY()
	{
		return renderObjects.get(0).isFlipY();
	}
	
	/**
	 * ダメージを与える。
	 * @param damage		ダメージ
	 */
	protected void damage(float damage)
	{
		lifePoint.add(-damage);
	}
	
	/**
	 * 反転フラグを設定する。
	 * @param flipX		X方向の反転フラグ
	 * @param flipY		Y方向の反転フラグ
	 */
	protected void flip(boolean flipX, boolean flipY)
	{
		for(RenderObject ro : renderObjects)
		{
			ro.flip(flipX, flipY);
		}
	}
	
	/**
	 * X方向の反転フラグを設定する。
	 * @param flipX		X方向の反転フラグ
	 */
	protected void flipX(boolean flipX)
	{
		for(RenderObject ro : renderObjects)
		{
			ro.flipX(flipX);
		}
	}
	
	/**
	 * Y方向の反転フラグを設定する。
	 * @param flipY		Y方向の反転フラグ
	 */
	protected void flipY(boolean flipY)
	{
		for(RenderObject ro : renderObjects)
		{
			ro.flipY(flipY);
		}
	}
	
	/**
	 * 位置情報を設定する。
	 * @param position		座標
	 * @param degrees		角度（度）
	 */
	protected void setTransform(Vector2 position, float degrees)
	{
		collisionObject.getBody().setTransform(position, degrees * MathUtils.degreesToRadians);
	}
	
	/**
	 * 描画オブジェクトを追加する。
	 * @param ro		描画オブジェクト
	 */
	protected void addRenderObject(RenderObject ro)
	{
		renderObjects.add(ro);
	}
	
	/**
	 * 描画オブジェクトの配列を取得する。
	 * @return		描画オブジェクトの配列
	 */
	protected ArrayList<RenderObject> getRenderObjects()
	{
		return renderObjects;
	}
	
	/**
	 * 衝突オブジェクトを取得する。
	 * @return		衝突オブジェクト
	 */
	protected CollisionObject getCollisionObject()
	{
		return collisionObject;
	}
	
	/**
	 * 生命力オブジェクトを取得する。
	 * @return		生命力オブジェクト
	 */
	protected GeneralPoint getLifePoint()
	{
		return lifePoint;
	}
	
	/**
	 * 衝突判定のコールバックオブジェクトを生成する。
	 * @return		衝突判定のコールバックオブジェクト
	 */
	protected abstract AbstractCharacterCollisionCallback createCollisionCallback();
	
	
	/** 描画オブジェクトの配列 */
	private final ArrayList<RenderObject>	renderObjects	= new ArrayList<RenderObject>(2);
	
	/** 衝突オブジェクト */
	private CollisionObject	collisionObject;
	
	/** 生命力 */
	private final GeneralPoint lifePoint = new GeneralPoint(100);
	
	
	/**
	 * 衝突判定のコールバックの基本クラス
	 */
	protected class AbstractCharacterCollisionCallback extends AbstractCollisionCallback
	{
		@Override
		public void dispatch(AbstractCollisionCallback collisionCallback, Contact contact)
		{
			collisionCallback.collision(AbstractCharacter.this, contact);
		}
	}
	
}