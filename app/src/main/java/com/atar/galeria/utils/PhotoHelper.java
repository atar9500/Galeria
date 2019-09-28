package com.atar.galeria.utils;

import com.atar.galeria.db.Photo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhotoHelper {

    public static String getPhotoUrl(Photo photo, int height, int width) {
        return Constants.IMG_PREFIX + width + "x" + height + "/" + photo.getMemberId()
                + "/3_" + photo.getPhotoId() + Constants.JPG;
    }

    public static boolean isPhotoLiked(List<String> likes, String photoId) {
        return likes.indexOf(photoId) != -1;
    }

    public static String formatCountLabel(int count) {
        if (count < 1000) {
            return Integer.toString(count);
        } else if (count > 1000 && count < 1000000) {
            return Math.round((count / 1000.0) * 10) / 10.0 + "k";
        } else if (count > 1000000) {
            return Math.round((count / 1000.0) * 10) / 10.0 + "m";
        }
        return "";
    }

    public static String formatDate(long time) {
        return new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault()).format(new Date(time));
    }

}
