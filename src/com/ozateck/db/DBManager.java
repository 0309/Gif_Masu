package com.ozateck.db;

import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager{
	
	private static final String TAG = "myTag";
	
	private String dbName     = "masuo.db";
	private int    dbVersion  = 1;
	private String tableName  = "mytable";
	private String primaryKey = "myind";
	private String columns[]  = {
			"item0", "item1", "item2", "item3", "item4", "item5",
			"item6", "item7", "item8", "item9", "item10",
			"item11", "item12", "item13", "item14", "item15", "item16", "item17"};
	
	private DBHelper       dbHelper;
	private SQLiteDatabase db;
	
	public DBManager(Context context){
		//DB�I�u�W�F�N�g�擾
		dbHelper = new DBHelper(context, dbName, dbVersion, tableName);
		db       = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		db.close();
	}
	
	//DB�̏�����(�����l����)
	public void resetAll(){
		dbHelper.resetAll(db);
	}
	
	//DB�̏�����(�����l�ݒ�)
	public void initialize(){
		Log.d(TAG, "DBManager initialize");
		resetAll();
		try{
			List<String> itemData = new ArrayList<String>();
			for(int i=0; i<columns.length; i++){
				itemData.add("0");//"0"�Ƃ��Ă�������ȊO�̓A�N�e�B�u�ɂȂ�
			}
			insert(itemData);
		}catch(Exception e){
			Log.d(TAG, "initialize error." + e.toString());
		}
	}

	//�ǉ�
	public void insert(List<String> itemData){
		//ContentValues
		ContentValues values = new ContentValues();
		//values.put("myind", "");//�ǉ��̏ꍇ�Aprimary key �Ȃ̂ł����͔����Ă���
		
		for(int i=0; i<columns.length; i++){
			String str = itemData.get(i);
			values.put(columns[i], str);
		}
		
		//db
		db.insert(tableName, "", values);
	}

	//�X�V
	public void update(int myind, List<String> itemData) throws Exception{
		//ContentValues
		ContentValues values = new ContentValues();
		values.put(primaryKey,  new Integer(myind));
		
		for(int i=0; i<itemData.size(); i++){
			String str = itemData.get(i);
			values.put(columns[i], str);
		}
		
		//db
		db.update(tableName, values, "myind=" + myind, null);
	}
	
	//�폜
	public void delete(int myind) throws Exception{
		//db
		db.delete(tableName, primaryKey + "=" + myind, null);
	}
	
	//�������̎擾
	public int getTotalCount(){
		int count;
		Cursor cursor = db.query(tableName, columns, 
								 null, null, null, null, null);
		count = cursor.getCount();
		cursor.close();
		return count;
	}
	
	//�f�[�^�̃��X�g���擾
	public List<List<String>> getList(int limit){
		List<List<String>> itemList = new ArrayList<List<String>>();
		Cursor cursor = db.query(tableName, columns, 
				null, null, null, null, primaryKey + " DESC", ""+limit);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			List<String> itemData = new ArrayList<String>();
			for(int i=0; i<columns.length; i++){
				String str = cursor.getString(i);
				itemData.add(str);
			}
			
			itemList.add(itemData);
			cursor.moveToNext();
		}
		cursor.close();
		return itemList;
	}
}