

package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Stage {

	private String name;
	private Vector2 size;
	private ArrayList<Background>backgroundLayers;
	private Sprite sprite;
	private ArrayList<Item> popItems;
	private ArrayList<Enemy> popEnemys;
	private ArrayList<ObJectBase> object;
	private static Vector2				cameraPos = new Vector2();		// カメラ座標
	private static OrthographicCamera 	camera;							// カメラ
	private Player 						player;							// プレイヤー

	private static Sprite spr[]  = new Sprite[3];

	private GameScreen zz;

	// コンストラクタ
	public Stage(String Name){
		name = new String(Name);
	}

	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void Update(Player player) {
		PopEnemy(player);


	}

	//************************************************************
	// PopEnemy
	// 敵の出現タイミングの設定
	//************************************************************
	public void PopEnemy(Player player) {
		if( player.GetPosition().x > 200 ) {
			EnemyManager.CreateEnemy("ざこ", 0, 200.0f, 300.0f);
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
		//float w = Gdx.graphics.getWidth();
		//float h = Gdx.graphics.getHeight();


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

	public static Vector2 GetCamPos() { return cameraPos; }


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
	public String GetName(){ return name; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************

}
