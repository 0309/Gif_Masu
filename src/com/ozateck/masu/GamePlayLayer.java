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
	
	//mWorldで使用する画面の横サイズは一定(単位はメートル)
	//モニタの横サイズを基準にして、メートル単位で制御する。
	protected static final float WORLD_WIDTH_METER = 1.0f;
	private float WORLD_HEIGHT_METER = 0.0f;
	
	//モニタサイズ
	private CGSize  dispSize;
	//1メートルにつき何ピクセルか
	private int     ptmRatio;
	//モニタの中心点
	private CGPoint cPoint;

	//岐阜野升男さん
	private List<Masuo>   masuoList;
	private List<Integer> itemList;
	private Masuo masuoA, masuoB;
	
	//DBManager
	private DBManager dbManager;
	
	//タイトル
	private BannerItem bannerItem;
	
	//戻るボタン
	private BackBtn backBtn;
	
	public GamePlayLayer(Context context){
		
		this.context = context;
		
		//タッチアクションを有効にする
		setIsTouchEnabled(true);
		
		//モニタサイズを確定
		dispSize = CCDirector.sharedDirector().winSize();
		
		//モニタサイズの確定後、WORLD_HEIGHT_METERを確定
		WORLD_HEIGHT_METER = WORLD_WIDTH_METER * (dispSize.height/dispSize.width);
		
		//1メートルにつき何ピクセルかを確定
		ptmRatio = (int)(dispSize.width / WORLD_WIDTH_METER);
		
		//モニタの中心点を確定
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

        //ステージ生成
        makeStage();
        
		//BGM再開
		SoundEngine.sharedEngine().playSound(context, R.raw.bgm_play, true);
	}
	
	//ステージ生成
	private void makeStage(){

		//背景のスプライトシート(画面一杯に広げる)
		CCSprite bgSprite = CCSprite.sprite("back_game.png", 
								CGRect.make(0, 0, 800, 480));
		bgSprite.setScaleX(dispSize.width  / 800);
		bgSprite.setScaleY(dispSize.height / 480);
        bgSprite.setPosition(CGPoint.make(dispSize.width/2, dispSize.height/2));
        addChild(bgSprite, 0);
        
        //升野升男
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
        
        //allListから6種類だけ選抜してitemListに入れる
        itemList  = new ArrayList<Integer>();
        for(int i=0; i<6; i++){
        	itemList.add(allList.get(i));
        	itemList.add(allList.get(i));
        }
        Collections.shuffle(itemList);
        
        //升野升男
        masuoList = new ArrayList<Masuo>();
        
        //1段目
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
        
        //2段目
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
        
        //3段目
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
        
        //タイトル(重なり順は30から)
        bannerItem = new BannerItem(this, ptmRatio, 30,
        							0.5f, 0.45f, 0.6f);
        
        //戻るボタン
        backBtn = new BackBtn(this, ptmRatio, 1,
        						0.06f,
        						WORLD_HEIGHT_METER-0.06f, 0.08f);
	}
	
	@Override
	public void onExit(){
		super.onExit();
		//BGM停止
		SoundEngine.sharedEngine().pauseSound();
		
		//DB
		dbManager.close();
	}
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event){

		//タッチされた座標
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
		
		//タッチされた座標
		CGPoint point = 
				CCDirector.sharedDirector().convertToGL(
						CGPoint.make(event.getX(), event.getY()));
		
		if(backBtn.isInside(point)){
			gameTop();
		}
		backBtn.off();
		
		return CCTouchDispatcher.kEventHandled;
	}
	
	//全てを選択可能に
	public void setEnableTouchingAll(){
		for(Masuo masuo:masuoList){
			masuo.enableTouching();
		}
	}
	
	//全てを選択不能に
	public void setDisableTouchingAll(){
		for(Masuo masuo:masuoList){
			masuo.disableTouching();
		}
	}
	
	//升男さんのタッチイベントチェック
	private void checkMasuo(CGPoint point){
		//タッチ出来る状況かテスト
		int count = 0;
		for(Masuo masuo:masuoList){
			if(!masuo.isTouchable()) count++;
		}
		
		//キャラが2個以上立っている時は触れない
		if(count < 2){
			for(int i=masuoList.size()-1; i>=0; i--){
				Masuo masuo = masuoList.get(i);
				if(masuo.isInside(point) && masuo.isTouchable()){

					if(masuoA == null && masuoB == null){
						masuoA = masuo;//masuoAに入れる
						masuoA.standup();
					}else{
						masuoB = masuo;//masuoBに入れる
						masuoB.standup();
					}
					
					if(masuoA != null && masuoB != null){
						//AとBを比較する
						if(masuoA.getItemInd() == masuoB.getItemInd()){
							Log.d(TAG, "判定:○　※当たりモーションの後に座る");
							masuoA.please(1.5f);
							masuoB.please(1.5f);
							bannerItem.showTitle(masuoA.getItemInd());//当たりタイトル
							SoundEngine.sharedEngine().playEffect(context, R.raw.effect_success);
							//DBアップデート
							checkDB(masuoA.getItemInd());
						}else{
							Log.d(TAG, "判定:×　※外れモーションの後に座る");
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
	
	//DBアップデート
	private void checkDB(int itemInd){
		
		//updateが必要か調べる
		boolean updateFlg = false;
		List<String> itemData = dbManager.getList(1).get(0);
		for(int i=0; i<itemData.size(); i++){
			if(i == itemInd){
				if(itemInd != Integer.valueOf(itemData.get(i))){
					itemData.set(i, "1");//"1"はアクティブ
					updateFlg = true;
				}
			}
		}
		
		//DBアップデート
		if(updateFlg){
			try{
				dbManager.update(1, itemData);
			}catch(Exception e){
				Log.e(TAG, "update に失敗しました。:" + e.toString());
			}
		}
	}
	
	private void gameTop(){
		//gameTopへ
		CCScene scene = CCScene.node();
		CCLayer layer = new GameTopLayer(context);
		scene.addChild(layer);
		CCDirector.sharedDirector().replaceScene(scene);
	}
}
