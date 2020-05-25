package eu.miltema.xmlutil;

import org.apache.xerces.dom.DOMInputImpl;
import org.w3c.dom.ls.*;

import java.io.InputStream;
import java.util.Objects;
import java.util.function.BiFunction;

class ResourceResolver implements LSResourceResolver {

	private BiFunction<String, String, String> xsdUriComposer;

	ResourceResolver(BiFunction<String, String, String> xsdUriComposer) {
		this.xsdUriComposer = xsdUriComposer;
	}

	@Override
	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
		String path = xsdUriComposer.apply(baseURI, systemId);
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(path);
		Objects.requireNonNull(resourceAsStream, String.format("Could not find the specified xsd file: %s", systemId));
		return new DOMInputImpl(publicId, systemId, baseURI, resourceAsStream, "UTF-8");
	}
}
