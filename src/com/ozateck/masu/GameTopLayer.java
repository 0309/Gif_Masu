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

public class GameTopLayer extends CCLayer{
	
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
	
	//���S
	private LogoTitle logo;
	
	//�X�^�[�g�{�^��,�R���N�V�����{�^��,�����т����{�^��
	private StartBtn startBtn;
	private CollectionBtn collectionBtn;
	private HowToBtn howtoBtn;
	
	public GameTopLayer(Context context){
		
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
        
        //���S
        logo = new LogoTitle(this, ptmRatio, 1,
        							0.5f, 0.41f, 0.8f);
        
        //�X�^�[�g�{�^��,�R���N�V�����{�^��,�����т����{�^��
        startBtn = new StartBtn(this, ptmRatio, 1,
        						0.5f, 0.24f, 0.3f);
        collectionBtn = new CollectionBtn(this, ptmRatio, 1,
								0.5f, 0.16f, 0.3f);
        howtoBtn = new HowToBtn(this, ptmRatio, 1,
								0.5f, 0.08f, 0.3f);
	}
	
	@Override
	public void onExit(){
		super.onExit();
		//BGM��~
		//SoundEngine.sharedEngine().pauseSound();
	}
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event){

		//�^�b�`���ꂽ���W
		CGPoint point = 
				CCDirector.sharedDirector().convertToGL(
						CGPoint.make(event.getX(), event.getY()));
		
		if(startBtn.isInside(point)){
			startBtn.on();
			SoundEngine.sharedEngine().playEffect(context, R.raw.effect_btn);
		}else if(collectionBtn.isInside(point)){
			collectionBtn.on();
			SoundEngine.sharedEngine().playEffect(context, R.raw.effect_btn);
		}else if(howtoBtn.isInside(point)){
			howtoBtn.on();
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

		if(startBtn.isInside(point)){
			gamePlay();
		}else if(collectionBtn.isInside(point)){
			gameCollection();
		}else if(howtoBtn.isInside(point)){
			gameHowto();
		}
		startBtn.off();
		collectionBtn.off();
		howtoBtn.off();
		
		return CCTouchDispatcher.kEventHandled;
	}
	
	private void gamePlay(){
		//gamePlay��
		CCScene scene = CCScene.node();
		CCLayer layer = new GamePlayLayer(context);
		scene.addChild(layer);
		CCDirector.sharedDirector().replaceScene(scene);
	}

	private void gameCollection(){
		//gameCollection��
		CCScene scene = CCScene.node();
		CCLayer layer = new GameCollectionLayer(context);
		scene.addChild(layer);
		CCDirector.sharedDirector().replaceScene(scene);
	}
	
	private void gameHowto(){
		//gameHowto��
		CCScene scene = CCScene.node();
		CCLayer layer = new GameHowToLayer(context);
		scene.addChild(layer);
		CCDirector.sharedDirector().replaceScene(scene);
	}
}
