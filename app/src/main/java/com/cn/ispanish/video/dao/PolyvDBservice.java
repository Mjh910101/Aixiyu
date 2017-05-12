package com.cn.ispanish.video.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cn.ispanish.box.VideoDownloadInfo;
import com.cn.ispanish.dao.DBHandler;

import java.util.LinkedList;

public class PolyvDBservice {
    private static final String TAG = "DBservice";
    private PolyvDBOpenHepler dbOpenHepler;
    private SQLiteDatabase db;

    public PolyvDBservice(Context context) {
        // 1 -> database version
        dbOpenHepler = PolyvDBOpenHepler.getInstance(context, DBHandler.DB_LEVER);
    }

    public void addDownloadFile(VideoDownloadInfo info) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "insert into downloadlist(vid,courseid,speed,title,image,duration,filesize,bitrate) values(?,?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{info.getVid(), info.getCourseid(), info.getSpeed(), info.getTitle(), info.getPic(), info.getDuration(),
                info.getFilesize(), info.getBitrate()});
    }

    public void deleteDownloadFile(VideoDownloadInfo info) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "delete from downloadlist where vid=? and bitrate=? and speed=?";
        db.execSQL(sql, new Object[]{info.getVid(), info.getBitrate(), info.getSpeed()});
    }

    public void updatePercent(VideoDownloadInfo info, long percent, long total) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "update downloadlist set percent=?,total=? where vid=? and bitrate=? and speed=?";
        db.execSQL(sql, new Object[]{percent, total, info.getVid(), info.getBitrate(), info.getSpeed()});
    }

    public boolean isAdd(VideoDownloadInfo info) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select vid ,duration,filesize,bitrate from downloadlist where vid=? and speed=? and bitrate="
                + info.getBitrate();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[]{info.getVid(), info.getSpeed()});
            if (cursor.getCount() == 1) {
                return true;
            } else {
                return false;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public boolean isCompleteForVideo(String id) {
        LinkedList<VideoDownloadInfo> list = getDownloadFilesForVid(id);
        if (list.isEmpty()) {
            return false;
        }
        VideoDownloadInfo info = list.get(0);
        if (info == null) {
            return false;
        }

        if (!info.getVid().equals(id)) {
            return false;
        }

        if (info.getPercent() != info.getTotal()) {
            return false;
        }

        return true;
    }

    public LinkedList<VideoDownloadInfo> getDownloadFilesForVid(String id) {
        LinkedList<VideoDownloadInfo> infos = new LinkedList<VideoDownloadInfo>();
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select * from downloadlist where vid = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[]{id});
            while (cursor.moveToNext()) {
                String vid = cursor.getString(cursor.getColumnIndex("vid"));
                String courseid = cursor.getString(cursor.getColumnIndex("courseid"));
                String speed = cursor.getString(cursor.getColumnIndex("speed"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                long filesize = cursor.getInt(cursor.getColumnIndex("filesize"));
                int bitrate = cursor.getInt(cursor.getColumnIndex("bitrate"));
                long percent = cursor.getInt(cursor.getColumnIndex("percent"));
                long total = cursor.getInt(cursor.getColumnIndex("total"));
                VideoDownloadInfo info = new VideoDownloadInfo(vid, courseid, duration, filesize, bitrate);
                info.setSpeed(speed);
                info.setPercent(percent);
                info.setTitle(title);
                info.setTotal(total);
                infos.addLast(info);
                info.setPic(image);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return infos;
    }

    public LinkedList<VideoDownloadInfo> getDownloadFilesForCourseid(String id) {
        LinkedList<VideoDownloadInfo> infos = new LinkedList<VideoDownloadInfo>();
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select * from downloadlist where courseid = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[]{id});
            while (cursor.moveToNext()) {
                String vid = cursor.getString(cursor.getColumnIndex("vid"));
                String courseid = cursor.getString(cursor.getColumnIndex("courseid"));
                String speed = cursor.getString(cursor.getColumnIndex("speed"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                long filesize = cursor.getInt(cursor.getColumnIndex("filesize"));
                int bitrate = cursor.getInt(cursor.getColumnIndex("bitrate"));
                long percent = cursor.getInt(cursor.getColumnIndex("percent"));
                long total = cursor.getInt(cursor.getColumnIndex("total"));
                VideoDownloadInfo info = new VideoDownloadInfo(vid, courseid, duration, filesize, bitrate);
                info.setSpeed(speed);
                info.setPercent(percent);
                info.setTitle(title);
                info.setTotal(total);
                infos.addLast(info);
                info.setPic(image);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return infos;
    }

    public LinkedList<VideoDownloadInfo> getDownloadFiles() {
        LinkedList<VideoDownloadInfo> infos = new LinkedList<VideoDownloadInfo>();
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select * from downloadlist";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String vid = cursor.getString(cursor.getColumnIndex("vid"));
                String courseid = cursor.getString(cursor.getColumnIndex("courseid"));
                String speed = cursor.getString(cursor.getColumnIndex("speed"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                long filesize = cursor.getInt(cursor.getColumnIndex("filesize"));
                int bitrate = cursor.getInt(cursor.getColumnIndex("bitrate"));
                long percent = cursor.getInt(cursor.getColumnIndex("percent"));
                long total = cursor.getInt(cursor.getColumnIndex("total"));
                VideoDownloadInfo info = new VideoDownloadInfo(vid, courseid, duration, filesize, bitrate);
                info.setSpeed(speed);
                info.setPercent(percent);
                info.setTitle(title);
                info.setTotal(total);
                info.setPic(image);

                infos.addLast(info);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return infos;
    }
}
