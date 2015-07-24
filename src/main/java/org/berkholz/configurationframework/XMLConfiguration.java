/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berkholz.configurationframework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 * Class for managing generic XML configuration files.
 *
 * @author Marcel Berkholz
 */
public class XMLConfiguration {

    private static final Logger LOG = Logger.getLogger(XMLConfiguration.class.getName());

    /**
     * Returns a pseudo-random number between min and max, inclusive. The
     * difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value. Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    private static int getRandomInteger(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Gets the path of a temporary directory.
     *
     * @return Complete path of a temporary directory.
     */
    public static String getTempDirectory() {
        try {
            File file = File.createTempFile("tmpfile_" + getRandomInteger(1000, 9999), ".tmp");
            file.delete();
            return file.getParent() + file.separator;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    /**
     * Load a XML configuration file in a generic way and return it as object,
     * so that you can cast it to the class you know.
     *
     * @param classType The class of the object that the XML configuration file
     * is mapped to. Wrong class will throw an exception.
     * @param configurationFile File object where the configuration is stored.
     * @return return the object representation of the XML configuration file.
     */
    public static Object loadConfigurationFile(Class classType, File configurationFile) {
        JAXBContext context;
        if (configurationFile.exists()) {
            LOG.log(Level.INFO, "Loading configuration file {0}", configurationFile.getAbsolutePath());
            try {
                context = JAXBContext.newInstance(classType);
                Unmarshaller ums = context.createUnmarshaller();
                return ums.unmarshal(configurationFile);
            } catch (JAXBException ex) {
                LOG.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
        } else {
            LOG.log(Level.SEVERE, "Cannot load configuration file {0}. It does not exist.", configurationFile.getAbsolutePath());
        }
        return null;
    }

    /**
     * Saves an object to an XML configuration file.
     *
     * @param instance This instance of an object will be saved to a XML
     * configuration file.
     * @param configurationFile The File object where the instance of an object
     * shall be stored.
     */
    public static void saveConfigurationFile(Object instance, File configurationFile) {
        if (configurationFile.exists()) {
            LOG.log(Level.INFO, MessageFormat.format("File {0} allready exists, overwriting it...", configurationFile.getAbsolutePath()));
        }

        try {
            JAXBContext context = JAXBContext.newInstance(instance.getClass());
            Marshaller ms = context.createMarshaller();
            ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            ms.marshal(instance, configurationFile);
            LOG.log(Level.INFO, "Saving configuration to file {0}", configurationFile.getAbsolutePath());
        } catch (PropertyException ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        } catch (JAXBException ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * Create a configuration property file for a time sheet.
     *
     * @param xmlPropertyFile File object to the configuration property file.
     * @return True if file exists or were created, otherwise false.
     */
    public Boolean createDefaultConfigurationFile(File xmlPropertyFile) {
        Properties serverProperties;
        if (xmlPropertyFile.exists()) {
            // TODO: check if content exists. only the existing file will result in an error.
            serverProperties = new Properties();
            return true;
        } else {
            try (BufferedWriter output = new BufferedWriter(new FileWriter(xmlPropertyFile))) {
                output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\n");
                output.close();
                return true;
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
        return false;
    }

    /**
     *
     * @param xmlPropertyFile
     * @return True if the configuration is valid, otherwise false.
     */
    public static boolean isXMLConfigurationFileValid(File xmlPropertyFile) {
        // TODO: validate xmlPropertyFile
        return true;
    }
}
