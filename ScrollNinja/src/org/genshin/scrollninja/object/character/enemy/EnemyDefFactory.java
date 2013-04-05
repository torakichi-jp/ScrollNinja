package org.genshin.scrollninja.object.character.enemy;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;

/**
 * 敵の定義情報のファクトリ
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class EnemyDefFactory extends AbstractFlyweightFactory<String, EnemyDef>
{
	/**
	 * 非公開コンストラクタ
	 */
	private EnemyDefFactory()
	{
		/* 何もしない */
	}
	
	/**
	 * シングルトンインスタンスを取得する。
	 * @return		シングルトンインスタンス
	 */
	public static EnemyDefFactory getInstance()
	{
		return instance;
	}

	@Override
	protected EnemyDef create(String key)
	{
		return new EnemyDef(key);
	}
	
	
	/** シングルトンインスタンス */
	private static EnemyDefFactory instance = new EnemyDefFactory();
}
