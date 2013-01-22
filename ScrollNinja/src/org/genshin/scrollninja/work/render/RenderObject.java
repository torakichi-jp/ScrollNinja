package org.genshin.scrollninja.work.render;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.work.render.sprite.SpriteFactory;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * 描画オブジェクト
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class RenderObject implements RenderObjectInterface
{
	/**
	 * コンストラクタ
	 * @param spriteName		スプライト名
	 * @param posture			位置情報
	 * @param depth				深度（値が大きいものを手前に描画する）
	 */
	public RenderObject(String spriteName, PostureInterface posture, int depth)
	{
		//---- フィールドを初期化する。
		sprite = SpriteFactory.getInstance().get(spriteName);
		this.posture = posture;
		this.depth = depth;
		
		//---- 描画管理オブジェクトに自身を追加する。
		Global.renderManager.add(this, this.depth);
	}
	
	/**
	 * コンストラクタ
	 * @param spriteName		スプライト名
	 * @param posture			位置情報
	 */
	public RenderObject(String spriteName, PostureInterface posture)
	{
		this(spriteName, posture, GlobalDefine.RenderDepth.DEFAULT);
	}
	
	/**
	 * コピーコンストラクタ
	 * @param src		コピー元となるオブジェクト
	 */
	public RenderObject(RenderObject src)
	{
		if(getClass() != src.getClass())
		{
			System.out.println("なんかちがうかも？");
		}
		
		//---- フィールドをコピーする。
		sprite = src.sprite;
		posture = src.posture;
		depth = src.depth;
		
		//---- 描画管理オブジェクトに自身を追加する。
		Global.renderManager.add(this, depth);
	}

	@Override
	public void dispose()
	{
		//---- フィールドを破棄する。
		sprite = null;
		posture = null;
		
		//---- 描画管理オブジェクトから自身を削除する。
		Global.renderManager.remove(this, depth);
	}

	@Override
	public void render()
	{
		final SpriteBatch sb = Global.spriteBatch;
		final float x = posture.getPositionX();
		final float y = posture.getPositionY();
		final float rotation = posture.getRotation();
		
		//---- 位置情報を反映する。
		sprite.translate(x, y);
		sprite.rotate(rotation);
		
		//---- 描画する。
		sprite.draw(sb);
		
		//---- 位置情報を元に戻す。
		sprite.translate(-x, -y);
		sprite.rotate(-rotation);
	}

	/**
	 * スプライトを取得する。
	 * @return			スプライト
	 * @deprecated		スプライトを直接いじるのは最終手段。基本的には使わない。
	 */
	@Deprecated
	public final Sprite getSprite()
	{
		return sprite;
	}

	@Override
	public boolean isDisposed()
	{
		return sprite == null;
	}

	/** スプライト */
	private Sprite	sprite;
	
	/** 位置情報 */
	private PostureInterface	posture;
	
	/** 深度（値が大きいものを手前に描画する） */
	private int	depth;
}
