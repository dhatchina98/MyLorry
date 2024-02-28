package com.dcode.mylorry;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UriToFileUtil {

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static File uriToFile(Context context, Uri uri) {
        File file = new File(context.getFilesDir(), "temp_file");
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[4 * 1024]; // 4KB buffer
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}


