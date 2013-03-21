package org.genshin.scrollninja.object.character.enemy;

import org.genshin.scrollninja.object.attack.AbstractAttack;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.terrain.Terrain;

import com.badlogic.gdx.physics.box2d.Contact;
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
	}
	
	@Override
	public void update(float deltaTime)
	{
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
		public void collision(AbstractAttack obj, Contact contact)
		{
			//---- ダメージを受ける。
			AbstractEnemy.this.damage(obj.getPower());
		}
	}
}
