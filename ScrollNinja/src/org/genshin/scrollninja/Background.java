package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;

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
public class Background extends ObJectBase {
	//========================================
	// 定数宣言
	// spriteの配列
	//========================================
	private final static int	FAR				= 0;
	private final static int	MAIN			= 1;
	private final static int	NEAR			= 2;

	// 変数宣言
	private float				zIndex;								// Zインデックス
	private Vector2				cameraPos;
	private int					bgNum;

	// コンストラクタ
	public Background(){}
	public Background(int num) {
		sprite = new ArrayList<Sprite>();
		sensor = new ArrayList<Fixture>();
		cameraPos = new Vector2(0.0f, 0.0f);
		bgNum = num;
		
		switch(num){
		case 1:
			LoadTexture();
			break;
		}
	}
	
	public void Draw(int i, boolean flag) {
		sprite.get(i).draw(MainMenu.batch);
	}
	
	public void Draw(int i) {
		sprite.get(i).draw(GameMain.spriteBatch);
	}

	//************************************************************
	// LoadTexture
	// テクスチャを読み込んでスプライトにセット！
	//************************************************************
	public void LoadTexture() {
		// 奥から作成
		// 奥
		Texture texture = new Texture(Gdx.files.internal("data/stage_far.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		sprite.add(new Sprite(tmpRegion));
		sprite.get(FAR).setPosition(-sprite.get(FAR).getWidth() * 0.5f, -sprite.get(FAR).getHeight() * 0.5f);
		if(sprite.get(FAR).getWidth() > ScrollNinja.window.x )
			sprite.get(FAR).setScale(0.11f);
		else
			sprite.get(FAR).setScale(0.1f * (ScrollNinja.window.x / sprite.get(FAR).getWidth()) * 1.05f);

		// メインステージ
		texture = new Texture(Gdx.files.internal("data/stage_main.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmpRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		sprite.add(new Sprite(tmpRegion));
		sprite.get(MAIN).setPosition(-sprite.get(MAIN).getWidth() * 0.5f, -sprite.get(MAIN).getHeight() * 0.5f);
		sprite.get(MAIN).setScale(0.1f);

		// 手前
		texture = new Texture(Gdx.files.internal("data/stage_near.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmpRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		sprite.add(new Sprite(tmpRegion));
		// 41.05は((メインテクスチャ1333)-(手前テクスチャ256*scale2倍) ÷　（空白は上下あるから）2) ?
		sprite.get(NEAR).setPosition(-sprite.get(NEAR).getWidth() * 0.5f,
										-sprite.get(NEAR).getHeight() * 0.5f -41.05f);
		sprite.get(NEAR).setScale(0.25f, 0.2f);
	}

	//************************************************************
	// moveBackground
	// 背景移動
	//************************************************************
	public void moveBackground() {
/*		// プレイヤーの座標をカメラの座標に代入
		cameraPos = player.GetPosition();

		// カメラ移動制限
		if (cameraPos.x < -(sprite.get(MAIN).getWidth() * 0.5 - ScrollNinja.window.x * 0.5f) * 0.1f)
			cameraPos.x = -(sprite.get(MAIN).getWidth() * 0.5f - ScrollNinja.window.x * 0.5f) *0.1f;
		if (cameraPos.x > (sprite.get(MAIN).getWidth() * 0.5 - ScrollNinja.window.x * 0.5f) * 0.1f)
			cameraPos.x = (sprite.get(MAIN).getWidth() * 0.5f - ScrollNinja.window.x * 0.5f) * 0.1f;
		// 1333は実際の画像のサイズ
		if (cameraPos.y < -(1333 - ScrollNinja.window.y) * 0.5f * 0.1f)
			cameraPos.y = -(1333 - ScrollNinja.window.y) * 0.5f * 0.1f;
		if (cameraPos.y > (1333 - ScrollNinja.window.y) * 0.5f * 0.1f)
			cameraPos.y = (1333 - ScrollNinja.window.y) * 0.5f * 0.1f;

		// 近景
		// 11.05はLoadTexture時の41.05-画面サイズ600÷2 ?
		sprite.get(NEAR).setPosition(-sprite.get(NEAR).getWidth() * 0.5f - player.GetPosition().x * 1.5f,
										-sprite.get(NEAR).getHeight() * 0.5f -11.05f + player.GetPosition().y);
		// 遠景
		sprite.get(FAR).setPosition(cameraPos.x - (sprite.get(FAR).getWidth() * 0.5f) + (cameraPos.x * -0.05f),
									cameraPos.y - (sprite.get(FAR).getHeight() * 0.5f) + (cameraPos.y * -0.15f));*/
	}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
/*	public Sprite GetSprite(int i) { return sprite.get(i); }
	public Body GetBody() { return body; }
	public Fixture GetSensor(int i) { return sensor.get(i); }*/
	public Vector2 GetCamPos() { return cameraPos; }
	public Background GetBackground(){ return this; }
	public int GetBackgroundNum(){ return bgNum; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
/*	public static void SetSprite(Sprite sp) { sprite.add(sp); }
	public static void SetBody(Body bd) { body = bd; }
	public static void SetFixture(Fixture ss){ sensor.add(ss); }
*/
}