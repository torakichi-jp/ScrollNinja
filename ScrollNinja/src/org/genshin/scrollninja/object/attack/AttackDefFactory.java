package org.genshin.scrollninja.object.attack;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.utils.JsonReader;

import com.badlogic.gdx.math.MathUtils;

/**
 * 攻撃の初期化用定義オブジェクトの生成を管理する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class AttackDefFactory extends AbstractFlyweightFactory<String, AttackDef>
{
	/**
	 * 非公開コンストラクタ
	 */
	private AttackDefFactory()
	{
		/* 何もしない */
	}
	
	/**
	 * シングルトンインスタンスを取得する。
	 * @return		シングルトンインスタンス
	 */
	public static AttackDefFactory getInstance()
	{
		return instance;
	}
	
	@Override
	protected AttackDef create(String key)
	{
		final AttackDef def = new AttackDef();
		final JsonReader reader = new JsonReader(key);
		
		def.collisionFilePath	= reader.read("collisionFilePath", String.class);
		def.power				= reader.read("power", Float.class);
		
		if(reader.hasChild("slash"))
		{
			def.type = reader.read("slash", SlashType.class);
		}
		else if(reader.hasChild("bullet"))
		{
			final BulletType bt = reader.read("bullet", BulletType.class);
			bt.velocity *= GlobalDefine.INSTANCE.WORLD_SCALE;
			bt.angularVelocity *= MathUtils.degreesToRadians;
			def.type = bt;
		}
		
		return def;
	}
	
	
	/** シングルトンインスタンス */
	private static final AttackDefFactory instance = new AttackDefFactory();
}
