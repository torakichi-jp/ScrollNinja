package org.genshin.scrollninja.object.character.enemy;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;
import org.genshin.scrollninja.object.attack.AbstractAttack;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.terrain.Terrain;
import org.genshin.scrollninja.utils.debug.Debug;

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
	 * @param collisionFilePath		衝突判定の定義ファイルのパス
	 * @param world					所属する世界オブジェクト
	 */
	public AbstractEnemy(String collisionFilePath, World world)
	{
		super(collisionFilePath, world);
		
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
	
	
	/** 敵の状態 */
	private EnemyStateInterface state;
	
	/** 状態の生成を管理するオブジェクト */
	private final EnemyStateFactory stateFactory;
	
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
			// TODO Auto-generated method stub
			Debug.logToConsole(getClass().getSimpleName());
		}

		@Override
		public void update(float deltaTime)
		{
			//---- 追跡対象が存在する場合は追跡状態へ
			if(AbstractEnemy.this.chaseTarget != null)
			{
				changeState("Chase");
			}
		}
	}
	
	
	/**
	 * プレイヤーを追跡する状態
	 */
	protected class ChaseEnemyState extends AbstractEnemyState
	{
		@Override
		public void initialize()
		{
			// TODO Auto-generated method stub
			Debug.logToConsole(getClass().getSimpleName());
		}

		@Override
		public void update(float deltaTime)
		{
			//---- 追跡対象が存在しない場合は巡回状態へ
			if(AbstractEnemy.this.chaseTarget == null)
			{
				changeState("Patrol");
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
			Debug.logToConsole(getClass().getSimpleName());
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
