package eu.miltema.xmlutil;

import java.util.function.*;

/**
 * Use this URI composer if all Your XSD files are located in classpath under a common base folder,
 * for example under /schemas
 */
public class SubfolderUriComposer implements BiFunction<String, String, String> {

	private String commonBaseFolder;

	/**
	 * @param commonBaseFolder a folder in Your main/resources folder.
	 *                         For example, if Your XSD files are located in main/resources/schemas, pass "schemas" as commonBaseFolder
	 */
	public SubfolderUriComposer(String commonBaseFolder) {
		this.commonBaseFolder = commonBaseFolder.trim();
		if (this.commonBaseFolder.endsWith("/"))
			this.commonBaseFolder = this.commonBaseFolder.substring(0, this.commonBaseFolder.length() - 1);
		if (this.commonBaseFolder.startsWith("/"))
			this.commonBaseFolder = this.commonBaseFolder.substring(1);
	}

	private String getParent(String path) {
		return path.substring(0, path.lastIndexOf('/'));
	}

	@Override
	public String apply(String referencingURI, String xsdName) {
		var baseUri = getParent(referencingURI.substring(referencingURI.indexOf(commonBaseFolder + "/")));
		var path = xsdName;
		while(path.startsWith("../")) {
			path = path.substring(3);
			baseUri = getParent(baseUri);
		}
		return baseUri + "/" + path;
	}
}
