package com.google.paly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DownloadInfoDb extends SQLiteOpenHelper {

	public DownloadInfoDb(Context context) {
		super(context, "download.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//TODO
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
