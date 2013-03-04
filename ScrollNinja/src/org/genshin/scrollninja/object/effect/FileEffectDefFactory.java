package org.genshin.scrollninja.object.effect;

import java.io.IOException;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;
import org.genshin.scrollninja.GlobalDefine;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		FileEffectDef def = null;
		
		//---- エフェクトの定義情報をファイルから読み込む。
		try
		{
			final ObjectMapper objectMapper = new ObjectMapper();
			def = objectMapper.readValue(Gdx.files.internal(key).read(), FileEffectDef.class);
		}
		catch (JsonParseException e) { e.printStackTrace(); }
		catch (JsonMappingException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		
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
