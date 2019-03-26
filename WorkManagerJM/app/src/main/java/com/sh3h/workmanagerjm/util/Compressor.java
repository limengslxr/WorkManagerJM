package com.sh3h.workmanagerjm.util;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;

/**
 * Created by LiMeng on 2018/2/23.
 */

public class Compressor {
    //max width and height values of the compressed image is taken as 612x816
    private int maxWidth = 612;
    private int maxHeight = 816;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private int quality = 80;

    public Compressor setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public Compressor setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public Compressor setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
        return this;
    }

    public Compressor setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public File compressToFile(String originalImagePath, File destinationFile) throws IOException {
        return ImageUtil.compressImage(originalImagePath, maxWidth, maxHeight, compressFormat,
                quality, destinationFile);
    }
}
