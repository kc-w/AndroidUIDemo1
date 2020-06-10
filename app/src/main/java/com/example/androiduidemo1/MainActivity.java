package com.example.androiduidemo1;

import android.content.ContentResolver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;


public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //读取登录历史信息userinfo
        preferences = this.getSharedPreferences("userinfo", MODE_PRIVATE);
        editor = preferences.edit();


        editText = findViewById(R.id.edit1);
        button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

    }

    /**
     * 图文详情页面选择图片
     */
    public void getImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }



    String imageurl="";
    @Override//获取到该图片并调用接口将图片上传到服务器，上传成功以后获取到服务器返回的该图片的url
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {

            if(requestCode==1){

            }

            if(requestCode==2){
                ContentResolver resolver = getContentResolver();
                // 获得图片的uri
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri));
                    bitmap = zoomImage(bitmap,1000,1000);

                    File file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);

                    Log.e("创建的临时文件存放地址", file.getAbsolutePath() );
                    //发出请求
                    okhttpDate(file);

                    //将图片加入到编辑器
                    addTextImage(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }




        }
    }




    //设置图片缩放
    public Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    public void addTextImage(Bitmap bitmap){
        // 根据Bitmap对象创建ImageSpan对象
        ImageSpan imageSpan = new ImageSpan(this, bitmap);


        // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        String tempUrl = "<img src=\"" + imageurl + "\" />";
        SpannableString spannableString = new SpannableString(tempUrl);
        // 用ImageSpan对象替换你指定的字符串添加到edit输入框中
        spannableString.setSpan(imageSpan, 0, tempUrl.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        //得到可编辑的文本对象
        Editable edit_text = editText.getEditableText();
        // 获取光标所在位置
        int index = editText.getSelectionStart();

        //装入字符
        SpannableString newLine = new SpannableString("\n");
        //插入换行符
        edit_text.insert(index, newLine);


        //判断光标的位置
        if (index < 0 || index >= edit_text.length()) {
            edit_text.append(spannableString);
        } else {
            // 将选择的图片追加到EditText中光标所在位置
            edit_text.insert(index, spannableString);
        }
        //插入图片后换行
        edit_text.insert(index, newLine);
        Log.e("插入的图片地址" , spannableString.toString() );

    }


    public void okhttpDate(final File file) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                //得到存储的sessionid
                String sessionid= preferences.getString("sessionid","null");

                //创建一个OkHttpClient对象
                OkHttpClient client=new OkHttpClient();

                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(
                                "img",
                                file.getName(),
                                RequestBody.create(MediaType.parse("image/jpg"),file)
                        );
                //使用 MultipartBody 构建 RequestBody
                RequestBody requestBody = builder.build();
                // 传入 RequestBody
                Request request = new Request.Builder()
                        .addHeader("cookie",sessionid)
                        .url("http://192.168.1.163:8080/FileServlet")
                        .post(requestBody)
                        .build();

                Call call = client.newCall(request);
                //执行请求,并产生回调
                call.enqueue(new Callback() {
                    @Override//回调失败
                    public void onFailure(Call call, IOException e) {
                        ToastMeaagge("网络异常");
                    }

                    @Override//回调成功
                    public void onResponse(Call call, Response response) throws IOException {
                        String data=response.body().string();
                        if("上传图片失败".equals(data)){
                            ToastMeaagge(data);
                        }else {
                            imageurl=data;
                            ToastMeaagge("图片上传成功");
                        }



                    }

                });

            }
        }).start();
    }

    //提示网络异常
    public void ToastMeaagge(String msg){
        Looper.prepare();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }








}