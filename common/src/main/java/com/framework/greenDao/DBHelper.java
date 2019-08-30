package com.framework.greenDao;

import android.content.Context;
import com.framework.common.BaseApplication;
import org.greenrobot.greendao.async.AsyncSession;
/**
 * Created by ltx on 2017/12/20.
 */
public class DBHelper {
    private static String DB_NAME = "test";
    private volatile static DaoSession session = null;
    private volatile static AsyncSession asyncSession = null;
    /**
     * DaoSession单例
     * @return
     */
    public static DaoSession getSessionInstance() {
        if (session == null) {
            synchronized (DBHelper.class) {
                if (session == null) {
                    Context appContext = BaseApplication.getApp();
                    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(appContext, DB_NAME, null);
                    DaoMaster master = new DaoMaster(helper.getWritableDatabase());
                    session = master.newSession();
                }
            }
        }
        return session;
    }

    /**
     * AsyncSession单例
     * @return
     */
    public static AsyncSession getAsyncSessionInstance() {
        if (asyncSession == null)
            synchronized (DBHelper.class) {
                if (asyncSession == null) {
                    if (session == null) {
                        session = getSessionInstance();
                    }
                    asyncSession = session.startAsyncSession();
                }
            }
        return asyncSession;
    }
}