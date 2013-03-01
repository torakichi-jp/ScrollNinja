package org.genshin.scrollninja.work.object.effect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * エフェクトの初期化用定義
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class EffectDef
{
	/** エフェクトの寿命（秒） */
	public float life;
	
	/** エフェクトの初期速度（毎秒） */
	public Vector2 startVelocity;
	
	/** エフェクトの最終速度（毎秒） */
	public Vector2 endVelocity;
	
	/** エフェクトの初期角速度 */
	public float startAngularVelocity;
	
	/** エフェクトの最終角速度 */
	public float endAngularVelocity;
	
	/** エフェクトの初期色 */
	public Color startColor;
	
	/** エフェクトの最終色 */
	public Color endColor;
}
