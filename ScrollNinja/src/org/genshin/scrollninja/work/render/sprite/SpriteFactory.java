package org.genshin.scrollninja.work.render.sprite;

import java.io.File;
import java.io.IOException;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * スプライトの生成を管理するクラス
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class SpriteFactory extends AbstractFlyweightFactory<String, Sprite>
{
	/**
	 * コンストラクタ
	 */
	private SpriteFactory()
	{
		/* 何もしない */
	}
	
	/**
	 * シングルトンインスタンスを取得する。
	 * @return
	 */
	public static SpriteFactory getInstance()
	{
		return instance;
	}
	
	@Override
	protected Sprite create(String key)
	{
		SpriteDef spriteDef = null;
		
		//---- スプライトの定義を読み込む
		{
			final ObjectMapper objectMapper = new ObjectMapper();
			final File source = Gdx.files.internal(key).file();
			
			try
			{
				spriteDef = objectMapper.readValue(source, SpriteDef.class);
			}
			catch (JsonParseException e)	{ e.printStackTrace(); }
			catch (JsonMappingException e)	{ e.printStackTrace(); }
			catch (IOException e)			{ e.printStackTrace(); }
			
			// 読み込み失敗
			if(spriteDef == null)
				return null;
		}
		
		//---- スプライトを生成する。
		final Sprite sprite = new Sprite();
		
//		sprite.setTexture( TextureFactory.getInstance().get(spriteDef.texturePath) );
		
		return sprite;
	}
	
	
	/** シングルトンインスタンス */
	private final static SpriteFactory instance = new SpriteFactory();
}
