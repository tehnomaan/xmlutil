# XmlUtil

XmlUtil is a lightweight collection of Java XML-related utils.

# XML Validation

To use XML validation, schema files (XSD) have to be accessible in classpath. Basic usage:

```
new XmlValidator().validate("<abc>Just testing</abc>", "shiporder.xsd");
```

Usage with imported/included XSD's in a classpath subfolder:

```
new XmlValidator(new SubfolderUriComposer("schemas")).
	validate("<abc>Just testing</abc>", "schemas/shiporder.xsd");

```

# Dependencies

Add SlimORM dependency into build.gradle:

```gradle
dependencies {
    implementation 'eu.miltema:xmlutil:0.0.1'
}
```
XmlUtil itself depends on xerces:xercesImpl. This is resolved by build system automatically.

