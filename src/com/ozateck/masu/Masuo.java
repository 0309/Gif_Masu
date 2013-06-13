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
 * 岐阜野升男さん
 */

public class Masuo{
	
	private static final String TAG = "myTag";

	private final CCLayer layer;
	private final int ptmRatio;
	private int baseLevel;      //重なり順の初期位置
	
	private CGPoint   cPoint;   //中央点
	private boolean   touchable;//選択出来るかどうか
	private boolean   ok;       //クリアされたかどうか
	
	private CCSprite  backSprite;
	
	private CCSprite  masuoSprite;      //升男
	private CCAnimate animateStandup;   //立つ
	private CCAnimate animateSitdown;   //座る
	private CCAnimate animatePlease;    //喜ぶ,座る
	private CCAnimate animateDisappoint;//ガッカリ,座る
	
	private CCSprite  itemSprite;//アイテム
	private CCSprite  okSprite;//OKマーク
	
	private static final int TAG_BACK  = 0;
	private static final int TAG_ANIME = 1;
	private static final int TAG_ITEM  = 2;
	
	private int itemInd;

	//アイテム
	public static final int ITEM_NOTHING    = 0;
	public static final int ITEM_MASU       = 1;
	public static final int ITEM_MIZU       = 2;
	public static final int ITEM_SENBEI     = 3;
	public static final int ITEM_GIFUJO     = 4;
	public static final int ITEM_UKAI       = 5;
	public static final int ITEM_KAKI       = 6;
	public static final int ITEM_UCHIWA     = 7;
	public static final int ITEM_CHOCHIN    = 8;
	public static final int ITEM_EDAMAME    = 9;
	public static final int ITEM_GUJO       = 10;
	public static final int ITEM_WASHI      = 11;
	public static final int ITEM_UDATSU     = 12;
	public static final int ITEM_SHIRAKAWA  = 13;
	public static final int ITEM_HIDAGYU    = 14;
	public static final int ITEM_GERO       = 15;
	public static final int ITEM_MINOYAKI   = 16;
	public static final int ITEM_KURIKINTON = 17;

	//x,yはメートル換算
	public Masuo(CCLayer layer, int ptmRatio, int baseLevel,
						float x, float y, float size, int itemInd){
		
		this.layer     = layer;
		this.ptmRatio  = ptmRatio;
		this.baseLevel = baseLevel;
		
		cPoint    = CGPoint.make(x, y);
		touchable = true;
		ok        = false;

		this.itemInd = itemInd;
		init(x, y, size);
	}
	
	private void init(float x, float y, float size){

		/////////////////////
        //背景のスプライトシート
		CCSpriteSheet ssBack = CCSpriteSheet.spriteSheet("masuo_base.png", 100);
		layer.addChild(ssBack, 0, TAG_BACK);
		
		CGRect rect = CGRect.make(0, 0, 200, 280);
		
		backSprite = new CCSprite();
		backSprite.setTexture(ssBack.getTexture());
		backSprite.setTextureRect(rect);

		float bw = size * ptmRatio;//メートル換算でのピクセル数
		float bh = (rect.size.height / rect.size.width) * bw;
		
		backSprite.setScaleX(bw/rect.size.width);
		backSprite.setScaleY(bh/rect.size.height);
		
		backSprite.setPosition(x * ptmRatio, y * ptmRatio);
		
		layer.addChild(backSprite, baseLevel++);//重なり順の数字も上げておく
		
		/////////////////////
		//升男のスプライトシート
		CCSpriteSheet masuoSSheet = CCSpriteSheet.spriteSheet("masuo_anime.png", 100);
		layer.addChild(masuoSSheet, 0, TAG_ANIME);
		
		CGRect masuRect = CGRect.make(0, 0, 200, 280);
		
		masuoSprite = new CCSprite();
		masuoSprite.setTexture(masuoSSheet.getTexture());
		masuoSprite.setTextureRect(masuRect);
		
		masuoSprite.setScale(bw/rect.size.width);//背景と同じスケールにする
		
		masuoSprite.setPosition(x * ptmRatio, y * ptmRatio);
		
		layer.addChild(masuoSprite, baseLevel++);//重なり順の数字も上げておく
		

		/////////////////////
		//フレーム
		CCSpriteFrame frame0 = CCSpriteFrame.frame(
				masuoSSheet.getTexture(), CGRect.make(0, 0*280, 210, 280), CGPoint.ccp(0, 0));
		CCSpriteFrame frame1 = CCSpriteFrame.frame(
				masuoSSheet.getTexture(), CGRect.make(0, 1*280, 210, 280), CGPoint.ccp(0, 0));
		CCSpriteFrame frame2 = CCSpriteFrame.frame(
				masuoSSheet.getTexture(), CGRect.make(0, 2*280, 210, 280), CGPoint.ccp(0, 0));
		CCSpriteFrame frame3 = CCSpriteFrame.frame(
				masuoSSheet.getTexture(), CGRect.make(0, 3*280, 210, 280), CGPoint.ccp(0, 0));
		CCSpriteFrame frame4 = CCSpriteFrame.frame(
				masuoSSheet.getTexture(), CGRect.make(210, 0*280, 210, 280), CGPoint.ccp(0, 0));
		CCSpriteFrame frame5 = CCSpriteFrame.frame(
				masuoSSheet.getTexture(), CGRect.make(210, 1*280, 210, 280), CGPoint.ccp(0, 0));
		CCSpriteFrame frame6 = CCSpriteFrame.frame(
				masuoSSheet.getTexture(), CGRect.make(210, 2*280, 210, 280), CGPoint.ccp(0, 0));
		CCSpriteFrame frame7 = CCSpriteFrame.frame(
				masuoSSheet.getTexture(), CGRect.make(210, 3*280, 210, 280), CGPoint.ccp(0, 0));
		
		/////////////////////
		//アニメーション(立つ)
		CCAnimation animationStandup = CCAnimation.animation("animationStandup", 0.1f);
			animationStandup.addFrame(frame0);
			animationStandup.addFrame(frame1);
			animationStandup.addFrame(frame2);
			animationStandup.addFrame(frame3);
		animateStandup = CCAnimate.action(animationStandup, true);

		/////////////////////
		//アニメーション(座る)
		CCAnimation animationSitdown = CCAnimation.animation("animationSitdown", 0.1f);
			animationSitdown.addFrame(frame3);
			animationSitdown.addFrame(frame2);
			animationSitdown.addFrame(frame1);
			animationSitdown.addFrame(frame0);
		animateSitdown = CCAnimate.action(animationSitdown, true);
		
		/////////////////////
		//アニメーション(喜ぶ,座る)
		CCAnimation animationPlease = CCAnimation.animation("animationPlease", 0.1f);
			animationPlease.addFrame(frame4);//喜ぶ
			animationPlease.addFrame(frame5);
			animationPlease.addFrame(frame4);
			animationPlease.addFrame(frame5);
			animationPlease.addFrame(frame3);//座る
			animationPlease.addFrame(frame2);
			animationPlease.addFrame(frame1);
			animationPlease.addFrame(frame0);
		animatePlease = CCAnimate.action(animationPlease, true);
		
		/////////////////////
		//アニメーション(ガッカリ,座る)
		CCAnimation animationDisappoint = CCAnimation.animation("animationDisappoint", 0.1f);
			animationDisappoint.addFrame(frame6);//ガッカリ
			animationDisappoint.addFrame(frame7);
			animationDisappoint.addFrame(frame6);
			animationDisappoint.addFrame(frame7);
			animationDisappoint.addFrame(frame3);//座る
			animationDisappoint.addFrame(frame2);
			animationDisappoint.addFrame(frame1);
			animationDisappoint.addFrame(frame0);
		animateDisappoint = CCAnimate.action(animationDisappoint, true);
		
		/////////////////////
        //OKマークのスプライトシート
		CCSpriteSheet ssOK = CCSpriteSheet.spriteSheet("ok_mark.png", 100);
		layer.addChild(ssOK, 0, TAG_ITEM);
		
		CGRect rectOK = CGRect.make(0, 0, 130, 130);
		
		okSprite = new CCSprite();
		okSprite.setTexture(ssOK.getTexture());
		okSprite.setTextureRect(rectOK);
		
		okSprite.setScale(bw/(rect.size.width*2));//背景と同じスケールにする
		
		okSprite.setPosition((x+0.05f) * ptmRatio, (y-0.08f) * ptmRatio);
		okSprite.setVisible(false);//隠しておく
		
		layer.addChild(okSprite, baseLevel++);//重なり順の数字も上げておく
		
		/////////////////////
        //アイテムのスプライトシート
		CCSpriteSheet ssItem = CCSpriteSheet.spriteSheet("item_base.png", 100);
		layer.addChild(ssBack, 0, TAG_ITEM);
		
		int row = itemInd / 6;//行
		int col = itemInd % 6;//列
		Log.d(TAG, "itemInd[row_col]:" + itemInd + "[" + 200*row + "_" + 200*col + "]");
		
		CGRect rectItem = CGRect.make(200 * col, 200 * row, 200, 200);
		
		itemSprite = new CCSprite();
		itemSprite.setTexture(ssItem.getTexture());
		itemSprite.setTextureRect(rectItem);
		
		itemSprite.setScale(bw/rect.size.width);//背景と同じスケールにする
		
		itemSprite.setPosition(x * ptmRatio, (y+0.12f) * ptmRatio);
		itemSprite.setVisible(false);//隠しておく
		
		layer.addChild(itemSprite, baseLevel++);//重なり順の数字も上げておく
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
				rect.size.height/2);
		
		if(innerRect.contains(point.x, point.y) && touchable && !ok){
			return true;
		}else{
			return false;
		}
	}
	
	//立つ
	public void standup(){
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "standupDone");
		CCSequence  actions = CCSequence.actions(animateStandup, actionMoveDone);
		masuoSprite.runAction(actions);
		disableTouching();
	}
	
	//立った後の処理
	public void standupDone(Object sender){
		itemOn();
	}
	
	//座る
	public void sitdown(){
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "sitdownDone");
		CCSequence  actions = CCSequence.actions(animateSitdown, actionMoveDone);
		masuoSprite.runAction(actions);
	}
	
	//座った後の処理
	public void sitdownDone(Object sender){
		itemOff();
		enableTouching();
	}

	//喜ぶ
	public void please(float delay){
		Log.d(TAG, "please");
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "pleaseDone");
		CCSequence  please = CCSequence.actions(CCDelayTime.action(delay), animatePlease);
		CCSequence  actions = CCSequence.actions(please, actionMoveDone);
		masuoSprite.runAction(actions);
	}
	
	//喜んだ後の処理
	public void pleaseDone(Object sender){
		itemOff();
		enableTouching();
		enableOK();
	}
	
	//ガッカリ
	public void disappoint(float delay){
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "disappointDone");
		CCSequence  disappoint = CCSequence.actions(CCDelayTime.action(delay), animateDisappoint);
		CCSequence  actions = CCSequence.actions(disappoint, actionMoveDone);
		masuoSprite.runAction(actions);
	}
	
	//ガッカリ後の処理
	public void disappointDone(Object sender){
		itemOff();
		enableTouching();
		disableOK();
	}
	
	//選択できる状態に
	public void enableTouching(){
		touchable = true;
	}
	
	//選択出来ない状態に
	public void disableTouching(){
		touchable = false;
	}
	
	//選択できるかどうか
	public boolean isTouchable(){
		return touchable;
	}
	
	//クリアフラグをオンに
	public void enableOK(){
		ok = true;
		okSprite.setVisible(true);
	}
	
	//クリアフラグをオフに
	public void disableOK(){
		ok = false;
		okSprite.setVisible(false);
	}
	
	//クリアされてるかどうか
	public boolean isOK(){
		return ok;
	}
	
	/*
	 * アイテム
	 */
	//アイテムの出現
	private void itemOn(){
		itemSprite.setVisible(true);
	}
	
	//アイテムの削除
	private void itemOff(){
		itemSprite.setVisible(false);
	}
	
	//アイテムID
	public int getItemInd(){
		return itemInd;
	}
}
