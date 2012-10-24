package org.genshin.scrollninja;

import java.awt.RenderingHints.Key;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

// 制作メモ
// 10/5 制作開始
// 10/6 とりあえず表示まで。シングルトンを全部モノステートに。
// 10/8 コメント追加
// 10/10 仕様変更

// *メモ*
// stageってのがシーンの代わりです。
// シーン（ステージ）を遷移させたい時は、Stage型の変数を宣言してnew(world)して
// StageManager.StageTrance(変数名)で遷移します。
// 初期化処理は今はコンストラクタでやってますがあとで追加していきます。
// 10/23 line135で画像など解放？してタイトル遷移して増殖しないようにスタート

public class GameMain implements Screen{
	private Game						scrollNinja;
	public static World					world;			// ワールド
	public static OrthographicCamera	camera;		// カメラ
	public static SpriteBatch			spriteBatch;	// スプライトバッチ
	public static Interface 				playerInfo;	// インターフェース
	public boolean				PauseFlag;
	private Stage 			stage;						// ステージ
	private int				stageNum;					// ステージナンバー
	private long 			error			= 0;
	private int				fps				= 60;
	private long			idealSleep		= (1000 << 16) / fps;
	private long			newTime			= System.currentTimeMillis() << 16;
	private long			oldTime;
	private long			sleepTime		= idealSleep - (newTime - oldTime) - error; // 休止できる時間

	private boolean gotomenu;
	private Sprite worldMap;
	private static boolean drawflag;

	// 仮
	public static int gameState;
	public final static int GAME_RUNNING	= 0;	// ゲーム中
	public final static int GAME_PAUSED		= 1;	// 一時停止中
	public final static int GO_TO_MENU		= 9;	// メニュー画面へ
	
	public static int pauseState;
	public final static int PAUSE_INIT = 0;
	public final static int PAUSE_UPDATE    = 1;

	// コンストラクタ
	public GameMain(Game game, int num) {
		scrollNinja		= game;
		// TODO 重力は調整必要あり
		world				= new World(new Vector2(0, -150.0f), true);

		// TODO 画面サイズによって数値を変更
		camera				= new OrthographicCamera(ScrollNinja.window.x * ScrollNinja.scale,
													 ScrollNinja.window.y * ScrollNinja.scale);
		spriteBatch 		= new SpriteBatch();
		stageNum			= num;
		stage				= new Stage(stageNum);
		playerInfo			= new Interface();

		StageManager.ChangeStage(stage);
		StageManager.GetNowStage().Init();
		BackgroundManager.CreateBackground(stageNum, true);

		gotomenu = false;
		drawflag = false;
		gameState = GAME_RUNNING;
		pauseState = PAUSE_INIT;
	}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
//	public World GetWorld(){ return world; }
//	public OrthographicCamera GetCamera(){ return camera; }
//	public SpriteBatch GetSpriteBatch(){ return spriteBatch; }

	//************************************************************
	// render
	// メイン処理
	//************************************************************
	@Override
	public void render(float delta) {
		switch (gameState) {
		case GAME_RUNNING:
			oldTime = newTime;

			//TODO
			if(Gdx.input.isKeyPressed(Keys.I)) {
				scrollNinja.setScreen(new StageEditor());
				StageEditor.Init();
			}

			StageManager.Update();
			StageManager.Draw();

			FPS();
			break;
		case GAME_PAUSED:
			switch(pauseState) {
			case PAUSE_INIT:
				InitPause();
				break;
			case PAUSE_UPDATE:
				spriteBatch.begin();
				updatePaused(delta);
				DrawPause();
				spriteBatch.end();
				//break;
			}
			break;
		case GO_TO_MENU:
			stage.dispose();
			scrollNinja.setScreen(new MainMenu(scrollNinja));
			break;
		}
	}

	//************************************************************
	// FPS
	// FPS処理。汚いので関数化
	//************************************************************
	public void FPS() {
		newTime = System.currentTimeMillis() << 16;
		if (sleepTime < 0x20000) sleepTime = 0x20000; // 最低でも2msは休止
		oldTime = newTime;
		try {
			Thread.sleep(sleepTime >> 16);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // 休止
		newTime = System.currentTimeMillis() << 16;
		error = newTime - oldTime - sleepTime; // 休止時間の誤差
	}

	/**
	 * updatePaused
	 * マップ表示中は他の描画をしない
	 */
	// TODO ぶれるので調整必要
	public void updatePaused(float delta) {
		// ポーズしたら全画面マップ表示

		// Lキーでポーズ解除（仮
		if(Gdx.input.isKeyPressed(Keys.L)) {
			playerInfo.SetPauseFlag(false);
			gameState = GAME_RUNNING;
		}

		if(Gdx.input.isKeyPressed(Keys.G)) {
			gameState = GO_TO_MENU;
		}
		
		if(Gdx.input.isTouched()) {
			/* 	マウス取得 ウィンドウの中心が原点 */
			float x = Gdx.input.getX() - Gdx.graphics.getWidth()*0.5f;
			float y = Gdx.graphics.getHeight()*0.5f - Gdx.input.getY();
			System.out.println("mouseX:"+ x);
			System.out.println("mouseY:"+ y);
			
			// (仮)コンティニューをクリックしたら
			if(x > 445 && x < 620 && y < 282 && y > 255) {
				playerInfo.SetPauseFlag(false);
				gameState = GAME_RUNNING;
			}			
			// (仮)コンティニューをクリックしたら
			if(x > 450 && x < 647 && y < 242 && y > 215) {

			}		
			// (仮)コンティニューをクリックしたら
			if(x > 450 && x < 656 && y < 208 && y > 183) {

			}
		}
		if(Gdx.input.isKeyPressed(Keys.V)) {
			drawflag = true;
		}
		if(Gdx.input.isKeyPressed(Keys.B)) {
			drawflag = false;
		}
		
		worldMap.setPosition(camera.position.x - worldMap.getWidth() * 0.5f
				+ (ScrollNinja.window.x * 0.5f * ScrollNinja.scale) - worldMap.getWidth() * 0.5f * 0.12f,
				camera.position.y - worldMap.getHeight() * 0.5f
				+ (ScrollNinja.window.y * 0.5f * ScrollNinja.scale)- worldMap.getHeight() * 0.5f * 0.12f);
		
		System.out.println(drawflag);
	}
	
	// ポーズ初期化
	public void InitPause() {
		// ワールドマップ
		Texture worldMaptexture = new Texture(Gdx.files.internal("data/worldmap.png"));
		worldMaptexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion worldRegion = new TextureRegion(worldMaptexture);
		worldMap = new Sprite(worldRegion);
		//worldMap.setOrigin(worldMap.getWidth() * 0.5f,worldMap.getHeight() * 0.5f);
		worldMap.setScale(ScrollNinja.scale * 1.5f);
		
		pauseState = PAUSE_UPDATE;
	}
	
	// ポーズ中描画
	public void DrawPause() {
		if(drawflag) {
			worldMap.draw(spriteBatch);
		}
		else {
			
		}
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {
		if (gameState == GAME_RUNNING)
			gameState = GAME_PAUSED;
	}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		world.dispose();
		spriteBatch.dispose();
		stage = null;
	}
}