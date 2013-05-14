package org.genshin.scrollninja.render.sprite;

import org.genshin.engine.system.factory.AbstractWeakFlyweightFactory;
import org.genshin.scrollninja.utils.JsonUtils;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * スプライトの生成を管理するクラス（シングルトン）<br>
 * スプライトを取得する際、既に同じファイルから作られたスプライトが存在する場合はそれを使い回す。<br>
 * 他クラスからの強参照がなくなったスプライトはGCで自動的に削除される。
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class SpriteFactory extends AbstractWeakFlyweightFactory<String, Sprite>
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
		
		//---- スプライトを生成する。
		return SpriteUtils.createSprite(spriteDef);
	}
	
	
	/** シングルトンインスタンス */
	private final static SpriteFactory instance = new SpriteFactory();
}
