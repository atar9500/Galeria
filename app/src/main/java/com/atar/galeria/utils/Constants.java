package com.atar.galeria.utils;

public class Constants {

    /**
     * Api related
     */
    public static final String IMG_PREFIX = "https://photos.gurushots.com/unsafe/";
    public static final String JPG = ".jpg";
    public static final String MEMBER_ID = "2a49ab04b1534574e578a08b8f9d7441";
    public static final boolean GET_LIKES = true;
    public static final int LIMIT = 50;
    public static final int START = 0;

    /**
     * Cache DB related constants
     */
    public static final String CACHE_DB_NAME = "cache_database";
    public static final int CACHE_DB_VERSION = 1;
    public static final String PHOTOS_TABLE = "photos_table";

    /**
     * Fragment Transitions related
     */
    public static final long PHOTO_ENTER_TRANSITION_DURATION = 325L;
    public static final long GALLERY_EXIT_TRANSITION_DURATION = 325L;
    public static final long GALLERY_EXIT_TRANSITION_DELAY_DURATION = 25L;

    /**
     * Lottie Animations
     */
    public static final String GALLERY_EMPTY_ANIMATION = "lottie/gallery_empty.json";
    public static final String NO_NETWORK_ANIMATION = "lottie/gallery_no_network.json";

    /**
     * Misc
     */
    public static final String DATE_FORMAT  = "dd/MM/yyyy HH:mm:ss";

}
