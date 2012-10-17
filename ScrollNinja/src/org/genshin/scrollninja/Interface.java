package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//========================================
//クラス宣言		インターフェース表示を扱うクラス
//========================================
public class Interface {
	private static Sprite scroll;		// HPの巻物本体部分
	private static Sprite hp;			// プレイヤーHP
	private static Sprite hyoutan;		// チャクラのひょうたん部分
	private static Sprite chakra;		// プレイヤーチャクラ
	private ArrayList<Sprite> weapon;	// 武器

	private Animation scrollAnimation;	// 巻物のアニメーション
	private TextureRegion nowFrame;		// 巻物の現在のコマ
	private float stateTime;			// アニメーション用

	private Player player;				// プレイヤー情報格納
	private float percentHP;			// 現在のHPの割合
	private float countHP;				// 巻物を0.01ずつ現在のHPの割合まで動かすためのカウンタ
	private float percentChakra;		// 現在のチャクラの割合
	private float countChakra;			// 巻物を0.01ずつ現在のチャクラの割合まで動かすためのカウンタ

	// コンストラクタ
	public Interface() {
		player = new Player("プレイヤー");
		weapon = new ArrayList<Sprite>();

		// テクスチャ画像読み込み
		Texture texture = new Texture(Gdx.files.internal("data/interface.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// 巻物アニメーション
		TextureRegion[][] tmp = TextureRegion.split(texture, 128, 128);
		TextureRegion[] frame = new TextureRegion[3];
		int index = 0;
		for (int i = 0; i < frame.length; i++)
			frame[index++] = tmp[0][i];
		scrollAnimation = new Animation(5.0f, frame);
		scroll = new Sprite(scrollAnimation.getKeyFrame(0, false));
		scroll.setOrigin(scroll.getX(), scroll.getY());
		scroll.setScale(0.1f);

		nowFrame = scrollAnimation.getKeyFrame(0, false);

		// HP部分
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 128, 512, 128);
		hp = new Sprite(tmpRegion);
		hp.setOrigin(hp.getX(), hp.getY());
		hp.setScale(0.1f);

		// ひょうたん
		tmpRegion = new TextureRegion(texture, 0, 256, 128, 128);
		hyoutan = new Sprite(tmpRegion);
		hyoutan.setScale(0.1f);

		// チャクラ
		tmpRegion = new TextureRegion(texture, 128, 256, 128, 128);
		chakra = new Sprite(tmpRegion);
		chakra.setScale(0.1f);

		// 最初の設定；
		percentHP = 1;
		countHP = percentHP;
		percentChakra = 0;
		countChakra = percentChakra;

		stateTime = 0;
	}

	public void update() {
		// プレイヤー情報取得
		player = PlayerManager.GetPlayer("プレイヤー");
		// 現在の割合を取得
		percentHP = player.GetHP() / player.GetMaxHP();
		//percentChakra =
		// いくつ減らすか計算
		countHP -= percentHP;
		countChakra -= percentChakra;

		// HP回復　1フレームで0.01ずつ増加
		if ( countHP > percentHP && countHP < 0.99 ) {
			countHP += 0.01f;
			hp.scroll(-0.01f, 0);
			stateTime += 1;
			scrollAnimation.setPlayMode(Animation.LOOP_REVERSED);
			nowFrame = scrollAnimation.getKeyFrame(stateTime, true);
			scroll.setRegion(nowFrame);
		}

		// HP減る　1フレームで0.01ずつ減少
		if ( countHP < percentHP && countHP > 0.01 ) {
			countHP -= 0.01f;
			hp.scroll(0.01f, 0);
			stateTime += 1;
			scrollAnimation.setPlayMode(Animation.LOOP);
			nowFrame = scrollAnimation.getKeyFrame(stateTime, true);
			scroll.setRegion(nowFrame);
		}

		// チャクラ増える　1フレームで0.01ずつ増加
		if ( countChakra > percentChakra && countChakra < 0.99 ) {
			countChakra += 0.01f;
			chakra.scroll(0, -0.01f);
		}

		// チャクラ減る　1フレームで0.01ずつ減少
		if ( countChakra < percentChakra && countChakra > 0.01 ) {
			countChakra -= 0.01f;
			chakra.scroll(0, 0.01f);
		}

		// 描画位置セット
		scroll.setPosition(GameMain.camera.position.x - (ScrollNinja.window.x * 0.5f * 0.1f),
						   GameMain.camera.position.y  - 12.8f + (ScrollNinja.window.y * 0.5f * 0.1f));
		hp.setPosition(scroll.getX(), scroll.getY());
		hyoutan.setPosition(scroll.getX() + 51.2f, scroll.getY());
		chakra.setPosition(hyoutan.getX(), hyoutan.getY());

		if (stateTime > 60)
			stateTime = 0;
	}

	public void Draw() {
		hp.draw(GameMain.spriteBatch);
		scroll.draw(GameMain.spriteBatch);
		chakra.draw(GameMain.spriteBatch);
		hyoutan.draw(GameMain.spriteBatch);
	}
}