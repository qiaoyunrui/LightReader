package com.qiao.androidlab.lightreader.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qiao.androidlab.lightreader.Parts.User;
import com.qiao.androidlab.lightreader.R;
import com.qiao.androidlab.lightreader.RequestUtil.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * PersonFragment
 *
 * @author: 乔云瑞
 * @time: 2016/3/12 17:00
 */
public class PersonFragment extends BaseFragment {

    private static final String USER_NAME = "username";
    private static final String USER_ID = "userid";
    private static final String LOGIN_STATE = "loginState"; //登录的标识符
    private static final String SHARED_PREFERENCE_SIGN = "com.juhezi.com";
    private static final String TAG = "PersonFragment";

    private final String URL = "http://juhezi.applinzi.com/function/queryPerson.php";

    private View rootView;
    private TextView username;
    private TextView count;
    private Button logout;
    private ProgressBar mProgressBar;

    private Bundle mBundle;
    private String result;
    private JSONObject mJSONObject;
    private User mUser;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x126) {
                mProgressBar.setVisibility(View.INVISIBLE);
                try {
                    mJSONObject = new JSONObject(result);
                    Log.i(TAG, mJSONObject.toString());
                    if (mJSONObject.get("code").equals("200")) {
                        count.setText(mJSONObject.getJSONObject("data").get("COUNT(uid)").toString());
                    } else {
                        showToast("未知错误");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_person, container, false);
        username = (TextView) rootView.findViewById(R.id.person_tv_username);
        logout = (Button) rootView.findViewById(R.id.person_btn_logout);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.person_progressBar);
        count = (TextView) rootView.findViewById(R.id.person_count);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        initEvent();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mProgressBar.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_SIGN, Context.MODE_PRIVATE);
        mUser = new User(sharedPreferences.getInt(USER_ID, 0), sharedPreferences.getString(USER_NAME, "居合子"));
        username.setText(mUser.getUsername());
        new Thread() {
            @Override
            public void run() {
                result = HttpUtil.sendPostRequest(URL, "uid=" + mUser.getUid());
                Log.i(TAG, result);
                mHandler.sendEmptyMessage(0x126);
            }
        }.start();
    }

    /**
     * 初始化监听事件
     */
    private void initEvent() {

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_SIGN, Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = sharedPreferences.edit();
                mEditor.putBoolean(LOGIN_STATE, false);
                mEditor.commit();
                getActivity()
                        .getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, new LoginFragment())
                        .commit();
            }
        });

    }

    @Override
    public String getTitle() {
        return "个人";
    }

    private void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
