package uk.co.codera.lang.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JaxbAnnotatedObject {

    private String stringAttribute;

    public JaxbAnnotatedObject() {
        super();
    }

    private JaxbAnnotatedObject(Builder builder) {
        this();
        this.stringAttribute = builder.stringAttribute;
    }

    public static Builder aJaxbAnnotatedObject() {
        return new Builder();
    }

    public void setStringAttribute(String attribute) {
        this.stringAttribute = attribute;
    }

    public String getStringAttribute() {
        return this.stringAttribute;
    }

    public static class Builder {

        private String stringAttribute;

        private Builder() {
            super();
        }

        public Builder stringAttribute(String attribute) {
            this.stringAttribute = attribute;
            return this;
        }

        public JaxbAnnotatedObject build() {
            return new JaxbAnnotatedObject(this);
        }
    }
}