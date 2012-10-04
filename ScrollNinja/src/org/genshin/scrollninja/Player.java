package org.genshin.scrollninja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

// 10/2 制作開始
// 10/3 変数と空の関数を実装
// 		ジャンプと移動だけ先に明日実装！

public class Player extends CharacterBase {
	// 定数宣言
	private final int RIGHT		= 0;
	private final int LEFT		= 1;
	
	// 変数宣言
	private int		charge;			// チャージゲージ
	private int		money;			// お金？←お金とかはシステムが管理した方がいいかも？
	private int		direction;		// 向いてる方向
	private int		jumpCount;		// ジャンプカウント
	private Weapon	weapon;			// 武器のポインタ
	private boolean jump;			// ジャンプフラグ
	
	// コンストラクタ
	public Player() {
		charge		= 0;
		money		= 0;
		direction	= 1;
		jump		= false;
	}
	
	//************************************************************
	// Update
	// 更新処理はここにまとめる
	//************************************************************
	public void Update() {
		Jump();			// ジャンプ処理
		Move();			// 移動処理
	}
	
	//************************************************************
	// Draw
	// 描画処理はここでまとめる
	//************************************************************
	public void Draw() {}
	
	//************************************************************
	// Jump
	// ジャンプ処理。上押すとジャンプ！
	//************************************************************
	private void Jump() {
	}
	
	//************************************************************
	// Move
	// 移動処理。左右押すと移動します
	//************************************************************
	private void Move() {
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			position.x ++;
			direction = RIGHT;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			position.x --;
			direction = LEFT;
		}
		
	}
	
	// 攻撃
	private void Attack(){}
	
	// カギ縄
	private void Kaginawa(){}
	
	// アニメーション
	private void animation(){}
	
	// 武器変更
	private void changeWeapon() {
	}
	
}
