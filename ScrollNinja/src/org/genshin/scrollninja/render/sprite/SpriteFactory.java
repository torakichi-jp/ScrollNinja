package org.genshin.scrollninja.render.sprite;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.utils.JsonUtils;
import org.genshin.scrollninja.utils.TextureFactory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

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
		//---- スプライトの定義を読み込む
		final SpriteDef spriteDef = JsonUtils.read(key, SpriteDef.class);
		
		// 読み込み失敗
		if(spriteDef == null)
			return null;
		
		//---- スプライト定義の補完
		final Texture texture = TextureFactory.getInstance().get(spriteDef.textureFilePath);
		
		// 値がnullだった場合の補完
		if(spriteDef.uv == null)
			spriteDef.uv = new UVMap();
		if(spriteDef.size == null)
			spriteDef.size = new Vector2();
		if(spriteDef.position == null)
			spriteDef.position = Vector2.Zero;
		if(spriteDef.origin == null)
			spriteDef.origin = new Vector2();
		
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
