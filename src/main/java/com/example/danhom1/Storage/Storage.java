package com.example.danhom1.Storage;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.danhom1.XMLParser;

// import java.beans.JavaBean;
// import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

// @ConfigurationProperties("Storage")
// @JavaBean
@Component
public class Storage {
    @Getter
    @Setter
    private File file;

    @Getter
    @NonNull
    private String pPath;

    @Getter
    private static final String vPath = "0:/";

    public Storage() {
        NodeList nodes;
        try {
            nodes = XMLParser.parse("src\\main\\java\\com\\example\\danhom1\\Storage\\StorageConfig.xml", "storage");
            if (nodes == null || nodes.getLength() <= 0) {
                throw new StorageException("XML Config Error." + nodes); 
            }
            Element element = (Element) nodes.item(0);
            this.pPath = element.getElementsByTagName("path").item(0).getTextContent();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new StorageException("XML Config Error.", e); 
        }
    }
}
