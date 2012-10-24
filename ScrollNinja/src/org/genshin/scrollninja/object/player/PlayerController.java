/**
 * 
 */
package org.genshin.scrollninja.object.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

/**
 * プレイヤーの操作状態クラス
 * @author kou
 * @since 1.0
 * @version 1.0
 */
public class PlayerController implements IPlayerController
{
	private IInputHelper inputJump;
	private IInputHelper inputKaginawa;
	
	/**
	 * コンストラクタ
	 */
	public PlayerController()
	{
		inputJump = new KeyInputHelper(Keys.W);
		inputKaginawa = new MouseInputHelper(Buttons.RIGHT);
	}

	@Override
	public void update()
	{
		inputJump.update();
		inputKaginawa.update();
	}

	@Override
	public float moveLevel()
	{
		float result = 0.0f;
		if(Gdx.input.isKeyPressed(Keys.A))		result -= 1.0f;
		if(Gdx.input.isKeyPressed(Keys.D))		result += 1.0f;
		return result;
	}

	@Override
	public boolean dash()
	{
		return Gdx.input.isKeyPressed(Keys.SHIFT_LEFT);
	}

	@Override
	public boolean jump()
	{
		return inputJump.trg();
	}

	@Override
	public boolean attack()
	{
		return Gdx.input.isButtonPressed(Buttons.LEFT);
	}

	@Override
	public boolean kaginawaThrow()
	{
		return inputKaginawa.trg();
	}
	
	/** 入力補助インタフェース */
	private interface IInputHelper
	{
		/**
		 * 入力状態を更新する。
		 */
		void update();
		
		/**
		 * プレス入力を取得する。
		 * @return	プレス入力
		 */
		boolean prs();

		/**
		 * トリガ入力を取得する。
		 * @return	トリガ入力
		 */
		boolean trg();

		/**
		 * リリース入力を取得する。
		 * @return	リリース入力
		 */
		boolean rls();
	}
	
	/** 入力補助基本クラス */
	private abstract class InputHelperBase implements IInputHelper
	{
		protected boolean prev, now;
		
		/**
		 * 入力状態を更新する。
		 * @param flg		新しい入力状態
		 */
		protected final void update(boolean flg)
		{
			prev = now;
			now = flg;
		}

		@Override
		public final boolean prs()
		{
			return now;
		}

		@Override
		public final boolean trg()
		{
			return !prev && now;
		}

		@Override
		public final boolean rls()
		{
			return prev && !now;
		}
	}
	
	/** キーボード入力補助クラス */
	private final class KeyInputHelper extends InputHelperBase
	{
		private final int key;
		
		public KeyInputHelper(int key)
		{
			this.key = key;
		}

		@Override
		public final void update()
		{
			update(Gdx.input.isKeyPressed(key));
		}
	}
	
	/** マウス入力補助クラス */
	private final class MouseInputHelper extends InputHelperBase
	{
		private final int button;
		
		public MouseInputHelper(int button)
		{
			this.button = button;
		}

		@Override
		public final void update()
		{
			update(Gdx.input.isButtonPressed(button));
		}
	}
}
