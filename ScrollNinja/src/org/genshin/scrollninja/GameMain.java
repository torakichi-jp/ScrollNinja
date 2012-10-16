package org.genshin.scrollninja;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

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

public class GameMain implements Screen{
	private Game						ScrollNinjya;
	public static World					world;			// ワールド
	public static OrthographicCamera	camera;			// カメラ
	public static SpriteBatch			spriteBatch;	// スプライトバッチ
	private long 			error			= 0;
	private int				fps				= 60;
	private long			idealSleep		= (1000 << 16) / fps;
	private long			newTime			= System.currentTimeMillis() << 16;
	private long			oldTime;
	private long			sleepTime		= idealSleep - (newTime - oldTime) - error; // 休止できる時間
	private Background		backGround;

	// コンストラクタ
	public GameMain(Game game, Background bg) {
		ScrollNinjya		= game;
		world				= new World(new Vector2(0, -20.0f), true);
		camera				= new OrthographicCamera(ScrollNinja.window.x * 0.1f, ScrollNinja.window.y * 0.1f);
		spriteBatch 		= new SpriteBatch();
		
		StageManager.GetNowStage().Init();
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
		oldTime = newTime;

		StageManager.Update();
		StageManager.Draw();

		FPS();
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
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} // 休止
		newTime = System.currentTimeMillis() << 16;
		error = newTime - oldTime - sleepTime; // 休止時間の誤差
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
	}
}
