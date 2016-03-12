package com.qiao.androidlab.lightreader.Fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
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
import android.widget.TextView;

import com.qiao.androidlab.lightreader.R;

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

    private View rootView;
    private TextInputLayout username;
    private TextInputLayout password;
    private TextView register;
    private Button login;

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
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        initEvent();


    }

    private void initEvent() {
        configureTextInputLayout(username, 6, login);  //账号和密码不能少于6位
        configureTextInputLayout(password, 6, login);
        login.setOnClickListener(new View.OnClickListener() {   //登录
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Login");
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "register");
                FragmentTransaction fragmentTransaction = LoginFragment
                        .this
                        .getActivity()
                        .getFragmentManager()
                        .beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new RegisterFragment());
                fragmentTransaction.addToBackStack("login");
                fragmentTransaction.commit();
            }
        });
    }
}
