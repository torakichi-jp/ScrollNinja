package org.genshin.scrollninja.object.effect;

import org.genshin.scrollninja.render.RenderObject;


/**
 * コピーエフェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class CopyEffect extends AbstractEffect
{
	/**
	 * コンストラクタ
	 * @param src		コピー元となる描画オブジェクト
	 * @param def		エフェクトの定義
	 */
	public CopyEffect(RenderObject src, EffectDef def)
	{
		super(def, src.getPositionX(), src.getPositionY(), src.getRotation());
		
		//---- 描画オブジェクトを複製し、エフェクト用の設定を適用する。
		RenderObject newRenderObject = src.duplicate();
		newRenderObject.setPosture(this);
		addRenderObject(newRenderObject);
	}

	/**
	 * コンストラクタ
	 * @param src		コピー元となる描画オブジェクト
	 * @param def		エフェクトの定義
	 * @param depth		描画深度（値が大きいものを手前に描画する）
	 */
	public CopyEffect(RenderObject src, EffectDef def, int depth)
	{
		super(def, src.getPositionX(), src.getPositionY(), src.getRotation());
		
		//---- 描画オブジェクトを複製し、エフェクト用の設定を適用する。
		RenderObject newRenderObject = src.duplicate();
		newRenderObject.setPosture(this);
		newRenderObject.setDepth(depth);
		addRenderObject(newRenderObject);
	}
}
