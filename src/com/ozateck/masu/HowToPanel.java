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
 * �����т����̏ڍ�
 */

public class HowToPanel{

	private static final String TAG = "myTag";

	private final CCLayer layer;
	private final int ptmRatio;
	private int baseLevel;//�d�Ȃ菇�̏����ʒu
	
	private CGPoint   cPoint;//�����_

	private CCSprite panelSprite = null;
	
	private float defX, defY, defSize;
	
	//x,y�̓��[�g�����Z
	public HowToPanel(CCLayer layer, int ptmRatio, int baseLevel,
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
		panelSprite = makeSprite("howto_panel.png");//�V�����p�l���𐶐�����
	}

	private CCSprite makeSprite(String filename){
		CCSprite backSprite = CCSprite.sprite(filename);
		
		CGRect rect = backSprite.getBoundingBox();
		
		float bw = defSize * ptmRatio;//���[�g�����Z�ł̃s�N�Z����
		float bh = (rect.size.height / rect.size.width) * bw;
		
		backSprite.setScaleX(bw/rect.size.width);
		backSprite.setScaleY(bh/rect.size.height);
		
		backSprite.setPosition(defX * ptmRatio, defY * ptmRatio);
		
		backSprite.setVisible(false);//��\���ɂ��Ă���
		
		return backSprite;
	}

	//�����_�𓾂�
	public CGPoint getCenterPoint(){
		return cPoint;
	}
	
	//�ڍ׃p�l���\��
	public void showPanel(){
		if(!panelSprite.getVisible()){
			panelSprite = makeSprite("howto_panel.png");//�V�����p�l���𐶐�����
			panelSprite.setVisible(true);
			layer.addChild(panelSprite, baseLevel);
		}
	}
}
