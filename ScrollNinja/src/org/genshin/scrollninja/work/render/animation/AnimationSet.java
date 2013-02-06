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
	 * @param def アニメーションセットの初期化用定義
	 */
	AnimationSet(AnimationSetDef def)
	{
		//---- テクスチャアニメーション
		if(def.textureAnimations != null)
		{
			for(TextureAnimationPair animationPair : def.textureAnimations)
			{
				if(animationPair.animation.uvSize == null)
					animationPair.animation.uvSize = def.uvSize;
				animations.put(animationPair.name, new TextureAnimation(animationPair.animation));
			}
		}
		
		//---- UVスクロールアニメーション
		if(def.uvScrollAnimations != null)
		{
			for(UVScrollAnimationPair animationPair : def.uvScrollAnimations)
			{
				if(animationPair.animation.uvSize == null)
					animationPair.animation.uvSize = def.uvSize;
				animations.put(animationPair.name, new UVScrollAnimation(animationPair.animation));
			}
		}
	}
	
	/**
	 * アニメーションを取得する。
	 * @param animationName		取得するアニメーションの名前
	 * @return		指定した名前のアニメーションオブジェクト
	 */
	public AnimationInterface getAnimation(String animationName)
	{
		return animations.get(animationName);
	}
	
	
	/** アニメーションのマップ */
	private final Map<String, AnimationInterface> animations = new HashMap<String, AnimationInterface>();
}
