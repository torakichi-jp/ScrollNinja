package org.genshin.scrollninja;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

public class ReadFile {
	
	private ReadFile(){}
	
	/**
	 * ファイル読み込み
	 * @param filePath		読み込むデータのファイルパス
	 */
	public static void read(String filePath) {
		int a[]=new int[1000];
        int i = 0;
        int num = 0;
        
        try {
        	FileReader fr=new FileReader(filePath);     		// FileReaderオブジェクトの作成
        	StreamTokenizer st=new StreamTokenizer(fr);     	// StreamTokenizerオブジェクトの作成

        	// ファイルの終わりに達するとTT_EOFが返されるので、そこでループ終了
        	while(st.nextToken() != StreamTokenizer.TT_EOF) {
    			// オブジェクトの生成
    			StructObject pStructObject = new StructObject();
        		
        		// 改行文字の判定
        		if( st.ttype != StreamTokenizer.TT_EOL ) {
            		// スラッシュ（文字）の場合
            		if( st.ttype != StreamTokenizer.TT_WORD ) {
            			i ++;
                		// 数字の場合
                		if( st.ttype == StreamTokenizer.TT_NUMBER ) {
                			switch( i ) {
                			case 1:
                    			pStructObject.type = (int)st.nval;
                				break;
                			case 2:
                				pStructObject.positionX = (int)st.nval;
                				break;
                			case 3:
                				pStructObject.positionY = (int)st.nval;
                				break;
                			case 4:
                				pStructObject.priority = (int)st.nval;
                				break;
                			}
                		}
            		}
        		}
        	}
        	fr.close();
        }
        catch(Exception e) {
        	System.out.println(e);  //エラーが起きたらエラー内容を表示
        }
        
        for(int j = 0; j < num; j ++) {
    //    	System.out.println();   //配列の中のデータを表示
        }
	}
}
