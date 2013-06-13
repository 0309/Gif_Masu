package com.ozateck.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	private String dbName;
	private int    dbVersion;
	private String tableName;
	
	public DBHelper(Context context, String dbName, int dbVersion, String tableName){
		super(context, dbName, null, dbVersion);
		
		this.dbName    = dbName;
		this.dbVersion = dbVersion;
		this.tableName = tableName;
	}
	
	//DB�̐���
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL("create table if not exists " +
					tableName + "(myind integer primary key, " +
							"item0 integer, " +
							"item1 integer, item2 integer, item3 integer, item4 integer, item5 integer, " +
							"item6 integer, item7 integer, item8 integer, item9 integer, item10 integer, " +
							"item11 integer, item12 integer, item13 integer, item14 integer, item15 integer, " +
							"item16 integer, item17 integer)");
	}
	
	//DB�̃A�b�v�O���[�h
	@Override
	public void onUpgrade(SQLiteDatabase db,
							int oldVersion, int newVersion){
		db.execSQL("drop table if exists " + tableName);
		onCreate(db);
	}
	
	//DB�̃��Z�b�g
	public void resetAll(SQLiteDatabase db){
		db.execSQL("drop table if exists " + tableName);
		onCreate(db);
	}
}