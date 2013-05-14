package org.genshin.scrollninja.render.animation;

import org.genshin.engine.system.factory.AbstractWeakFlyweightFactory;
import org.genshin.scrollninja.utils.JsonUtils;

/**
 * アニメーションセットの生成を管理するクラス
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class AnimationSetFactory extends AbstractWeakFlyweightFactory<String, AnimationSet>
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
		//---- アニメーションセットの定義を読み込む。
		final AnimationSetDef animationSetDef = JsonUtils.read(key, AnimationSetDef.class);
		
		// 読み込み失敗
		if(animationSetDef == null)
			return null;
		
		return new AnimationSet(animationSetDef);
	}
	
	
	/** シングルトンインスタンス */
	private final static AnimationSetFactory instance = new AnimationSetFactory();
}
