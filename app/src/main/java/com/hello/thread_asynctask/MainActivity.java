package com.hello.thread_asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends Activity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.textView1);

        findViewById(R.id.read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadURL("http://www.baidu.com");

            }


        });
    }

    private void ReadURL(String url) {

        /**
         * 异步方法
         * arg:
         */
        new AsyncTask<String,Float,String>(){

            @Override
            protected String doInBackground(String... params) {

                try {

                    URL url = new URL(params[0]);//网址
                    URLConnection connection = url.openConnection();//打开

                    //读取当前任务的全部长度
                    long total = connection.getContentLength();

                    InputStream inputStream = connection.getInputStream();//数据流
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);//读取数据流
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);//缓冲

                    String line; //存放一行数据
                    StringBuilder builder = new StringBuilder(); //存放所有字符串数据

                    while ((line = bufferedReader.readLine()) != null){ //当前行不为空
                        builder.append(line); //添加一行
                        //任务执行进度 获取进度的百分比
                        publishProgress((float)builder.toString().length()/total);


                    }

                    bufferedReader.close();
//                    inputStreamReader.close();
                    inputStream.close();

                    return builder.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            /**
             * 以下是回调方法
             * UI 操作也是在回调方法中操作
             * 1 onPreExecute 执行完，再执行onPostExecute
             */
            @Override
            protected void onPreExecute() {

                Toast.makeText(MainActivity.this,"开始读取",Toast.LENGTH_SHORT).show();
                super.onPreExecute();
            }

            @Override
            protected void onProgressUpdate(Float... values) {

                System.err.println(values[0]);

                super.onProgressUpdate(values);
            }

            /**
             * doInBackground 调用的回调方法，更新UI
             * @param s
             */
            @Override
            protected void onPostExecute(String s) {
                text.setText(s);

                super.onPostExecute(s);
            }

            /**
             * 向外发布当前任务执行的进度
             * @param
             */



            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute(url);
    }
}
