package com.example.mnistandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }
    private static final String TAG = "MainActivity";
    private static final String MODEL_FILE = "file:///android_asset/graph_opt_nearest.pb"; //模型存放路径
    TextView txt;
    TextView tv;
    ImageView imageView;
    Bitmap bitmap;
    PredictionTF preTF;
    pafprocess pafPR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Example of a call to a native method
        tv = (TextView) findViewById(R.id.sample_text);
        txt=(TextView)findViewById(R.id.txt_id);
        imageView =(ImageView)findViewById(R.id.imageView1);
        bitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/sxy.jpg"));
        imageView.setImageBitmap(bitmap);
        preTF = new PredictionTF(getAssets(),MODEL_FILE);//输入模型存放路径，并加载TensoFlow模型
        pafPR = new pafprocess();
    }

    public void click01(View v){
        String res="预测结果为：";
        Result_out result= preTF.getPredict(bitmap);
//        for (int i=0;i<5;i++){
//            Log.i(TAG, res+result[i] );
//            res=res+String.valueOf(result[i])+" ";
//        }
//        Log.d();

//        pafPR.process_paf(216, 184, 19, result.peaks_outputs, 216,184, 19,  result.upsample_heatmat_outputs,216,184, 38, result.upsample_pafmat_outputs);
        pafPR.process_paf(184, 216, 19, result.peaks_outputs, 184, 216, 19,  result.upsample_heatmat_outputs,184, 216, 38, result.upsample_pafmat_outputs);
//        res=res+String.valueOf(result.peaks_outputs[1])+ " " + String.valueOf(result.upsample_heatmat_outputs[0]) + " "+ String.valueOf(result.upsample_pafmat_outputs[0]);
        res=res+ Integer.toString(pafPR.get_num_humans());
//        Log.d("qweqwe", result.peaks_outputs);
        txt.setText(res);
    }


}
