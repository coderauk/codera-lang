package uk.co.codera.lang.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import uk.co.codera.lang.Adapter;

public class JaxbXmlAdapter<I> implements Adapter<I, String> {

    private final Marshaller marshaller;

    public JaxbXmlAdapter(Class<I> clazz) {
        try {
            this.marshaller = JAXBContext.newInstance(clazz).createMarshaller();
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public String adapt(I input) {
        try {
            return toXmlExceptionally(input);
        } catch (JAXBException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String toXmlExceptionally(I input) throws JAXBException, IOException {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            this.marshaller.marshal(input, stream);
            return stream.toString();
        }
    }
}