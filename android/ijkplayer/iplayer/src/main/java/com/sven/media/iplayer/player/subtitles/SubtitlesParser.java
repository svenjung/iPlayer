package com.sven.media.iplayer.player.subtitles;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import timber.log.Timber;

/**
 * Created by Sven.J on 18-3-7.
 * 字幕解析器，使用github源码：https://github.com/Sriharia/Subtitle-Converter
 * 修改两处：
 * 1 srt解析时行数判断问题
 * 2 文件读取时增加编码信息
 */

public class SubtitlesParser {

    public SubtitlesParser() {

    }

    public TimedTextObject parseFile(String filePath) {
        try {
            String extension = getFileExtension(filePath);
            TimedTextFileFormat parser = getFormatParser(extension);

            FileInputStream in = openInputStream(new File(filePath));
            String charset = detectCharset(filePath);
            Timber.d("subtitle file %s, charset = %s", filePath, charset);
            return parser.parseFile(filePath, in, charset);
        } catch (Exception e) {
            Timber.e(e, "parse subtitle failed for file : %s", filePath);
            return null;
        }
    }

    private TimedTextFileFormat getFormatParser(String extension)
            throws FatalParsingException {
        if (extension == null) {
            throw new FatalParsingException("get format parser for empty extension");
        }

        extension = extension.toLowerCase();
        switch (extension) {
            case "ssa":
            case "ass":
                return new FormatASS();
            case "srt":
                return new FormatSRT();
            case "stl":
                return new FormatSTL();
            case "xml":
                return new FormatTTML();
            case "scc":
                return new FormatSCC();
            default:
                throw new FatalParsingException("unsupported file extension : " + extension);
        }
    }

    private String getFileExtension(String filePath) {
        try {
            File file = new File(filePath);
            String fileName = file.getName();
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch (Exception e) {
            Timber.w(e, "get file extension failed for file path : %s", filePath);
            return null;
        }
    }

    private String detectCharset(String filePath) throws FatalParsingException {
        FileInputStream in = null;
        try {
            in = openInputStream(new File(filePath));
            byte[] bytes = new byte[1024];
            int readLength = in.read(bytes, 0, 1024);
            UniversalDetector detector = new UniversalDetector(null);
            detector.handleData(bytes, 0, readLength);
            detector.dataEnd();

            return detector.getDetectedCharset();
        } catch (IOException e) {
            throw new FatalParsingException(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Timber.w(e, "Close file input stream failed");
                }
            }
        }
    }

    private FileInputStream openInputStream(final File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }
}
