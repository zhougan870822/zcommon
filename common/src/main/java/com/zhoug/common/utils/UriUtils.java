package com.zhoug.common.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;


import com.zhoug.common.Constant;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static android.provider.DocumentsContract.PROVIDER_INTERFACE;

/**
 * Uri 处理工具
 */
public class UriUtils {
    private static final String TAG = "UriUtils";

    /**
     * 根据uri获取图片path
     *
     * @param context
     * @param uri
     * @return
     * @deprecated use {@link #getPathFromUri(Context, Uri)} instead
     */
    @SuppressLint("NewApi")
    @Deprecated
    public static String getPathFromUri1(Context context, Uri uri) {
        Log.i("getPathFromUri1", "getPathFromUri1: uri=" + uri);
        if (uri == null) return null;
        String type = null;
        Cursor cursor = null;
        String[] columns = null;//要查询的列
        //处理这种类型的uri：content://com.android.providers.media.documents/document/image:67
        //处理这种类型的uri：content://com.android.providers.media.documents/document/image%67
        try {
            //DocumentsContract.isDocumentUri(context, uri)
            if (uri.toString().startsWith("content://com.")) {
                System.out.println("DocumentsContract 类型的uri=" + uri);
                //documentId为 image:67
                String documentId = DocumentsContract.getDocumentId(uri);
                System.out.println("documentId=" + documentId);

                String[] split = documentId.split(":");
                if (split != null && split.length == 2) {
                    //类型
                    type = split[0];
                    //id为 67
                    String id = documentId.split(":")[1];
                    System.out.println(" type=" + type);
                    System.out.println(" id=" + id);

                    if (type.equalsIgnoreCase("image")) {
                        //根据id查询语句
                        String sel = MediaStore.Images.Media._ID + "=" + id;
                        columns = new String[]{MediaStore.Images.Media.DATA};
                        cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, sel, null, null);
                    } else if (type.equalsIgnoreCase("video")) {
                        //根据id查询语句
                        String sel = MediaStore.Video.Media._ID + "=" + id;
                        columns = new String[]{MediaStore.Video.Media.DATA};
                        cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, sel, null, null);
                    } else if (type.equalsIgnoreCase("audio")) {
                        //根据id查询语句
                        String sel = MediaStore.Audio.Media._ID + "=" + id;
                        columns = new String[]{MediaStore.Audio.Media.DATA};
                        cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, sel, null, null);
                    } else {
                        return null;
                    }


                }


            } else {
                System.out.println("media 类型的uri=" + uri);
                //处理这种类型的uri：
                // content://media/external/images/media/3951
                // content://media/external/video/media/197934
                // content://media/external/audio/media/202844
                String strUri = uri.toString();
                boolean b = strUri.startsWith("content:");
                if (b) {
                    int i = strUri.indexOf("media");
                    String substring = strUri.substring(i);
                    String[] split = substring.split("/");
                    if (split != null && split.length >= 5) {
                        type = split[2];
                        System.out.println(type);
                        if (type.equalsIgnoreCase("images")) {
                            type = "image";
                            columns = new String[]{MediaStore.Images.Media.DATA};
                            cursor = context.getContentResolver().query(uri, columns, null, null, null);
                        } else if (type.equalsIgnoreCase("video")) {
                            columns = new String[]{MediaStore.Video.Media.DATA};
                            cursor = context.getContentResolver().query(uri, columns, null, null, null);
                        } else if (type.equalsIgnoreCase("audio")) {
                            columns = new String[]{MediaStore.Audio.Media.DATA};
                            cursor = context.getContentResolver().query(uri, columns, null, null, null);
                        } else {
                            return null;
                        }


                    } else {
                        return null;
                    }

                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor == null) {
//			Toast.makeText(context, "该图片不存在", Toast.LENGTH_SHORT).show();
            return null;
        }
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(columns[0]));
        cursor.close();
        return path;

    }

    /**
     * 根据uri获取文件path
     * 支持 uri类型:media,documents,file,fileProvider
     * uri类型:[
     * file:///storage/emulated/0/0audio/1568103220253.mp4
     * content://media/extenral/images/media/17766
     * content://com.android.providers.media.documents/document/image:67
     * content://com.android.providers.media.documents/document/image%67
     * content://com.zhoug.androidcommon.app.fileprovider/sdcard/0audio/1568103220253.mp4
     * <p>
     * ]
     *
     * @param context {@link Context}
     * @param uri     {@link Uri}
     * @return string
     */
    public static String getPathFromUri(Context context, Uri uri) {
        if (uri == null) {
            Log.e(TAG, "getPathFromUri: uri is null");
            return null;
        }
        Log.d(TAG, "getPathFromUri:uri=" + uri);
        String path = null;
        String scheme = uri.getScheme();
        if (scheme == null) {
            path = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            path = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            if (isDocumentUri(uri)) {
                Log.d(TAG, "getPathFromUri:isDocumentUri");
                path = getPathFromDocumentUri(context, uri);
            } else if (isMediaUri(uri)) {
                Log.d(TAG, "getPathFromUri:isMediaUri");
                path = getPathFromMediaUri(context, uri);
            } else if (isFileProviderUri(uri)) {
                Log.d(TAG, "getPathFromUri:isFileProviderUri");
                path = getPathFromFileProvider(context, uri);
            } else {
                Log.e(TAG, "unknow content uri");
            }
        } else {
            Log.e(TAG, "unknow uri");
        }

        Log.i(TAG, "getPathFromUri:path=" + path);
        return path;
    }


    /**
     * 解析uri获取文件真实路径
     * 支持uri类型:content://media/
     * eg:content://media/extenral/images/media/17766
     *
     * @param context
     * @param uri
     * @return
     */
    private static String getPathFromMediaUri(Context context, Uri uri) {
//        Log.d(TAG, "getPathFromMediaUri:media uri");
        if (uri == null) {
            return null;
        }
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                if (columnIndex >= 0) {
                    path = cursor.getString(columnIndex);
                }
            }
            cursor.close();
        }
//        Log.d(TAG, "getPathFromMediaUri:path="+path);
        return path;
    }


    /**
     * 解析uri获取文件真实路径
     * 4.4以上版本
     * 支持uri类型:content://com.android.providers.media.documents/document/
     * eg: content://com.android.providers.media.documents/document/image:67
     * eg:content://com.android.providers.media.documents/document/image%67
     *
     * @param context
     * @param uri
     * @return
     */
    private static String getPathFromDocumentUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String path = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //image:378558
            try {
                String documentId = DocumentsContract.getDocumentId(uri);
//                Log.d(TAG, "getPathFromDocumentUri:documentId="+documentId);
                if (documentId != null) {
                    String[] split = documentId.split(":");
                    //image
                    String type = split[0];
                    //378558
                    String id = split[1];
                    String[] columns = null;
                    Cursor cursor = null;
                    if (type.equalsIgnoreCase("image")) {
                        //根据id查询语句
                        String sel = MediaStore.Images.Media._ID + "=" + id;
                        columns = new String[]{MediaStore.Images.Media.DATA};
                        cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, sel, null, null);
                    } else if (type.equalsIgnoreCase("video")) {
                        //根据id查询语句
                        String sel = MediaStore.Video.Media._ID + "=" + id;
                        columns = new String[]{MediaStore.Video.Media.DATA};
                        cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, sel, null, null);
                    } else if (type.equalsIgnoreCase("audio")) {
                        //根据id查询语句
                        String sel = MediaStore.Audio.Media._ID + "=" + id;
                        columns = new String[]{MediaStore.Audio.Media.DATA};
                        cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, sel, null, null);
                    }

                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            path = cursor.getString(cursor.getColumnIndex(columns[0]));
                        }
                        cursor.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        Log.d(TAG, "getPathFromDocumentUri:path="+path);
        return path;
    }

    /**
     * 解析uri获取文件真实路径
     * 支持uri类型 fileProvider 类型的uri
     * eg:content://com.tencent.mtt.fileprovider/QQBrowser/Music/123123.mp3
     * 原理:FileProvider的内部接口PathStrategy提供了Uri和File相互转换的方法,但是只暴露给开发者获取Uri的方法,
     * 要想通过uri获取File我们可以同过反射实现
     * {@link FileProvider#getPathStrategy(Context, String),FileProvider.PathStrategy#getFileForUri(Uri)}
     *
     * @param context
     * @param uri
     * @return
     */
    private static String getPathFromFileProvider(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String authority = uri.getAuthority();
        if (authority == null) {
            return uri.getPath();
        }
        Class<FileProvider> fileProviderCls = FileProvider.class;
        try {
            Method getPathStrategy = fileProviderCls.getDeclaredMethod("getPathStrategy", Context.class, String.class);
            if (null != getPathStrategy) {
                //getPathStrategy 是静态方法所以可以传入null
                getPathStrategy.setAccessible(true);
                //获取到了PathStrategy实例
                Object pathStrategy = getPathStrategy.invoke(null, context, authority);
                getPathStrategy.setAccessible(false);
                if (pathStrategy != null) {
                    //PathStrategy类是私有的所以只能通过类型来加载class
                    //PathStrategy是FileProvider的内部类
                    Class<?> PathStrategyCls = Class.forName(FileProvider.class.getName() + "$PathStrategy");
                    Method getFileForUri = PathStrategyCls.getDeclaredMethod("getFileForUri", Uri.class);
                    if (null != getFileForUri) {
                        getFileForUri.setAccessible(true);
                        Object file = getFileForUri.invoke(pathStrategy, uri);
                        getFileForUri.setAccessible(false);
                        if (file instanceof File) {
                            return ((File) file).getAbsolutePath();
                        }
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断uri的authority是否是com.android.providers.media.documents
     *
     * @param uri
     * @return
     */
    private static boolean isDocumentUri(Uri uri) {
        if (uri != null) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }
        return false;
    }

    /**
     * 判断uri的authority是否是media
     *
     * @param uri
     * @return
     */
    private static boolean isMediaUri(Uri uri) {
        if (uri != null) {
            return "media".equals(uri.getAuthority());
        }
        return false;
    }

    /**
     * 判断是否是fileprovider类型的uri
     * content://com.zhoug.androidcommon.app.fileprovider/sdcard/0audio/1568103220253.mp4
     *
     * @param uri
     * @return
     */
    private static boolean isFileProviderUri(Uri uri) {
        if (null != uri) {
            String authority = uri.getAuthority();
            if (authority != null) {
                int index = authority.lastIndexOf(".");
                if (index >= 0 && index < authority.length() - 2) {
                    String fileprovider = authority.substring(index + 1);
                    return "fileprovider".equalsIgnoreCase(fileprovider);
                }
            }
        }
        return false;
    }


    /**
     * @param context
     * @param authority
     * @return
     */
    private static boolean _isDocumentsProvider(Context context, String authority) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent intent = new Intent(PROVIDER_INTERFACE);
            final List<ResolveInfo> infos;
            infos = context.getPackageManager().queryIntentContentProviders(intent, 0);
            for (ResolveInfo info : infos) {
                /**
                 *  com.android.documentsui.archives
                 *  com.android.externalstorage.documents
                 *  com.android.mtp.documents
                 *  com.android.providers.downloads.documents
                 *  com.android.providers.media.documents
                 * com.android.shell.documents
                 */
                Log.d(TAG, "isDocumentsProvider:" + info.providerInfo.authority);
                if (authority.equals(info.providerInfo.authority)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 根据文件路径获取uri
     *
     * @param context
     * @param path
     * @param authority
     * @return
     */
    public static Uri getUriForFile(Context context, String path, String authority) {
        if (authority == null) {
            authority = context.getPackageName() + Constant.FILE_PROVIDER_AUTHORITY_SUFFIX;
        }
        //7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, authority, new File(path));
        } else {
            return Uri.fromFile(new File(path));
        }
    }

    /**
     * 根据文件路径获取uri
     *
     * @param context
     * @param path
     * @return
     */
    public static Uri getUriForFile(Context context, String path) {
        return getUriForFile(context, path, null);
    }

}
