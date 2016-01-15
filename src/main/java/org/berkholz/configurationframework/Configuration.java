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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
public class Configuration {
    
    private static final Logger LOG = LogManager.getLogger(Configuration.class.getName());

    /**
     * Load a XML configuration file in a generic way and return it as object,
     * so that you can cast it to the class you know.
     *
     * @param classType The class of the object that the XML configuration file
     * is mapped to. Wrong class will throw an exception.
     * @param configurationFile File object where the configuration is stored.
     * @return return the object representation of the XML configuration file.
     */
    public static Object load(Class classType, File configurationFile) {
        JAXBContext context;
        if (configurationFile.exists()) {
            LOG.info("Loading configuration file {0}", configurationFile.getAbsolutePath());
            try {
                context = JAXBContext.newInstance(classType);
                Unmarshaller ums = context.createUnmarshaller();
                return ums.unmarshal(configurationFile);
            } catch (JAXBException ex) {
                LOG.error(ex.getLocalizedMessage(), ex);
            }
        } else {
            LOG.error("Cannot load configuration file {0}. It does not exist.", configurationFile.getAbsolutePath());
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
    public static void save(Object instance, File configurationFile) {
        if (configurationFile.exists()) {
            LOG.info(MessageFormat.format("File {0} allready exists, overwriting it...", configurationFile.getAbsolutePath()));
        }
        
        try {
            LOG.trace("Instanciating new JAXB context.");
            JAXBContext context = JAXBContext.newInstance(instance.getClass());
            
            LOG.trace("Create Marshaller with JAXB context.");
            Marshaller ms = context.createMarshaller();
            
            LOG.trace("Setting property of JAXB_FORMATTED_OUTPUT to true");
            ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            
            LOG.trace("Marshallering.");
            ms.marshal(instance, configurationFile);
            LOG.info("Saving configuration to file {0}", configurationFile.getAbsolutePath());
        } catch (PropertyException ex) {
            LOG.error(ex.getLocalizedMessage(), ex);
        } catch (JAXBException ex) {
            LOG.error(ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * Create a configuration property file for a time sheet.
     *
     * @param xmlPropertyFile File object to the configuration property file.
     * @return True if file exists or were created, otherwise false.
     */
    public Boolean init(File xmlPropertyFile) {
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
                LOG.error(e.getLocalizedMessage(), e);
            }
        }
        return false;
    }

    /**
     *
     * @param xmlPropertyFile
     * @return True if the configuration is valid, otherwise false.
     */
    public static boolean isValid(File xmlPropertyFile) {
        throw new UnsupportedOperationException("Not implemented, yet.");
    }
}
