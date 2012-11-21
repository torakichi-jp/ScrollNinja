package org.genshin.scrollninja.render;

import java.util.ArrayList;

import org.genshin.engine.system.Updatable;
import org.genshin.scrollninja.object.ObjectInterface;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

/**
 * カメラを動かすオブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class CameraTranslater implements Updatable
{
	/**
	 * コンストラクタ
	 * @param camera	動かすカメラオブジェクト
	 */
	public CameraTranslater(Camera camera)
	{
		this.camera = camera;
	}
	
	@Override
	public void update()
	{
		//---- 目標座標を計算する。
		targetPosition.set(Vector2.Zero);
		for(ObjectInterface object : targetObjects)
		{
			targetPosition.add(object.getPositionX(), object.getPositionY());
		}
		targetPosition.mul(1.0f / targetObjects.size());
		
		//---- 座標を適用する。
		camera.position.x = targetPosition.x;
		camera.position.y = targetPosition.y;
		
		//---- 移動範囲を制限する。
		// X
		if( camera.position.x > translateArea.right )
		{
			camera.position.x = translateArea.right;
		}
		else if( camera.position.x < translateArea.left )
		{
			camera.position.x = translateArea.left;
		}
		// Y
		if( camera.position.y > translateArea.bottom )
		{
			camera.position.y = translateArea.bottom;
		}
		else if( camera.position.y < translateArea.top )
		{
			camera.position.y = translateArea.top;
		}
		
		//---- カメラを更新する。
		camera.update();
	}
	
	/**
	 * 追従するオブジェクトを追加する。
	 * @param object	追従するオブジェクト
	 */
	public void addTargetObject(ObjectInterface object)
	{
		targetObjects.add(object);
	}
	
	/**
	 * 追従するオブジェクトを削除する。
	 * @param object	追従するオブジェクト
	 */
	public void removeTargetObject(ObjectInterface object)
	{
		targetObjects.remove(object);
	}
	
	/**
	 * 移動可能な範囲を設定する。
	 * @param x			X座標
	 * @param y			Y座標
	 * @param width		X方向の大きさ
	 * @param height	Y方向の大きさ
	 */
	public void setTranslateArea(float x, float y, float width, float height)
	{
		translateArea.set(x + camera.viewportWidth * 0.5f, y + camera.viewportHeight * 0.5f, width - camera.viewportWidth, height - camera.viewportHeight);
	}

	
	/** 動かすカメラオブジェクト */
	private final Camera camera;
	
	/** 移動可能な範囲 */
	private final Area translateArea = new Area(0, 0, 0, 0);
	
	/** 追従するオブジェクトの配列 */
	private final ArrayList<ObjectInterface> targetObjects = new ArrayList<ObjectInterface>(1);
	
	/** 目標座標 */
	private final Vector2 targetPosition = new Vector2();
	
	/** 補間係数（デフォルト値：0.9） */
	private float interporation = 0.9f;
	
	
	private class Area
	{
		private Area(float x, float y, float width, float height)
		{
			set(x, y, width, height);
		}
		private void set(float x, float y, float width, float height)
		{
			left = x;
			top = y;
			right = x+width;
			bottom = y+height;
		}
		private float left, right, top, bottom;
	}
}
