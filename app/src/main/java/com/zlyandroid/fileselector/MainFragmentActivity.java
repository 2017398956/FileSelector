package com.zlyandroid.fileselector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.zlylib.fileselectorlib.FileSelector;
import com.zlylib.fileselectorlib.ui.FileSelectorFragment;

import java.util.List;

public class MainFragmentActivity extends FragmentActivity {

    private FileSelectorFragment fileSelectorFragment ;
    private boolean isShowingFileSelectorFragment = false;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainFragmentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container , new OneFragment())
                .addToBackStack(OneFragment.class.getSimpleName())
                .commitAllowingStateLoss();
    }

    public void showFileSelectorFragment(){
        if(fileSelectorFragment == null){
            fileSelectorFragment = new FileSelectorFragment();
            fileSelectorFragment.setOnFilesSelectedListener(filePaths -> {
                Log.d("NFL" , "paths:" + filePaths);
                getSupportFragmentManager().beginTransaction().remove(fileSelectorFragment)
                        .commitAllowingStateLoss();
                fileSelectorFragment = null;
            });
            FileSelector.from(fileSelectorFragment)
                    // .onlyShowFolder()  //只显示文件夹
                    //.onlySelectFolder()  //只能选择文件夹
                    // .isSingle() // 只能选择一个
                    .setMaxCount(1) //设置最大选择数
                    .setFileTypes("png", "jpg", "doc", "apk", "mp3", "gif", "txt", "mp4", "zip") //设置文件类型
                    .setSortType(FileSelector.BY_NAME_ASC) //设置名字排序
                    .done();
            ;
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container , fileSelectorFragment)
                .addToBackStack(FileSelectorFragment.class.getSimpleName())
                .commitAllowingStateLoss();
        isShowingFileSelectorFragment = true;
    }

    @Override
    public void onBackPressed() {
        if (isShowingFileSelectorFragment){
            if (fileSelectorFragment.onBackPressed()){
                // do nothing
            }else{
                // FileSelectorFragment 没有触发回退事件，则调用 activity 的回退事件，该事件会使 FileSelectorFragment 被销毁
                super.onBackPressed();
                isShowingFileSelectorFragment = false ;
            }
        }else{
            super.onBackPressed();
        }
    }
}
