package com.qiao.androidlab.lightreader.ClickListeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidlab.qiao.guillotineview.animtor.GuillotineAnimtor;
import com.qiao.androidlab.lightreader.Activities.MainActivity;
import com.qiao.androidlab.lightreader.Fragment.BaseFragment;
import com.qiao.androidlab.lightreader.Fragment.FindFragment;
import com.qiao.androidlab.lightreader.Fragment.LoginFragment;
import com.qiao.androidlab.lightreader.Fragment.MapFragment;
import com.qiao.androidlab.lightreader.Fragment.PersonFragment;
import com.qiao.androidlab.lightreader.R;

/**
 * MenuSelect
 *
 * @author: 乔云瑞
 * @time: 2016/3/11 9:serious
 * <p/>
 * 菜单选项选择处理事件
 */
public class MenuSelect {

    private static final String TAG = "MenuSelect";
    private static final String LOGIN_STATE = "loginState"; //登录的标识符
    private static final String SHARED_PREFERENCE_SIGN = "com.juhezi.com";
    private Intent mIntent;

    private Context mContext;
    private Activity mActivity;
    private GuillotineAnimtor mGuillotineAnimtor;

    public MenuSelect(Context context, Activity activity, GuillotineAnimtor guillotineAnimtor) {
        this.mContext = context;
        this.mActivity = activity;
        this.mGuillotineAnimtor = guillotineAnimtor;
    }

    /**
     * 处理事件
     */
    public void handle(int id) {

        if (id == R.id.btn_menu_main) {
            closeFragment();    //关闭Fragment
            changeMenuColor(id);    //改变菜单颜色
            closeGull(mGuillotineAnimtor);  //关闭铡刀动画
            return;
        }


        if (checkLoginState()) { //已经登录
            changeMenuColor(id);    //改变菜单颜色
            switch (id) {
                case R.id.btn_menu_person:
                    openFragment(new PersonFragment());
                    break;
                case R.id.btn_menu_find:
                    openFragment(new FindFragment());
                    break;
                case R.id.btn_menu_map:
                    openFragment(new MapFragment());
                    break;
            }

        } else {    //还没有登录,打开登录fragment,
            showToast("请先登录");
            openFragment(new LoginFragment());
        }
        closeGull(mGuillotineAnimtor);  //最后再关闭动画
    }

    void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 检查是否登录
     *
     * @return
     */
    public boolean checkLoginState() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCE_SIGN, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(LOGIN_STATE, false);
//        return false;
    }

    /**
     * 改变菜单颜色
     */
    private void changeMenuColor(int id) {

        int[] ids = new int[]{R.id.btn_menu_main, R.id.btn_menu_find, R.id.btn_menu_person, R.id.btn_menu_map};
        for (int mId : ids) {
//            ((TextView) mActivity.findViewById(id)).setClickable(true);
            //把所有的颜色变成黑色
            ((TextView) mActivity.findViewById(mId)).setTextColor(mContext.getResources().getColor(R.color.primary_text));
        }
        //将点击的颜色变为橙色
        ((TextView) mActivity.findViewById(id)).setTextColor(mContext.getResources().getColor(R.color.accent));
//        ((TextView) mActivity.findViewById(id)).setClickable(false);
    }

    /**
     * 关闭铡刀菜单
     */
    private void closeGull(GuillotineAnimtor guillotineAnimtor) {
        if (guillotineAnimtor != null) {
            //使用关闭动画
            guillotineAnimtor.close();
        }
    }

    /**
     * 打开fragment
     *
     * @param fragment
     */
    private void openFragment(BaseFragment fragment) {
        mActivity.findViewById(R.id.fragment).setVisibility(View.VISIBLE);
        mActivity.getFragmentManager().
                beginTransaction().
                replace(R.id.fragment, fragment).
                commit();
        ((TextView) mActivity.findViewById(R.id.toolbarTitle)).setText(fragment.getTitle());
    }

    /**
     * 关闭fragment，表现为按首页键
     */
    private void closeFragment() {
        if (mActivity.findViewById(R.id.fragment).getVisibility() == View.VISIBLE) {
            mActivity.findViewById(R.id.fragment).setVisibility(View.INVISIBLE);  //将fragment隐藏
            mActivity.getFragmentManager()
                    .beginTransaction()
                    .remove(mActivity.getFragmentManager().findFragmentById(R.id.fragment))
                    .commit(); //删除fragment
            ((TextView) mActivity.findViewById(R.id.toolbarTitle)).setText("首页");
        }
    }

}
