package org.genshin.scrollninja.object.effect;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.utils.JsonUtils;

class FileEffectDefFactory extends AbstractFlyweightFactory<String, FileEffectDef>
{
	/**
	 * コンストラクタ（非公開）
	 */
	private FileEffectDefFactory()
	{
		/* 何もしない */
	}
	
	/**
	 * シングルトンインスタンスを取得する。
	 * @return		シングルトンインスタンス
	 */
	public static FileEffectDefFactory getInstance()
	{
		return instance;
	}

	@Override
	protected FileEffectDef create(String key)
	{
		//---- エフェクトの定義情報をファイルから読み込む。
		final FileEffectDef def = JsonUtils.read(key, FileEffectDef.class);
		
		//---- 世界の法則を適用する。
		if(def != null)
		{
			final float worldScale = GlobalDefine.INSTANCE.WORLD_SCALE;
			
			def.effectDef.startVelocity.mul(worldScale);
			def.effectDef.endVelocity.mul(worldScale);
		}
		
		return def;
	}
	
	
	/** シングルトンインスタンス */
	private static FileEffectDefFactory instance = new FileEffectDefFactory();
}
