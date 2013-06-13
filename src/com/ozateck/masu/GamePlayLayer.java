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

public class GamePlayLayer extends CCLayer{
	
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

	//�򕌖쏡�j����
	private List<Masuo>   masuoList;
	private List<Integer> itemList;
	private Masuo masuoA, masuoB;
	
	//DBManager
	private DBManager dbManager;
	
	//�^�C�g��
	private BannerItem bannerItem;
	
	//�߂�{�^��
	private BackBtn backBtn;
	
	public GamePlayLayer(Context context){
		
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

        //�X�e�[�W����
        makeStage();
        
		//BGM�ĊJ
		SoundEngine.sharedEngine().playSound(context, R.raw.bgm_play, true);
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
        
        //���쏡�j
        ArrayList<Integer> allList = new ArrayList<Integer>();
        allList.add(Masuo.ITEM_MASU);
        allList.add(Masuo.ITEM_MIZU);
        allList.add(Masuo.ITEM_SENBEI);
        allList.add(Masuo.ITEM_GIFUJO);
        allList.add(Masuo.ITEM_UKAI);
        allList.add(Masuo.ITEM_KAKI);
        allList.add(Masuo.ITEM_UCHIWA);
        allList.add(Masuo.ITEM_CHOCHIN);
        allList.add(Masuo.ITEM_EDAMAME);
        allList.add(Masuo.ITEM_GUJO);
        allList.add(Masuo.ITEM_WASHI);
        allList.add(Masuo.ITEM_UDATSU);
        allList.add(Masuo.ITEM_SHIRAKAWA);
        allList.add(Masuo.ITEM_HIDAGYU);
        allList.add(Masuo.ITEM_GERO);
        allList.add(Masuo.ITEM_MINOYAKI);
        allList.add(Masuo.ITEM_KURIKINTON);
        Collections.shuffle(allList);
        
        //allList����6��ނ����I������itemList�ɓ����
        itemList  = new ArrayList<Integer>();
        for(int i=0; i<6; i++){
        	itemList.add(allList.get(i));
        	itemList.add(allList.get(i));
        }
        Collections.shuffle(itemList);
        
        //���쏡�j
        masuoList = new ArrayList<Masuo>();
        
        //1�i��
        Masuo masuo0 = new Masuo(this, ptmRatio, 1,
        		0.17f, 0.37f, 0.15f, itemList.get(0));
        masuoList.add(masuo0);
        
        Masuo masuo1 = new Masuo(this, ptmRatio, 1,
        		0.37f, 0.37f, 0.15f, itemList.get(1));
        masuoList.add(masuo1);

        Masuo masuo2 = new Masuo(this, ptmRatio, 1,
        		0.57f, 0.37f, 0.15f, itemList.get(2));
        masuoList.add(masuo2);

        Masuo masuo3 = new Masuo(this, ptmRatio, 1,
        		0.77f, 0.37f, 0.15f, itemList.get(3));
        masuoList.add(masuo3);
        
        //2�i��
        Masuo masuo4 = new Masuo(this, ptmRatio, 1,
        		0.27f, 0.25f, 0.15f, itemList.get(4));
        masuoList.add(masuo4);
        
        Masuo masuo5 = new Masuo(this, ptmRatio, 1,
        		0.47f, 0.25f, 0.15f, itemList.get(5));
        masuoList.add(masuo5);
        
        Masuo masuo6 = new Masuo(this, ptmRatio, 1,
        		0.67f, 0.25f, 0.15f, itemList.get(6));
        masuoList.add(masuo6);
        
        Masuo masuo7 = new Masuo(this, ptmRatio, 1,
        		0.87f, 0.25f, 0.15f, itemList.get(7));
        masuoList.add(masuo7);
        
        //3�i��
        Masuo masuo8 = new Masuo(this, ptmRatio, 1,
        		0.17f, 0.15f, 0.15f, itemList.get(8));
        masuoList.add(masuo8);
        
        Masuo masuo9 = new Masuo(this, ptmRatio, 1,
        		0.37f, 0.15f, 0.15f, itemList.get(9));
        masuoList.add(masuo9);
        
        Masuo masuo10 = new Masuo(this, ptmRatio, 1,
        		0.57f, 0.15f, 0.15f, itemList.get(10));
        masuoList.add(masuo10);
        
        Masuo masuo11 = new Masuo(this, ptmRatio, 1,
        		0.77f, 0.15f, 0.15f, itemList.get(11));
        masuoList.add(masuo11);
        
        //�^�C�g��(�d�Ȃ菇��30����)
        bannerItem = new BannerItem(this, ptmRatio, 30,
        							0.5f, 0.45f, 0.6f);
        
        //�߂�{�^��
        backBtn = new BackBtn(this, ptmRatio, 1,
        						0.06f,
        						WORLD_HEIGHT_METER-0.06f, 0.08f);
	}
	
	@Override
	public void onExit(){
		super.onExit();
		//BGM��~
		SoundEngine.sharedEngine().pauseSound();
		
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
		}else{
			checkMasuo(point);
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
	
	//�S�Ă�I���\��
	public void setEnableTouchingAll(){
		for(Masuo masuo:masuoList){
			masuo.enableTouching();
		}
	}
	
	//�S�Ă�I��s�\��
	public void setDisableTouchingAll(){
		for(Masuo masuo:masuoList){
			masuo.disableTouching();
		}
	}
	
	//���j����̃^�b�`�C�x���g�`�F�b�N
	private void checkMasuo(CGPoint point){
		//�^�b�`�o����󋵂��e�X�g
		int count = 0;
		for(Masuo masuo:masuoList){
			if(!masuo.isTouchable()) count++;
		}
		
		//�L������2�ȏ㗧���Ă��鎞�͐G��Ȃ�
		if(count < 2){
			for(int i=masuoList.size()-1; i>=0; i--){
				Masuo masuo = masuoList.get(i);
				if(masuo.isInside(point) && masuo.isTouchable()){

					if(masuoA == null && masuoB == null){
						masuoA = masuo;//masuoA�ɓ����
						masuoA.standup();
					}else{
						masuoB = masuo;//masuoB�ɓ����
						masuoB.standup();
					}
					
					if(masuoA != null && masuoB != null){
						//A��B���r����
						if(masuoA.getItemInd() == masuoB.getItemInd()){
							Log.d(TAG, "����:���@�������胂�[�V�����̌�ɍ���");
							masuoA.please(1.5f);
							masuoB.please(1.5f);
							bannerItem.showTitle(masuoA.getItemInd());//������^�C�g��
							SoundEngine.sharedEngine().playEffect(context, R.raw.effect_success);
							//DB�A�b�v�f�[�g
							checkDB(masuoA.getItemInd());
						}else{
							Log.d(TAG, "����:�~�@���O�ꃂ�[�V�����̌�ɍ���");
							masuoA.disappoint(0.8f);
							masuoB.disappoint(0.8f);
						}
						masuoA = null;
						masuoB = null;
						
						break;
					}
				}
			}
		}
	}
	
	//DB�A�b�v�f�[�g
	private void checkDB(int itemInd){
		
		//update���K�v�����ׂ�
		boolean updateFlg = false;
		List<String> itemData = dbManager.getList(1).get(0);
		for(int i=0; i<itemData.size(); i++){
			if(i == itemInd){
				if(itemInd != Integer.valueOf(itemData.get(i))){
					itemData.set(i, "1");//"1"�̓A�N�e�B�u
					updateFlg = true;
				}
			}
		}
		
		//DB�A�b�v�f�[�g
		if(updateFlg){
			try{
				dbManager.update(1, itemData);
			}catch(Exception e){
				Log.e(TAG, "update �Ɏ��s���܂����B:" + e.toString());
			}
		}
	}
	
	private void gameTop(){
		//gameTop��
		CCScene scene = CCScene.node();
		CCLayer layer = new GameTopLayer(context);
		scene.addChild(layer);
		CCDirector.sharedDirector().replaceScene(scene);
	}
}
