package eu.miltema.xmlutil.test;

import eu.miltema.xmlutil.*;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class SimpleTest {

	private String loadXml(String xmlFilename) throws Exception {
		var xmlFile = XmlValidator.class.getClassLoader().getResource(xmlFilename);
		if (xmlFile == null)
			throw new FileNotFoundException(xmlFilename);
		return new String(Files.readAllBytes(Paths.get(xmlFile.toURI())), StandardCharsets.UTF_8);
	}

	@Test
	public void testThis() throws Exception {
		new XmlValidator().validate(loadXml("test1.xml"), "shiporder.xsd");
	}

	@Test(expected = SAXParseException.class)
	public void testInvalidXml() throws Exception {
		new XmlValidator().validate(loadXml("test2.xml"), "shiporder.xsd");
	}

	@Test(expected = FileNotFoundException.class)
	public void testXsdNotFound() throws Exception {
		new XmlValidator().validate(loadXml("test2.xml"), "shiporderABC.xsd");
	}

	@Test
	public void testImportedXsd() throws Exception {
		new XmlValidator().validate(loadXml("testimport.xml"), "shiporder2.xsd");
	}

	@Test
	public void testFoldersXsdSuccess() throws Exception {
		new XmlValidator(new SubfolderUriComposer("schemas")).
			validate(loadXml("testfolders.xml"), "schemas/mainxsd/shiporder3.xsd");
	}

	@Test(expected = NullPointerException.class)
	public void testFoldersXsdWithIncorrectComposer() throws Exception {
		new XmlValidator().validate(loadXml("testfolders.xml"), "schemas/mainxsd/shiporder3.xsd");
	}
}
