/**
 *
 */
package org.genshin.scrollninja.object.weapon;

import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.ScrollNinja;
import org.genshin.scrollninja.object.AbstractObject;
import org.genshin.scrollninja.object.Background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

/**
 * 鉤縄クラス
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class Kaginawa extends AbstractWeapon
{
	/**
	 * コンストラクタ
	 * @param world		所属先となるWorldオブジェクト
	 * @param owner		持ち主となるBodyオブジェクト
	 */
	public Kaginawa(World world, Body owner)
	{
		// Body生成
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;		// 移動するオブジェクト
		bodyDef.active = false;					// 最初は活動しない
		bodyDef.bullet = true;					// すり抜けない
		bodyDef.gravityScale = 0.0f;				// 重力の影響を受けない
		createBody(world, bodyDef);

		// 衝突オブジェクト生成
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(COLLISION.RADIUS);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density		= 0.0f;			// 密度
		fixtureDef.friction		= 0.0f;			// 摩擦
		fixtureDef.isSensor		= true;			// センサーフラグ
		fixtureDef.shape		= circleShape;	// 形状

		createFixture(fixtureDef);
		circleShape.dispose();

		// スプライト生成
		Texture texture = new Texture(Gdx.files.internal(SPRITE.PATH));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Sprite sprite = new Sprite(texture);
		sprite.setOrigin(texture.getWidth()*0.5f, texture.getHeight()*0.5f);
		sprite.setScale(ScrollNinja.scale);

		this.sprites.add(sprite);

		// フィールド初期化
		this.owner = owner;
		
		changeState(State.IDLE);
	}

	/**
	 * 鉤縄を投げる。
	 */
	public final void doThrow()
	{
		state.doThrow(this);
	}
	
	/**
	 * 鉤縄を縮める。
	 */
	public final void doShrink()
	{
		state.doShrink(this);
	}

	/**
	 * 鉤縄にぶら下がる。
	 */
	public final void doHang()
	{
		state.doHang(this);
	}

	/**
	 * 鉤縄を離す。
	 */
	public final void doRelease()
	{
		state.doRelease(this);
	}

	@Override
	public void update()
	{
		state.update(this);
	}

	@Override
	public void dispatchCollision(AbstractObject object, Contact contact)
	{
		object.notifyCollision(this, contact);
	}

	@Override
	public void notifyCollision(Background obj, Contact contact)
	{
		// TODO 鉤縄の衝突処理とか。
		doHang();
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
	

	/** 衝突関連の定数 */
	private static final class COLLISION
	{
		/** 衝突オブジェクトの半径({@value}) */
		private static final float RADIUS = 1.0f;
	}

	/** スプライト関連の定数 */
	private static final class SPRITE
	{
		/** テクスチャのパス({@value}) */
		private static final String PATH = "data/shuriken.png";
	}

	/** 鉤縄の飛ぶ速度({@value}) */
	private static final float THROW_VEL = 45.0f;

	/** 鉤縄の縮む速度({@value}) */
	private static final float SHRINK_VEL = THROW_VEL*2.0f;

	/** 鉤縄の長さ({@value}) */
	private static final float LEN_MAX = THROW_VEL*1.0f;

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
			void doThrow(Kaginawa me)
			{
				me.changeState(THROW);
			}
		},
		
		/**
		 * 鉤縄を投げている状態
		 */
		THROW
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
				kaginawa.setLinearVelocity(dir.x*THROW_VEL, dir.y*THROW_VEL);
				kaginawa.setTransform(owner.getPosition(), 0);
				kaginawa.setActive(true);
				
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
				
				if(direction.len2() > LEN_MAX*LEN_MAX)
				{
					//doRelease(me);
					doHang(me);
				}
			}

			@Override
			void doShrink(Kaginawa me)
			{
				me.changeState(SHRINK);
			}

			@Override
			void doHang(Kaginawa me)
			{
				me.changeState(HANG);
			}

			@Override
			void doRelease(Kaginawa me)
			{
				me.changeState(RELEASE);
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
				direction.nor().mul(SHRINK_VEL);
	
				owner.setLinearVelocity(direction);
	
				if(len2 < (SHRINK_VEL*SHRINK_VEL)/30/30)
				{
					doRelease(me);
				}
			}

			@Override
			void doHang(Kaginawa me)
			{
				me.changeState(HANG);
			}

			@Override
			void doRelease(Kaginawa me)
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
				RopeJointDef jd = new RopeJointDef();
				jd.bodyA = owner;
				jd.bodyB = kaginawa;
				jd.localAnchorA.set(Vector2.Zero);
				jd.localAnchorB.set(Vector2.Zero);
				jd.maxLength = kaginawa.getPosition().sub(owner.getPosition()).len();
				
				me.joint = world.createJoint(jd);
			}

			@Override
			void update(Kaginawa me)
			{
				// TODO Auto-generated method stub
			}

			@Override
			void doShrink(Kaginawa me)
			{
				me.changeState(SHRINK);
			}

			@Override
			void doRelease(Kaginawa me)
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
		 * 鉤縄を投げる。
		 * @param me	自身を指す鉤縄オブジェクト
		 * 
		 * FIXME 実験用に鉤縄投げ放題モード
		 */
		void doThrow(Kaginawa me) {	me.changeState(RELEASE); }///* 何もしない */ }

		/**
		 * 鉤縄を縮める。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		void doShrink(Kaginawa me) { /* 何もしない */ }

		/**
		 * 鉤縄にぶら下がる。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		void doHang(Kaginawa me) { /* 何もしない */ }

		/**
		 * 鉤縄を離す。
		 * @param me	自身を示す鉤縄オブジェクト
		 */
		void doRelease(Kaginawa me) { /* 何もしない */ }
	}
}
