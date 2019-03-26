package com.sh3h.localprovider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sh3h.localprovider.dao.DaoMaster;
import com.sh3h.localprovider.dao.PersonDao;
import com.sh3h.localprovider.dao.TaskDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by LiMeng on 2018/2/27.
 */

public class MyDevOpenHelper extends DaoMaster.DevOpenHelper{
    public MyDevOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MyDevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                boolean isExistPlatformRoles = checkColumnExist(db,
                        PersonDao.TABLENAME, PersonDao.Properties.PlatformRoles.columnName);
                if (isExistPlatformRoles){
                    return;
                }

                db.beginTransaction();//开始事务
                try {
                    db.execSQL("ALTER TABLE " + PersonDao.TABLENAME + " ADD COLUMN "
                            + PersonDao.Properties.PlatformRoles.columnName + " TEXT;");
                    db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                } finally {
                    db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
                }
                onUpgrade(db, ++oldVersion, newVersion);
                break;
            case 2:
                boolean isExistAcceptName = checkColumnExist(db,
                        TaskDao.TABLENAME, TaskDao.Properties.AcceptName.columnName);
                if (isExistAcceptName){
                    return;
                }

                db.beginTransaction();//开始事务
                try {
                    db.execSQL("ALTER TABLE " + TaskDao.TABLENAME + " ADD COLUMN "
                            + TaskDao.Properties.AcceptName.columnName + " TEXT;");
                    db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                } finally {
                    db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
                }
                onUpgrade(db, ++oldVersion, newVersion);
                break;
            default:
                break;
        }
    }

    private static boolean checkColumnExist(Database db, String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            //查询一行
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0", null);
            result = cursor != null && cursor.getColumnIndex(columnName) != -1;
        } catch (Exception e) {
            Log.e("", "checkColumnExists..." + e.getMessage());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return result;
    }

}
