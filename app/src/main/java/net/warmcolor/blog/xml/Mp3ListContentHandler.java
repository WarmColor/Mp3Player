package net.warmcolor.blog.xml;

import net.warmcolor.blog.model.Mp3Info;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class Mp3ListContentHandler extends DefaultHandler {
    private List<Mp3Info> infos = null;
    private Mp3Info mp3Info = null;
    private String tagName = null;

    public Mp3ListContentHandler(List<Mp3Info> infos) {
        this.infos = infos;
    }

    public List<Mp3Info> getInfos() {
        return infos;
    }

    public void setInfos(List<Mp3Info> infos) {
        this.infos = infos;
    }

    public void startDocument() throws SAXException {
        super.startDocument();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        this.tagName = localName;
        if (tagName.equals("resource")) {
            mp3Info = new Mp3Info();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        String temp = new String(ch, start, length);
        if (tagName.equals("id")) {
            mp3Info.setId(temp);
        } else if (tagName.equals("mp3.name")) {
            mp3Info.setMp3Name(temp);
        } else if (tagName.equals("mp3.size")) {
            mp3Info.setMp3Size(temp);
        } else if (tagName.equals("lrc.name")) {
            mp3Info.setLrcName(temp);
        } else if (tagName.equals("lrc.size")) ;
    }


    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals("resource")) {
            infos.add(mp3Info);
        }
        tagName = "";
    }

    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
