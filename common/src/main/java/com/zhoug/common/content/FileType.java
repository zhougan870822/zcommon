package com.zhoug.common.content;

/**
 * 文件类型
 *
 * @Author HK-LJJ
 * @Date 2019/12/6
 * @Description
 */
public class FileType {
    /**
     * 未知
     */
    public static final int UNKNOWN = -1;
    /**
     * 图片
     */
    public static final int IMAGE = 1;
    /**
     * 视频
     */
    public static final int VIDEO = 2;
    /**
     * 音频
     */
    public static final int AUDIO = 3;

    /**
     * 所有类型
     */
//    public static final int ALL = 10;


//    public static final String MIME_IMAGE = "image/*";
//    public static final String MIME_VIDEO = "video/*";
//    public static final String MIME_AUDIO = "audio/*";


    /**
     * 文件后缀和MIMEType对应表
     */
    private static final String[][] MIMEType = {
            {"jpg", "image/*"},
            {"jpeg", "image/*"},
            {"png", "image/*"},
            {"bmp", "image/*"},
            {"gif", "image/*"},
            {"webp", "image/*"},

            {"mp4", "video/*"},
            {"3gp", "video/*"},
            {"3gpp", "video/*"},
            {"3gpp2", "video/*"},
            {"avi", "video/*"},
            {"mkv", "video/*"},
            {"webm", "video/*"},


            {"mp3", "audio/*"},
            {"aac", "audio/*"},
            {"ogg", "audio/*"},
            {"amr", "audio/*"},
            {"wav", "audio/*"},

            {"txt", "text/plain"},
            {"doc", "application/msword"},
            {"docx", "application/msword"},
            {"pdf", "application/pdf"},

    };


    /**
     * 获取文件的mimeType 用于设置intent.setDataAndType(uri,type) 中的type;
     *
     * @param path 文件路径或后缀
     * @return 中的type
     */
    public static String getMimeType(String path) {
        String mimeType = "*/*";
        if (path == null) {
            return mimeType;
        }

        String fileType = path;
        int index = path.lastIndexOf(".");
        if (index >= 0) {
            fileType = path.substring(index + 1);
        }
        for (int i = 0; i < MIMEType.length; i++) {
            if (fileType.equalsIgnoreCase(MIMEType[i][0])) {
                mimeType = MIMEType[i][1];
                break;
            }
        }
        return mimeType;
    }


    /**
     * 通过文件后缀获取文件类型
     *
     * @param path 包含后缀的路径
     * @return {@link #IMAGE,#VIDEO,#AUDIO,#UNKNOWN}
     */
    public static int getType(String path) {
        if(path==null){
            return UNKNOWN;
        }
        String mimeType = getMimeType(path);
        if ("image/*".equalsIgnoreCase(mimeType)) {
            return IMAGE;
        } else if ("video/*".equalsIgnoreCase(mimeType)) {
            return VIDEO;
        } else if ("audio/*".equalsIgnoreCase(mimeType)) {
            return AUDIO;
        } else {
            return UNKNOWN;
        }
    }


}
