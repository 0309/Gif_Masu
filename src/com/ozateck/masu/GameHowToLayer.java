package com.ozateck.masu;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import java.lang.Math;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.MotionEvent;
import android.util.Log;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4F;
import org.cocos2d.config.ccMacros;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.particlesystem.CCParticleSystem;
import org.cocos2d.particlesystem.CCQuadParticleSystem;

import com.ozateck.db.DBManager;

public class GameHowToLayer extends CCLayer{
	
	private static final String TAG = "myTag";
	
	private Context context;
	
	//mWorld�Ŏg�p�����ʂ̉��T�C�Y�͈��(�P�ʂ̓��[�g��)
	//���j�^�̉��T�C�Y����ɂ��āA���[�g���P�ʂŐ��䂷��B
	protected static final float WORLD_WIDTH_METER = 1.0f;
	private float WORLD_HEIGHT_METER = 0.0f;
	
	//���j�^�T�C�Y
	private CGSize  dispSize;
	//1���[�g���ɂ����s�N�Z����
	private int     ptmRatio;
	//���j�^�̒��S�_
	private CGPoint cPoint;

	//DBManager
	private DBManager dbManager;
	
	//�^�C�g��
	private HowToTitle collectionTitle;

	//�߂�{�^��
	private BackBtn backBtn;

	//�ڍ׃p�l��
	private HowToPanel howtoPanel;
	
	public GameHowToLayer(Context context){
		
		this.context = context;
		
		//�^�b�`�A�N�V������L���ɂ���
		setIsTouchEnabled(true);
		
		//���j�^�T�C�Y���m��
		dispSize = CCDirector.sharedDirector().winSize();
		
		//���j�^�T�C�Y�̊m���AWORLD_HEIGHT_METER���m��
		WORLD_HEIGHT_METER = WORLD_WIDTH_METER * (dispSize.height/dispSize.width);

		//1���[�g���ɂ����s�N�Z�������m��
		ptmRatio = (int)(dispSize.width / WORLD_WIDTH_METER);
		
		//���j�^�̒��S�_���m��
		cPoint = CCDirector.sharedDirector().convertToGL(
				CGPoint.make(dispSize.width/2, dispSize.height/2));

		Log.d(TAG, "WORLD_WIDTH_METER:" + WORLD_WIDTH_METER);
		Log.d(TAG, "mSize:" + dispSize.width + "_" + dispSize.height);
		Log.d(TAG, "ptmRatio:" + ptmRatio);
		
		//DBManager
		dbManager = new DBManager(context);
		if(dbManager.getTotalCount() == 0){
			dbManager.initialize();
		}
	}
	
	@Override
	public void onEnter(){
		super.onEnter();
		//BGM�ĊJ
		//SoundEngine.sharedEngine().playSound(context, R.raw.bgm_play, true);
		
        //�X�e�[�W����
        makeStage();
	}

	//�X�e�[�W����
	private void makeStage(){

		//�w�i�̃X�v���C�g�V�[�g(��ʈ�t�ɍL����)
		CCSprite bgSprite = CCSprite.sprite("back_game.png", 
								CGRect.make(0, 0, 800, 480));
		bgSprite.setScaleX(dispSize.width  / 800);
		bgSprite.setScaleY(dispSize.height / 480);
        bgSprite.setPosition(CGPoint.make(dispSize.width/2, dispSize.height/2));
        addChild(bgSprite, 0);
        
        //�^�C�g��
        collectionTitle = new HowToTitle(this, ptmRatio, 1, 0.5f, 0.5f, 0.4f);

        //�߂�{�^��
        backBtn = new BackBtn(this, ptmRatio, 1,
        						0.06f,
        						WORLD_HEIGHT_METER-0.06f, 0.08f);

        //�ڍ׃p�l��
        howtoPanel = new HowToPanel(this, ptmRatio, 2, 0.5f, 0.3f, 0.8f);
        howtoPanel.showPanel();
	}
	
	@Override
	public void onExit(){
		super.onExit();
		//BGM��~
		//SoundEngine.sharedEngine().pauseSound();
		
		//DB
		dbManager.close();
		
	}
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event){

		//�^�b�`���ꂽ���W
		CGPoint point = 
				CCDirector.sharedDirector().convertToGL(
						CGPoint.make(event.getX(), event.getY()));

		if(backBtn.isInside(point)){
			backBtn.on();
			SoundEngine.sharedEngine().playEffect(context, R.raw.effect_btn);
		}
		
		return CCTouchDispatcher.kEventHandled;
	}
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event){
		
		//�^�b�`���ꂽ���W
		CGPoint point = 
				CCDirector.sharedDirector().convertToGL(
						CGPoint.make(event.getX(), event.getY()));
		
		if(backBtn.isInside(point)){
			gameTop();
		}
		backBtn.off();
		
		return CCTouchDispatcher.kEventHandled;
	}

	private void gameTop(){
		//gameTop��
		CCScene scene = CCScene.node();
		CCLayer layer = new GameTopLayer(context);
		scene.addChild(layer);
		CCDirector.sharedDirector().replaceScene(scene);
	}
}
