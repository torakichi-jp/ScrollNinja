package org.genshin.scrollninja.object.gui;

import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.AbstractObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * マウスカーソル
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class Cursor extends AbstractObject
{
	public Cursor()
	{
		Gdx.input.setCursorCatched(true);
	}

	@Override
	public void update()
	{
		// XXX 仮実装
		Vector2 mousePosition = new Vector2(
			Gdx.input.getX() - Gdx.graphics.getWidth()*0.5f,
			Gdx.graphics.getHeight()*0.5f - Gdx.input.getY()
		);

		mousePosition.mul(GlobalParam.INSTANCE.WORLD_SCALE);
		mousePosition.x += GameMain.camera.position.x;
		mousePosition.y += GameMain.camera.position.y;
		
		Sprite sprite = sprites.get(0);
		sprite.setPosition(mousePosition.x - sprite.getOriginX(), mousePosition.y - sprite.getOriginY());
	}

	@Override
	protected void initializeSprite()
	{
		this.sprites.add( CursorParam.INSTANCE.SPRITE_LOADER.create() );
	}
}
