package org.genshin.scrollninja.object;

import java.util.ArrayList;

import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;
import org.genshin.scrollninja.object.item.Item;
import org.genshin.scrollninja.object.weapon.AbstractWeapon;

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
public abstract class AbstractObject {
	/**
	 * コンストラクタ
	 * TODO 将来的には引数にWorldを投げて初期化させる？
	 */
	public AbstractObject()
	{
		// フィールドの初期化
		body = null;
		sprites = new ArrayList<Sprite>();
		fixtures = new ArrayList<Fixture>();
	}

	//************************************************************
	// Release
	// 開放処理まとめ
	// XXX このメソッドは果たして必要なのか。
	//************************************************************
	public void Release(){
		GameMain.world.destroyBody(body);
		body = null;
		sprites.clear();
		fixtures.clear();
	}

	/**
	 * オブジェクトを更新する。
	 */
	public void update()
	{
		/* 何もしない */
	}

	/**
	 * スプライトを描画する。
	 */
	public void render()
	{
		// アクティブでなければ描画しない
		if(!body.isActive())
			return;
		
		// 描画処理
		SpriteBatch sb = GameMain.spriteBatch;
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
			current.draw(sb);
		}
	}
	
	/**
	 * Bodyを生成する。
	 * @param world		Bodyを生成するWorldオブジェクト
	 * @param bodyDef	生成するBodyの定義
	 * @return			生成したBody
	 */
	protected final Body createBody(World world, BodyDef bodyDef)
	{
		assert body == null : "Bodyの生成は1オブジェクト1回まで！";
		
		body = world.createBody(bodyDef);
		
		return body;
	}
	
	/**
	 * Fixtureを生成する。
	 * @param fixtureDef	生成するFixtureの定義
	 */
	protected final void createFixture(FixtureDef fixtureDef)
	{
		assert body != null : "先にBodyを生成する。";
		
		Fixture newFixture = body.createFixture(fixtureDef);
		newFixture.setUserData(this);
		fixtures.add(newFixture);
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
	 */
	protected Body getBody()
	{
		return body;
	}
	
	/**
	 * Fixtureオブジェクトの配列を取得する。
	 * @return		Fixtureオブジェクトの配列
	 */
	protected ArrayList<Fixture> getFixtures()
	{
		return fixtures;
	}
	
	/**
	 * Fixtureオブジェクトを取得する。
	 * @param index		インデックス番号
	 * @return			Fixtureオブジェクト
	 */
	protected Fixture getFixture(int index)
	{
		return fixtures.get(index);
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
	
	
	// TODO フィールドはいずれprivate化すべし
	/** 衝突判定まとめオブジェクト */
	protected Body		body;
	
	/** 衝突判定用オブジェクトの配列 */
	protected ArrayList<Fixture>	fixtures;

	/** スプライト配列 */
	protected ArrayList<Sprite>	sprites;
}
