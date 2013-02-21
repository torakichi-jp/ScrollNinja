package org.genshin.scrollninja.object.ninja;

import java.util.ArrayList;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.object.ninja.controller.NinjaControllerInterface;
import org.genshin.scrollninja.utils.debug.DebugString;
import org.genshin.scrollninja.work.collision.AbstractCollisionCallback;
import org.genshin.scrollninja.work.collision.CollisionObject;
import org.genshin.scrollninja.work.object.AbstractObject;
import org.genshin.scrollninja.work.object.terrain.Terrain;
import org.genshin.scrollninja.work.render.AnimationRenderObject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;


/**
 * プレイヤーが操作する忍者
 * @author	インターン生
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractNinja extends AbstractObject
{
	/**
	 * コンストラクタ
	 * @param world			所属するWorldオブジェクト
	 * @param position		初期座標
	 */
	public AbstractNinja(World world, Vector2 position)
	{
		//---- 描画オブジェクトを生成する。
		renderObjects.add(new AnimationRenderObject("data/jsons/render/ninja_sprite.json", "data/jsons/render/ninja_foot_animation.json", this, GlobalDefine.RenderDepth.NINJA));
		renderObjects.add(new AnimationRenderObject("data/jsons/render/ninja_sprite.json", "data/jsons/render/ninja_body_animation.json", this, GlobalDefine.RenderDepth.NINJA));
		
		//---- 衝突オブジェクトを生成する。
		collisionObject = new CollisionObject("data/jsons/collision/ninja.json", world, new CollisionCallback());
		getBody().setGravityScale(0.0f);
		
		//---- フィールドを初期化する。
		state = new AerialState(this);
		defaultFriction = getFootFixture().getFriction();
		gravityPower = world.getGravity().len();
		
		//---- アニメーションを設定する。
		setAnimation("Stay");
	}
	
	@Override
	public void dispose()
	{
		//---- フィールドを解放する。
		state = null;
		
		//---- 衝突オブジェクトを破棄する。
		if(collisionObject != null)
		{
			collisionObject.dispose();
			collisionObject = null;
		}
		
		//---- 描画オブジェクトを破棄する。
		for(AnimationRenderObject renderObject : renderObjects)
		{
			renderObject.dispose();
		}
		renderObjects.clear();
		
		//---- 基本クラスの破棄処理を実行する。
		super.dispose();
	}

	@Override
	public void update(float deltaTime)
	{
		//---- 各種更新処理
		controller.update(deltaTime);
		state = state.update(this, deltaTime);
		
		//---- 地上フラグをへし折る準備
		if(isGrounded())
			--groundedTimer;

		//---- デバッグ文字列
		DebugString.add("");
		DebugString.add("Ninja State : " + state.getClass().getSimpleName());
		DebugString.add("Ninja Position : " + getPositionX() + ", " + getPositionY());
		DebugString.add("Ninja Velocity : " + getBody().getLinearVelocity().x + ", " + getBody().getLinearVelocity().y + " (" + getBody().getLinearVelocity().len() + ")");
	}
	
	@Override
	public boolean isDisposed()
	{
		return controller == null;
	}
	
	@Override
	public float getPositionX()
	{
		return getBody().getPosition().x;
	}
	
	@Override
	public float getPositionY()
	{
		return getBody().getPosition().y;
	}
	
	@Override
	public float getRotation()
	{
		return getBody().getAngle() * MathUtils.radiansToDegrees;
	}
	
	/**
	 * 忍者の操作状態を管理するオブジェクトを設定する。
	 * @param controller		忍者の操作状態を管理するオブジェクト
	 */
	protected void setController(NinjaControllerInterface controller)
	{
		this.controller = controller;
	}
	
	
	/** 描画オブジェクトの配列 */
	private final ArrayList<AnimationRenderObject>	renderObjects	= new ArrayList<AnimationRenderObject>();
	
	/** 衝突オブジェクト */
	private CollisionObject	collisionObject;
	
	/** 忍者の操作を管理するオブジェクト */
	private NinjaControllerInterface	controller;

	/** 忍者の状態を管理するオブジェクト */
	private StateInterface	state;
	
	/** 摩擦のデフォルト値 */
	private final float	defaultFriction;
	
	/** 重力の強さ */
	private final float	gravityPower;

	/** 移動する方向（天井に張り付いた状態から自然に移動する用） */
	private float	moveDirection;
	
	/** 正面方向を表すベクトル */
	private final Vector2	frontDirection = new Vector2(Vector2.X);
	
	/** 空中でジャンプできる残り回数 */
	private int	aerialJumpCount;
	
	/** ジャンプ方向を表すベクトル */
	private final Vector2	jumpDirection = new Vector2(Vector2.Y);
	
	/** 地上・空中判定用タイマー（0より大きければ地上、そうでなければ空中） */
	private int	groundedTimer;
	
	
	/**
	 * 衝突判定のコールバック
	 */
	protected class CollisionCallback extends AbstractCollisionCallback
	{
		@Override
		public void dispatch(AbstractCollisionCallback collisionCallback, Contact contact)
		{
			collisionCallback.collision(AbstractNinja.this, contact);
		}

		@Override
		public void collision(Terrain obj, Contact contact)
		{
			AbstractNinja.this.state.collisionTerrain(AbstractNinja.this, contact);
		}
	}

	
	/**
	 * 
	 * 以下、Stateから参照させるためにパッケージプライベートにしているメソッド
	 * 
	 */
	/**
	 * 移動する方向を更新する。
	 */
	void updateMoveDirection()
	{
		//---- 天井に張り付いている時
		if(isGrounded() && frontDirection.x < 0.0f)
			moveDirection = -1.0f;
		
		//---- 地上、もしくは空中にいる時
		else
			moveDirection = 1.0f;
	}
	
	/**
	 * 移動する方向を反転する。
	 */
	void reverseMoveDirection()
	{
		moveDirection = -moveDirection;
	}
	
	/**
	 * 忍者を地上状態にする。
	 */
	void toGrounded()
	{
		groundedTimer = NinjaDefine.INSTANCE.GROUNDED_JUDGE_TIME;
		aerialJumpCount = NinjaDefine.INSTANCE.AERIAL_JUMP_COUNT;
	}
	
	/**
	 * 忍者を空中状態にする。
	 */
	void toAerial()
	{
		groundedTimer = 0;
	}
	
	/**
	 * アニメーションを設定する。
	 * @param animationName		アニメーション名
	 */
	void setAnimation(String animationName)
	{
		for(AnimationRenderObject ro : renderObjects)
		{
			ro.setAnimation(animationName);
		}
	}
	
	/**
	 * 摩擦の有効フラグを設定する。
	 * @param enabled		摩擦の有効フラグ
	 */
	void setFrictionEnabled(boolean enabled)
	{
		getFootFixture().setFriction(enabled ? defaultFriction : 0.0f);
	}
	
	/**
	 * 正面方向を表すベクトルを設定する。
	 * @param direction		正面方向を表すベクトル
	 */
	void setFrontDirection(Vector2 direction)
	{
		frontDirection.set(direction);
	}
	
	/**
	 * 正面方向を表すベクトルを設定する。
	 * @param x		X成分
	 * @param y		Y成分
	 */
	void setFrontDirection(float x, float y)
	{
		frontDirection.set(x, y);
	}
	
	/**
	 * 空中でジャンプできる回数を減らす。
	 */
	void decrementAerialJumpCount()
	{
		--aerialJumpCount;
	}
	
	/**
	 * ジャンプ方向を表すベクトルを設定する。
	 * @param direction		ジャンプ方向を表すベクトル
	 */
	void setJumpDirection(Vector2 direction)
	{
		jumpDirection.set(direction);
	}
	
	/**
	 * ジャンプ方向を表すベクトルを設定する。
	 * @param x		X成分
	 * @param y		Y成分
	 */
	void setJumpDirection(float x, float y)
	{
		jumpDirection.set(x, y);
	}
	
	/**
	 * 描画オブジェクトの配列を取得する。
	 * @return		描画オブジェクトの配列
	 */
	ArrayList<AnimationRenderObject> getRenderObjects()
	{
		return renderObjects;
	}
	
	/**
	 * 上半身の描画オブジェクトを取得する。
	 * @return		上半身の描画オブジェクト
	 */
	AnimationRenderObject getBodyRenderObject()
	{
		return renderObjects.get(1);
	}
	
	/**
	 * 下半身の描画オブジェクトを取得する。
	 * @return		下半身の描画オブジェクト
	 */
	AnimationRenderObject getFootRenderObject()
	{
		return renderObjects.get(0);
	}
	
	/**
	 * Bodyオブジェクトを取得する。
	 * @return		Bodyオブジェクト
	 */
	Body getBody()
	{
		return collisionObject.getBody();
	}

	/**
	 * 上半身のFixtureオブジェクトを取得する。
	 * @return		上半身のFixtureオブジェクト
	 */
	Fixture getBodyFixture()
	{
		return collisionObject.getFixture("Body");
	}

	/**
	 * 下半身のFixtureオブジェクトを取得する。
	 * @return		下半身のFixtureオブジェクト
	 */
	Fixture getFootFixture()
	{
		return collisionObject.getFixture("Foot");
	}
	
	/**
	 * 忍者の操作状態を管理するオブジェクトを取得する。
	 * @return		忍者の操作状態を管理するオブジェクト
	 */
	NinjaControllerInterface getController()
	{
		return controller;
	}
	
	/**
	 * 重力の強さを取得する。
	 * @return		重力の強さ
	 */
	float getGravityPower()
	{
		return gravityPower;
	}
	
	/**
	 * 移動する方向を取得する。
	 * @return		移動する方向
	 */
	float getMoveDirection()
	{
		return moveDirection;
	}
	
	/**
	 * 正面方向を表すベクトルを取得する。
	 * @return		正面方向を表すベクトル
	 */
	Vector2 getFrontDirection()
	{
		return frontDirection;
	}
	
	/**
	 * 空中でジャンプできる状態か調べる。
	 * @return		空中でジャンプできる状態ならtrue、できない状態ならfalse
	 */
	boolean canAerialJump()
	{
		return aerialJumpCount > 0;
	}
	
	/**
	 * ジャンプ方向を表すベクトルを取得する。
	 * @return		ジャンプ方向を表すベクトル
	 */
	Vector2 getJumpDirection()
	{
		return jumpDirection;
	}
	
	/**
	 * 忍者が地上にいるか調べる。
	 * @return		地上にいる場合はtrue、そうでなければfalse
	 */
	boolean isGrounded()
	{
		return groundedTimer > 0;
	}

	/**
	 * 忍者が空中にいるか調べる。
	 * @return		空中にいる場合はtrue、そうでなければfalse
	 */
	boolean isAerial()
	{
		return !isGrounded();
	}
	
	
	
	
	
	
//	/**
//	 * コンストラクタ
//	 * @param world		所属するWorldオブジェクト
//	 * @param position	初期座標
//	 * @param cursor	カーソルオブジェクト（鉤縄の向き計算用）
//	 */
//	public PlayerNinja(World world, Vector2 position, Cursor cursor) {
//		super(world, position);
//		
//		// 初期座標設定
//		getBody().setTransform(position, 0.0f);
//		
//		// フィールドの初期化
//		controller = new DefaultPlayerNinjaController(this, cursor);
//		kaginawa = new Kaginawa(world, getBody());
//		sword = new SwordWeapon(this);
//		restAerialJumpCount = NinjaDefine.INSTANCE.AERIAL_JUMP_COUNT;
//		groundedTimer = 0;
//		worldGravity = world.getGravity().len();
//		defaultFriction = getFootFixture().getFriction();
//
//		state = new AerialState(this);
//		
//		// 描画登録（仮）
//		Global.currentRenderableManager.add(this, 1);
//	}
//
//	/**
//	 * 更新処理
//	 */
//	public void update(float deltaTime)
//	{
//		// 操作状態を更新
//		controller.update();
//
//		// 状態に合わせた更新処理
//		state = state.update(this, deltaTime);
//		
//		// 鉤縄を更新
//		kaginawa.update(deltaTime);
//		
//		//---- 左右の反転フラグを設定する。
//		final Vector2 direction = controller.getDirection();
//		final float angle = getBody().getAngle();
//		final Vector2 upDirection = new Vector2(-(float)Math.sin(angle), (float)Math.cos(angle));
//		flip = upDirection.crs(direction) < 0.0f;
//		
//		//---- 残像を付けてみる。
//		new AfterimageEffect(getRenderObjects(), getPositionX(), getPositionY(), getBody().getAngle() * MathUtils.radiansToDegrees);
//		
//		//---- 刀を振ってみる。
//		if( controller.isAttack() && getBodyRenderObject().isAnimationLooping() )
//		{
//			sword.attack();
//			getBodyRenderObject().setAnimation("AttackSword");
//		}
//		
//		//---- デバッグ文字列
//		DebugString.add("");
//		DebugString.add("Ninja State : " + state.getClass().getSimpleName());
//		DebugString.add("Ninja Position : " + getPositionX() + ", " + getPositionY());
//		DebugString.add("Ninja Velocity : " + getBody().getLinearVelocity().x + ", " + getBody().getLinearVelocity().y + " (" + getBody().getLinearVelocity().len() + ")");
//	}
//	
//	/** 鉤縄オブジェクト */
//	Kaginawa	kaginawa;
//	
//	/** 刀系の武器オブジェクト */
//	SwordWeapon sword;
}
