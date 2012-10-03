package org.genshin.scrollninja;

// 10/2 制作開始
// 10/3 変数と空の関数を実装
// 		ジャンプと移動だけ先に明日実装！

public class Player extends CharacterBase {
	// 変数宣言
	private int		charge;			// チャージゲージ
	private int		money;			// お金？←お金とかはシステムが管理した方がいいかも？
	private int		direction;		// 向いてる方向
	private Weapon	weapon;			// 武器のポインタ
	private boolean jump;			// ジャンプフラグ
	
	// コンストラクタ
	public Player() {
		charge		= 0;
		money		= 0;
		direction	= 1;
		jump		= false;
	}
	
	// 更新
	public void Update(){}
	
	// 移動
	public void Move(){}
	
	// 攻撃
	public void Attack(){}
	
	// カギ縄
	public void Kaginawa(){}
	
	// アニメーション
	public void animation(){}
	
	// 武器変更
	public void changeWeapon() {
	}
	
}
