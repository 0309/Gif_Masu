package com.ozateck.masu;

import java.util.List;
import java.util.ArrayList;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.MotionEvent;
import android.util.Log;

import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.config.ccMacros;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.opengl.CCGLSurfaceView;

/*
 * コレクションの詳細
 * 画像の幅、高さが大きすぎるので、タップの度にそれぞれの画像を読み込むロジックを採用
 */

public class CollectionPanel{

	private static final String TAG = "myTag";

	private final CCLayer layer;
	private final int ptmRatio;
	private int baseLevel;//重なり順の初期位置
	
	private CGPoint   cPoint;//中央点

	private CCSprite panelSprite = null;
	private String fileNames[] = {
			"collection_panel00.png", "collection_panel01.png",
			"collection_panel02.png", "collection_panel03.png",
			"collection_panel04.png", "collection_panel05.png",
			"collection_panel06.png", "collection_panel07.png",
			"collection_panel08.png", "collection_panel09.png",
			"collection_panel10.png", "collection_panel11.png",
			"collection_panel12.png", "collection_panel13.png",
			"collection_panel14.png", "collection_panel15.png",
			"collection_panel16.png", "collection_panel17.png"
	};
	
	private float defX, defY, defSize;
	
	//x,yはメートル換算
	public CollectionPanel(CCLayer layer, int ptmRatio, int baseLevel,
						float x, float y, float size){

		this.layer     = layer;
		this.ptmRatio  = ptmRatio;
		this.baseLevel = baseLevel;
		
		this.defX    = x;
		this.defY    = y;
		this.defSize = size;
		
		cPoint = CGPoint.make(x, y);
		
		init();
	}
	
	private void init(){
		panelSprite = makeSprite(fileNames[0]);//新しいパネルを生成する
	}

	private CCSprite makeSprite(String filename){
		CCSprite backSprite = CCSprite.sprite(filename);
		
		CGRect rect = backSprite.getBoundingBox();
		
		float bw = defSize * ptmRatio;//メートル換算でのピクセル数
		float bh = (rect.size.height / rect.size.width) * bw;
		
		backSprite.setScaleX(bw/rect.size.width);
		backSprite.setScaleY(bh/rect.size.height);
		
		backSprite.setPosition(defX * ptmRatio, defY * ptmRatio);
		
		backSprite.setVisible(false);//非表示にしておく
		
		return backSprite;
	}

	//中央点を得る
	public CGPoint getCenterPoint(){
		return cPoint;
	}
	
	//詳細パネル表示
	public void showPanel(int itemInd){
		if(!panelSprite.getVisible()){
			panelSprite = makeSprite(fileNames[itemInd]);//新しいパネルを生成する
			panelSprite.setVisible(true);
			layer.addChild(panelSprite, baseLevel);
		}
	}
	
	//詳細パネル非表示
	public void hidePanel(){
		if(panelSprite.getVisible()){
			panelSprite.setVisible(false);
			layer.removeChild(panelSprite, true);
		}
	}
	
	//パネルが表示されているかどうか
	public boolean getVisible(){
		return panelSprite.getVisible();
	}
}
