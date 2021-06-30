package com.dream.annotation_api.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.dream.annotation_ann.model.RouteMeta;
import com.dream.annotation_api.core.RouterMap;
import com.dream.annotation_api.template.IFieldHelper;
import com.dream.annotation_api.template.IRouterMap;
import com.dream.annotation_api.utils.ClassUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.dream.annotation_api.Consts.Contants.GENERATE_FILE_PATH;
import static com.dream.annotation_api.Consts.Contants.GENERATE_HELPER_PATH;

public class Router {
    private static final String TAG = "Router";
    private HashMap<String,String> mFields = new HashMap<>();

    private static String mPath;
    private Class<?> dst;

    private Router() {
    }

    public static Router getInstance(){
        return Holder.router;
    }
    private static class Holder{
        static Router router = new Router();
    }


    public static void init(Context context){
        try {
            Set<String> fileSet = ClassUtils.getFileNameByPackageName(context,GENERATE_FILE_PATH);
            for (String str :
                    fileSet) {
                if (str.equals(GENERATE_FILE_PATH + ".XRouterMap")){
                   ((IRouterMap)Class.forName(str).getConstructor().newInstance()).loadInto(RouterMap.map);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public Router setPath(String path){
        this.mPath = path;
        return this;
    }

    public Router setField(String key,String obj){
        mFields.put(key,obj);
        return this;
    }

    public void navigation(Context context){
            RouteMeta meta = RouterMap.map.get(mPath);
            if (null != meta){
                Class<?> clazz = meta.getDestination();
                Intent t = new Intent(context,clazz);
                for (Map.Entry<String, String> entry:mFields.entrySet()){
                    t.putExtra(entry.getKey(),entry.getValue());
                }
                context.startActivity(t);
            }else{
                Log.e(TAG, "navigation: check if dst registered or not");
            }
    }

    public boolean validParam(){
        if (null == mPath){
            Log.e(TAG, "navigation: 请指明path");
            return false;
        }

        return true;
    }

    public void inject(Object obj){
        String name = GENERATE_HELPER_PATH;
        name += ".";
        name += obj.getClass().getSimpleName();
        name += "Helper";
        try {
            ((IFieldHelper)(Class.forName(name).getConstructor().newInstance())).inject(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
