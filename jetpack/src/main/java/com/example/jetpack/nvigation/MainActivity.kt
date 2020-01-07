package com.example.jetpack.nvigation

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation.findNavController
import com.example.jetpack.R
import com.framework.common.base_mvp.BaseActivity
import com.framework.common.base_mvp.BasePresenter

//Activity中 mWindow.getLayoutInflater().setPrivateFactory(this);
class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_navigation_main
    }

    override fun bindData() {
    }

    override fun initEvent() {
    }

    override fun getPresenter(): BasePresenter<*>? {
        return null
    }

    //Activity将它的 back键点击事件的委托出去
    override fun onSupportNavigateUp() =
            findNavController(this, R.id.my_nav_host_fragment).navigateUp()

    companion object {
        fun startMe(context: Context){
            var intent = Intent(context,MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private fun isAppRunningForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcessList = activityManager.runningAppProcesses ?: return false

        Log.e("realxz", "running app process list size is ${runningAppProcessList.size}")
        runningAppProcessList.forEach {
            Log.e(
                    "realxz",
                    "running app process name is ${it.processName} and importance is ${it.importance}"
            )
            if (it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && it.processName == context.applicationInfo.processName
            ) {
                return true
            }
        }
        return false
    }

    private fun moveAppToFront(context: Context) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.getRunningTasks(10)
        for (taskInfo: ActivityManager.RunningTaskInfo in runningTasks) {
            if (taskInfo.topActivity!!.packageName == context.packageName) {
                //activityManager.moveTaskToFront(taskInfo.id, 0)
                break
            }
        }
    }

    private fun moveAppToFrontt(context: Context) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (activityManager != null) {
            val runningTasks:List<ActivityManager.RunningTaskInfo> = activityManager.getRunningTasks(10)
            var cn: ComponentName? = null
            val info:ActivityManager.RunningTaskInfo = runningTasks[0]
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                cn = info.topActivity
            }
            if (cn!!.packageName == context.packageName) {
                //                activityManager.moveTaskToFront(taskInfo.id, 0)
                //                break
            }
        }
    }
}
