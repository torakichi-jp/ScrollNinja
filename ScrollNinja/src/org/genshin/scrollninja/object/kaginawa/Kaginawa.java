package org.genshin.scrollninja.object.kaginawa;

import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.ScrollNinja;
import org.genshin.scrollninja.object.AbstractCollisionObject;
import org.genshin.scrollninja.object.AbstractDynamicObject;
import org.genshin.scrollninja.object.Background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

/**
 * 鉤縄クラス
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class Kaginawa extends AbstractDynamicObject
{
	/**
	 * コンストラクタ
	 * @param world		所属先となるWorldオブジェクト
	 * @param owner		持ち主となるBodyオブジェクト
	 */
	public Kaginawa(World world, Body owner)
	{
		super(world);

		this.owner = owner;
		changeState(State.IDLE);
	}

	/**
	 * 鉤縄を伸ばす。
	 */
	public final void slack()
	{
		state.slack(this);
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
	public void update()
	{
		state.update(this);
	}

	@Override
	public void dispatchCollision(AbstractCollisionObject object, Contact contact)
	{
		object.notifyCollision(this, contact);
	}

	@Override
	public void notifyCollision(Background obj, Contact contact)
	{
		// TODO 鉤縄の衝突処理とか。
		state.collision(this);
	}

	@Override
	protected void initializeSprite()
	{
		this.sprites.add(KaginawaParam.INSTANCE.ROPE_SPRITE_LOADER.create());
		this.sprites.add(KaginawaParam.INSTANCE.ANCHOR_SPRITE_LOADER.create());
	}
	
	@Override
	protected BodyDef createBodyDef()
	{
		BodyDef bd = super.createBodyDef();
		bd.gravityScale = 0.0f;
		return bd;
	}

	@Override
	protected FixtureDef createFixtureDef()
	{
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(KaginawaParam.INSTANCE.COLLISION_RADIUS);

		FixtureDef fd	= super.createFixtureDef();
		fd.isSensor		= true;			// XXX センサーフラグ。いずれはFilterに代わる予定。
		fd.shape		= circleShape;
		return fd;
	}

	/**
	 * 状態を変更する。
	 * @param next	次の状態
	 */
	private void changeState(State next)
	{
		assert next!=null;
		
		state = next;
		state.initialize(this);
	}

	@Override
	public void render()
	{
		// XXX 縄の描画（仮）
		Vector2 kaginawaPosition = getBody().getPosition();
		Vector2 ownerPosition = owner.getPosition();
		Vector2 direction = ownerPosition.sub(kaginawaPosition);
		Sprite rope = sprites.get(0);
		float len = direction.len()/GlobalParam.INSTANCE.WORLD_SCALE;
		
		rope.setSize(len, rope.getHeight());
		rope.setRotation(direction.angle());
		rope.setRegion(0, 0, (int)len, 64);
		
		super.render();
	}

	/** 鉤縄の持ち主 */
	private Body owner;

	/** 鉤縄の状態 */
	private State state;
	
	/** 縄の長さを制限するためのジョイント */
	private Joint joint;
	
	
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
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				
				// 鉤縄を初期化
				kaginawa.setActive(false);
				
				// 持ち主を初期化
				owner.setGravityScale(1.0f);
			}

			@Override
			void slack(Kaginawa me)
			{
				me.changeState(SLACK);
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
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				Vector2 dir		= new Vector2();
				
				// FIXME 鉤縄の向き設定（仮）
				Vector2 mousePos = new Vector2(
					Gdx.input.getX() - Gdx.graphics.getWidth()*0.5f,
					Gdx.graphics.getHeight()*0.5f - Gdx.input.getY()
				);
				Vector2 ownerPos = owner.getPosition();

				mousePos.mul(ScrollNinja.scale);
				mousePos.x += GameMain.camera.position.x;
				mousePos.y += GameMain.camera.position.y;

				dir.x = mousePos.x - ownerPos.x;
				dir.y = mousePos.y - ownerPos.y;
				dir.nor();
				
				// 鉤縄を初期化
				kaginawa.setType(BodyType.DynamicBody);
				kaginawa.setLinearVelocity(dir.x*KaginawaParam.INSTANCE.SLACK_VELOCITY, dir.y*KaginawaParam.INSTANCE.SLACK_VELOCITY);
				kaginawa.setTransform(owner.getPosition(), 0.0f);
				kaginawa.setActive(true);
				
				// XXX 仮
				Sprite anchor = me.sprites.get(1);
				anchor.setRotation(dir.angle());
				
				// 持ち主を初期化
				//owner.setGravityScale(1.0f);
			}

			@Override
			void update(Kaginawa me)
			{
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				
				// 縄の長さが限界に達したらぶら下がりん。
				Vector2 kaginawaPos = kaginawa.getPosition();
				Vector2 ownerPos = owner.getPosition();
				Vector2 direction = new Vector2(kaginawaPos.x-ownerPos.x, kaginawaPos.y-ownerPos.y);
				
				if(direction.len2() > KaginawaParam.INSTANCE.LENGTH*KaginawaParam.INSTANCE.LENGTH)
				{
					me.changeState(HANG);
				}
			}

			@Override
			void shrink(Kaginawa me)
			{
				me.changeState(SHRINK);
			}

			@Override
			void release(Kaginawa me)
			{
				me.changeState(RELEASE);
			}

			@Override
			void collision(Kaginawa me)
			{
				me.changeState(HANG);
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
				kaginawa.setType(BodyType.DynamicBody);
				kaginawa.setLinearVelocity(Vector2.Zero);
				
				// 持ち主を初期化
				owner.setGravityScale(0.0f);
				owner.setLinearVelocity(Vector2.Zero);		// TODO なくす？
				
				// ジョイントがあれば切り離す
				if(me.joint != null)
				{
					kaginawa.getWorld().destroyJoint(me.joint);
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
				float len2 = direction.len2();
				direction.nor().mul(KaginawaParam.INSTANCE.SHRINK_VELOCITY);
	
				owner.setLinearVelocity(direction);
	
				if(len2 < (KaginawaParam.INSTANCE.SHRINK_VELOCITY*KaginawaParam.INSTANCE.SHRINK_VELOCITY)/30/30)
				{
					release(me);
				}
			}

			@Override
			void hang(Kaginawa me)
			{
				me.changeState(HANG);
			}

			@Override
			void release(Kaginawa me)
			{
				me.changeState(RELEASE);
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
				owner.setGravityScale(1.0f);
				
				// ジョイントを生成
				// XXX RopeJoint使っても面白いかも知れない。
				DistanceJointDef jd = new DistanceJointDef();
				jd.initialize(owner, kaginawa, owner.getPosition(), kaginawa.getPosition());
				me.joint = world.createJoint(jd);
			}

			@Override
			void update(Kaginawa me)
			{
				// TODO Auto-generated method stub
			}

			@Override
			void shrink(Kaginawa me)
			{
				me.changeState(SHRINK);
			}

			@Override
			void release(Kaginawa me)
			{
				me.changeState(RELEASE);
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
				Body owner		= me.owner;
				World world		= kaginawa.getWorld();
				
				// 鉤縄を初期化
				kaginawa.setActive(false);
				
				// 持ち主を初期化
				owner.setGravityScale(1.0f);
				
				// ジョイントがあれば切り離す
				if(me.joint != null)
				{
					kaginawa.getWorld().destroyJoint(me.joint);
					me.joint = null;
				}
				
				// TODO 鉤縄を離した時のエフェクト的なものを発生させる。
			}

			@Override
			void update(Kaginawa me)
			{
				// TODO エフェクトが消えるのを待つ処理とか？
				me.changeState(IDLE);
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
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		void slack(Kaginawa me) { /* 何もしない */ }

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
