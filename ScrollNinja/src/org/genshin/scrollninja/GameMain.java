package org.genshin.scrollninja;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class GameMain implements Screen{
	private Game					ScrollNinjya;
	private OrthographicCamera		camera;				// カメラ
	private SpriteBatch				spriteBatch;		// スプライトバッチ
	private World					world;				// ワールドマトリクス
	private Box2DDebugRenderer		renderer;			//
	private StageObjectManager		SOM;				// ステージオブジェクトマネージャ
	private Background				BG;					// 背景
	private Player					player;				// プレイヤー
	private Stage stage;


	// コンストラクタ
	public GameMain(Game game) {
		ScrollNinjya		= game;
		SOM					= StageObjectManager.GetInstace();
		BG					= Background.GetInstace();
		player				= new Player();
		camera				= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch 		= new SpriteBatch();
		world				= new World(new Vector2(0, -100.0f), true);
		renderer			= new Box2DDebugRenderer();


		CreateStage();
		CreatePlayer();
		CreateStageObject();
		BG.LoadTexture();
	}

	//************************************************************
	// Update
	// 更新処理
	//************************************************************
	public void Update() {
		player.Update(world);
	}

	//************************************************************
	// Draw
	// 描画処理
	//************************************************************
	public void Draw() {
		spriteBatch.setProjectionMatrix(camera.combined);		// プロジェクション行列のセット
		spriteBatch.begin();									// 描画開始
		player.Draw(spriteBatch);
		spriteBatch.end();										// 描画終了
		
		renderer.render(world, camera.combined);
		world.step(Gdx.graphics.getDeltaTime(), 20, 20);
		player.GetBody().setAwake(true);
	}

	//************************************************************
	// CreateWorld
	// フィールドの作成
	//************************************************************
	private void CreateStage() {
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/test.json"));

		// ボディタイプ設定
		BodyDef bd	= new BodyDef();
		bd.type		= BodyType.StaticBody;		// 動かない物体
		bd.position.set(-(Gdx.graphics.getWidth() / 2), -1024);

		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 1000;		// 密度
		fd.friction		= 100.0f;	// 摩擦
		fd.restitution	= 0;		// 反発係数

		// ボディ作成
		BG.SetBody(world.createBody(bd));
		loader.attachFixture( BG.GetBody(), "bgTest", fd, 2048);
	}

	//************************************************************
	// CreateCharacter
	// プレイヤーの作成
	//************************************************************
	private void CreatePlayer() {
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
		poly.dispose();
		player.GetBody().setBullet(true);			// すり抜け防止
		player.GetBody().setTransform(0, 300, 0);	// 初期位置
	}

	//************************************************************
	// CreateStageObject
	// ステージオブジェクトの作成
	//************************************************************
	private void CreateStageObject() {
		// 当たり判定読み込み
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/stageObject.json"));

		// Bodyのタイプを設定 Staticは動かない物体
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(0, 0);

		// Bodyの設定を設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 1000;				// 密度
		fd.friction		= 100f;				// 摩擦
		fd.restitution	= 0;				// 反発係数

		// ステージオブジェクトの作成
		SOM.CreateStageObject("岩");
		SOM.GetStageObject("岩").SetBody(world.createBody(bd));

		// 各種設定を適用。引数は　Body、JSON中身のどのデータを使うか、FixtureDef、サイズ
		loader.attachFixture(SOM.GetStageObject("岩").GetBody(), "gravestone", fd, 256);
	}







































	@Override
	public void render(float delta) {
		// TODO 自動生成されたメソッド・スタブ
		Update();
		Draw();

	}

	@Override
	public void resize(int width, int height) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void show() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void hide() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void pause() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void resume() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void dispose() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
