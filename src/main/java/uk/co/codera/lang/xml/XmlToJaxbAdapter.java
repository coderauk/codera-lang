package uk.co.codera.lang.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import uk.co.codera.lang.Adapter;

public class XmlToJaxbAdapter<O> implements Adapter<String, O> {

    private final Unmarshaller unmarshaller;
    private final Class<O> declaredType;

    public XmlToJaxbAdapter(Class<O> clazz) {
        this.declaredType = clazz;
        try {
            this.unmarshaller = JAXBContext.newInstance(clazz).createUnmarshaller();
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public O adapt(String input) {
        try {
            return toJaxbExceptionally(input);
        } catch (JAXBException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private O toJaxbExceptionally(String input) throws JAXBException, IOException {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(input.getBytes())) {
            StreamSource streamSource = new StreamSource(byteStream);
            JAXBElement<O> jaxbElement = this.unmarshaller.unmarshal(streamSource, this.declaredType);
            return jaxbElement.getValue();
        }
    }
}