package org.genshin.scrollninja.object.effect;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.render.AnimationRenderObject;
import org.genshin.scrollninja.render.RenderObject;

import com.badlogic.gdx.math.Vector2;


/**
 * 定義をファイルから読み込む汎用エフェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class FileEffect extends AbstractEffect
{
	/**
	 * コンストラクタ
	 * @param effectFilePath		定義ファイルのパス
	 * @param x						X座標
	 * @param y						Y座標
	 * @param degrees				角度（単位：度）
	 */
	public FileEffect(String effectFilePath, float x, float y, float degrees)
	{
		this(effectFilePath, x, y, degrees, false, false);
	}
	
	/**
	 * コンストラクタ
	 * @param effectFilePath		定義ファイルのパス
	 * @param x						X座標
	 * @param y						Y座標
	 * @param degrees				角度（単位：度）
	 * @param flipX					X反転フラグ
	 * @param flipY					Y反転フラグ
	 */
	public FileEffect(String effectFilePath, float x, float y, float degrees, boolean flipX, boolean flipY)
	{
		this(effectFilePath, x, y, degrees, flipX, flipY, GlobalDefine.RenderDepth.EFFECT);
	}
	
	/**
	 * コンストラクタ
	 * @param effectFilePath		定義ファイルのパス
	 * @param x						X座標
	 * @param y						Y座標
	 * @param degrees				角度（単位：度）
	 * @param depth					描画深度（値が大きいものを手前に描画する）
	 */
	public FileEffect(String effectFilePath, float x, float y, float degrees, int depth)
	{
		this(effectFilePath, x, y, degrees, false, false, depth);
	}
	
	/**
	 * コンストラクタ
	 * @param effectFilePath		定義ファイルのパス
	 * @param x						X座標
	 * @param y						Y座標
	 * @param degrees				角度（単位：度）
	 * @param flipX					X反転フラグ
	 * @param flipY					Y反転フラグ
	 * @param depth					描画深度（値が大きいものを手前に描画する）
	 */
	public FileEffect(String effectFilePath, float x, float y, float degrees, boolean flipX, boolean flipY, int depth)
	{
		super(x, y, degrees);
		initialize(effectFilePath, flipX, flipY, depth);
	}
	
	/**
	 * コンストラクタ
	 * @param effectFilePath		定義ファイルのパス
	 * @param posture				位置情報オブジェクト
	 */
	public FileEffect(String effectFilePath, PostureInterface posture)
	{
		this(effectFilePath, posture, false, false);
	}
	
	/**
	 * コンストラクタ
	 * @param effectFilePath		定義ファイルのパス
	 * @param posture				位置情報オブジェクト
	 * @param flipX					X反転フラグ
	 * @param flipY					Y反転フラグ
	 */
	public FileEffect(String effectFilePath, PostureInterface posture, boolean flipX, boolean flipY)
	{
		this(effectFilePath, posture, flipX, flipY, GlobalDefine.RenderDepth.EFFECT);
	}
	
	/**
	 * コンストラクタ
	 * @param effectFilePath		定義ファイルのパス
	 * @param posture				位置情報オブジェクト
	 * @param depth					描画深度（値が大きいものを手前に描画する）
	 */
	public FileEffect(String effectFilePath, PostureInterface posture, int depth)
	{
		this(effectFilePath, posture, false, false, depth);
	}
	
	/**
	 * コンストラクタ
	 * @param effectFilePath		定義ファイルのパス
	 * @param posture				位置情報オブジェクト
	 * @param flipX					X反転フラグ
	 * @param flipY					Y反転フラグ
	 * @param depth					描画深度（値が大きいものを手前に描画する）
	 */
	public FileEffect(String effectFilePath, PostureInterface posture, boolean flipX, boolean flipY, int depth)
	{
		super(posture);
		initialize(effectFilePath, flipX, flipY, depth);
	}
	
	/**
	 * 初期化処理を実行する。
	 * @param effectFilePath		定義ファイルのパス
	 * @param flipX					X反転フラグ
	 * @param flipY					Y反転フラグ
	 * @param depth					描画深度（値が大きいものを手前に描画する）
	 */
	private void initialize(String effectFilePath, boolean flipX, boolean flipY, int depth)
	{
		final FileEffectDef def = FileEffectDefFactory.getInstance().get(effectFilePath);
		
		if(def != null)
		{
			//---- 描画オブジェクト生成
			RenderObject renderObject;
			
			// アニメーションなし
			if(def.animationFilePath == null || def.animationFilePath.isEmpty() || def.animationName == null || def.animationName.isEmpty())
			{
				renderObject = new RenderObject(def.spriteFilePath, this, depth);
				
				// ついでに寿命設定
				setLife(def.effectDef.life);
			}
			// アニメーションあり
			else
			{
				final AnimationRenderObject aro = new AnimationRenderObject(def.spriteFilePath, def.animationFilePath, this, depth);
				aro.setAnimation(def.animationName);
				renderObject = aro;
				
				// ついでに寿命設定
				if(def.effectDef.life == 0.0f)
				{
					setLife(aro.getAnimationLength());
				}
			}
			
			// 初期化
			renderObject.setColor(def.effectDef.startColor);
			renderObject.flip(flipX, flipY);
			addRenderObject(renderObject);

			//---- エフェクトのパラメータ設定
			// 速度・角速度・色
			final Vector2 flip = Vector2.tmp.set(flipX ? -1.0f : 1.0f, flipY ? -1.0f : 1.0f);
			final float degrees = getRotation();
			final Vector2 startVelocity = tmpVector2.set(def.effectDef.startVelocity.x * flip.x, def.effectDef.startVelocity.y * flip.y).rotate(degrees);
			final Vector2 endVelocity = Vector2.tmp.set(def.effectDef.endVelocity.x * flip.x, def.effectDef.endVelocity.y * flip.y).rotate(degrees);
			
			setVelocity(startVelocity, endVelocity);
			setAngularVelocity(def.effectDef.startAngularVelocity, def.effectDef.endAngularVelocity);
			setColor(def.effectDef.startColor, def.effectDef.endColor);
		}
	}
	
	
	/** 作業用Vector2オブジェクト */
	private final static Vector2 tmpVector2 = new Vector2();
}
