package com.cn.ispanish.box;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 17/3/20.
 */
public class VideoDownloadInfo {

    private String vid;
    private String courseid;
    private String duration;
    private long filesize;
    private int bitrate;
    private long percent;
    private String title;
    private long total;
    private String speed;
    private String pic;

    public VideoDownloadInfo() {

    }

    public VideoDownloadInfo(String vid, String courseid, String speed, String duration, long filesize, int bitrate) {
        this.vid = vid;
        this.courseid = courseid;
        this.speed = speed;
        this.duration = duration;
        this.filesize = filesize;
        this.bitrate = bitrate;
    }

    public VideoDownloadInfo(String vid, String courseid, String duration, long filesize, int bitrate) {
        this.vid = vid;
        this.courseid = courseid;
        this.duration = duration;
        this.filesize = filesize;
        this.bitrate = bitrate;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getSpeed() {
        return speed;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPercent() {
        return percent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPercent(long percent) {
        this.percent = percent;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public boolean isComplete() {
        return getTotal() != 0 && getTotal() == getPercent();
    }

    @Override
    public String toString() {
        return "DownloadInfo [ title = " + title + " , vid = " + vid + " , courseId = "+courseid+" , duration = " + duration + " , filesize = " + filesize + " , total = " + total + " , percent = " + percent + " ]";
    }

}
