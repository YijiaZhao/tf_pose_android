package com.example.mnistandroid;

public class pafprocess {
    public native int process_paf(int p1, int p2, int p3, float[] peaks, int h1, int h2, int h3, float[] heatmap, int f1, int f2, int f3, float[] pafmap);
    public native int get_num_humans();
    public native int get_part_cid(int human_id, int part_id);
    public native float get_score(int human_id);
    public native int get_part_x(int cid);
    public native int get_part_y(int cid);
    public native float get_part_score(int cid);
}
