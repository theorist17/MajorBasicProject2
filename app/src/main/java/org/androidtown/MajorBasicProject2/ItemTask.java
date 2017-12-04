package org.androidtown.MajorBasicProject2;

import android.graphics.drawable.Drawable;

public class ItemTask {
    private Drawable front;
    private String id_task;
    private String title;
    private String summary;
    private String count;
    public ItemTask(){

    }
    public ItemTask(Drawable front, String id_task, String title, String summary, String count) {
        this.front = front;
        this.id_task = id_task;
        this.title = title;
        this.summary = summary;
        this.count = count;
    }

    public void setFront(Drawable icon) {
        front = icon ;
    }
    public void setId_task(String id_task) {
        this.id_task = id_task;
    }
    public void setTitle(String title) {
        this.title = title ;
    }
    public void setSummary(String desc) {
        summary = desc ;
    }

    public String getCount() {
        return count;
    }
    public Drawable getFront() {
        return this.front;
    }
    public String getId_task() {
        return id_task;
    }
    public String getTitle() {
        return this.title;
    }
    public String getSummary() {
        return this.summary;
    }
    public void setCount(String count) {
        this.count = count;
    }


}