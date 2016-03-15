package com.qiao.androidlab.lightreader.Fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.qiao.androidlab.lightreader.R;
import com.qiao.androidlab.lightreader.RequestUtil.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * RegisterFragment
 *
 * @author: 乔云瑞
 * @time: 2016/3/12 11:09
 */
public class RegisterFragment extends BaseFragment {

    private final static String TAG = "RegisterFragment";

    private String result;
    private View rootView;
    private TextInputLayout username;
    private TextInputLayout password;
    private TextInputLayout passwordRepeat;
    private Button register;
    private ProgressBar mProgressBar;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x124) {
                mProgressBar.setVisibility(View.INVISIBLE);
//                Log.i(TAG, HttpUtil.getJsonString(result));
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.i(TAG, jsonObject.toString());
                    if (jsonObject.get("code").equals("200")) {
                        showToast("注册成功");
                        FragmentTransaction fragmentTransaction
                                = getActivity().getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, new LoginFragment());
                        fragmentTransaction.commit();
                    } else if (jsonObject.get("code").equals("401")) {
                        showToast("注册失败，用户名已存在");
                    } else {
                        showToast("注册失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "转换json数据失败");
                    showToast("注册失败");
                }
            }
        }
    };

    private RequestQueue queue;
    private String url = "http://juhezi.applinzi.com/function/register.php";

    private String strUsername;
    private String strPassword;

    private static void comparePassword
            (final TextInputLayout passwordRepeat, final TextInputLayout password, final Button button) {

        passwordRepeat.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                /*Log.i(TAG, "%" + s.toString());
                Log.i(TAG, "#" + password.getEditText().getText().toString());*/

                if (!(s.toString()).equals(password.getEditText().getText().toString())) {//两个密码不一致
                    passwordRepeat.setError("密码不一致");
                    passwordRepeat.setErrorEnabled(true);
                    button.setClickable(false);
                } else {
                    passwordRepeat.setErrorEnabled(false);
                    button.setClickable(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        username = (TextInputLayout) rootView.findViewById(R.id.register_username);
        password = (TextInputLayout) rootView.findViewById(R.id.register_password);
        passwordRepeat = (TextInputLayout) rootView.findViewById(R.id.register_password_repeat);
        register = (Button) rootView.findViewById(R.id.register_register);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.register_progressBar);
        ((TextView) this.getActivity().findViewById(R.id.toolbarTitle)).setText(getTitle());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public String getTitle() {
        return "注册";
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();

    }

    private void initView() {
        LoginFragment.configureTextInputLayout(username, 6, register);
        LoginFragment.configureTextInputLayout(password, 6, register);
        comparePassword(passwordRepeat, password, register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //注册
                register();
            }
        });
    }

    private void register() {
        strUsername = username.getEditText().getText().toString();
        strPassword = password.getEditText().getText().toString();
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {

                result = HttpUtil.sendPostRequest(url, "username=" + strUsername + "&password=" + strPassword);
//                Log.i(TAG, result);
                handler.sendEmptyMessage(0x124);
            }
        }.start();


    }

    private void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
