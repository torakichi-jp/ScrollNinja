package org.genshin.scrollninja.object.kaginawa;

import java.util.ArrayList;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.utils.debug.DebugString;
import org.genshin.scrollninja.work.collision.AbstractCollisionCallback;
import org.genshin.scrollninja.work.collision.CollisionObject;
import org.genshin.scrollninja.work.object.AbstractObject;
import org.genshin.scrollninja.work.object.terrain.Terrain;
import org.genshin.scrollninja.work.render.RenderObject;

import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

/**
 * 鉤縄クラス
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class Kaginawa extends AbstractObject
{
	/**
	 * コンストラクタ
	 * @param world		所属先となるWorldオブジェクト
	 * @param owner		持ち主となるBodyオブジェクト
	 */
	public Kaginawa(World world, Body owner)
	{
		//---- 描画オブジェクトを生成する。
		renderObjects.add(new RenderObject("data/jsons/render/kaginawa_rope_sprite.json", this, GlobalDefine.RenderDepth.KAGINAWA));
		renderObjects.add(new RenderObject("data/jsons/render/kaginawa_anchor_sprite.json", this, GlobalDefine.RenderDepth.KAGINAWA));
		getRopeSprite().getTexture().setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);
		
		//---- 衝突オブジェクトを生成する。
		collisionObject = new CollisionObject("data/jsons/collision/kaginawa.json", world, new CollisionCallback());
		
		//---- フィールドを初期化する。
		this.owner = owner;
		state = State.IDLE;
		state.initialize(this);
		nextState = null;
	}

	@Override
	public void dispose()
	{
		//---- 衝突オブジェクトを破棄する。
		if(collisionObject != null)
		{
			collisionObject.dispose();
			collisionObject = null;
		}
		
		//---- 描画オブジェクトを破棄する。
		for(RenderObject ro : renderObjects)
		{
			ro.dispose();
		}
		renderObjects.clear();
		
		//---- 基本クラスの破棄処理を実行する。
		super.dispose();
	}

	/**
	 * 鉤縄を伸ばす。
	 * @param direction 伸ばす方向
	 */
	public final void slack(Vector2 direction)
	{
		state.slack(this, direction);
	}
	
	/**
	 * 鉤縄を縮める。
	 */
	public final void shrink()
	{
		state.shrink(this);
	}

	/**
	 * 鉤縄にぶら下がる。
	 */
	public final void hang()
	{
		state.hang(this);
	}

	/**
	 * 鉤縄を離す。
	 */
	public final void release()
	{
		state.release(this);
	}

	@Override
	public void update(float deltaTime)
	{
		//---- 状態遷移
		if(nextState != null)
		{
			state = nextState;
			state.initialize(this);
			nextState = null;
		}
		
		//---- 状態を更新する。
		state.update(this);
		
		//---- 縄の描画オブジェクトの長さを調整する。
		final Body body = getBody();
		final Vector2 kaginawaPosition = body.getPosition();
		final Vector2 ownerPosition = owner.getPosition();
		final Vector2 direction = ownerPosition.sub(kaginawaPosition);
		final float len = direction.len();
		final Sprite ropeSprite = getRopeSprite();
		
		ropeSprite.setSize(len, ropeSprite.getHeight());
		ropeSprite.setRotation(direction.angle() - body.getAngle() * MathUtils.radiansToDegrees);
		ropeSprite.setRegion(0, 0, (int)(len * GlobalDefine.INSTANCE.INV_WORLD_SCALE), ropeSprite.getRegionHeight());
		
		
		//---- test
		Vector2 anchorPosition = ((CircleShape)collisionObject.getFixture("Anchor").getShape()).getPosition();
		DebugString.add("Anchor Position : " + anchorPosition);
	}
	
	public void setActive(boolean active)
	{
		getBody().setActive(active);
		for(RenderObject ro : renderObjects)
			ro.setRenderEnabled(active);
	}
	
	/**
	 * ロープジョイントを使うフラグを設定する。
	 * @param useRopeJoint		ロープジョイントを使うフラグ
	 */
	public void setUseRopeJoint(boolean useRopeJoint)
	{
		this.useRopeJoint = useRopeJoint;
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
	 * 鉤縄が縮んでいる状態か調べる。
	 * @return		鉤縄が縮んでいる状態の場合true
	 */
	public boolean isShrinkState()
	{
		return state == State.SHRINK;
	}
	
	/**
	 * 鉤縄にぶら下がっている状態か調べる。
	 * @return		鉤縄にぶら下がっている状態の場合true
	 */
	public boolean isHangState()
	{
		return state == State.HANG;
	}
	
	/**
	 * 鉤縄を離した状態か調べる。
	 * @return		鉤縄を離した状態の場合true
	 */
	public boolean isReleaseState()
	{
		return state == State.RELEASE;
	}

	/**
	 * 状態を変更する。
	 * @param next	次の状態
	 */
	void setNextState(State next)
	{
		nextState = next;
	}
	
	/**
	 * 縄の描画オブジェクトを取得する。
	 * @return		縄の描画オブジェクト
	 */
	RenderObject getRopeRenderObject()
	{
		return renderObjects.get(0);
	}
	
	/**
	 * 鉤の描画オブジェクトを取得する。
	 * @return		鉤の描画オブジェクト
	 */
	RenderObject getAnchorRenderObject()
	{
		return renderObjects.get(1);
	}
	
	/**
	 * 縄のスプライトオブジェクトを取得する。
	 * @return		縄のスプライトオブジェクト
	 */
	@SuppressWarnings("deprecation")
	Sprite getRopeSprite()
	{
		return getRopeRenderObject().getSprite();
	}
	
	/**
	 * Bodyオブジェクトを取得する。
	 * @return		Bodyオブジェクト
	 */
	Body getBody()
	{
		return collisionObject.getBody();
	}
	
	
	/** 描画オブジェクトの配列 */
	private final ArrayList<RenderObject> renderObjects = new ArrayList<RenderObject>(2);
	
	/** 衝突オブジェクト */
	private CollisionObject collisionObject;

	/** 鉤縄の持ち主 */
	private final Body owner;

	/** 鉤縄の状態 */
	private State state;
	
	/** 鉤縄の次の状態 */
	private State nextState;
	
	/** 縄の長さを制限するためのジョイント */
	private Joint joint;
	
	/** 鉤縄の向き */
	private final Vector2 direction = new Vector2();
	
	/** XXX あやしいフラグ（仮）　外部から指定するのは何ともメンドクサイので何とかならんかね。 */
	boolean useRopeJoint = false;
	
	
	/**
	 * 衝突判定のコールバック
	 */
	protected class CollisionCallback extends AbstractCollisionCallback
	{
		@Override
		public void dispatch(AbstractCollisionCallback collisionCallback, Contact contact)
		{
			collisionCallback.collision(Kaginawa.this, contact);
		}

		@Override
		public void collision(Terrain obj, Contact contact)
		{
			Kaginawa.this.state.collision(Kaginawa.this);
		}
	}
	
	
	/** 
	 * 鉤縄の状態による振る舞い
	 */
	private enum State
	{
		/** 
		 * 待機状態
		 */
		IDLE
		{
			@Override
			void initialize(Kaginawa me)
			{
				// 鉤縄を初期化
				me.setActive(false);
				
				// 持ち主を初期化
			}

			@Override
			void slack(Kaginawa me, Vector2 direction)
			{
				me.direction.set(direction.nor());
				me.setNextState(SLACK);
			}
		},
		
		/**
		 * 鉤縄を伸ばしている状態
		 */
		SLACK
		{
			@Override
			void initialize(Kaginawa me)
			{
				Body kaginawa		= me.getBody();
				Body owner			= me.owner;
				Vector2 direction	= me.direction;
				
				// 鉤縄を初期化
				kaginawa.setType(BodyType.DynamicBody);
				kaginawa.setLinearVelocity(direction.x*KaginawaDefine.INSTANCE.SLACK_VELOCITY, direction.y*KaginawaDefine.INSTANCE.SLACK_VELOCITY);
				kaginawa.setTransform(owner.getPosition(), direction.angle() * MathUtils.degreesToRadians);
				me.setActive(true);
				
				// 持ち主を初期化
			}

			@Override
			void update(Kaginawa me)
			{
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				
				// 縄の長さが限界に達したら、鉤縄を窓から投げ捨てろ！
				if(kaginawa.getPosition().dst2(owner.getPosition()) > KaginawaDefine.INSTANCE.LENGTH*KaginawaDefine.INSTANCE.LENGTH)
				{
					release(me);
				}
			}

			@Override
			void shrink(Kaginawa me)
			{
				me.setNextState(RELEASE);
			}

			@Override
			void release(Kaginawa me)
			{
				me.setNextState(RELEASE);
			}

			@Override
			void collision(Kaginawa me)
			{
				me.setNextState(HANG);
			}
		},
		
		/**
		 * 鉤縄を縮めている状態
		 */
		SHRINK
		{
			@Override
			void initialize(Kaginawa me)
			{
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				World world		= kaginawa.getWorld();
				
				// 鉤縄を初期化
				kaginawa.setType(BodyType.StaticBody);
				kaginawa.setLinearVelocity(Vector2.Zero);
				
				// 持ち主を初期化
				owner.setLinearVelocity(Vector2.Zero);		// TODO なくす？
				
				// ジョイントがあれば切り離す
				if(me.joint != null)
				{
					world.destroyJoint(me.joint);
					me.joint = null;
				}
			}

			@Override
			void update(Kaginawa me)
			{
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				Vector2 kaginawaPos = kaginawa.getPosition();
				Vector2 ownerPos = owner.getPosition();
				Vector2 direction = new Vector2(kaginawaPos.x-ownerPos.x, kaginawaPos.y-ownerPos.y);
				direction.nor().mul(KaginawaDefine.INSTANCE.SHRINK_VELOCITY);
	
				owner.setLinearVelocity(direction);
			}

			@Override
			void hang(Kaginawa me)
			{
				me.setNextState(HANG);
			}

			@Override
			void release(Kaginawa me)
			{
				me.setNextState(RELEASE);
			}
		},
		
		/** 
		 * 鉤縄にぶら下がっている状態
		 */
		HANG
		{
			@Override
			void initialize(Kaginawa me)
			{
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				World world		= kaginawa.getWorld();
				
				// 鉤縄にジョイントは存在しないハズ
				assert kaginawa.getJointList().isEmpty();

				// 鉤縄を初期化
				kaginawa.setType(BodyType.StaticBody);
				kaginawa.setLinearVelocity(Vector2.Zero);
				
				// 持ち主を初期化
				owner.setLinearVelocity(Vector2.Zero);
				
				// ジョイントを生成
				if( me.useRopeJoint )
				{
					RopeJointDef jd = new RopeJointDef();
					jd.bodyA = owner;
					jd.bodyB = kaginawa;
					jd.localAnchorA.set(Vector2.Zero);
					jd.localAnchorB.set(Vector2.Zero);
					jd.maxLength = KaginawaDefine.INSTANCE.LENGTH;
					me.joint = world.createJoint(jd);
				}
				else
				{
					DistanceJointDef jd = new DistanceJointDef();
					jd.initialize(owner, kaginawa, owner.getPosition(), kaginawa.getPosition());
					me.joint = world.createJoint(jd);
				}
			}

			@Override
			void update(Kaginawa me)
			{
				// TODO Auto-generated method stub
			}

			@Override
			void shrink(Kaginawa me)
			{
				me.setNextState(SHRINK);
			}

			@Override
			void release(Kaginawa me)
			{
				me.setNextState(RELEASE);
			}
		},
		
		/** 
		 * 鉤縄を離した状態
		 */
		RELEASE
		{
			@Override
			void initialize(Kaginawa me)
			{
				Body kaginawa	= me.getBody();
				World world		= kaginawa.getWorld();
				
				// 鉤縄を初期化
				me.setActive(false);
				
				// 持ち主を初期化
				
				// ジョイントがあれば切り離す
				if(me.joint != null)
				{
					world.destroyJoint(me.joint);
					me.joint = null;
				}
				
				//---- エフェクトを発生させる。
//				new KaginawaReleaseEffect(me.getRenderObjects(), me.getPositionX(), me.getPositionY(), kaginawa.getAngle() * MathUtils.radiansToDegrees);
			}

			@Override
			void update(Kaginawa me)
			{
				// TODO エフェクトが消えるのを待つ処理とか？
				me.setNextState(IDLE);
			}
		},
		;
		
		/**
		 * 状態を初期化する。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		abstract void initialize(Kaginawa me);
		
		/**
		 * 状態を更新する。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		void update(Kaginawa me) { /* 何もしない */ }

		/**
		 * 鉤縄を伸ばす。
		 * @param me			自身を指す鉤縄オブジェクト
		 * @param direction		伸ばす方向
		 */
		void slack(Kaginawa me, Vector2 direction) { /* 何もしない */ }

		/**
		 * 鉤縄を縮める。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		void shrink(Kaginawa me) { /* 何もしない */ }

		/**
		 * 鉤縄にぶら下がる。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		void hang(Kaginawa me) { /* 何もしない */ }

		/**
		 * 鉤縄を離す。
		 * @param me	自身を示す鉤縄オブジェクト
		 */
		void release(Kaginawa me) { /* 何もしない */ }
		
		/**
		 * 鉤縄が何かに衝突した。
		 * @param me	自身を示す鉤縄オブジェクト
		 */
		void collision(Kaginawa me) { /* 何もしない */ }
	}
}
