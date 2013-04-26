package org.genshin.scrollninja.object.character;

import java.util.ArrayList;

import org.genshin.scrollninja.collision.AbstractCollisionCallback;
import org.genshin.scrollninja.collision.CollisionObject;
import org.genshin.scrollninja.object.AbstractObject;
import org.genshin.scrollninja.object.terrain.Terrain;
import org.genshin.scrollninja.render.RenderObject;
import org.genshin.scrollninja.utils.GeneralPoint;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
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
		
		final Body body = collisionObject.getBody();
		gravityPower = world.getGravity().len() * body.getGravityScale();
		body.setGravityScale(0.0f);
		
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

	@Override
	public void update(float deltaTime)
	{
		//---- 重力を加える
		final Body body = collisionObject.getBody();
		body.applyLinearImpulse(gravityDirection.tmp().mul(gravityPower*deltaTime), body.getPosition());
		
		//---- キャラクターを更新
		updateCharacter(deltaTime);
		
		//---- 移動ベクトル補正
		setMoveDirection(Vector2.X);
	}

	/**
	 * キャラクターを出現させる。
	 * @param position		出現させる座標
	 */
	public void spawn(Vector2 position)
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
	 * キャラクターを更新する。
	 * @param deltaTime		経過時間（秒）
	 */
	protected abstract void updateCharacter(float deltaTime);
	
	/**
	 * 移動する。
	 * @param accel			加速度
	 * @param maxVelocity	制限速度
	 */
	protected void move(float accel, float maxVelocity)
	{
		boolean flag = true;
		
		//---- 忍者で使ってたパターンのやつやん
		if(flag)
		{
			final Body body = collisionObject.getBody();
			final Vector2 oldVelocity = Vector2.tmp.set(body.getLinearVelocity());
			final float oldVelocityLenAbs = Math.abs(moveDirection.dot(oldVelocity));
			
			//--- 加速度を加える
			body.applyLinearImpulse(Vector2.tmp2.set(moveDirection).mul(accel), body.getPosition());
			
			//---- 速度に制限をかける
			final Vector2 newVelocity = body.getLinearVelocity();
			final float newVelocityLen = moveDirection.dot(newVelocity);
			final float newVelocityLenAbs = Math.abs(newVelocityLen);
			
			// 制限速度を越えた場合
			if(newVelocityLenAbs > maxVelocity)
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
					body.setLinearVelocity(newVelocity.sub(
						Vector2.tmp2.set(moveDirection).mul(Math.signum(newVelocityLen) * (newVelocityLenAbs - maxVelocity))
					));
				}
			}
		}
		
		//---- 敵で使ってたパターンのやつやん
		else
		{
		}
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
	 * 特定のFixtureが衝突情報に含まれているか調べる。
	 * @param fixtureName		Fixtureの名前
	 * @param contact			衝突情報
	 * @return		Fixtureが衝突情報に含まれている場合はtrue
	 */
	protected boolean contactIs(String fixtureName, Contact contact)
	{
		final Fixture target = getCollisionObject().getFixture(fixtureName);
		return contact.getFixtureA() == target || contact.getFixtureB() == target;
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
	 * 重力の強さを取得する。
	 * @return		重力の強さ
	 */
	protected float getGravityPower()
	{
		return gravityPower;
	}
	
	/**
	 * 重力の向きを取得する。
	 * @return		重力の向き
	 */
	protected Vector2 getGravityDirection()
	{
		return gravityDirection;
	}
	
	/**
	 * 移動する向きを設定する。（ついでに重力の向きも設定する。）
	 * @param direction		移動する向き
	 */
	protected void setMoveDirection(Vector2 direction)
	{
		setMoveDirection(direction.x, direction.y);
	}
	
	/**
	 * 移動する向きを設定する。（ついでに重力の向きも設定する。）
	 * @param x		移動する向き（X成分）
	 * @param y		移動する向き（Y成分）
	 */
	protected void setMoveDirection(float x, float y)
	{
		moveDirection.set(x, y);
		gravityDirection.set(y, -x);
	}
	
	/**
	 * 移動する向きを取得する。
	 * @return		移動する向き
	 */
	protected Vector2 getMoveDirection()
	{
		return moveDirection;
	}
	
	/**
	 * 目標物への向きを設定する。
	 * @param direction		目標物への向き
	 */
	protected void setLookAtDirection(Vector2 direction)
	{
		setLookAtDirection(direction.x, direction.y);
	}
	
	/**
	 * 目標物への向きを設定する。
	 * @param x		目標物への向き（X成分）
	 * @param y		目標物への向き（Y成分）
	 */
	protected void setLookAtDirection(float x, float y)
	{
		lookAtDirection.set(x, y).nor();
	}
	
	/**
	 * 目標物への向きを取得する。
	 * @return		目標物への向き
	 */
	public Vector2 getLookAtDirection()
	{
		return lookAtDirection;
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
	
	/** 重力の強さ */
	private final float gravityPower;
	
	/** 重力の向き */
	private final Vector2 gravityDirection = new Vector2(0.0f, -1.0f);
	
	/** 移動する向き */
	private final Vector2 moveDirection = new Vector2(Vector2.X);
	
	/** 視線の向き */
	private final Vector2 lookAtDirection = new Vector2(moveDirection);
	
	
	/**
	 * 衝突判定のコールバックの基本クラス
	 */
	protected abstract class AbstractCharacterCollisionCallback extends AbstractCollisionCallback
	{
		@Override
		public void dispatch(AbstractCollisionCallback collisionCallback, Contact contact)
		{
			collisionCallback.collision(AbstractCharacter.this, contact);
		}

		@Override
		public void collision(Terrain obj, Contact contact)
		{
			//---- 地形と足が衝突した時
			if(contactIs("Foot", contact))
			{
				final Vector2 terrainNormal = contact.getWorldManifold().getNormal();
				setMoveDirection(terrainNormal.y, -terrainNormal.x);
			}
		}
	}
	
}