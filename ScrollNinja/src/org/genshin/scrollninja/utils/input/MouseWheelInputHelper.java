package org.genshin.scrollninja.utils.input;

import org.genshin.scrollninja.Global;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

/**
 * マウスホイールの入力を補助するクラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class MouseWheelInputHelper extends AbstractInputHelper
{
	/**
	 * コンストラクタ
	 * @param button	ホイール入力の種類
	 */
	public MouseWheelInputHelper(WheelButtons button)
	{
		if(processer == null)
		{
			processer = new WheelInputProcesser();
		}
			
		this.button = button;
	}
	
	/**
	 * ホイール入力の状態をそのまま取得する。
	 * @return		ホイール入力の状態
	 */
	public int getAmount()
	{
		return processer.getAmount();
	}
	
	@Override
	protected boolean getCurrentInput()
	{
		return button.getCurrentInput();
	}
	
	
	/** ホイール入力の取得用 */
	private static WheelInputProcesser processer;
	
	/** ホイール入力の種類 */
	private final WheelButtons button;
	
	
	/**
	 * ホイール入力の種類
	 */
	public enum WheelButtons
	{
		/** 上方向 */
		UP(){ boolean getCurrentInput(){ return processer.getAmount() < 0; } },
		
		/** 下方向 */
		DOWN(){ boolean getCurrentInput(){ return processer.getAmount() > 0; } },
		
		;
		
		abstract boolean getCurrentInput();
	}
	
	
	/**
	 * ホイール入力を取得するクラス
	 */
	private class WheelInputProcesser extends InputAdapter
	{
		/**
		 * コンストラクタ
		 */
		public WheelInputProcesser()
		{
			Gdx.input.setInputProcessor(this);
		}
		
		@Override
		public boolean scrolled(int amount)
		{
			lastUpdateTime = Global.frameCount;
			this.amount = amount;
			return true;
		}
		
		/**
		 * ホイール入力の状態を取得する。
		 * @return		ホイール入力の状態
		 */
		public int getAmount()
		{
			return (lastUpdateTime == Global.frameCount) ? amount : 0;
		}
		
		/** ホイール入力の状態 */
		private int amount;
		
		/** 最後に更新処理を行った時間 */
		private int lastUpdateTime = 0;
	}
}
