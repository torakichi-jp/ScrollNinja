package org.genshin.scrollninja.collision;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

/**
 * 衝突判定の初期化用定義オブジェクトの生成を管理するクラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class CollisionDefFactory extends AbstractFlyweightFactory<String, CollisionDef>
{
	/**
	 * コンストラクタ
	 */
	private CollisionDefFactory()
	{
		/* 何もしない */
	}
	
	/**
	 * シングルトンインスタンスを取得する。
	 * @return		シングルトンインスタンス
	 */
	public static CollisionDefFactory getInstance()
	{
		return instance;
	}
	
	@Override
	protected CollisionDef create(String key)
	{
		Json json = new Json();
		
		return json.fromJson(CollisionDef.class, Gdx.files.internal(key));
	}
	
	/** シングルトンインスタンス */
	private static CollisionDefFactory instance = new CollisionDefFactory();
}
