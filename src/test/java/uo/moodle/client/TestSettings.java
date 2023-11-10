package uo.moodle.client;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestSettings {
    public static String clientSertificate = null;
    public static String serverAddress = null;

    // NOTE: Test users are read from the test config XML files.
    public static String existingUser = null;
    public static String existingPassword = null;
    public static String existingEmail = null;



    public static boolean readSettingsXML(String xmlFile) {
        try {

            if (xmlFile == null) {
                System.out.println("No xml config file selected");
                System.exit(-1);
            }
            System.out.println("Test setting file: " + xmlFile);
            File fXmlFile = new File(xmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            if (!doc.getDocumentElement().getNodeName().equals("testconfig")) {
                System.out.println("testconfig xml file invalid");
                System.exit(-1);
            }


            NodeList node = doc.getElementsByTagName("servercertificate");
            if (node != null && node.getLength() > 0) {
                clientSertificate = doc.getElementsByTagName("servercertificate").item(0).getTextContent();
                if (null != clientSertificate && clientSertificate.trim().length() == 0) {
                    clientSertificate = null;
                }
            }


            System.out.println(clientSertificate);

            
            NodeList node2 = doc.getElementsByTagName("serveraddress");
            if (node2 != null && node2.getLength() > 0) {
                serverAddress = doc.getElementsByTagName("serveraddress").item(0).getTextContent();
                if (null != serverAddress && serverAddress.trim().length() == 0) {
                    serverAddress = null;
                }
            }

            NodeList usersList = doc.getElementsByTagName("user");
                Node user = usersList.item(0);
                if (user.getNodeType() == Node.ELEMENT_NODE) {
                    Element userElement = (Element) user;
                        existingUser = userElement.getElementsByTagName("username").item(0).getTextContent();
                        existingPassword = userElement.getElementsByTagName("password").item(0).getTextContent();
                        existingEmail = userElement.getElementsByTagName("email").item(0).getTextContent();

                    } 

        } catch (IOException e) {
            System.out.println("test user XML file does not exist or is invalid: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("test user XML file does not exist or is invalid: " + e.getMessage());
            return false;
        }
        return (existingUser != null && 
                existingPassword != null); 
    }


        public String getUsername() {
            return TestSettings.existingUser;
        }


        public String getPassword() {
            return TestSettings.existingPassword;
        }

        public String getNick() {
            return TestSettings.existingUser;
        }

        public String getServerAddress(){
            return TestSettings.serverAddress;
        }

        public String getCertificate(){
            return TestSettings.clientSertificate;
        }


        public String getEmail() {
            return TestSettings.existingEmail;
        }

}