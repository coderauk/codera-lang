package uk.co.codera.lang.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;

import uk.co.codera.lang.xml.JaxbAnnotatedObject.Builder;

public class XmlToJaxbAdapterTest {

    private XmlToJaxbAdapter<JaxbAnnotatedObject> toJaxbAdapter;
    private JaxbToXmlAdapter<JaxbAnnotatedObject> toXmlAdapter;

    @Before
    public void before() {
        this.toJaxbAdapter = new XmlToJaxbAdapter<>(JaxbAnnotatedObject.class);
        this.toXmlAdapter = new JaxbToXmlAdapter<>(JaxbAnnotatedObject.class);
    }

    @Test
    public void shouldBeAbleToRoundTripToXmlAndBack() {
        JaxbAnnotatedObject original = aValidJaxbAnnotatedObject().build();
        JaxbAnnotatedObject marshalled = toXmlAndBack(original);
        assertThat(EqualsBuilder.reflectionEquals(original, marshalled), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfXmlIsNotWellFormed() {
        toJaxb("<not xml");
    }

    private Builder aValidJaxbAnnotatedObject() {
        return JaxbAnnotatedObject.aJaxbAnnotatedObject().stringAttribute("here is the news");
    }

    private JaxbAnnotatedObject toXmlAndBack(JaxbAnnotatedObject original) {
        return toJaxb(toXmlAdapter.adapt(original));
    }

    private JaxbAnnotatedObject toJaxb(String xml) {
        return this.toJaxbAdapter.adapt(xml);
    }
}