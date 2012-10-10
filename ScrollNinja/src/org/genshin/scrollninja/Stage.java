

package org.genshin.scrollninja;

import java.util.ArrayList;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Stage {

	private Vector2						size;
	private static Vector2				cameraPos = new Vector2();		// カメラ座標
	private static OrthographicCamera 	camera;							// カメラ
	private Player 						player;							// プレイヤー
	private static Sprite				spr[]  = new Sprite[3];
	private World						world;

	private String name;
	private ArrayList<Background>backgroundLayers;
	private Sprite sprite;
	private ArrayList<Item> popItems;
	private ArrayList<Enemy> popEnemys;
	private ArrayList<ObJectBase> object;


	private GameScreen zz;

	// コンストラクタ
	public Stage(World wrd){
		world = new World(new Vector2(0.0f, 0.0f), true );
		world = wrd;
	}

	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void Update(Player player) {
		player.Update(world);
		PopEnemy(player);
	}

	//************************************************************
	// CreateEnemy
	// 敵の作成
	//************************************************************
	public void CreateEnemy() {
		BodyDef def	= new BodyDef();
		def.type	= BodyType.DynamicBody;		// 動く物体
		player.SetBody(world.createBody(def));

		// 当たり判定の作成
		PolygonShape poly		= new PolygonShape();
		poly.setAsBox(16, 24);

		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 50;
		fd.friction		= 100.0f;
		fd.restitution	= 0;
		fd.shape		= poly;

		//
		player.GetBody().createFixture(fd);
		player.SetFixture(player.GetBody().createFixture(poly, 0));
		player.GetBody().setBullet(true);			// すり抜け防止
		player.GetBody().setTransform(0, 300, 0);	// 初期位置

		// とりあえず
		EnemyManager.GetEnemy("1").SetBody(world.createBody(def));
		EnemyManager.GetEnemy("1").GetBody().createFixture(fd);
		EnemyManager.GetEnemy("1").SetFixture(EnemyManager.GetEnemy("1").GetBody().createFixture(poly, 0));
		poly.dispose();
		EnemyManager.GetEnemy("1").GetBody().setBullet(true);
		EnemyManager.GetEnemy("1").GetBody().setTransform(0, 300, 0);
	}

	//************************************************************
	// PopEnemy
	// 敵の出現タイミングの設定
	//************************************************************
	public void PopEnemy(Player player) {
		if( player.GetPosition().x > 200 ) {
			EnemyManager.CreateEnemy("1", 0, 200.0f, 300.0f);
		}
	}

	// アイテムポップ
	public void popItem() {

	}

	public void moveBackground() {

	}
	//************************************************************
	// moveBackground
	// 背景移動
	//************************************************************
	public static void moveBackground(Player player) {


		// カメラ移動制限
		if (cameraPos.x < 0)
			cameraPos.x = 0;
		if (cameraPos.x > 1248)		// 2048-画面の横幅
			cameraPos.x = 1248;
		if (cameraPos.y < 0)
			cameraPos.y = 0;
		if (cameraPos.y > 724)		// 1024-画面の縦幅/2
			cameraPos.y = 724;


		// プレイヤーの座標をカメラの座標に代入
		cameraPos = player.GetPosition();
		spr = Background.GetSprite();
		//sprite[NEAR].setPosition(cameraPos.x - 400 + (cameraPos.x * -0.05f), -1024 + (cameraPos.y * -0.15f));

		// spr[0]は後ろの山と雲の背景
		spr[0].setPosition(cameraPos.x - 400 + (cameraPos.x * -0.05f), -512 + (cameraPos.y * -0.15f));
	}
	/*public void moveBackground() {

	}*/

	public Player spawnPlayer(Player player) {
		return player;
	}

	public int timeCount(int nowTime) {
		// 制限時間 or 経過時間加算
		nowTime += 1;

		return nowTime;
	}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public Stage GetStage() { return this; }
	public Vector2 GetCamPos() { return cameraPos; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************

}
