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

public class GameCollectionLayer extends CCLayer{
	
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
	private CollectionTitle collectionTitle;

	//�߂�{�^��
	private BackBtn backBtn;
	
	//�ڍ׃p�l��
	private CollectionPanel collectionPanel;
	
	//����{�^��
	private CloseBtn closeBtn;
	
	//�X�R�A���x��
	private CCLabel scoreLabel;
	
	//�X�R�A�A�C�e��
	private List<CollectionItem> items;
	
	public GameCollectionLayer(Context context){
		
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
        collectionTitle = new CollectionTitle(this, ptmRatio, 1, 0.5f, 0.5f, 0.4f);

        //�߂�{�^��
        backBtn = new BackBtn(this, ptmRatio, 1,
        						0.06f,
        						WORLD_HEIGHT_METER-0.06f, 0.08f);
        
        //�ڍ׃p�l��
        collectionPanel = new CollectionPanel(this, ptmRatio, 2, 0.5f, 0.3f, 0.8f);
        collectionPanel.hidePanel();//��\��
        
        //����{�^��
        closeBtn = new CloseBtn(this, ptmRatio, 3,
								0.86f,
								WORLD_HEIGHT_METER-0.07f, 0.09f);
        closeBtn.hideBtn();//��\��
        
        //DBManager����f�[�^���擾
        List<List<String>> itemList = dbManager.getList(1);
        
        List<String> itemData = itemList.get(0);
        
        if(itemData.size() > 0){
        	
        	items = new ArrayList<CollectionItem>();
        	
            //�A�C�R��
            //1�i��
            CollectionItem  item1 = new CollectionItem(this, ptmRatio, 1,
            		0.15f, 0.37f, 0.135f, 1, Integer.valueOf(itemData.get(1)));
            items.add(item1);
            CollectionItem  item2 = new CollectionItem(this, ptmRatio, 1,
    				0.29f, 0.37f, 0.135f, 2, Integer.valueOf(itemData.get(2)));
            items.add(item2);
            CollectionItem  item3 = new CollectionItem(this, ptmRatio, 1,
    				0.43f, 0.37f, 0.135f, 3, Integer.valueOf(itemData.get(3)));
            items.add(item3);
            CollectionItem  item4 = new CollectionItem(this, ptmRatio, 1,
    				0.57f, 0.37f, 0.135f, 4, Integer.valueOf(itemData.get(4)));
            items.add(item4);
            CollectionItem  item5 = new CollectionItem(this, ptmRatio, 1,
    				0.71f, 0.37f, 0.135f, 5, Integer.valueOf(itemData.get(5)));
            items.add(item5);
            CollectionItem  item6 = new CollectionItem(this, ptmRatio, 1,
    				0.85f, 0.37f, 0.135f, 6, Integer.valueOf(itemData.get(6)));
            items.add(item6);
            
            //2�i��
            CollectionItem  item7 = new CollectionItem(this, ptmRatio, 1,
            		0.15f, 0.225f, 0.135f, 7, Integer.valueOf(itemData.get(7)));
            items.add(item7);
            CollectionItem  item8 = new CollectionItem(this, ptmRatio, 1,
    				0.29f, 0.225f, 0.135f, 8, Integer.valueOf(itemData.get(8)));
            items.add(item8);
            CollectionItem  item9 = new CollectionItem(this, ptmRatio, 1,
    				0.43f, 0.225f, 0.135f, 9, Integer.valueOf(itemData.get(9)));
            items.add(item9);
            CollectionItem  item10 = new CollectionItem(this, ptmRatio, 1,
    				0.57f, 0.225f, 0.135f, 10, Integer.valueOf(itemData.get(10)));
            items.add(item10);
            CollectionItem  item11 = new CollectionItem(this, ptmRatio, 1,
    				0.71f, 0.225f, 0.135f, 11, Integer.valueOf(itemData.get(11)));
            items.add(item11);
            CollectionItem  item12 = new CollectionItem(this, ptmRatio, 1,
    				0.85f, 0.225f, 0.135f, 12, Integer.valueOf(itemData.get(12)));
            items.add(item12);
            
            //3�i��
            CollectionItem  item13 = new CollectionItem(this, ptmRatio, 1,
            		0.15f, 0.08f, 0.135f, 13, Integer.valueOf(itemData.get(13)));
            items.add(item13);
            CollectionItem  item14 = new CollectionItem(this, ptmRatio, 1,
    				0.29f, 0.08f, 0.135f, 14, Integer.valueOf(itemData.get(14)));
            items.add(item14);
            CollectionItem  item15 = new CollectionItem(this, ptmRatio, 1,
    				0.43f, 0.08f, 0.135f, 15, Integer.valueOf(itemData.get(15)));
            items.add(item15);
            CollectionItem  item16 = new CollectionItem(this, ptmRatio, 1,
    				0.57f, 0.08f, 0.135f, 16, Integer.valueOf(itemData.get(16)));
            items.add(item16);
            CollectionItem  item17 = new CollectionItem(this, ptmRatio, 1,
    				0.71f, 0.08f, 0.135f, 17, Integer.valueOf(itemData.get(17)));
            items.add(item17);
            
    		/////////////////////
    		//���x���̒ǉ�
    		//dw=144�Ńt�H���g�T�C�Y30(��������12����1)
    		int txSize = (int)(dispSize.width/18);
    		int total = itemData.size()-1;
    		int score = 0;
    		for(int i=0; i<itemData.size(); i++){
    			if(Integer.valueOf(itemData.get(i)) == 1){
    				score++;
    			}
    		}
    		scoreLabel = CCLabel.makeLabel(score + "/" + total, "Pollyanna.ttf", txSize);
    		scoreLabel.setColor(ccColor3B.ccBLACK);
    		scoreLabel.setAnchorPoint(1.0f, 0.5f);
    		scoreLabel.setPosition((WORLD_WIDTH_METER-0.05f) * ptmRatio,
    							   (WORLD_HEIGHT_METER-0.07f) * ptmRatio);
    		addChild(scoreLabel, 1);
        }
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
		
		if(closeBtn.isInside(point)){
			closeBtn.on();
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
		
		//�R���N�V�����ɐG�������ǂ���
		if(!collectionPanel.getVisible()){
			for(int i=0; i<items.size(); i++){
				CollectionItem item = items.get(i);
				if(item.isInside(point) && item.isActive()){
					collectionPanel.showPanel(item.getItemInd());
					closeBtn.showBtn();//�{�^����\��
				}
			}
		}
		
		if(closeBtn.isInside(point)){
			collectionPanel.hidePanel();
			closeBtn.hideBtn();//�{�^���\��
		}
		closeBtn.off();
		
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
