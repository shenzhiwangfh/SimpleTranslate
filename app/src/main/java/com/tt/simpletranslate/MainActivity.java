package com.tt.simpletranslate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tt.simpletranslate.presenter.ITranslatePresenter;
import com.tt.simpletranslate.presenter.TranslatePresenter;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, BroadcastUtils.OnSendListener {

    private EditText mContent;
    private Button mTranslate;
    private TextView mResult;
    private Button cet4;
    private Button cet6;

    private String name;
    private String key;

    private BroadcastUtils utils;
    private ITranslatePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init logger
        Logger.addLogAdapter(new AndroidLogAdapter());

        name = getString(R.string.name);
        key = getString(R.string.key);

        mContent = findViewById(R.id.content);
        mTranslate = findViewById(R.id.translate);
        mResult = findViewById(R.id.result);
        cet4 = findViewById(R.id.cet4);
        cet6 = findViewById(R.id.cet6);

        mTranslate.setOnClickListener(this);
        cet4.setOnClickListener(this);
        cet6.setOnClickListener(this);

        utils = BroadcastUtils.getInstance(this);
        utils.register();
        utils.setOnSendListener(this);
        presenter = new TranslatePresenter(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        utils.unregister();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.translate:
                String word = mContent.getText().toString();
                if (word != null && !word.isEmpty()) {
                    presenter.translate(name, key, word);
                }
                break;
        }
    }

    @Override
    public void send(int id, Intent intent) {
        switch (id) {
            case BroadcastUtils.ID_TRANSLATION_RESULT:
                String result = intent.getStringExtra("result");
                mResult.setText(result);
                break;
        }
    }
}
