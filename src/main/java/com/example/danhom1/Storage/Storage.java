package com.example.danhom1.Storage;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import com.example.danhom1.Exception.StorageException;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.danhom1.XMLParser;

// import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;

// @ConfigurationProperties("Storage")
@Getter
@Component
public class Storage {
    @NonNull
    private final String pPath;

    private final String vPath = "0:/";

    //limit in MB
    private final Float defaultLimit;

    public Storage() {
        NodeList nodes;
        try {
            nodes = XMLParser.parse("src\\main\\java\\com\\example\\danhom1\\Storage\\StorageConfig.xml", "storage");
            if (nodes == null || nodes.getLength() <= 0) {
                throw new StorageException("XML Config Error." + nodes);
            }
            Element element = (Element) nodes.item(0);
            this.pPath = element.getElementsByTagName("path").item(0).getTextContent();
            this.defaultLimit = Float.valueOf(element.getElementsByTagName("limit").item(0).getTextContent());
        } catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
            throw new StorageException("XML Config Error.", e); 
        }
    }
}
