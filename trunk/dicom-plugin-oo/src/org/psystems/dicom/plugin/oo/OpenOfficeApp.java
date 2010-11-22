package org.psystems.dicom.plugin.oo;

public class OpenOfficeApp {
	public static void main(String[] args) {
		// Call the bootstrap to get the Component context
		com.sun.star.uno.XComponentContext oComponentContext = null;
		try {
			oComponentContext = com.sun.star.comp.helper.Bootstrap.bootstrap();
		} catch (com.sun.star.comp.helper.BootstrapException ex) {
			System.out.println(ex.getMessage());
		}

		if (oComponentContext != null) {
			try {
				// Get the service manager
				com.sun.star.lang.XMultiComponentFactory oMultiComponentFactory = oComponentContext
						.getServiceManager();
				// Create a new desktop instance
				Object oDesktop = oMultiComponentFactory
						.createInstanceWithContext(
								"com.sun.star.frame.Desktop", oComponentContext);
				// Create a new component loader within our desktop
				com.sun.star.frame.XComponentLoader oComponentLoader = (com.sun.star.frame.XComponentLoader) com.sun.star.uno.UnoRuntime
						.queryInterface(
								com.sun.star.frame.XComponentLoader.class,
								oDesktop);
				// Create a blank writer document
				com.sun.star.lang.XComponent oComponent = oComponentLoader
						.loadComponentFromURL("private:factory/swriter", // Blank
																			// document
								"_blank", // new frame
								0, // no search flags
								// No additional aruments
								new com.sun.star.beans.PropertyValue[0]);
				// Get the textdocument
				com.sun.star.text.XTextDocument oTextDocument = (com.sun.star.text.XTextDocument) com.sun.star.uno.UnoRuntime
						.queryInterface(com.sun.star.text.XTextDocument.class,
								oComponent);
				// Insert some text
				oTextDocument.getText().setString("Hello I'm the text");
				// Create a storable object
				com.sun.star.frame.XStorable oStorable = (com.sun.star.frame.XStorable) com.sun.star.uno.UnoRuntime
						.queryInterface(com.sun.star.frame.XStorable.class,
								oComponent);
				// Store the document
				oStorable.storeToURL("file:///F:/odtFiles/test2.odt",
				// No additional aruments
						new com.sun.star.beans.PropertyValue[0]);
				System.out.println("Document created and saved");
			} catch (Exception ex) {
				System.out.println("An exception occurs " + ex.getMessage());
			}
		}
	}
}