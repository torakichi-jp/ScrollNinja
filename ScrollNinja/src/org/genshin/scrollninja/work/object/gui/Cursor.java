package org.genshin.scrollninja.work.object.gui;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.work.render.RenderObject;

import com.badlogic.gdx.Gdx;

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
		renderObject = new RenderObject("data/jsons/render/cursor_sprite.json", this, GlobalDefine.RenderDepth.CURSOR);
		
		//---- OSのマウスカーソルを非表示
		Gdx.input.setCursorCatched(true);
		
		//---- 初期座標は中央にしよう。
		setScreenPosition(GlobalDefine.INSTANCE.CLIENT_WIDTH * 0.5f, GlobalDefine.INSTANCE.CLIENT_HEIGHT * 0.5f);
		
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
		final float screenWidth = getScreenWidth();
		final float screenHeight = getScreenHeight();

		targetX = Math.max(targetX, 0.0f);
		targetX = Math.min(targetX, screenWidth);
		targetY = Math.max(targetY, 0.0f);
		targetY = Math.min(targetY, screenHeight);
		
		//---- 座標を反映する。
		setScreenPosition(targetX, targetY);
	}


	/** 描画オブジェクト */
	private final RenderObject renderObject;
	
	/** カーソルの移動速度 */
	private final float speed;
}
