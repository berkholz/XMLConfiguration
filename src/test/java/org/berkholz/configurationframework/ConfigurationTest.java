/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berkholz.configurationframework;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import static org.junit.Assert.*;

/**
 *
 * @author Marcel Berkholz
 */
public class ConfigurationTest {

    private static final Logger LOG = LogManager.getLogger(ConfigurationTest.class.getName());

    private File templateInitFile = null;
    private File templateSaveFile = null;
    private File templateLoadFile = null;
    private File targetInitFile = null;
    private File targetSaveFile = null;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        // loading init_testfile.xml
        try {
            templateInitFile = new File("src/test/java/org/berkholz/configurationframework/init_testfile.xml");
            templateSaveFile = new File("src/test/java/org/berkholz/configurationframework/save_testfile.xml");
            templateLoadFile = new File("src/test/java/org/berkholz/configurationframework/load_testfile.xml");
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage());
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of load method, of class Configuration.
     */
    @Test
    public void testLoad() {
        TestXMLAnnotatedClass testConfig = new TestXMLAnnotatedClass();
        testConfig.setTestVariable("42");

        TestXMLAnnotatedClass testXMLAnnotatedClass = (TestXMLAnnotatedClass) Configuration.load(TestXMLAnnotatedClass.class, templateLoadFile);

        assertEquals(testConfig.getTestVariable(), testXMLAnnotatedClass.getTestVariable());
    }

    /**
     * Test of save method, of class Configuration.
     */
    @Test
    public void testSave_3args() {
        Boolean result = null;
        TestXMLAnnotatedClass testConfig = new TestXMLAnnotatedClass();

        // creating instance of configuration
        Configuration instance = new Configuration();

        try {
            // this file is initialized
            targetSaveFile = File.createTempFile("pabamo_targetSaveFile-", ".xml");
//            targetSaveFile = new File(File.createTempFile("pabamo_targetSaveFile-", ".xml").getParent() + File.separator + "pabamo_targetSaveFile.xml");
            targetSaveFile.deleteOnExit();

        } catch (IOException ex) {
            LOG.error(ex);
        }

        if (targetSaveFile.exists()) {
            // initialize our testfile(here: targetInitFile)
            LOG.trace("Saving file: " + targetSaveFile.getAbsolutePath());
            Configuration.save(testConfig, targetSaveFile, true);
        } else {
            LOG.error("targetSaveFile: \"" + targetSaveFile + "\" does not exist.");
        }

        try {
            LOG.trace("Starting comparation of saved files.");
            result = FileUtils.contentEquals(templateSaveFile, targetSaveFile);
            LOG.trace("Result of file comparation: " + result);
        } catch (IOException ex) {
            LOG.error(ex);
        }
        assertEquals(true, result);
//        fail("The test case is not implemented, yet.");
    }

    /**
     * Test of init method, of class Configuration.
     */
    @Test
    public void testInit() {
        LOG.trace("Testing init method.");

        // creating instance of configuration
        Configuration instance = new Configuration();

        try {
            // this file is initialized
            File tmpFile = File.createTempFile("pabamo_targetInitFile-", ".xml");
            targetInitFile = new File(tmpFile.getParent() + File.separator + "pabamo_targetInitFile.xml");
            targetInitFile.deleteOnExit();
            tmpFile.deleteOnExit();
        } catch (IOException ex) {
            LOG.error(ex);
        }

        // initialize our testfile(here: targetInitFile)
        Boolean resultOfInit = instance.init(targetInitFile);
        Boolean compareResult = null;

        // we compare both files with Apache Commons IO library
        try {
            compareResult = FileUtils.contentEquals(templateInitFile, targetInitFile);
        } catch (IOException ex) {
            LOG.error(ex);
        }

        // both, init and comparation of files have to be successful
        assertEquals(true, resultOfInit && compareResult);
    }

    /**
     * Test of isValid method, of class Configuration.
     */
    @Test
    public void testIsValid() {
        // TODO: should be implemented
        assertTrue("The test case is not implemented, yet.", true);
    }

}
