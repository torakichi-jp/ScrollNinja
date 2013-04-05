package org.genshin.scrollninja.object.character.enemy;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.utils.JsonReader;


/**
 * 敵のパラメータ定義
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class EnemyDef
{
	/**
	 * コンストラクタ
	 * @param filePath		定義ファイルのパス
	 */
	EnemyDef(String filePath)
	{
		final float worldScale = GlobalDefine.INSTANCE.WORLD_SCALE;
		final JsonReader reader = new JsonReader(filePath);
		
		collisionFilePath	= reader.read("collisionFilePath"	, String.class);
		patrolAccel			= reader.read("patrolAccel"			, Float.class) * worldScale;
		patrolMaxVelocity	= reader.read("patrolMaxVelocity"	, Float.class) * worldScale;
		patrolTurnInterval	= reader.read("patrolTurnInterval"	, Float.class);
		chaseAccel			= reader.read("chaseAccel"			, Float.class) * worldScale;
		chaseMaxVelocity	= reader.read("chaseMaxVelocity"	, Float.class) * worldScale;
	}
	
	/** 衝突判定の定義ファイルのパス */
	final String collisionFilePath;
	
	/** 巡回時の加速度 */
	final float patrolAccel;
	
	/** 巡回時の速度上限 */
	final float patrolMaxVelocity;
	
	/** 巡回時に向きを折り返す間隔（秒） */
	final float patrolTurnInterval;
	
	/** 追跡時の加速度 */
	final float chaseAccel;
	
	/** 追跡時の速度上限 */
	final float chaseMaxVelocity;
}
