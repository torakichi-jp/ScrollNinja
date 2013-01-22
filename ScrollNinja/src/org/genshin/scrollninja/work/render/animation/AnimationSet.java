package org.genshin.scrollninja.work.render.animation;

import java.util.HashMap;
import java.util.Map;

/**
 * 複数のアニメーションを管理するクラス
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class AnimationSet
{
	/**
	 * コンストラクタ
	 * @param animationSetDef アニメーションセットの初期化用定義
	 */
	AnimationSet(AnimationSetDef animationSetDef)
	{
		for(AnimationPair animationPair : animationSetDef.animations)
		{
			animationPair.animation.uvSize = animationSetDef.uvSize;
			animations.put(animationPair.name, new AnimationWrapper(animationPair.animation));
		}
	}
	
	/**
	 * アニメーションを取得する。
	 * @param animationName		取得するアニメーションの名前
	 * @return		指定した名前のアニメーションオブジェクト
	 */
	public AnimationWrapper getAnimation(String animationName)
	{
		return animations.get(animationName);
	}
	
	/** アニメーションのマップ */
	private final Map<String, AnimationWrapper> animations = new HashMap<String, AnimationWrapper>();
}
