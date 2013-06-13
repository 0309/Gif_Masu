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
 * �^�C�g��
 */

public class BannerItem{
	
	private static final String TAG = "myTag";

	private final CCLayer layer;
	private final int ptmRatio;
	private int baseLevel;//�d�Ȃ菇�̏����ʒu
	
	private CGPoint   cPoint;//�����_
	
	private CCSprite titleSprite;
	private List<CCSprite> spriteList;
	private float defX, defY, defSize;
	
	private static final int TAG_BACK  = 0;
	
	private ParticleHappy particleHappy;

	//x,y�̓��[�g�����Z
	public BannerItem(CCLayer layer, int ptmRatio, int baseLevel,
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
		
		spriteList = new ArrayList<CCSprite>();
		
		spriteList.add(makeSprite("t00.png")); spriteList.add(makeSprite("t01.png"));
		spriteList.add(makeSprite("t02.png")); spriteList.add(makeSprite("t03.png"));
		spriteList.add(makeSprite("t04.png")); spriteList.add(makeSprite("t05.png"));
		spriteList.add(makeSprite("t06.png")); spriteList.add(makeSprite("t07.png"));
		spriteList.add(makeSprite("t08.png")); spriteList.add(makeSprite("t09.png"));
		spriteList.add(makeSprite("t10.png")); spriteList.add(makeSprite("t11.png"));
		spriteList.add(makeSprite("t12.png")); spriteList.add(makeSprite("t13.png"));
		spriteList.add(makeSprite("t14.png")); spriteList.add(makeSprite("t15.png"));
		spriteList.add(makeSprite("t16.png")); spriteList.add(makeSprite("t17.png"));
		
        //�p�[�e�B�N��
        particleHappy = new ParticleHappy(layer, spriteList.size());
	}
	
	private CCSprite makeSprite(String filename){
		CCSprite backSprite = CCSprite.sprite(filename);
		
		CGRect rect = CGRect.make(0, 0, 550, 130);
		
		float bw = defSize * ptmRatio;//���[�g�����Z�ł̃s�N�Z����
		float bh = (rect.size.height / rect.size.width) * bw;
		
		backSprite.setScaleX(bw/rect.size.width);
		backSprite.setScaleY(bh/rect.size.height);
		
		backSprite.setPosition(defX * ptmRatio, defY * ptmRatio);
		
		layer.addChild(backSprite, baseLevel++);//�d�Ȃ菇�̐������グ�Ă���
		
		backSprite.setVisible(false);//��\���ɂ��Ă���
		
		return backSprite;
	}
	
	//�����_�𓾂�
	public CGPoint getCenterPoint(){
		return cPoint;
	}
	
	//�^�C�g���\��
	public void showTitle(int itemInd){
		
		titleSprite = spriteList.get(0);
		
		for(int i=0; i<spriteList.size(); i++){
			CCSprite sprite = spriteList.get(i);
			sprite.setPosition(defX*ptmRatio,
					defY*ptmRatio - titleSprite.getContentSize().height);//�����ʒu�ɖ߂��Ă���
			if(i == itemInd){
				sprite.setVisible(true);
				titleSprite = sprite;
			}else{
				sprite.setVisible(false);
			}
		}

		//���x�̌���
		float actualDuration = 0.05f;
		
		//�A�N�V�����̐ݒ�
		CCMoveTo actionShow = CCMoveTo.action(
				actualDuration,
				CGPoint.ccp(titleSprite.getPosition().x,
							defY*ptmRatio));
		CCMoveTo actionHide = CCMoveTo.action(
				actualDuration,
				CGPoint.ccp(titleSprite.getPosition().x,
							defY*ptmRatio + titleSprite.getContentSize().height));
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "showDone");
		CCSequence  actions = CCSequence.actions(actionShow, CCDelayTime.action(2.5f), actionHide, actionMoveDone);
		titleSprite.runAction(actions);
		
		//�p�[�e�B�N��
		particleHappy.emmit(CGPoint.ccp(defX*ptmRatio, defY*ptmRatio));
	}
	
	//�^�C�g���\����
	public void showDone(Object sender){
		titleSprite.setVisible(false);
	}
}
