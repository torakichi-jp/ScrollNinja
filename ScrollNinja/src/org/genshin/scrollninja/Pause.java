package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/*
 * 10/25 ゲームメインのポーズ処理をこっちで
 * 10/25 キャラクターアニメーション + マップ移動
 * */
// ポーズ中の処理クラス
public class Pause {
	private static boolean drawflag;		// (仮)ワールドマップ表示フラグ
	private Sprite returnGame;				// (仮)コンティニュー文字
	private Sprite title;					// (仮)タイトル文字
	private Sprite load;						// (仮)ロード
	private Sprite pausemenu;				// ポーズメニュー
	private Sprite worldMap;					// ワールドマップ
	
	private boolean gotoMain;				// メイン移行フラグ
	private boolean gotoTitleMenu;			// タイトル移行フラグ
	
	/* アニメーション用
	private Animation		walkAnimation;			// 歩きアニメーション
	private TextureRegion[]	frame;					// アニメーションのコマ
	private Animation		footWalkAnimation;		// 下半身・歩きアニメーション
	private TextureRegion	nowFrame;				// 現在のコマ
	private TextureRegion	nowFootFrame;			// 下半身用の現在のコマ
	private int					count;					// カウント用変数
	private float				stateTime;
	private ArrayList<Sprite> 	sprite;
	
	private Sprite footSprite;
	private Sprite bodySprite;
	private Body body;
	private Vector2 position;
	*/
	// コンストラクタ
	public Pause() {
		// ワールドマップ
		Texture worldMaptexture = new Texture(Gdx.files.internal("data/worldmap.png"));
		worldMaptexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion worldRegion = new TextureRegion(worldMaptexture);
		worldMap = new Sprite(worldRegion);
		worldMap.setScale(ScrollNinja.scale * 1.5f);
		
		// ポーズ画面中の背景
		Texture pausemenubackTexture = new Texture(Gdx.files.internal("data/pausemenuback.png"));
		pausemenubackTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion pauseRegion = new TextureRegion(pausemenubackTexture);
		pausemenu = new Sprite(pauseRegion);
		pausemenu.setScale(ScrollNinja.scale * 1.5f);
		
		// ポーズメニュー
		Texture pauseMenuTexture = new Texture(Gdx.files.internal("data/menu.png"));
		pauseMenuTexture.setFilter(TextureFilter.Linear,TextureFilter.Linear);
		TextureRegion returnGameRegion = new TextureRegion(pauseMenuTexture,0,0,256,35);
		returnGame = new Sprite(returnGameRegion);
		returnGame.setScale(ScrollNinja.scale);
		TextureRegion titleRegion = new TextureRegion(pauseMenuTexture,0,40,256,35);
		title = new Sprite(titleRegion);
		title.setScale(ScrollNinja.scale);
		TextureRegion loadRegion = new TextureRegion(pauseMenuTexture,0,85,256,35);
		load = new Sprite(loadRegion);
		load.setScale(ScrollNinja.scale);
		
		// アニメーション
			
		gotoMain = false;
		gotoTitleMenu = false;
	}
	
	// アップデート
	public void update() {
		spriteUpdate();
		clickedUpdate();
		pressedUpdate();
		playerAnimation();
	}
	
	// 描画
	public void draw() {
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		GameMain.spriteBatch.begin();
		pausemenu.draw(GameMain.spriteBatch);
		if(drawflag)
			worldMap.draw(GameMain.spriteBatch);
		returnGame.draw(GameMain.spriteBatch);
		title.draw(GameMain.spriteBatch);
		load.draw(GameMain.spriteBatch);
		GameMain.spriteBatch.end();
	}
	
	// スプライトアップデート
	public void spriteUpdate() {
		worldMap.setPosition(GameMain.camera.position.x - worldMap.getWidth()*0.5f
				+ (ScrollNinja.window.x*0.5f*ScrollNinja.scale) - worldMap.getWidth()*0.5f*0.12f,
				GameMain.camera.position.y - worldMap.getHeight()*0.5f
				+ (ScrollNinja.window.x*0.5f*ScrollNinja.scale) - worldMap.getWidth()*0.5f*0.16f);
		
		pausemenu.setPosition(GameMain.camera.position.x - pausemenu.getWidth()*0.5f
				+ (ScrollNinja.window.x*0.5f*ScrollNinja.scale) - pausemenu.getWidth()*0.5f*0.12f,
				GameMain.camera.position.y - pausemenu.getHeight()*0.5f
				+ (ScrollNinja.window.y*0.5f*ScrollNinja.scale) - pausemenu.getHeight()*0.5f*0.115f);
		
		returnGame.setPosition(GameMain.camera.position.x - returnGame.getWidth()*0.5f
				+ (ScrollNinja.window.x*0.5f*ScrollNinja.scale) - returnGame.getWidth()*0.5f*0.1f,
				GameMain.camera.position.y - returnGame.getHeight()*0.5f
				+ (ScrollNinja.window.y*0.5f*ScrollNinja.scale)- returnGame.getHeight()*0.5f*0.5f);

		title.setPosition(GameMain.camera.position.x - title.getWidth()*0.5f
				+ (ScrollNinja.window.x*0.5f*ScrollNinja.scale) - title.getWidth()*0.5f*0.1f,
				GameMain.camera.position.y - title.getHeight()*0.5f
				+ (ScrollNinja.window.y*0.5f*ScrollNinja.scale)- title.getHeight()*0.5f*0.7f);
			
		load.setPosition(GameMain.camera.position.x - load.getWidth()*0.5f
				+ (ScrollNinja.window.x*0.5f*ScrollNinja.scale) - load.getWidth()*0.5f*0.1f,
				GameMain.camera.position.y - load.getHeight()*0.5f
				+ (ScrollNinja.window.y*0.5f*ScrollNinja.scale)- load.getHeight()*0.5f*0.9f);
	}
	
	// クリック時の処理
	public void clickedUpdate() {
		if(Gdx.input.isTouched()) {
			/*マウス座標取得 ウィンドウの中心が原点*/
			float mousePositionX = Gdx.input.getX() - Gdx.graphics.getWidth()*0.5f;
			float mousePositionY =  Gdx.graphics.getHeight()*0.5f - Gdx.input.getY();
			System.out.println("mousePX:"+mousePositionX);
			System.out.println("mousePY:"+mousePositionY);
			
			// ポーズメニュー中の文字をクリックしたら
			// コンティニュー
			if(mousePositionX > 445 && mousePositionX < 620 &&
				mousePositionY < 282 && mousePositionY > 255) {
				gotoMain = true;
			}
			// 
			if(mousePositionX > 450 && mousePositionX < 647 &&
				mousePositionY < 242 && mousePositionY > 215) {

			}
			// 
			if(mousePositionX > 450 && mousePositionX < 656 &&
				mousePositionY < 208 && mousePositionY > 183) {

			}
		}
	}
	
	// キーボード押したときの処理
	public void pressedUpdate() {
		if(Gdx.input.isKeyPressed(Keys.L)) {
			gotoMain = true;
		}
		if(Gdx.input.isKeyPressed(Keys.G)) {
			gotoTitleMenu = true;
		}
		if(Gdx.input.isKeyPressed(Keys.V)) {
			drawflag = true;
		}
		if(Gdx.input.isKeyPressed(Keys.B)) {
			drawflag = false;
		}
	}
	
	// プレイヤーアニメーション
	public void playerAnimation() {
	}
	
	// ゲッター セッター
	public boolean GetgotoMainFlag(){return gotoMain;}
	public void SetgotoMainFlag(boolean flag){gotoMain = flag;}
	public boolean GetgotoTitleFlag(){return gotoTitleMenu;}
	public void SetgotoTitleFlag(boolean flag){gotoTitleMenu = flag;}
}
