package org.genshin.scrollninja.work.render.sprite;

import java.io.File;
import java.io.IOException;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.utils.TextureFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
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
		
		//---- スプライト定義の補完
		final Texture texture = TextureFactory.getInstance().get(spriteDef.textureFilePath);
		
		// UVマップの横幅、縦幅が0の場合はテクスチャのサイズに合わせる。
		if(spriteDef.uv.width == 0)
			spriteDef.uv.width = texture.getWidth();
		if(spriteDef.uv.height == 0)
			spriteDef.uv.height = texture.getHeight();
		
		// UVマップが1.0の範囲を越える場合、テクスチャのラップ設定をrepeatにしておく。
		texture.setWrap(
			(spriteDef.uv.x + spriteDef.uv.width > texture.getWidth()) ? TextureWrap.Repeat : texture.getUWrap(),
			(spriteDef.uv.y + spriteDef.uv.height > texture.getHeight()) ? TextureWrap.Repeat : texture.getVWrap()
		);
		
		// スプライトのサイズが0の場合はUVマップのサイズに合わせる。
		if(spriteDef.size.x == 0.0f)
			spriteDef.size.x = spriteDef.uv.width;
		if(spriteDef.size.y == 0.0f)
			spriteDef.size.y = spriteDef.uv.height;
		
		// originは(0, 0)をスプライトの中心点とする。
		spriteDef.origin.add(spriteDef.size.x * 0.5f, spriteDef.size.y * 0.5f);
		
		// 世界のスケールを押し付けておく。
		spriteDef.position.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
		spriteDef.size.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
		spriteDef.origin.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
		
		//---- スプライトを生成する。
		final Sprite sprite = new Sprite();
		
		sprite.setTexture( texture );
		sprite.setRegion(spriteDef.uv.x, spriteDef.uv.y, spriteDef.uv.width, spriteDef.uv.height);
		sprite.setBounds(spriteDef.size.x * -0.5f + spriteDef.position.x, spriteDef.size.y * -0.5f + spriteDef.position.y, spriteDef.size.x, spriteDef.size.y);
		sprite.setOrigin(spriteDef.origin.x, spriteDef.origin.y);
		
		return sprite;
	}
	
	
	/** シングルトンインスタンス */
	private final static SpriteFactory instance = new SpriteFactory();
}
