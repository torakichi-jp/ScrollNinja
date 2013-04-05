package org.genshin.scrollninja.object.character.enemy;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;
import org.genshin.scrollninja.object.attack.AbstractAttack;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.terrain.Terrain;

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
		
		stateFactory = createStateFactory();
		state = stateFactory.get("Patrol");
		state.initialize();
	}
	
	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
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
			dispose();
		}
	}

	@Override
	protected AbstractCharacterCollisionCallback createCollisionCallback()
	{
		return new EnemyCollisionCallback();
	}
	
	/**
	 * 状態の生成を管理するオブジェクトを生成する。
	 * @return		状態の生成を管理するオブジェクト
	 */
	protected EnemyStateFactory createStateFactory()
	{
		return new EnemyStateFactory();
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
			// TODO 敵が地形に衝突した時の処理
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
				direction = Math.signum( chaseTarget.getPositionX() - getPositionX() );
				move(enemyDef.chaseAccel * deltaTime, enemyDef.chaseMaxVelocity);
			}
		}
	}
	
	
	/**
	 * 攻撃する状態
	 */
	protected class AttackEnemyState extends AbstractEnemyState
	{
		@Override
		public void initialize()
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void update(float deltaTime)
		{
			// TODO Auto-generated method stub
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
			if(key.equals("Attack"))	return new AttackEnemyState();
			
			return null;
		}
	}
}
