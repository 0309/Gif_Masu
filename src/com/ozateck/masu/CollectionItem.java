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
 * コレクションアイテム
 */

public class CollectionItem{
	
	private static final String TAG = "myTag";

	private final CCLayer layer;
	private final int ptmRatio;
	private int baseLevel;      //重なり順の初期位置
	
	private CGPoint  cPoint;   //中央点
	
	private CCSprite backSprite;
	
	private static final int TAG_BACK = 0;
	
	private int itemInd;
	private int activeInd;

	//x,yはメートル換算
	public CollectionItem(CCLayer layer, int ptmRatio, int baseLevel,
						float x, float y, float size, int itemInd, int activeInd){
		
		this.layer     = layer;
		this.ptmRatio  = ptmRatio;
		this.baseLevel = baseLevel;
		
		cPoint    = CGPoint.make(x, y);
		
		this.itemInd = itemInd;
		this.activeInd = activeInd;
		
		init(x, y, size);
	}
	
	private void init(float x, float y, float size){

		/////////////////////
        //背景のスプライトシート
		CCSpriteSheet ssBack = CCSpriteSheet.spriteSheet("collection_icon.png", 100);
		layer.addChild(ssBack, 0, TAG_BACK);
		
		int row = 0;
		int col = 0;
		if(activeInd != 0){
			row = itemInd / 6;
			col = itemInd % 6;
		}
		CGRect rect = CGRect.make(col*160, row*160, 160, 160);
		
		backSprite = new CCSprite();
		backSprite.setTexture(ssBack.getTexture());
		backSprite.setTextureRect(rect);

		float bw = size * ptmRatio;//メートル換算でのピクセル数
		float bh = (rect.size.height / rect.size.width) * bw;
		
		backSprite.setScaleX(bw/rect.size.width);
		backSprite.setScaleY(bh/rect.size.height);
		
		backSprite.setPosition(x * ptmRatio, y * ptmRatio);
		
		layer.addChild(backSprite, baseLevel++);//重なり順の数字も上げておく
	}
	
	//中央点を得る
	public CGPoint getCenterPoint(){
		return cPoint;
	}
	
	//タッチ判定
	public boolean isInside(CGPoint point){
		CGRect rect = backSprite.getBoundingBox();
		
		CGRect innerRect = CGRect.make(
				rect.origin.x, rect.origin.y,
				rect.size.width,
				rect.size.height);
		
		if(innerRect.contains(point.x, point.y)){
			return true;
		}else{
			return false;
		}
	}

	public int getItemInd(){
		return itemInd;
	}
	
	public boolean isActive(){
		if(activeInd == 1){
			return true;
		}else{
			return false;
		}
	}
}
