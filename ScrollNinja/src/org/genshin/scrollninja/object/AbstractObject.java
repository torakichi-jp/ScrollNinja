package org.genshin.scrollninja.object;

import java.util.ArrayList;

import org.genshin.engine.system.Renderable;
import org.genshin.engine.system.Updatable;
import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;
import org.genshin.scrollninja.object.item.Item;
import org.genshin.scrollninja.object.kaginawa.Kaginawa;
import org.genshin.scrollninja.object.weapon.AbstractWeapon;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * オブジェクトの基本クラス
 * @author インターン生
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractObject implements Updatable, Renderable
{
	/**
	 * コンストラクタ
	 */
	@Deprecated
	public AbstractObject()
	{
		initializeSprite();
	}
	
	/**
	 * 解放すべきものを全て解放する。
	 * 
	 * XXX このメソッドは果たして必要なのか。
	 */
	public void dispose()
	{
		sprites.clear();
	}

	@Override
	public void update()
	{
		/* 何もしない */
	}

	@Override
	public void render()
	{
		// 描画処理
		final SpriteBatch spriteBatch = GameMain.spriteBatch;

		final int count = sprites.size();
		for (int i = 0; i < count; ++i)
		{
			final Sprite current = sprites.get(i);
			
			current.draw(spriteBatch);
		}
	}
	
	/**
	 * ここでSpriteを生成する。<br>
	 * このメソッドは、AbstractObjectのコンストラクタから自動的に呼び出される。
	 */
	protected void initializeSprite()
	{
		// TODO 最終的にはabstract化する。
	}
	
	/**
	 * スプライト反転フラグを設定する。
	 * @param x		x方向の反転フラグ
	 * @param y		y方向の反転フラグ
	 */
	protected final void flip(boolean x, boolean y)
	{
		int count = sprites.size();
		for(int i = 0; i < count;  ++i)
		{
			sprites.get(i).flip(x, y);
		}
	}
	
	/**
	 * Spriteオブジェクトの配列を取得する。
	 * @return		Spriteオブジェクトの配列
	 */
	protected ArrayList<Sprite> getSprites()
	{
		return sprites;
	}
	
	/**
	 * Spriteオブジェクトを取得する。
	 * @param		index インデックス番号
	 * @return		Spriteオブジェクト
	 */
	protected Sprite getSprite(int index)
	{
		return sprites.get(index);
	}
	

	/** スプライト配列 */
	protected final ArrayList<Sprite>	sprites = new ArrayList<Sprite>(1);		// TODO いずれprivate化すべし。
}
