import org.junit.Test;

public class TestOther {

  @Test
  public void testStr() {
    //language=TEXT
    String str = " application/java-archive\n"
        + " application/EDI-X12   \n"
        + " application/EDIFACT   \n"
        + " application/javascript (obsolete) \n"
        + " application/octet-stream   \n"
        + " application/ogg   \n"
        + " application/pdf  \n"
        + " application/xhtml+xml   \n"
        + " application/x-shockwave-flash    \n"
        + " application/json  \n"
        + " application/ld+json  \n"
        + " application/xml   \n"
        + " application/zip  \n"
        + " application/x-www-form-urlencoded  \n"
        + " audio/mpeg   \n"
        + " audio/x-ms-wma   \n"
        + " audio/vnd.rn-realaudio   \n"
        + " image/gif   \n"
        + " image/jpeg   \n"
        + " image/png   \n"
        + " image/tiff    \n"
        + " image/vnd.microsoft.icon    \n"
        + " image/x-icon   \n"
        + " image/vnd.djvu   \n"
        + " image/svg+xml    \n"
        + " multipart/mixed    \n"
        + " multipart/alternative   \n"
        + " multipart/related\n"
        + " multipart/form-data  \n"
        + " text/css    \n"
        + " text/csv    \n"
        + " text/html    \n"
        + " text/javascript   \n"
        + " text/plain    \n"
        + " text/xml    \n"
        + " video/mpeg    \n"
        + " video/mp4    \n"
        + " video/quicktime    \n"
        + " video/x-ms-wmv    \n"
        + " video/x-msvideo    \n"
        + " video/x-flv   \n"
        + " video/webm   \n"
        + " application/vnd.android.package-archive\n"
        + " application/vnd.oasis.opendocument.text    \n"
        + " application/vnd.oasis.opendocument.spreadsheet  \n"
        + " application/vnd.oasis.opendocument.presentation   \n"
        + " application/vnd.oasis.opendocument.graphics   \n"
        + " application/vnd.ms-excel    \n"
        + " application/vnd.openxmlformats-officedocument.spreadsheetml.sheet   \n"
        + " application/vnd.ms-powerpoint    \n"
        + " application/vnd.openxmlformats-officedocument.presentationml.presentation    \n"
        + " application/msword   \n"
        + " application/vnd.openxmlformats-officedocument.wordprocessingml.document   \n"
        + " application/vnd.mozilla.xul+xml";
    str = str.replace(" ", "");
    String[] lines = str.split("\n");
    StringBuilder builder = new StringBuilder();
    for (String line : lines) {
      String name = line.toUpperCase().replace("/", "_")
          .replace("-", "_")
          .replace("+", "_")
          .replace(".", "_");
      builder.append("/**\n").append("* ").append(line).append("\n").append("*/\n");
      builder.append(name).append("(\"").append(line).append("\"),").append("\n");
    }
    System.out.println(builder);
  }
}
