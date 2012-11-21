package org.genshin.scrollninja.object;

//========================================
// インポート
//========================================
import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.MainMenu;
import org.genshin.scrollninja.ScrollNinja;
import org.genshin.scrollninja.object.StageDataList.StageData;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;
import org.genshin.scrollninja.object.item.Item;
import org.genshin.scrollninja.object.weapon.AbstractWeapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;

// メモ
// このクラスは、背景を読み込んで表示するところ。
// ステージの当たり判定もこのクラスが持っています。
// その他の敵の出現位置などの細かな情報はステージクラスが持っています
// ↑StageDataListにまとめました

// 10/9 背景移動追加、カメラ座標のゲッター追加

//========================================
// クラス宣言
//========================================
public class Background extends AbstractCollisionObject {
	//========================================
	// 定数宣言
	// spriteの配列
	//========================================
	public final static int	FAR				= 0;
	public final static int	MAIN			= 1;
	public final static int	NEAR			= 2;

	// 変数宣言
	private int				stageNum;		// ステージ番号
	private StageData 		stageData;		// ステージのデータ

	/**
	 *  コンストラクタ
	 *  @param num			ステージ番号
	 *  @param createFlag	bodyを作成するかどうか
	 */
	public Background(int num, boolean createFlag) {
		stageNum = num;
		stageData = StageDataList.lead(stageNum);

		LoadTexture();
		// MainMenuではcreateしない
		if (createFlag)
			createBody();
	}

	/**************************************************
	 * @Override
	 * @param i		スプライト番号
	 * @param flag	MainMenuで描画するにはこれが必要
	 *
	 * 描画処理
	 ***************************************************/
	public void Draw(int i, boolean flag) {
		sprites.get(i).draw(MainMenu.spriteBatch);
	}

	/**************************************************
	 * @Override
	 * @param i		スプライト番号
	 *
	 * 描画処理
	 ***************************************************/
	public void Draw(int i) {
		sprites.get(i).draw(GameMain.spriteBatch);
	}

	/**************************************************
	 * テクスチャ読み込み、スプライトセット
	 ***************************************************/
	public void LoadTexture() {
		// 奥から作成
		// 遠景
		Texture texture =
			new Texture(Gdx.files.internal(stageData.backgroundFileName.get(FAR)));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		sprites.add(new Sprite(tmpRegion));
		sprites.get(FAR).setPosition(-sprites.get(FAR).getWidth() * 0.5f,
									-sprites.get(FAR).getHeight() * 0.5f);
		// TODO 遠景のスケールは白い部分が見えないようにとりあえずの拡大数値
		if(sprites.get(FAR).getWidth() > ScrollNinja.window.x )
			sprites.get(FAR).setScale(ScrollNinja.scale + 0.05f);
		else
			sprites.get(FAR).setScale(ScrollNinja.scale * (ScrollNinja.window.x / sprites.get(FAR).getWidth()) * 1.05f);

		// メインステージ
		texture = new Texture(Gdx.files.internal(stageData.backgroundFileName.get(MAIN)));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmpRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		sprites.add(new Sprite(tmpRegion));
		sprites.get(MAIN).setPosition(-sprites.get(MAIN).getWidth() * 0.5f,
									 -sprites.get(MAIN).getHeight() * 0.5f);
		sprites.get(MAIN).setScale(ScrollNinja.scale);

		// 近景
		texture = new Texture(Gdx.files.internal(stageData.backgroundFileName.get(NEAR)));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmpRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		sprites.add(new Sprite(tmpRegion));
		float tmp = (stageData.backgroundSize.get(MAIN).x - sprites.get(NEAR).getWidth() * 2) * 0.5f * ScrollNinja.scale;
		sprites.get(NEAR).setPosition(-sprites.get(NEAR).getWidth() * 0.5f,
									 -sprites.get(NEAR).getHeight() * 0.5f - tmp);
		// TODO 近景のスケールは元画像が小さかったのでとりあえずの数値
		sprites.get(NEAR).setScale(ScrollNinja.scale * 2.5f, ScrollNinja.scale * 2f);
	}

	/**************************************************
	 * 当たり判定作成
	 ***************************************************/
	public void createBody() {
		// ボディタイプ設定
		BodyDef bd	= new BodyDef();
		bd.type		= BodyType.StaticBody;		// 動かない物体

		float tmp = (sprites.get(MAIN).getHeight() - stageData.backgroundSize.get(MAIN).y) * 0.5f;
		bd.position.set(-sprites.get(MAIN).getWidth() * 0.5f * ScrollNinja.scale,
					   (-sprites.get(MAIN).getHeight() * 0.5f - tmp) * ScrollNinja.scale);

		// ボディ設定
		FixtureDef fd	= TerrainParam.INSTANCE.FIXTURE_DEF_LOADER.createFixtureDef();

		// ボディ作成
		createBody(GameMain.world, bd);
		createFixtureFromFile(fd, stageData.backgroundBodyFileName, stageData.backgroundBodyName, sprites.get(MAIN).getWidth() * ScrollNinja.scale);
	}

	/**************************************************
	 * 更新処理
	 ***************************************************/
	public void update() {
		// プレイヤーの座標を代入
		Vector3 cameraPos = GameMain.camera.position;

		// 近景
		if (cameraPos.x > -(sprites.get(MAIN).getWidth() - ScrollNinja.window.x) * 0.5 * ScrollNinja.scale
			&& cameraPos.x < (sprites.get(MAIN).getWidth() - ScrollNinja.window.x) * 0.5 * ScrollNinja.scale)
			sprites.get(NEAR).setPosition(-sprites.get(NEAR).getWidth() * 0.5f - cameraPos.x * 1.5f,
										 sprites.get(NEAR).getY());
		// TODO 要調整 20は適当
		float tmp = (ScrollNinja.window.y - sprites.get(NEAR).getHeight() * 2) * 0.5f * ScrollNinja.scale;
		if (cameraPos.y >
			-(stageData.backgroundSize.get(MAIN).y - ScrollNinja.window.y) * 0.5  * ScrollNinja.scale
			  && cameraPos.y < 20)
			sprites.get(NEAR).setPosition(sprites.get(NEAR).getX(),
										 -sprites.get(NEAR).getHeight() * 0.5f - tmp + cameraPos.y);

		// 遠景
		if (cameraPos.x > -(sprites.get(MAIN).getWidth() - ScrollNinja.window.x) * 0.5 * ScrollNinja.scale
			&& cameraPos.x < (sprites.get(MAIN).getWidth() - ScrollNinja.window.x) * 0.5 * ScrollNinja.scale)
			sprites.get(FAR).setPosition(cameraPos.x - (sprites.get(FAR).getWidth() * 0.5f) + (cameraPos.x * -0.05f),
										sprites.get(FAR).getY());

		// 近景
		if (cameraPos.y > -(sprites.get(FAR).getHeight() - ScrollNinja.window.y) * 0.5 * ScrollNinja.scale
			&& cameraPos.y < (stageData.backgroundSize.get(MAIN).y - ScrollNinja.window.y) * 0.5 * ScrollNinja.scale)
			sprites.get(FAR).setPosition(sprites.get(FAR).getX(),
										cameraPos.y - (sprites.get(FAR).getHeight() * 0.5f) + (cameraPos.y * -0.15f));
	}

	@Override
	public void dispatchCollision(AbstractCollisionObject object, Contact contact) {
		object.notifyCollision(this, contact);
	}

	@Override
	public void notifyCollision(PlayerNinja obj, Contact contact) {

	}

	@Override
	public void notifyCollision(Enemy obj, Contact contact){}

	@Override
	public void notifyCollision(Effect obj, Contact contact){}

	@Override
	public void notifyCollision(Item obj, Contact contact){}

	@Override
	public void notifyCollision(StageObject obj, Contact contact){}

	@Override
	public void notifyCollision(AbstractWeapon obj, Contact contact){}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
/*	public Sprite GetSprite(int i) { return sprite.get(i); }
	public Body GetBody() { return body; }
	public Fixture GetSensor(int i) { return sensor.get(i); }*/
	//public Vector2 GetCamPos() { return cameraPos; }
	public Background GetBackground(){ return this; }
	public int GetBackgroundNum(){ return stageNum; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
/*	public static void SetSprite(Sprite sp) { sprite.add(sp); }
	public static void SetBody(Body bd) { body = bd; }
	public static void SetFixture(Fixture ss){ sensor.add(ss); }
*/
}