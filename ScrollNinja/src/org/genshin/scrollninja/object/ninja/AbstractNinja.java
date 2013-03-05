package org.genshin.scrollninja.object.ninja;

import java.util.ArrayList;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.collision.AbstractCollisionCallback;
import org.genshin.scrollninja.collision.CollisionObject;
import org.genshin.scrollninja.object.AbstractObject;
import org.genshin.scrollninja.object.effect.CopyEffect;
import org.genshin.scrollninja.object.effect.EffectDef;
import org.genshin.scrollninja.object.kaginawa.Kaginawa;
import org.genshin.scrollninja.object.ninja.controller.NinjaControllerInterface;
import org.genshin.scrollninja.object.terrain.Terrain;
import org.genshin.scrollninja.object.weapon.SwordWeapon;
import org.genshin.scrollninja.render.AnimationRenderObject;
import org.genshin.scrollninja.utils.JsonUtils;
import org.genshin.scrollninja.utils.debug.DebugString;

import com.badlogic.gdx.graphics.Color;
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
		
		//---- フィールドを初期化する。
		kaginawa = new Kaginawa(world, getBody());
		sword = new SwordWeapon(world, this);
		state = new AerialState(this);
		defaultFriction = getFootFixture().getFriction();
		gravityPower = world.getGravity().len();
		
		//---- アニメーションを設定する。
		setAnimation("Stay");
		
		//---- 残像エフェクトの定義
		if(afterimageEffectDef == null)
		{
			afterimageEffectDef = JsonUtils.read("data/jsons/effect/ninja_afterimage.json", EffectDef.class);
			afterimageEffectDef.startVelocity.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
			afterimageEffectDef.endVelocity.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
		}
	}
	
	@Override
	public void dispose()
	{
		//---- フィールドを解放する。
		state = null;
		if(kaginawa != null)
		{
			kaginawa.dispose();
			kaginawa = null;
		}
		
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
		
		//---- 残像エフェクト
		// TODO 残像は常に出すワケではない？
		for(AnimationRenderObject ro : renderObjects)
		{
			//---- コピーエフェクトを生成する前にアニメーションを一時停止しておく。
			final boolean oldPaused = ro.isAnimationPaused();
			ro.pauseAnimation();
			
			//---- コピーエフェクトを生成する。
			new CopyEffect(ro, afterimageEffectDef);
			
			//---- アニメーションの一時停止状態を元に戻す
			if(!oldPaused)
			{
				ro.resumeAnimation();
			}
		}

		//---- デバッグ文字列
		DebugString.add("");
		DebugString.add("Ninja State : " + state.getClass().getSimpleName());
		DebugString.add("Ninja Position : " + getPositionX() + ", " + getPositionY());
		DebugString.add("Ninja Velocity : " + getBody().getLinearVelocity().x + ", " + getBody().getLinearVelocity().y + " (" + getBody().getLinearVelocity().len() + ")");
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
	private final ArrayList<AnimationRenderObject>	renderObjects	= new ArrayList<AnimationRenderObject>(2);
	
	/** 衝突オブジェクト */
	private CollisionObject	collisionObject;
	
	/** 忍者の操作を管理するオブジェクト */
	private NinjaControllerInterface	controller;
	
	/** 鉤縄 */
	private Kaginawa kaginawa;
	
	/** 刀 */
	private SwordWeapon sword;

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
	
	/** 残像エフェクトの定義 */
	private static EffectDef afterimageEffectDef = null;
	
	
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
	 * 鉤縄を取得する。
	 * @return		鉤縄
	 */
	Kaginawa getKaginawa()
	{
		return kaginawa;
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
}
