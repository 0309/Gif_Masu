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

public class ParticleHappy{
	
	private CCLayer layer;
	private int depth;
	private CCParticleSystem emitter;
	
	public ParticleHappy(CCLayer layer, int depth){
		this.layer = layer;
		this.depth = depth;
	}
	
	public void emmit(CGPoint point){
		emitter = new CCQuadParticleSystem(40);
		layer.addChild(emitter, depth);
		emitter.setTexture(CCTextureCache.sharedTextureCache().addImage("particle_star.png"));
		
		// duration
		//emitter.setDuration(CCParticleSystem.kCCParticleDurationInfinity);//forever
		emitter.setDuration(1.0f);

		// Set "Gravity" mode (default one)
		emitter.setEmitterMode(CCParticleSystem.kCCParticleModeGravity);

		// Gravity mode: gravity
		emitter.setGravity(CGPoint.zero());

		// Gravity mode: speed of particles
		emitter.setSpeed(160);
		emitter.setSpeedVar(20);

		// Gravity mode: radial
		emitter.setRadialAccel(-120);
		emitter.setRadialAccelVar(0);

		// Gravity mode: tagential
		emitter.setTangentialAccel(30);
		emitter.setTangentialAccelVar(0);

		// emitter position
		emitter.setPosition(point);
		emitter.setPosVar(CGPoint.zero());

		// angle
		emitter.setAngle(90);
		emitter.setAngleVar(360);

		// life of particles
		emitter.setLife(2);
		emitter.setLifeVar(1);

		// spin of particles
		emitter.setStartSpin(0);
		emitter.setStartSpinVar(0);
		emitter.setEndSpin(0);
		emitter.setEndSpinVar(2000);

		// color of particles
		ccColor4F startColor = new ccColor4F(0.5f, 0.5f, 0.5f, 1.0f);
		emitter.setStartColor(startColor);

		ccColor4F startColorVar = new ccColor4F(0.5f, 0.5f, 0.5f, 1.0f);
		emitter.setStartColorVar(startColorVar);

		ccColor4F endColor = new ccColor4F(0.1f, 0.1f, 0.1f, 0.2f);
		emitter.setEndColor(endColor);

		ccColor4F endColorVar = new ccColor4F(0.1f, 0.1f, 0.1f, 0.2f);	
		emitter.setEndColorVar(endColorVar);

		// size, in pixels
		emitter.setStartSize(30.0f);
		emitter.setStartSizeVar(00.0f);
		emitter.setEndSize(CCParticleSystem.kCCParticleStartSizeEqualToEndSize);

		// emits per second
		emitter.setEmissionRate(emitter.getTotalParticles()/emitter.getLife());

		// additive
		emitter.setBlendAdditive(false);
	}
}
