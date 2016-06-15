package uk.co.codera.lang.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;

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
        JaxbAnnotatedObject original = JaxbAnnotatedObject.aJaxbAnnotatedObject().stringAttribute("here is the news")
                .build();
        JaxbAnnotatedObject marshalled = toXmlAndBack(original);
        assertThat(EqualsBuilder.reflectionEquals(original, marshalled), is(true));
    }

    private JaxbAnnotatedObject toXmlAndBack(JaxbAnnotatedObject original) {
        return toJaxbAdapter.adapt(toXmlAdapter.adapt(original));
    }
}