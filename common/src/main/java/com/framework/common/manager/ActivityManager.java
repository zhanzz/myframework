package com.framework.common.manager;

import android.app.Activity;

import com.framework.common.utils.ListUtils;

import java.util.LinkedList;
import java.util.List;
/**
 * 
 * 名称：AbActivityManager.java
 * 描述：用于处理退出程序时可以退出所有的activity，而编写的通用类
 */
public class ActivityManager {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static ActivityManager instance;

	private ActivityManager() {
	}

	/**
	 * 单例模式中获取唯一的ActivityManager实例.
	 * @return
	 */
	public static ActivityManager getInstance() {
		if (null == instance) {
			instance = new ActivityManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到容器中.
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	/**
	 * 移除Activity从容器中.
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	public List<Activity> getActivityList(){
		if (ListUtils.isEmpty(activityList)){
			return null;
		}
		return  activityList;
	}

	/**
	 * 遍历所有Activity并finish.
	 */
	public void clearAllActivity() {
		for (Activity activity : activityList) {
			if(activity!=null){
				activity.finish();
			}
		}
	}

	/**
	 * @author zhenggl
	 * @date 2016/4/15 22:07
	 * @desc 获取当前的activity
	*/
	public Activity getCurrent_1Activity(){
		if(null != activityList && !activityList.isEmpty()){
			Activity activity = activityList.get(activityList.size() - 2);
			return activity;
		}
		return null;
	}

	/**
	 * 获取最后一个页面
	 * @return
	 */
	public Activity getLastActivity(){
		if(!ListUtils.isEmpty(activityList)){
			Activity activity = activityList.get(activityList.size()-1);
			return activity;
		}
		return null;
	}

	public int getActivityNum(){
		return activityList.size();
	}

	public boolean isContainsActivity(String className){
		boolean result = false;
		if(!ListUtils.isEmpty(activityList)){
			for(Activity activity : activityList){
				if(activity.getClass().getSimpleName().equals(className)){
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 列表倒数第二是不是 className
	 */
	public boolean lastIsThisActivity(String className){
		boolean result = false;
		if(!ListUtils.isEmpty(activityList)&&activityList.size()>1){
			Activity activity = activityList.get(activityList.size()-2);
			if(activity.getClass().getSimpleName().equals(className)){
				result = true;
			}
		}
		return result;
	}

	public boolean isBackground(){
		return ListUtils.isEmpty(activityList);
	}
}