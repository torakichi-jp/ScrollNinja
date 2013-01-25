package org.genshin.scrollninja.work.object.background;

import java.util.ArrayList;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.work.render.RenderObject;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

/**
 * 背景レイヤー
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class BackgroundLayer extends AbstractBackground
{
	/**
	 * コンストラクタ
	 * @param scale			背景レイヤーの倍率
	 * @param renderDepth	描画処理の優先順位
	 */
	public BackgroundLayer(float scale, int renderDepth)
	{
		this.scale = scale;
		this.renderDepth = renderDepth;
	}
	
	@Override
	public void dispose()
	{
		for(RenderObject ro : backgrounds)
		{
			ro.dispose();
		}
		backgrounds.clear();
		
		super.dispose();
	}
	
	@Override
	public void update(float deltaTime)
	{
		final Camera camera = Global.camera;
		// TODO カメラに合わせて座標を調整する。
		position.set(camera.position.x, camera.position.y);
	}

	/**
	 * 背景オブジェクトを生成する。
	 * @param spriteFilePath		スプライトの定義ファイルのパス
	 * @param position				座標
	 */
	public void createBackground(String spriteFilePath, Vector2 position)
	{
		final PostureInterface posture = new BackgroundPosture(position, 0.0f);
		final RenderObject renderObject = new RenderObject(spriteFilePath, posture, renderDepth);
		backgrounds.add(renderObject);
	}
	
	@Override
	public boolean isDisposed()
	{
		return backgrounds.isEmpty();
	}
	
	@Override
	public float getPositionX()
	{
		return position.x;
	}
	
	@Override
	public float getPositionY()
	{
		return position.y;
	}
	
	@Override
	public float getRotation()
	{
		return 0.0f;
	}
	
	/** 座標 */
	private final Vector2 position = new Vector2();
	
	/** 背景レイヤーの倍率 */
	private final float scale;
	
	/** 描画処理の優先順位 */
	private final int renderDepth;
	
	/** 背景オブジェクトの配列 */
	private final ArrayList<RenderObject> backgrounds = new ArrayList<RenderObject>();
	
	
	/**
	 * 背景の位置情報
	 */
	private class BackgroundPosture implements PostureInterface
	{
		/**
		 * コンストラクタ
		 * @param localPosition		ローカル座標
		 * @param localRotation		ローカル角度（単位：度）
		 */
		public BackgroundPosture(Vector2 localPosition, float localRotation)
		{
			this.localPosition.set(localPosition);
			this.localRotation = localRotation;
		}
		
		@Override
		public float getPositionX()
		{
			return BackgroundLayer.this.getPositionX() + localPosition.x;
		}

		@Override
		public float getPositionY()
		{
			return BackgroundLayer.this.getPositionY() + localPosition.y;
		}

		@Override
		public float getRotation()
		{
			return BackgroundLayer.this.getRotation() + localRotation;
		}
		
		/** ローカル座標 */
		private final Vector2 localPosition = new Vector2();
		
		/** ローカル角度（単位：度） */
		private final float localRotation;
	}
}
