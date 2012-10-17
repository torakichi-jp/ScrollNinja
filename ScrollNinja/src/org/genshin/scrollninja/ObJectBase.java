package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

//========================================
// クラス宣言
//========================================
public abstract class ObJectBase {
	protected ArrayList<Sprite> 	sprite;			// スプライト
	protected Body 					body;			// 当たり判定用BOX
	protected ArrayList<Fixture> 	sensor;			// センサー

	/**
	 * コンストラクタ
	 * 将来的には引数にWorldを投げて初期化させる？
	 */
	ObJectBase(){}

	//************************************************************
	// Release
	// 開放処理まとめ
	//************************************************************
	public void Release(){
		GameMain.world.destroyBody(body);
		body = null;
		sprite = null;

	}

	/**
	 * オブジェクトを更新する。
	 * @param world	ワールドオブジェクト。多分そのうち消す。
	 */
	public void Update(World world){}

	/**
	 * スプライトを描画する。
	 */
	public void Draw()
	{
		Vector2 pos = body.getPosition();
		float rot = (float) Math.toDegrees(body.getAngle());

		int count = sprite.size();
		for (int i = 0; i < count; ++i)
		{
			Sprite current = sprite.get(i);
			// 座標・回転
			current.setPosition(pos.x - current.getOriginX(), pos.y - current.getOriginY());
			current.setRotation(rot);
			// 描画
			current.draw(GameMain.spriteBatch);
		}
	}

	/**
	 * 衝突を振り分ける。
	 * @param obj			衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	protected void collisionDispatch(ObJectBase obj, Contact contact)
	{
		// TODO いずれ抽象メソッド化する。
	}

	/**
	 * 衝突を通知する。
	 * @param obj			衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	protected void collisionNotify(Background obj, Contact contact){}
	protected void collisionNotify(Player obj, Contact contact){}
	protected void collisionNotify(Enemy obj, Contact contact){}
	protected void collisionNotify(Effect obj, Contact contact){}
	protected void collisionNotify(Item obj, Contact contact){}
	protected void collisionNotify(StageObject obj, Contact contact){}
	protected void collisionNotify(Weapon obj, Contact contact){}

	protected void flip(boolean x, boolean y)
	{
		int count = sprite.size();
		for(int i = 0; i < count;  ++i)
		{
			sprite.get(i).flip(x, y);
		}
	}
}
