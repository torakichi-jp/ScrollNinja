package org.genshin.scrollninja.work.render.animation;

import java.io.IOException;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			animationSetDef = objectMapper.readValue(Gdx.files.internal(key).file(), AnimationSetDef.class);
		} catch (JsonParseException e)
		{
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (JsonMappingException e)
		{
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		animationSetDef.test();
		return null;
	}
	
	
	/** シングルトンインスタンス */
	private final static AnimationSetFactory instance = new AnimationSetFactory();
}
