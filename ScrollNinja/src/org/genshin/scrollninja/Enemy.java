package org.genshin.scrollninja;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

// 制作メモ
// 10/9	座標は動いてるけど絵が付いてってない。
//		当たり判定と同期するから先に当たり判定作る。

public class Enemy extends CharacterBase {
	//========================================
	// 定数宣言
	// 敵の種類
	//========================================
	private final static int WALKENEMY		= 0;			// 歩兵?
	private final static int JUMPENEMY		= 1;			// ジャンプしてくるやつ
	
	// 変数宣言
	private String			name;			// 呼び出す時の名前
	private int				enemyType;		// 敵の種類
	private TextureRegion	frame[];		// アニメーションのコマ
	private TextureRegion	nowFrame;		// 現在のコマ
	
	// コンストラクタ 
	Enemy(String Name, int type, Vector2 pos) {
		name		= new String(Name);
		enemyType	= type;
		position	= pos;
		hp			= 100;
		attackNum	= 0;
		speed		= 0;
	}
	
	// コンストラクタ 
	Enemy(String Name, int type, float x, float y) {
		name		= new String(Name);
		enemyType	= type;
		position.x	= x;
		position.y	= y;
		hp			= 100;
		attackNum	= 0;
		speed		= 0;
		
		Create();
	}
	
	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void Update() {
//		position = body.getPosition();
		
		Move();
		
//		body.setTransform(position, body.getAngle());
	}
	
	//************************************************************
	// Draw
	// 描画処理まとめ
	//************************************************************
	public void Draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	//************************************************************
	// Create
	// テクスチャの読み込みとかスプライトのセットとかやる
	//************************************************************
	public void Create() {
		switch(enemyType) {
		case WALKENEMY:
			// テクスチャの読み込み
			Texture texture = new Texture(Gdx.files.internal("data/enemy.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion region = new TextureRegion(texture, 0, 0, 64, 64);

			// スプライトに反映
			sprite = new Sprite(region);
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

			// アニメーション
			TextureRegion[][] tmp = TextureRegion.split(texture, 64, 64);
			frame = new TextureRegion[4];
			int index = 0;
			for (int i = 1; i < 2; i++) {
				for (int j = 0; j < 4; j++) {
					if(index < 4)
						frame[index++] = tmp[i][j];
				}
			}
			
			break;
		case JUMPENEMY:
			break;
		}
	}
	
	//************************************************************
	// Move
	// 移動処理。歩りたりジャンプしたり
	//************************************************************
	public void Move() {
		switch(enemyType) {
		case WALKENEMY :
			position.x -= 1;
			System.out.println(position.x);
			break;
		case JUMPENEMY :
			break;
		}
	}
	
	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public Enemy GetEnemy() { return this; }
	public String GetName(){ return name; }
	
	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
}
