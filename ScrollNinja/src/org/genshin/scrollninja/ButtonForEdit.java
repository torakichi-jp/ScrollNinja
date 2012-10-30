package org.genshin.scrollninja;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;

public class ButtonForEdit extends JButton implements ActionListener {
	
	private int i = 0;
	private TextField text = new TextField();

	public ButtonForEdit(String name) {
		// TODO 自動生成されたコンストラクター・スタブ
		setText(name);
		addActionListener(this);
	}

	/**
	 * @OVERRIDE
	 * ボタンが押されたら呼ばれる
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
//		this.setText("bbb");		
		BufferedReader fp = new BufferedReader(new InputStreamReader(System.in));
		try {this.setLabel(fp.readLine());}
		catch (IOException err) { }
	}
}
