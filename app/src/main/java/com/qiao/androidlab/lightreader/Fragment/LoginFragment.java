package com.qiao.androidlab.lightreader.Fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qiao.androidlab.lightreader.Parts.User;
import com.qiao.androidlab.lightreader.R;
import com.qiao.androidlab.lightreader.RequestUtil.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * LoginFragment
 * <p/>
 * 登录
 *
 * @author: 乔云瑞
 * @time: 2016/3/12 9:35
 */
public class LoginFragment extends BaseFragment {

    private final static String TAG = "LoginFragment";
    private static final String LOGIN_STATE = "loginState"; //登录的标识符
    private static final String SHARED_PREFERENCE_SIGN = "com.juhezi.com";
    private static final String USER_SIGN = "userSign";
    private static final String USER_NAME = "username";
    private static final String USER_ID = "userid";
    private final String URL = "http://juhezi.applinzi.com/function/Login.php";
    private User user;
    private View rootView;
    private TextInputLayout username;
    private TextInputLayout password;
    private TextView register;
    private Button login;
    private ProgressBar mProgressBar;
    private String strUsername;
    private String strPassword;
    private String result;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x125) {
                mProgressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.i(TAG, jsonObject.toString());
                    if (jsonObject.get("code").equals("200")) {
                        showToast("登陆成功");
                        user = new User(Integer.parseInt(jsonObject.getJSONObject("data").get("uid").toString()), strUsername);
                        changeLogState(true, user);
                        turnToPerson(user);
                    } else if (jsonObject.get("code").equals("402")) {
                        showToast("登陆失败,密码不匹配");
                    } else {
                        showToast("登陆失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("登陆失败");
                }
            }
        }
    };
    private JSONObject jsonObject;


    /**
     * 配置TextInputLayout
     */
    public static void configureTextInputLayout(final TextInputLayout textInputLayout, final int length, final Button button) {
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < length) {
                    textInputLayout.setError("长度不能小于" + length + "个字符");
                    textInputLayout.setErrorEnabled(true); //显示错误星系
                    button.setClickable(false);
                } else {
                    textInputLayout.setErrorEnabled(false);
                    button.setClickable(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public String getTitle() {
        return "登录";
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        username = (TextInputLayout) rootView.findViewById(R.id.username);
        password = (TextInputLayout) rootView.findViewById(R.id.password);
        register = (TextView) rootView.findViewById(R.id.btn_register);
        login = (Button) rootView.findViewById(R.id.btn_login);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.login_progressBar);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
//        Log.i(TAG, "onStart");
        initEvent();

    }

    private void initEvent() {
        configureTextInputLayout(username, 6, login);  //账号和密码不能少于6位
        configureTextInputLayout(password, 6, login);
        login.setOnClickListener(new View.OnClickListener() {   //登录
            @Override
            public void onClick(View v) {
                login();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = LoginFragment
                        .this
                        .getActivity()
                        .getFragmentManager()
                        .beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new RegisterFragment());
                fragmentTransaction.commit();
            }
        });
    }

    private void login() {
        strUsername = username.getEditText().getText().toString();
        strPassword = password.getEditText().getText().toString();
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                result = HttpUtil.sendPostRequest(URL, "username=" + strUsername + "&password=" + strPassword);
//                Log.i(TAG, result);
                handler.sendEmptyMessage(0x125);
            }
        }.start();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 修改登录状态
     *
     * @param logState
     */
    private void changeLogState(Boolean logState, User user) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_SIGN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_STATE, true);
        if (user != null) {
            editor.putString(USER_NAME, user.getUsername());
            editor.putInt(USER_ID, user.getUid());
        }
        /*if (user != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(USER_SIGN, user);
        }*/
        editor.commit();
    }

    /**
     * 跳转到个人界面
     */
    private void turnToPerson(User user) {
        PersonFragment fragment = new PersonFragment();
//            fragment.setArguments(bundle);  //传入数据
        getActivity()
                .getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();

    }
}
