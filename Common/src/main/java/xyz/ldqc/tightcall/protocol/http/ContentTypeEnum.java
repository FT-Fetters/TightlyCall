package xyz.ldqc.tightcall.protocol.http;

/**
 * ContentType 类型枚举
 *
 * @author Fetters
 */
public enum ContentTypeEnum {

    /**
     * application/java-archive
     */
    APPLICATION_JAVA_ARCHIVE("application/java-archive"),
    /**
     * application/EDI-X12
     */
    APPLICATION_EDI_X12("application/EDI-X12"),
    /**
     * application/EDIFACT
     */
    APPLICATION_EDIFACT("application/EDIFACT"),
    /**
     * application/javascript(obsolete)
     */
    APPLICATION_JAVASCRIPT("application/javascript"),
    /**
     * application/octet-stream
     */
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    /**
     * application/ogg
     */
    APPLICATION_OGG("application/ogg"),
    /**
     * application/pdf
     */
    APPLICATION_PDF("application/pdf"),
    /**
     * application/xhtml+xml
     */
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    /**
     * application/x-shockwave-flash
     */
    APPLICATION_X_SHOCKWAVE_FLASH("application/x-shockwave-flash"),
    /**
     * application/json
     */
    APPLICATION_JSON("application/json"),
    /**
     * application/ld+json
     */
    APPLICATION_LD_JSON("application/ld+json"),
    /**
     * application/xml
     */
    APPLICATION_XML("application/xml"),
    /**
     * application/zip
     */
    APPLICATION_ZIP("application/zip"),
    /**
     * application/x-www-form-urlencoded
     */
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    /**
     * audio/mpeg
     */
    AUDIO_MPEG("audio/mpeg"),
    /**
     * audio/x-ms-wma
     */
    AUDIO_X_MS_WMA("audio/x-ms-wma"),
    /**
     * audio/vnd.rn-realaudio
     */
    AUDIO_VND_RN_REALAUDIO("audio/vnd.rn-realaudio"),
    /**
     * image/gif
     */
    IMAGE_GIF("image/gif"),
    /**
     * image/jpeg
     */
    IMAGE_JPEG("image/jpeg"),
    /**
     * image/png
     */
    IMAGE_PNG("image/png"),
    /**
     * image/tiff
     */
    IMAGE_TIFF("image/tiff"),
    /**
     * image/vnd.microsoft.icon
     */
    IMAGE_VND_MICROSOFT_ICON("image/vnd.microsoft.icon"),
    /**
     * image/x-icon
     */
    IMAGE_X_ICON("image/x-icon"),
    /**
     * image/vnd.djvu
     */
    IMAGE_VND_DJVU("image/vnd.djvu"),
    /**
     * image/svg+xml
     */
    IMAGE_SVG_XML("image/svg+xml"),
    /**
     * multipart/mixed
     */
    MULTIPART_MIXED("multipart/mixed"),
    /**
     * multipart/alternative
     */
    MULTIPART_ALTERNATIVE("multipart/alternative"),
    /**
     * multipart/related
     */
    MULTIPART_RELATED("multipart/related"),
    /**
     * multipart/form-data
     */
    MULTIPART_FORM_DATA("multipart/form-data"),
    /**
     * text/css
     */
    TEXT_CSS("text/css"),
    /**
     * text/csv
     */
    TEXT_CSV("text/csv"),
    /**
     * text/html
     */
    TEXT_HTML("text/html"),
    /**
     * text/javascript
     */
    TEXT_JAVASCRIPT("text/javascript"),
    /**
     * text/plain
     */
    TEXT_PLAIN("text/plain"),
    /**
     * text/xml
     */
    TEXT_XML("text/xml"),
    /**
     * video/mpeg
     */
    VIDEO_MPEG("video/mpeg"),
    /**
     * video/mp4
     */
    VIDEO_MP4("video/mp4"),
    /**
     * video/quicktime
     */
    VIDEO_QUICKTIME("video/quicktime"),
    /**
     * video/x-ms-wmv
     */
    VIDEO_X_MS_WMV("video/x-ms-wmv"),
    /**
     * video/x-msvideo
     */
    VIDEO_X_MSVIDEO("video/x-msvideo"),
    /**
     * video/x-flv
     */
    VIDEO_X_FLV("video/x-flv"),
    /**
     * video/webm
     */
    VIDEO_WEBM("video/webm"),
    /**
     * application/vnd.android.package-archive
     */
    APPLICATION_VND_ANDROID_PACKAGE_ARCHIVE("application/vnd.android.package-archive"),
    /**
     * application/vnd.oasis.opendocument.text
     */
    APPLICATION_VND_OASIS_OPENDOCUMENT_TEXT("application/vnd.oasis.opendocument.text"),
    /**
     * application/vnd.oasis.opendocument.spreadsheet
     */
    APPLICATION_VND_OASIS_OPENDOCUMENT_SPREADSHEET(
        "application/vnd.oasis.opendocument.spreadsheet"),
    /**
     * application/vnd.oasis.opendocument.presentation
     */
    APPLICATION_VND_OASIS_OPENDOCUMENT_PRESENTATION(
        "application/vnd.oasis.opendocument.presentation"),
    /**
     * application/vnd.oasis.opendocument.graphics
     */
    APPLICATION_VND_OASIS_OPENDOCUMENT_GRAPHICS("application/vnd.oasis.opendocument.graphics"),
    /**
     * application/vnd.ms-excel
     */
    APPLICATION_VND_MS_EXCEL("application/vnd.ms-excel"),
    /**
     * application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
     */
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_SHEET(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    /**
     * application/vnd.ms-powerpoint
     */
    APPLICATION_VND_MS_POWERPOINT("application/vnd.ms-powerpoint"),
    /**
     * application/vnd.openxmlformats-officedocument.presentationml.presentation
     */
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_PRESENTATIONML_PRESENTATION(
        "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    /**
     * application/msword
     */
    APPLICATION_MSWORD("application/msword"),
    /**
     * application/vnd.openxmlformats-officedocument.wordprocessingml.document
     */
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_WORDPROCESSINGML_DOCUMENT(
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    /**
     * application/vnd.mozilla.xul+xml
     */
    APPLICATION_VND_MOZILLA_XUL_XML("application/vnd.mozilla.xul+xml");

    private final String value;

    ContentTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ContentTypeEnum getContentType(String value) {
        for (ContentTypeEnum contentTypeEnum : ContentTypeEnum.values()) {
            if (contentTypeEnum.getValue().equals(value)) {
                return contentTypeEnum;
            }
        }
        return null;
    }
}
