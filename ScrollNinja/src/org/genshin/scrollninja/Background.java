package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.awt.Point;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

// メモ
// このクラスは、背景を読み込んで表示するところ。
// ステージの当たり判定もこのクラスが持っています。
// その他の敵の出現位置などの細かな情報はステージクラスが持っています

// 10/9 背景移動追加、カメラ座標のゲッター追加

//========================================
// クラス宣言
//========================================
public class Background {
	//========================================
	// 定数宣言
	// spriteの配列
	//========================================
	private final static int	FALL			= 0;
	private final static int	STAGE			= 1;
	private final static int	NEAR			= 2;

	// 変数宣言
	private static Sprite 				sprite[]  = new Sprite[3];		// スプライト
	private static Body 					body;								// 当たり判定用BOX
	private static Fixture 				sensor[]	= new Fixture[9];		// センサー
	private static float					zIndex;							// Zインデックス

	private static Vector2				cameraPos = new Vector2();		// カメラ座標
	private static OrthographicCamera 	camera;							// カメラ
	private Player 						player;							// プレイヤー

	// コンストラクタ
	private Background() {}

	//************************************************************
	// LoadTexture
	// テクスチャを読み込んでスプライトにセット！
	//************************************************************
	public static void LoadTexture() {
		// 手前
		Texture texture = new Texture(Gdx.files.internal("data/stage_near_test.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 0, 2048, 2048);
		sprite[NEAR] = new Sprite(tmpRegion);
		sprite[NEAR].setPosition(-(Gdx.graphics.getWidth() / 2), -1024);

		// 奥
		texture = new Texture(Gdx.files.internal("data/stage_far_test.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmpRegion = new TextureRegion(texture, 0, 0, 1024, 1024);
		sprite[FALL] = new Sprite(tmpRegion);
		sprite[FALL].setPosition(-(Gdx.graphics.getWidth() / 2), -512);
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
		//cameraPos = PlayerManager.GetPlayer("プレイヤー").GetPosition();
		cameraPos = player.GetPosition();
		
		// sprite[0]は後ろの山と雲の背景
		sprite[0].setPosition(cameraPos.x - 400 + (cameraPos.x * -0.05f), -512 + (cameraPos.y * -0.15f));
	}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public static Sprite[] GetSprite() { return sprite; }
	public static Body GetBody() { return body; }
	public static Fixture GetSensor(int i) { return sensor[i]; }
	public static Vector2 GetCamPos() { return cameraPos; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
	public static void SetSprite( Sprite sp) { sprite[STAGE] = sp; }
	public static void SetBody(Body bd) { body = bd; }
	public static void SetFixture(Fixture ss, int i){ sensor[i] = ss; }

}