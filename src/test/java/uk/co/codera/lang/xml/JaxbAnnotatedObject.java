package uk.co.codera.lang.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JaxbAnnotatedObject {

    private String stringAttribute;

    public void setStringAttribute(String attribute) {
        this.stringAttribute = attribute;
    }

    public String getStringAttribute() {
        return this.stringAttribute;
    }
}