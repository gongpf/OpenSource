package com.org.source.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileHelper
{
    public enum PathType
    {
        FILE_SYSTEM, ASSETS,
    }

    @SuppressWarnings("resource")
    public static InputStream openStream(String path, PathType type)
    {
        InputStream stream = null;

        switch (type)
        {
            case FILE_SYSTEM:
                {
                    try
                    {
                        stream = new FileInputStream(path);
                    }
                    catch (FileNotFoundException ex)
                    {
                    }
                }
                break;

            case ASSETS:
                stream = AssetHelper.openFile(path);
                break;

            default:
                throw new IllegalArgumentException();
        }

        return stream;
    }

    public static boolean fileExists(String path, PathType type)
    {
        switch (type)
        {
            case ASSETS:
                return AssetHelper.fileExists(path);

            case FILE_SYSTEM:
                return new File(path).exists();

            default:
                throw new IllegalArgumentException();
        }
    }
}
