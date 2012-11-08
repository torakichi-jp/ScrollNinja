package org.genshin.scrollninja.object;

import java.util.ArrayList;

import org.genshin.engine.system.Renderable;
import org.genshin.engine.system.Updatable;
import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;
import org.genshin.scrollninja.object.item.Item;
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
	 * 古いソースとの整合性を保つためのコンストラクタ。
	 * FIXME 最終的には消し去る。
	 */
	@Deprecated
	public AbstractObject()
	{
		/* 何もしない */
	}
	
	/**
	 * コンストラクタ 
	 * @param world			所属するWorldオブジェクト
	 */
	public AbstractObject(World world)
	{
		initializeBody(world);
		initializeSprite();
		initializeFixture();
	}
	
	/**
	 * 解放すべきものを全て解放する。
	 * 
	 * XXX このメソッドは果たして必要なのか。
	 */
	public void dispose(){
		if(body!=null)
		{
			body.getWorld().destroyBody(body);
			body = null;
		}
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
		// アクティブでなければ描画しない
		if(!body.isActive())
			return;
		
		// 描画処理
		SpriteBatch spriteBatch = GameMain.spriteBatch;
		Vector2 pos = body.getPosition();
		float rot = (float) Math.toDegrees(body.getAngle());

		int count = sprites.size();
		for (int i = 0; i < count; ++i)
		{
			Sprite current = sprites.get(i);
			// 座標・回転
			current.setPosition(pos.x - current.getOriginX(), pos.y - current.getOriginY());
			current.setRotation(rot);
			// 描画
			current.draw(spriteBatch);
		}
	}

	/**
	 * 衝突を振り分ける。
	 * @param object		衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	abstract public void dispatchCollision(AbstractObject object, Contact contact);

	/**
	 * 背景との衝突を通知する。
	 * @param obj		衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	public void notifyCollision(Background obj, Contact contact)
	{
		/* 何もしない */
	}

	/**
	 * プレイヤーとの衝突を通知する。
	 * @param obj		衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	public void notifyCollision(PlayerNinja obj, Contact contact)
	{
		/* 何もしない */
	}

	/**
	 * 敵との衝突を通知する。
	 * @param obj		衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	public void notifyCollision(Enemy obj, Contact contact)
	{
		/* 何もしない */
	}

	/**
	 * エフェクトとの衝突を通知する。
	 * XXX 「エフェクトと衝突する」って捉え方はなんかおかしい気がする
	 * @param obj		衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	public void notifyCollision(Effect obj, Contact contact)
	{
		/* 何もしない */
	}

	/**
	 * アイテムとの衝突を通知する。
	 * @param obj		衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	public void notifyCollision(Item obj, Contact contact)
	{
		/* 何もしない */
	}

	/**
	 * ステージオブジェクトとの衝突を通知する。
	 * @param obj			衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	public void notifyCollision(StageObject obj, Contact contact)
	{
		/* 何もしない */
	}

	/**
	 * 武器との衝突を通知する。
	 * @param obj		衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	public void notifyCollision(AbstractWeapon obj, Contact contact)
	{
		/* 何もしない */
	}

	/**
	 * 鉤縄との衝突を通知する。
	 * @param obj		衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	public void notifyCollision(Kaginawa obj, Contact contact)
	{
		/* 何もしない */
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
	 * ここでBodyを生成する。<br>
	 * このメソッドは、AbstractObjectのコンストラクタから自動的に呼び出される。
	 * @param world			所属するWorldオブジェクト
	 */
	private final void initializeBody(World world)
	{
		body = world.createBody(createBodyDef());
		body.setUserData(this);
	}
	
	/**
	 * Bodyの定義を生成する。<br>
	 * initializeBodyメソッドにて、ここで生成した定義を元に自動的にBodyを生成する。<br>
	 * Bodyの定義を変更したい場合は、このメソッドをオーバーライドする。<br>
	 * @return		Bodyの定義
	 */
	protected BodyDef createBodyDef()
	{
		BodyDef bd = new BodyDef();
		return bd;
	}

	/**
	 * 古いソースとの整合性を保つためのBody生成メソッド。
	 * FIXME 最終的には消し去る。
	 */
	@Deprecated
	protected final void createBody(World world, BodyDef bodyDef)
	{
		body = world.createBody(bodyDef);
		body.setUserData(this);
	}
	
	/**
	 * ここでFixtureを生成する。<br>
	 * このメソッドは、AbstractObjectのコンストラクタから自動的に呼び出される。<br>
	 * ファイルからFixtureを生成したい場合、又は、複数のFixtureを生成したい場合は、このメソッドをオーバーライドする。
	 */
	protected void initializeFixture()
	{
		createFixture(createFixtureDef());
	}
	
	/**
	 * Fixtureの定義を生成する。<br>
	 * initializeFixtureメソッドにて、ここで生成した定義を元にFixtureを生成する。<br>
	 * Fixtureの定義を変更したい場合は、このメソッドをオーバーライドする。
	 * @return		Fixtureの定義
	 */
	protected FixtureDef createFixtureDef()
	{
		FixtureDef fd = new FixtureDef();
		fd.density		= 0.0f;		// 密度
		fd.friction		= 0.0f;		// 摩擦 
		return fd;
	}
	
	/**
	 * Fixtureを生成する。<br>
	 * このメソッドは、基本的にinitializeFixtureメソッドからのみ呼び出す。
	 * @param fixtureDef	生成するFixtureの定義
	 */
	protected final void createFixture(FixtureDef fixtureDef)
	{
		assert body != null : "先にBodyを生成する。";
		
		body.createFixture(fixtureDef);
	}
	
	/**
	 * BodyEditorで作成されたファイルからFixtureを生成する。<br>
	 * このメソッドは、基本的にinitializeFixtureメソッドからのみ呼び出す。
	 * @param fixtureDef	生成するFixtureの定義
	 * @param fileName		ファイル名
	 * @param fixtureName	ファイル内に設定されているFixtureの名前
	 * @param scale			倍率
	 */
	protected final void createFixtureFromFile(FixtureDef fixtureDef, String fileName, String fixtureName, float scale)
	{
		assert body != null : "先にBodyを生成する。";
		
		final ArrayList<Fixture> fixtureList = body.getFixtureList();
		final int firstIndex = fixtureList.size();
		
		BodyEditorLoader loader = new BodyEditorLoader( Gdx.files.internal(fileName) );
		loader.attachFixture(body, fixtureName, fixtureDef, scale);
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
	 * Bodyオブジェクトを取得する。
	 * @return		Bodyオブジェクト
	 * XXX 最終的にはprotected化できると望ましい。
	 */
	public Body getBody()
	{
		return body;
	}
	
	/**
	 * Fixtureオブジェクトの配列を取得する。
	 * @return		Fixtureオブジェクトの配列
	 */
	protected ArrayList<Fixture> getFixtures()
	{
		return body.getFixtureList();
	}
	
	/**
	 * Fixtureオブジェクトを取得する。
	 * @param index		インデックス番号
	 * @return			Fixtureオブジェクト
	 */
	protected Fixture getFixture(int index)
	{
		return getFixtures().get(index);
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
	
	
	/** 衝突判定まとめオブジェクト */
	private Body		body;

	/** スプライト配列 */
	protected final ArrayList<Sprite>	sprites = new ArrayList<Sprite>(1);		// TODO いずれprivate化すべし。
}
