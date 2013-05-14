package org.genshin.scrollninja.render.sprite;

import java.awt.Rectangle;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.render.texture.TextureFactory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * スプライト関連の便利操作
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class SpriteUtils
{
	/**
	 * スプライトを生成する
	 * @param def		スプライトの定義
	 * @return			生成したスプライト
	 */
	public static Sprite createSprite(SpriteDef def)
	{
		//---- スプライト定義の補完
		final Texture texture = TextureFactory.getInstance().get(def.textureFilePath);
		
		// 値がnullだった場合の補完
		if(def.uv == null)
			def.uv = new Rectangle();
		if(def.size == null)
			def.size = new Vector2();
		if(def.position == null)
			def.position = Vector2.Zero;
		if(def.origin == null)
			def.origin = new Vector2();
		
		// UVマップの横幅、縦幅が0の場合はテクスチャのサイズに合わせる。
		if(def.uv.width == 0)
			def.uv.width = texture.getWidth();
		if(def.uv.height == 0)
			def.uv.height = texture.getHeight();
		
		// UVマップが1.0の範囲を越える場合、テクスチャのラップ設定をrepeatにしておく。
		texture.setWrap(
			(def.uv.x + def.uv.width > texture.getWidth()) ? TextureWrap.Repeat : texture.getUWrap(),
			(def.uv.y + def.uv.height > texture.getHeight()) ? TextureWrap.Repeat : texture.getVWrap()
		);
		
		// スプライトのサイズが0の場合はUVマップのサイズに合わせる。
		if(def.size.x == 0.0f)
			def.size.x = def.uv.width;
		if(def.size.y == 0.0f)
			def.size.y = def.uv.height;
		
		// originは(0, 0)をスプライトの中心点とする。
		def.origin.add(def.size.x * 0.5f, def.size.y * 0.5f);
		
		// 世界のスケールを押し付けておく。
		def.position.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
		def.size.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
		def.origin.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
		
		//---- スプライトを生成する。
		final Sprite sprite = new Sprite();
		
		sprite.setTexture(texture);
		sprite.setRegion(def.uv.x, def.uv.y, def.uv.width, def.uv.height);
		sprite.setBounds(def.size.x * -0.5f + def.position.x, def.size.y * -0.5f + def.position.y, def.size.x, def.size.y);
		sprite.setOrigin(def.origin.x, def.origin.y);
		
		return sprite;
	}
}
