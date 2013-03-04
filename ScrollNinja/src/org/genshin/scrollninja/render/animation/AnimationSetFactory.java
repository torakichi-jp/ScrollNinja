package org.genshin.scrollninja.render.animation;

import java.io.IOException;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * アニメーションセットの生成を管理するクラス
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class AnimationSetFactory extends AbstractFlyweightFactory<String, AnimationSet>
{
	/**
	 * コンストラクタ
	 */
	private AnimationSetFactory()
	{
		/* 何もしない */
	}
	
	/**
	 * シングルトンインスタンスを取得する。
	 * @return		シングルトンインスタンス
	 */
	public static AnimationSetFactory getInstance()
	{
		return instance;
	}
	
	@Override
	protected AnimationSet create(String key)
	{
		AnimationSetDef animationSetDef = null;
		
		//---- アニメーションセットの定義を読み込む。
		try
		{
			final ObjectMapper objectMapper = new ObjectMapper();
			animationSetDef = objectMapper.readValue(Gdx.files.internal(key).read(), AnimationSetDef.class);
		}
		catch (JsonParseException e)	{ e.printStackTrace(); }
		catch (JsonMappingException e)	{ e.printStackTrace(); }
		catch (IOException e)			{ e.printStackTrace(); }
		
		// 読み込み失敗
		if(animationSetDef == null)
			return null;
		
		return new AnimationSet(animationSetDef);
	}
	
	
	/** シングルトンインスタンス */
	private final static AnimationSetFactory instance = new AnimationSetFactory();
}
