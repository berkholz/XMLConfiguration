# Configuration
Class for simple XML configuration file management.

# Dependencies
- Apache log4j 2.11.1
- JUnit 4.12
- Apache Common IO 2.4

# Licenses
This software is distributed under the GNU Public License 2.

## Used projects
### Apache log4j & Apache Common IO 
Both are under the Apache License 2.0 (http://www.apache.org/licenses/LICENSE-2.0.txt)

### JUnit 
JUnit is under the Eclipse Public License 1.0 (http://junit.org/license.html or http://www.eclipse.org/org/documents/epl-v10.html

# Usage
- Clone git repository

- Open and build it with Netbeans(tested with 8.1)

- Add to your Maven project the following dependency: 
		
		<dependency>
           		<groupId>${project.groupId}</groupId>
           		<artifactId>Configuration</artifactId>
           		<version>0.1</version>
       	</dependency>
		
- Add import in your java class:

		import org.berkholz.configurationframework.Configuration;

- Create a java xml definition class:

		@XmlRootElement(name = "DummyConfiguration")
		public class DummyConfiguration {
		
			@XmlElement(name = "Variable")
			private final String variable;
			
			public DummyConfiguration() {
				variable = "test";
			}

			public String getVariable(){
				return variable;
			}
		}

- For loading a xml configuration:
		
		DummyConfiguration mainConfig = (DummyConfiguration) Configuration.load(DummyConfiguration.class, new File("yourXMLConfigurationFile.conf.xml"));

- For saving a xml configuration to file:
		
		Configuration.save(new DummyConfiguration(), new File("yourXMLConfigurationFile.conf.xml"));
