package com.example.proxydemo;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.proxydemo.annotion.ContentView;
import com.example.proxydemo.annotion.OnClick;
import com.example.proxydemo.annotion.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
  @ViewInject(R.id.tv)
  TextView mTextView;

  @Override
  protected void onResume() {
    super.onResume();
    mTextView.setText("我已经被findViewById了");
  }

  @OnClick(value = {R.id.tv})
  public void click(View view) {
    Log.i("TAG", "click: ");
  }
}