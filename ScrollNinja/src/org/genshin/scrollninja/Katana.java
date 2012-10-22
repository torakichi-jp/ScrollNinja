package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;


//TODO  プレイヤークラスに追加
//		WeaponBase weapon;
//		コンストラクタで適当に代入
//		ChangeWeapon(WeaponBase Weapon) {
//			weapon = Weapon;
//		}
public class Katana extends WeaponBase {
	
	/**
	 * コンストラクタ
	 * @param i		管理番号
	 */
	public Katana(CharacterBase chara, int i){
		owner		= chara;					// 使用者
		number		= i;						// 管理番号
		level		= 2;						// レベル
		attackNum	= (level * 10);				// 攻撃力（てきとー）
		position 	= new Vector2(0.0f, 0.0f);
		use			= false;
		
//		EffectManager.CreateEffect(Effect.FIRE_1);
		EffectManager.CreateEffect(Effect.FIRE_2);
//		EffectManager.CreateEffect(Effect.FIRE_3);
//		sprite		= new ArrayList<Sprite>();
//		sensor		= new ArrayList<Fixture>();
	}
	
	/**
	 * 更新
	 */
	public void Update() {
		switch( level ) {
		case 1:
			if( EffectManager.GetEffect(Effect.FIRE_1).GetStateTime() % 18 == 0 ) {
				use = false;
			}
			EffectManager.GetEffect(Effect.FIRE_1).Update(use);
			break;
		case 2:
			if( EffectManager.GetEffect(Effect.FIRE_2).GetStateTime() % 18 == 0 ) {
				use = false;
			}
			EffectManager.GetEffect(Effect.FIRE_2).Update(use);
			break;
		case 3:
			if( EffectManager.GetEffect(Effect.FIRE_3).GetStateTime() % 18 == 0 ) {
				use = false;
			}
			EffectManager.GetEffect(Effect.FIRE_3).Update(use);
			break;
		}
	}
}
