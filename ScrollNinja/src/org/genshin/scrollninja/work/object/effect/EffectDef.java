package org.genshin.scrollninja.work.object.effect;

import org.genshin.scrollninja.GlobalDefine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;

/**
 * エフェクトの初期化用定義
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class EffectDef
{
	/**
	 * コンストラクタ
	 * @param effectFilePath		エフェクト初期化用定義ファイルのパス
	 */
	public EffectDef(String effectFilePath)
	{
		final Json json = new Json();
		final JsonReader jr = new JsonReader();
		final Object jsonData = jr.parse(Gdx.files.internal(effectFilePath));
		
		spriteFilePath			= json.readValue("spriteFilePath", String.class, jsonData);
		animationFilePath		= json.readValue("animationFilePath", String.class, jsonData);
		animationName			= json.readValue("animationName", String.class, jsonData);
		
		life					= json.readValue("life", Float.class, jsonData);
		
		final float invLife = 1.0f / life;
		final float worldScale = GlobalDefine.INSTANCE.WORLD_SCALE;
		
		startVelocity			= json.readValue("startVelocity", Vector2.class, jsonData).mul(worldScale);
		velocityAccel			= json.readValue("endVelocity", Vector2.class, jsonData).mul(worldScale).sub(startVelocity).mul(invLife);
		
		startAngularVelocity	= json.readValue("startAngularVelocity", Float.class, jsonData);
		angularVelocityAccel	= (json.readValue("endAngularVelocity", Float.class, jsonData).floatValue() - startAngularVelocity) * invLife;
		
		startColor				= json.readValue("startColor", Color.class, jsonData);
		colorVelocity			= json.readValue("endColor", Color.class, jsonData);
		
		colorVelocity.r = (colorVelocity.r - startColor.r) * invLife;
		colorVelocity.g = (colorVelocity.g - startColor.g) * invLife;
		colorVelocity.b = (colorVelocity.b - startColor.b) * invLife;
		colorVelocity.a = (colorVelocity.a - startColor.a) * invLife;
	}
	
	
	/** スプライト定義ファイルのパス */
	public final String spriteFilePath;
	
	/** アニメーション定義ファイルのパス */
	public final String animationFilePath;
	
	/** アニメーション名 */
	public final String animationName;
	
	/** エフェクトの寿命 */
	public final float life;
	
	/** エフェクトの初期速度 */
	public final Vector2 startVelocity;
	
	/** エフェクトの加速度 */
	public final Vector2 velocityAccel;
	
	/** エフェクトの初期角速度 */
	public final float startAngularVelocity;
	
	/** エフェクトの角速度の加速度 */
	public final float angularVelocityAccel;
	
	/** エフェクトの初期色 */
	public final Color startColor;
	
	/** エフェクトの色の変化速度 */
	public final Color colorVelocity;
}
