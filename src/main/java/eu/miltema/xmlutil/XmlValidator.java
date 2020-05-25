package eu.miltema.xmlutil;

import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.*;
import java.io.*;
import java.util.function.*;

public class XmlValidator {

	private BiFunction<String, String, String> xsdUriComposer;

	/**
	 * Use this constructor if:
	 * 1) You only have a single XSD file or
	 * 2) if all XSD files are located in classpath root folder
	 */
	public XmlValidator() {
		xsdUriComposer = (referencingXsdUri, xsdName) -> xsdName;
	}

	/**
	 * Use this constructor if Your XSD files are not in classpath root folder.
	 * You most likely can use SubfolderUriComposer; if that does not help, You have to use a custom composer
	 * @param xsdUriComposer combines referencing XSD file URI and XSD file name into a new URI
	 */
	public XmlValidator(BiFunction<String, String, String> xsdUriComposer) {
		this.xsdUriComposer = xsdUriComposer;
	}

	/**
	 * @param xml XML document as a string, for example <abc>my demo content</abc>
	 * @param xsdFilename XSD file name in classpath. If the XSD refers to another XSD file, that file too has to be located in the classpath
	 * @throws Exception when validation fails
	 */
	public void validate(String xml, String xsdFilename) throws Exception {

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setNamespaceAware(true);

		DocumentBuilder parser = builderFactory.newDocumentBuilder();

		// parse the XML into a document object
		Document document = parser.parse(new ByteArrayInputStream(xml.getBytes()));

		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// associate the schema factory with the resource resolver, which is responsible for resolving the imported XSD's
		factory.setResourceResolver(new ResourceResolver(xsdUriComposer));

		// note that if your XML already declares the XSD to which it has to conform, then there's no need to create a validator from a Schema object
		var xsdStreamUrl = this.getClass().getClassLoader().getResource(xsdFilename);
		if (xsdStreamUrl == null)
			throw new FileNotFoundException(xsdFilename);
		Schema schema = factory.newSchema(xsdStreamUrl);

		Validator validator = schema.newValidator();
		validator.validate(new DOMSource(document));
	}
}
