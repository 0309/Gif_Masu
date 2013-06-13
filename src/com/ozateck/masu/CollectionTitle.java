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
 * �R���N�V�����̃^�C�g��
 */

public class CollectionTitle{
	
	private static final String TAG = "myTag";

	private final CCLayer layer;
	private final int ptmRatio;
	private int baseLevel;//�d�Ȃ菇�̏����ʒu
	
	private CGPoint   cPoint;//�����_
	
	private CCSprite backSprite;
	
	private static final int TAG_BACK  = 0;

	//x,y�̓��[�g�����Z
	public CollectionTitle(CCLayer layer, int ptmRatio, int baseLevel,
						float x, float y, float size){
		
		this.layer     = layer;
		this.ptmRatio  = ptmRatio;
		this.baseLevel = baseLevel;
		
		cPoint = CGPoint.make(x, y);
		
		init(x, y, size);
	}

	private void init(float x, float y, float size){

		/////////////////////
        //�w�i�̃X�v���C�g�V�[�g
		CCSpriteSheet ssBack = CCSpriteSheet.spriteSheet("collection_title.png", 100);
		layer.addChild(ssBack, 0, TAG_BACK);
		
		CGRect rect = CGRect.make(0, 0, 430, 80);
		
		backSprite = new CCSprite();
		backSprite.setTexture(ssBack.getTexture());
		backSprite.setTextureRect(rect);

		float bw = size * ptmRatio;//���[�g�����Z�ł̃s�N�Z����
		float bh = (rect.size.height / rect.size.width) * bw;
		
		backSprite.setScaleX(bw/rect.size.width);
		backSprite.setScaleY(bh/rect.size.height);
		
		backSprite.setPosition(x * ptmRatio, y * ptmRatio);
		
		layer.addChild(backSprite, baseLevel++);//�d�Ȃ菇�̐������グ�Ă���
	}
	
	//�����_�𓾂�
	public CGPoint getCenterPoint(){
		return cPoint;
	}
}