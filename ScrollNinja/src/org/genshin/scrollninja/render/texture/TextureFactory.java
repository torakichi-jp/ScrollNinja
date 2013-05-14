package org.genshin.scrollninja.render.texture;

import org.genshin.engine.system.factory.AbstractWeakFlyweightFactory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

/**
 * テクスチャを生成・管理するクラス（シングルトン）<br>
 * テクスチャを取得する際、既に同じファイルから作られたテクスチャが存在する場合はそれを使い回す。<br>
 * 他クラスからの強参照がなくなったテクスチャはGCで自動的に削除される。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public final class TextureFactory extends AbstractWeakFlyweightFactory<String, Texture>
{
	/**
	 * コンストラクタ
	 */
	private TextureFactory()
	{
		/* 何もしない */
	}

	/**
	 * シングルトンインスタンスを取得する。
	 * @return		シングルトンインスタンス
	 */
	public static TextureFactory getInstance()
	{
		return instance;
	}
	
	@Override
	protected Texture create(String key)
	{
		final Texture texture = new Texture(key);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return texture;
	}

	/** シングルトンインスタンス */
	private static final TextureFactory instance = new TextureFactory(); 
}
