package xyz.ldqc.tightcall.protocol.http.support.multipart;

/**
 * @author Fetters
 */
public abstract class AbstractMultipartContent {

    protected String name;

    AbstractMultipartContent(String name){
        this.name = name;
    }

}
