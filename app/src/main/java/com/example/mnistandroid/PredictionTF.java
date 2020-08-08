package com.example.mnistandroid;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import java.util.Collection;
import java.util.ArrayList;
import android.graphics.BitmapFactory;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

public class PredictionTF {
    private static final String TAG = "PredictionTF";
    //设置模型输入/输出节点的数据维度
    private static final int IN_COL = 368;
    private static final int IN_ROW = 432;

    private static final int OUT_COL = 216;
    private static final int OUT_ROW = 184;
    private static final int OUT_CHENNEL = 19;
    //模型中输入变量的名称
    private static final String inputName = "image";
    //模型中输出变量的名称
    private static final String peaks_name = "peaks";
    private static final String upsample_heatmat_name = "upsample_heatmat";
    private static final String upsample_pafmat_name = "upsample_pafmat";

    TensorFlowInferenceInterface inferenceInterface;
    static {
        //加载libtensorflow_inference.so库文件
        System.loadLibrary("tensorflow_inference");
        Log.e(TAG,"libtensorflow_inference.so库加载成功");
    }

    PredictionTF(AssetManager assetManager, String modePath) {
        //初始化TensorFlowInferenceInterface对象
        inferenceInterface = new TensorFlowInferenceInterface(assetManager,modePath);
        Log.e(TAG,"TensoFlow模型文件加载成功");
    }

    /**
     *  利用训练好的TensoFlow模型预测结果
     * @param bitmap 输入被测试的bitmap图
     * @return 返回预测结果，int数组
     */
    public Result_out getPredict(Bitmap bitmap) {
        float[] inputdata = bitmapToFloatArray(bitmap,432, 368);
        Log.i(TAG,"inputdata:"+ Float.toString(inputdata[3]) + " " + Float.toString(inputdata[4])+ " " + Float.toString(inputdata[5]));
        //将数据feed给tensorflow的输入节点
        inferenceInterface.feed(inputName, inputdata,1,IN_COL, IN_ROW,3);
        //运行tensorflow
        String[] outputNames = new String[] {peaks_name,upsample_heatmat_name,upsample_pafmat_name};
        inferenceInterface.run(outputNames);
        ///获取输出节点的输出信息
        float[] peaks_outputs = new float[OUT_COL*OUT_ROW*OUT_CHENNEL];
        float[] upsample_heatmat_outputs = new float[OUT_COL*OUT_ROW*OUT_CHENNEL];
        float[] upsample_pafmat_outputs = new float[OUT_COL*OUT_ROW*OUT_CHENNEL*2]; //用于存储模型的输出数据

        inferenceInterface.fetch(peaks_name, peaks_outputs);
        inferenceInterface.fetch(upsample_heatmat_name, upsample_heatmat_outputs);
        inferenceInterface.fetch(upsample_pafmat_name, upsample_pafmat_outputs);

//        Log.d()
        Log.d("outputs[0]:",Float.toString(upsample_heatmat_outputs[0]));
//        Collection<float[]> result = new ArrayList<float[]>();
//        result.add(peaks_outputs);
//        result.add(upsample_heatmat_outputs);
//        result.add(upsample_pafmat_outputs);
        Result_out result_out = new Result_out();
        result_out.peaks_outputs = peaks_outputs;
        result_out.upsample_heatmat_outputs = upsample_heatmat_outputs;
        result_out.upsample_pafmat_outputs = upsample_pafmat_outputs;
        return result_out;
    }

    /**
     * 将bitmap转为（按行优先）一个float数组，并且每个像素点都归一化到0~1之间。
     * @param bitmap 输入被测试的bitmap图片
     * @param rx 将图片缩放到指定的大小（列）->28
     * @param ry 将图片缩放到指定的大小（行）->28
     * @return   返回归一化后的一维float数组 ->28*28
     */
    public static float[] bitmapToFloatArray(Bitmap bitmap, int rx, int ry){
//        int height = bitmap.getHeight();
//        int width = bitmap.getWidth();
//        // 计算缩放比例
//        float scaleWidth = ((float) rx) / width;
//        float scaleHeight = ((float) ry) / height;
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
//        Log.i(TAG,"bitmap width:"+bitmap.getWidth()+",height:"+bitmap.getHeight());
//        Log.i(TAG,"bitmap.getConfig():"+bitmap.getConfig());
//        height = bitmap.getHeight();
//        width = bitmap.getWidth();

//        FileInputStream fs = new FileInputStream("file:///android_asset/sxy.jpg");
//        Bitmap bitmap_sxy  = BitmapFactory.decodeStream(fs);

//        Bitmap bitmap_sxy=BitmapFactory.decodeStream(PredictionTF.class.getResourceAsStream("file:///android_asset/sxy.jpg"));
//        Log.i(TAG,"bitmap_sxy :"+bitmap_sxy);
//        Log.i(TAG,"bitmap width:"+bitmap.getWidth()+",height:"+bitmap.getHeight());
//        Log.i(TAG,"bitmap.getConfig():"+bitmap.getConfig());

        int[] intValues = new int[432*368];
        float[] floatValues = new float[432*368*3];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
//        Log.i(TAG,"intValues:"+String.valueOf(-999));
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
//            floatValues[i * 3 + 0] = ((val >> 16) & 0xFF);//10 3
//            floatValues[i * 3 + 1] = ((val >> 8) & 0xFF);//14 14
//            floatValues[i * 3 + 2] = (val & 0xFF);//3 10
            floatValues[i * 3 + 0] = (val & 0xFF);//10 3
            floatValues[i * 3 + 1] = ((val >> 8) & 0xFF);//14 14
            floatValues[i * 3 + 2] = ((val >> 16) & 0xFF);//3 10
        }
//        Log.i(TAG,"floatValues:"+ Float.toString(floatValues[432*368*2]));


//        float[] result = new float[432*368*3];
//        for(int j = 0;j < 432*368*3;j++){
//            result[j] = 255;
//        }
        return floatValues;
    }
}