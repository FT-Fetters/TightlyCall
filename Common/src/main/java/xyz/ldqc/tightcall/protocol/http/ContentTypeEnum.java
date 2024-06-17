package xyz.ldqc.tightcall.protocol.http;

import java.util.HashMap;
import java.util.Map;

/**
 * ContentType 类型枚举
 *
 * @author Fetters
 */
public enum ContentTypeEnum {

    /**
     * application/java-archive
     */
    APPLICATION_JAVA_ARCHIVE("application/java-archive", new String[]{"jar"}),
    /**
     * application/EDI-X12
     */
    APPLICATION_EDI_X12("application/EDI-X12", new String[]{}),
    /**
     * application/EDIFACT
     */
    APPLICATION_EDIFACT("application/EDIFACT", new String[]{}),
    /**
     * application/javascript(obsolete)
     */
    APPLICATION_JAVASCRIPT("application/javascript", new String[]{"js"}),
    /**
     * application/octet-stream
     */
    APPLICATION_OCTET_STREAM("application/octet-stream", new String[]{}),
    /**
     * application/ogg
     */
    APPLICATION_OGG("application/ogg", new String[]{"ogg"}),
    /**
     * application/pdf
     */
    APPLICATION_PDF("application/pdf", new String[]{"pdf"}),
    /**
     * application/xhtml+xml
     */
    APPLICATION_XHTML_XML("application/xhtml+xml", new String[]{"xhtml"}),
    /**
     * application/x-shockwave-flash
     */
    APPLICATION_X_SHOCKWAVE_FLASH("application/x-shockwave-flash", new String[]{"swf"}),
    /**
     * application/json
     */
    APPLICATION_JSON("application/json", new String[]{"json"}),
    /**
     * application/ld+json
     */
    APPLICATION_LD_JSON("application/ld+json", new String[]{}),
    /**
     * application/xml
     */
    APPLICATION_XML("application/xml", new String[]{"xml"}),
    /**
     * application/zip
     */
    APPLICATION_ZIP("application/zip", new String[]{"zip"}),
    /**
     * application/x-www-form-urlencoded
     */
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded", new String[]{}),
    /**
     * audio/mpeg
     */
    AUDIO_MPEG("audio/mpeg", new String[]{"mp3"}),
    /**
     * audio/x-ms-wma
     */
    AUDIO_X_MS_WMA("audio/x-ms-wma", new String[]{"wma"}),
    /**
     * audio/vnd.rn-realaudio
     */
    AUDIO_VND_RN_REALAUDIO("audio/vnd.rn-realaudio", new String[]{"ra"}),
    /**
     * image/gif
     */
    IMAGE_GIF("image/gif", new String[]{"gif"}),
    /**
     * image/jpeg
     */
    IMAGE_JPEG("image/jpeg", new String[]{"jpeg", "jpg"}),
    /**
     * image/png
     */
    IMAGE_PNG("image/png", new String[]{"png"}),
    /**
     * image/tiff
     */
    IMAGE_TIFF("image/tiff", new String[]{"tiff"}),
    /**
     * image/vnd.microsoft.icon
     */
    IMAGE_VND_MICROSOFT_ICON("image/vnd.microsoft.icon", new String[]{"ico"}),
    /**
     * image/x-icon
     */
    IMAGE_X_ICON("image/x-icon", new String[]{"ico"}),
    /**
     * image/vnd.djvu
     */
    IMAGE_VND_DJVU("image/vnd.djvu", new String[]{"djvu"}),
    /**
     * image/svg+xml
     */
    IMAGE_SVG_XML("image/svg+xml", new String[]{"svg"}),
    /**
     * multipart/mixed
     */
    MULTIPART_MIXED("multipart/mixed", new String[]{}),
    /**
     * multipart/alternative
     */
    MULTIPART_ALTERNATIVE("multipart/alternative", new String[]{}),
    /**
     * multipart/related
     */
    MULTIPART_RELATED("multipart/related", new String[]{}),
    /**
     * multipart/form-data
     */
    MULTIPART_FORM_DATA("multipart/form-data", new String[]{}),
    /**
     * text/css
     */
    TEXT_CSS("text/css", new String[]{"css"}),
    /**
     * text/csv
     */
    TEXT_CSV("text/csv", new String[]{"csv"}),
    /**
     * text/html
     */
    TEXT_HTML("text/html", new String[]{"html", "htm"}),
    /**
     * text/javascript
     */
    TEXT_JAVASCRIPT("text/javascript", new String[]{"js"}),
    /**
     * text/plain
     */
    TEXT_PLAIN("text/plain", new String[]{"txt"}),
    /**
     * text/xml
     */
    TEXT_XML("text/xml", new String[]{"xml"}),
    /**
     * video/mpeg
     */
    VIDEO_MPEG("video/mpeg", new String[]{"mpeg", "mpg"}),
    /**
     * video/mp4
     */
    VIDEO_MP4("video/mp4", new String[]{"mp4"}),
    /**
     * video/quicktime
     */
    VIDEO_QUICKTIME("video/quicktime", new String[]{"mov"}),
    /**
     * video/x-ms-wmv
     */
    VIDEO_X_MS_WMV("video/x-ms-wmv", new String[]{"wmv"}),
    /**
     * video/x-msvideo
     */
    VIDEO_X_MSVIDEO("video/x-msvideo", new String[]{"avi"}),
    /**
     * video/x-flv
     */
    VIDEO_X_FLV("video/x-flv", new String[]{"flv"}),
    /**
     * video/webm
     */
    VIDEO_WEBM("video/webm", new String[]{"webm"}),
    /**
     * application/vnd.android.package-archive
     */
    APPLICATION_VND_ANDROID_PACKAGE_ARCHIVE("application/vnd.android.package-archive", new String[]{"apk"}),
    /**
     * application/vnd.oasis.opendocument.text
     */
    APPLICATION_VND_OASIS_OPENDOCUMENT_TEXT("application/vnd.oasis.opendocument.text", new String[]{"odt"}),
    /**
     * application/vnd.oasis.opendocument.spreadsheet
     */
    APPLICATION_VND_OASIS_OPENDOCUMENT_SPREADSHEET("application/vnd.oasis.opendocument.spreadsheet", new String[]{"ods"}),
    /**
     * application/vnd.oasis.opendocument.presentation
     */
    APPLICATION_VND_OASIS_OPENDOCUMENT_PRESENTATION("application/vnd.oasis.opendocument.presentation", new String[]{"odp"}),
    /**
     * application/vnd.oasis.opendocument.graphics
     */
    APPLICATION_VND_OASIS_OPENDOCUMENT_GRAPHICS("application/vnd.oasis.opendocument.graphics", new String[]{"odg"}),
    /**
     * application/vnd.ms-excel
     */
    APPLICATION_VND_MS_EXCEL("application/vnd.ms-excel", new String[]{"xls"}),
    /**
     * application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
     */
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_SHEET("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new String[]{"xlsx"}),
    /**
     * application/vnd.ms-powerpoint
     */
    APPLICATION_VND_MS_POWERPOINT("application/vnd.ms-powerpoint", new String[]{"ppt"}),
    /**
     * application/vnd.openxmlformats-officedocument.presentationml.presentation
     */
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_PRESENTATIONML_PRESENTATION("application/vnd.openxmlformats-officedocument.presentationml.presentation", new String[]{"pptx"}),
    /**
     * application/msword
     */
    APPLICATION_MSWORD("application/msword", new String[]{"doc"}),
    /**
     * application/vnd.openxmlformats-officedocument.wordprocessingml.document
     */
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_WORDPROCESSINGML_DOCUMENT("application/vnd.openxmlformats-officedocument.wordprocessingml.document", new String[]{"docx"}),
    /**
     * application/vnd.mozilla.xul+xml
     */
    APPLICATION_VND_MOZILLA_XUL_XML("application/vnd.mozilla.xul+xml", new String[]{"xul"});

    private final String value;
    private final String[] extensions;

    private static final Map<String, ContentTypeEnum> EXTENSION_MAP = new HashMap<>();

    static {
        for (ContentTypeEnum contentTypeEnum : ContentTypeEnum.values()) {
            for (String extension : contentTypeEnum.getExtensions()) {
                EXTENSION_MAP.put(extension, contentTypeEnum);
            }
        }
    }

    ContentTypeEnum(String value, String[] extensions) {
        this.value = value;
        this.extensions = extensions;
    }

    public String getValue() {
        return value;
    }

    public String[] getExtensions() {
        return extensions;
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

    public static ContentTypeEnum getContentTypeByExtension(String extension) {
        return EXTENSION_MAP.get(extension);
    }
}
