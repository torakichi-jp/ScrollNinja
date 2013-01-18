package org.genshin.scrollninja.work.object.gui;

import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.work.render.RenderObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * マウスカーソル
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class Cursor extends AbstractGUI
{
	/**
	 * コンストラクタ
	 * @param speed			カーソルの移動速度
	 */
	public Cursor(float speed)
	{
		renderObject = new RenderObject("data/jsons/render_object/cursor_sprite.json", this, GlobalDefine.RenderDepth.CURSOR);
		
		//---- OSのマウスカーソルを非表示
		Gdx.input.setCursorCatched(true);
		
		//---- フィールド初期化
		this.speed = speed;
	}

	@Override
	public void dispose()
	{
		//---- フィールドを破棄する。
		renderObject.dispose();
		
		//---- 基本クラスを破棄する。
		super.dispose();
	}

	@Override
	public void update(float deltaTime)
	{
		//---- 移動先の座標を計算する。
		float targetX = getScreenPositionX() + Gdx.input.getDeltaX() * speed;
		float targetY = getScreenPositionY() + Gdx.input.getDeltaY() * speed;
		
		//---- 移動範囲を画面内に制限する。
		final float halfScreenWidth = getScreenWidth() * 0.5f;
		final float halfScreenHeight = getScreenHeight() * 0.5f;
		
		//---- 座標を反映する。
		setScreenPosition(0.0f, 0.0f);
		
//		//---- 移動
//		position.x += Gdx.input.getDeltaX() * speed;
//		position.y -= Gdx.input.getDeltaY() * speed;
//
//		//---- 範囲制限
//		final float halfWidth = camera.viewportWidth * 0.5f;
//		final float halfHeight = camera.viewportHeight * 0.5f;
//		if( Math.abs(position.x) > halfWidth )	position.x = Math.signum(position.x) * halfWidth;
//		if( Math.abs(position.y) > halfHeight )	position.y = Math.signum(position.y) * halfHeight;
//		
//		//---- 適用
//		getRenderObject(0).setPosition(camera.position.x + position.x, camera.position.y + position.y);
	}
	
	@Override
	public boolean isDisposed()
	{
		return renderObject.isDisposed();
	}


	/** 描画オブジェクト */
	private final RenderObject renderObject;
	
	/** カーソルの移動速度 */
	private final float speed;
	
	/** カーソルのスクリーン座標 */
	private final Vector2 position = new Vector2(Vector2.Zero);
}
