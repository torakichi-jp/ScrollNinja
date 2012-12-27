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
	 */
	void AnimationSet()
	{
		
	}
	
	/** アニメーションのマップ */
	private final Map<String, Animation> animations = new HashMap<String, Animation>();
}
