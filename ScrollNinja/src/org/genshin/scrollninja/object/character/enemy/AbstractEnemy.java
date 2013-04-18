package org.genshin.scrollninja.object.character.enemy;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;
import org.genshin.scrollninja.object.attack.AbstractAttack;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.terrain.Terrain;
import org.genshin.scrollninja.object.weapon.AbstractWeapon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 敵の基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractEnemy extends AbstractCharacter
{
	/**
	 * コンストラクタ
	 * @param enemyFilePath			敵の定義ファイルのパス
	 * @param world					所属する世界オブジェクト
	 */
	public AbstractEnemy(String enemyFilePath, World world)
	{
		super((enemyDefTmp = EnemyDefFactory.getInstance().get(enemyFilePath)).collisionFilePath, world);
		
		enemyDef = enemyDefTmp;
		enemyDefTmp = null;
		
		weapon = createWeapon();
		
		stateFactory = createStateFactory();
		state = stateFactory.get("Patrol");
		state.initialize();
	}
	
	@Override
	public void dispose()
	{
		//---- いろいろ破棄する。
		weapon.dispose();
		stateFactory.clear();
		
		//---- 基本クラスを破棄する。
		super.dispose();
	}

	@Override
	public void update(float deltaTime)
	{
		//---- 状態別更新処理
		state.update(deltaTime);
		
		//---- 向きの設定
		final boolean flip = direction > 0.0f;
		flipX(flip);
		getCollisionObject().flipX(flip);
		
		//---- 追跡対象は毎フレームクリアしておく。
		chaseTarget = null;
		
		//---- お前はもう死んでいる。
		if(isDead())
		{
// FIXME 敵をrespawnさせて遊ぶために、一時的にコメントアウト。最終的には戻す。
//			dispose();
		}
	}

	@Override
	protected AbstractCharacterCollisionCallback createCollisionCallback()
	{
		return new EnemyCollisionCallback();
	}
	
	/**
	 * 武器を生成する。
	 * @return		武器
	 */
	protected abstract AbstractWeapon createWeapon();
	
	/**
	 * 状態の生成を管理するオブジェクトを生成する。
	 * @return		状態の生成を管理するオブジェクト
	 */
	protected EnemyStateFactory createStateFactory()
	{
		return new EnemyStateFactory();
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
	 * 移動する。
	 * @param accel				加速度
	 * @param maxVelocity		最大速度
	 */
	private void move(float accel, float maxVelocity)
	{
		final Body body = getCollisionObject().getBody();
		
		// 最高速度に達していなければ加速する
		if( Math.abs(body.getLinearVelocity().x) < maxVelocity )
		{
			body.applyLinearImpulse(accel * direction, 0.0f, body.getPosition().x, body.getPosition().y);
			
			// 最高速度を越えている場合は丸める
			final Vector2 velocity = Vector2.tmp.set(body.getLinearVelocity());
			if( Math.abs(velocity.x) > maxVelocity )
			{
				body.setLinearVelocity(Math.signum(velocity.x) * maxVelocity, velocity.y);
			}
		}
	}
	
	
	/** 敵の定義（作業用の一時領域） */
	private static EnemyDef enemyDefTmp;
	
	/** 敵の定義 */
	private final EnemyDef enemyDef;
	
	/** 武器 */
	private final AbstractWeapon weapon;
	
	/** 状態の生成を管理するオブジェクト */
	private final EnemyStateFactory stateFactory;
	
	/** 敵の状態 */
	private EnemyStateInterface state;
	
	/** 敵の向き */
	private float direction = 1.0f;
	
	/** 追跡する対象となるキャラクター */
	private AbstractCharacter chaseTarget = null;
	
	
	/**
	 * 衝突判定のコールバック
	 */
	private class EnemyCollisionCallback extends AbstractCharacterCollisionCallback
	{
		@Override
		public void collision(Terrain obj, Contact contact)
		{
			//---- 地形と足が衝突した時
			if(contactIs("Foot", contact))
			{
				
			}
		}

		@Override
		public void collision(AbstractCharacter obj, Contact contact)
		{
			final Fixture visionFixture = AbstractEnemy.this.getCollisionObject().getFixture("Vision");
			if(contact.getFixtureA() == visionFixture || contact.getFixtureB() == visionFixture)
			{
				AbstractEnemy.this.chaseTarget = obj;
			}
		}

		@Override
		public void collision(AbstractAttack obj, Contact contact)
		{
			//---- ダメージを受ける。
			AbstractEnemy.this.damage(obj.getPower());
		}
	}
	
	
	/**
	 * 敵の状態のインタフェース
	 */
	protected interface EnemyStateInterface
	{
		/**
		 * 状態を初期化する。
		 */
		public void initialize();
		
		/**
		 * 状態を更新する。
		 * @param deltaTime		経過時間（秒）
		 */
		public void update(float deltaTime);
	}
	
	
	/**
	 * 敵の状態の基本クラス
	 */
	protected abstract class AbstractEnemyState implements EnemyStateInterface
	{
		/**
		 * 状態を変更する。
		 * @param stateName		状態の名前
		 */
		protected void changeState(String stateName)
		{
			final EnemyStateInterface newState = AbstractEnemy.this.stateFactory.get(stateName);
			if(AbstractEnemy.this.state != newState)
			{
				newState.initialize();
				AbstractEnemy.this.state = newState;
			}
		}
	}
	
	
	/**
	 * 一定の区間を巡回する状態
	 */
	protected class PatrolEnemyState extends AbstractEnemyState
	{
		@Override
		public void initialize()
		{
			direction = -direction;
			turnTimer = enemyDef.patrolTurnInterval;
		}

		@Override
		public void update(float deltaTime)
		{
			//---- 移動
			move(enemyDef.patrolAccel * deltaTime, enemyDef.patrolMaxVelocity);
			
			//---- 移動方向の折り返し
			if((turnTimer -= deltaTime) < 0.0f)
			{
				turnTimer = enemyDef.patrolTurnInterval;
				direction = -direction;
			}
			
			//---- 追跡対象が存在する場合は追跡状態へ
			if(chaseTarget != null)
			{
				changeState("Chase");
			}
		}
		
		
		/** 移動方向の折り返し判定用のタイマー */
		private float turnTimer;
	}
	
	
	/**
	 * プレイヤーを追跡する状態
	 */
	protected class ChaseEnemyState extends AbstractEnemyState
	{
		@Override
		public void initialize()
		{
		}

		@Override
		public void update(float deltaTime)
		{
			//---- 追跡対象が存在しない場合は巡回状態へ
			if(chaseTarget == null)
			{
				changeState("Patrol");
			}
			
			//---- 追跡中
			else
			{
				//---- 追跡対象に向かって移動する。
				direction = Math.signum( chaseTarget.getPositionX() - getPositionX() );
				move(enemyDef.chaseAccel * deltaTime, enemyDef.chaseMaxVelocity);
				
				//---- ぶんぶん丸
				weapon.attack();
			}
		}
	}
	
	/**
	 * 状態の生成を管理するファクトリクラス
	 */
	protected class EnemyStateFactory extends AbstractFlyweightFactory<String, EnemyStateInterface>
	{
		@Override
		protected EnemyStateInterface create(String key)
		{
			if(key.equals("Patrol"))	return new PatrolEnemyState();
			if(key.equals("Chase"))		return new ChaseEnemyState();
			
			return null;
		}
	}
}
