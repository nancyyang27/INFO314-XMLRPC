package edu.uw.info314.xmlrpc.server;
import java.util.*;
import java.util.logging.*;

import static spark.Spark.*;
// import org.apache.xmlrpc.XmlRpcResponse;
import java.io.*;
import java.net.*;
import java.net.http.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


class Call {
    public String name;
    public List<Object> args = new ArrayList<Object>();
}

public class App {
    private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    public static final Logger LOG = Logger.getLogger(App.class.getCanonicalName());
    public static void main(String[] args) throws UnknownHostException {
        LOG.info("Starting up on port 8080");
        port(8080);

        // define a handler for the /RPC endpoint
    
        post("/RPC", (request, response) -> {
        
            String requestBody = request.body();
            
            try {
                Call call = extractXMLRPCCall(requestBody);
                Object result = myHandlerMapping(call.name, call.args);
                response.type("text/xml");
                response.status(200);
                response.header("Host", InetAddress.getLocalHost().getHostName()); // set the Host headers
                return generateStringCorrect((int) result);
            } catch (IllegalArgumentException e) {
                response.type("text/xml");
                response.status(200);
                response.header("Host", InetAddress.getLocalHost().getHostName()); // set the Host header
                return generateStringWrong(3, e.getMessage());
            } catch (ArithmeticException e) {
                // System.out.println("aaa");
                response.type("text/xml");
                response.status(200);
                response.header("Host", InetAddress.getLocalHost().getHostName());
                return generateStringWrong(1, "divide by zero");
            }
        });
        notFound((request, response) -> {
            response.status(404);
            response.type("text/xml");
            response.header("Host", InetAddress.getLocalHost().getHostName());
            return "404 Not Found";
        });

        // define a handler for all other HTTP methods, which returns a 405 Method Not Allowed error
        before((request, response) -> {
            if (!request.requestMethod().equals("POST")) {
                response.status(405);
                response.header("Allow", "POST");
                halt();
            }
        });
       
    }
    public static String generateStringCorrect(int num) throws Exception {
        // Create a new document
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();

        // Create the root element
        Element methodResponse = doc.createElement("methodResponse");
        doc.appendChild(methodResponse);

        // Create the params element
        Element params = doc.createElement("params");
        methodResponse.appendChild(params);

        // Create the param element
        Element param = doc.createElement("param");
        params.appendChild(param);

        // Create the value element
        Element value = doc.createElement("value");
        param.appendChild(value);

        // Create the integer element
        Element integer = doc.createElement("i4");
        integer.appendChild(doc.createTextNode(num + ""));
        value.appendChild(integer);

        // Convert the document to a string
        String xmlString = toString(doc);
        return xmlString;
    }

    public static String generateStringWrong(int code, String message) throws Exception {

        // Create the XML document
        // DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbf.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        // Create the root element and add it to the document
        Element root = doc.createElement("methodResponse");
        doc.appendChild(root);

        // Create the fault element and add it to the root
        Element fault = doc.createElement("fault");
        root.appendChild(fault);

        // Create the value element and add it to the fault
        Element value = doc.createElement("value");
        fault.appendChild(value);

        // Create the struct element and add it to the value
        Element struct = doc.createElement("struct");
        value.appendChild(struct);

        // Create the faultCode element and add it to the struct
        Element faultCode = doc.createElement("member");
        struct.appendChild(faultCode);

        Element faultCodeName = doc.createElement("name");
        faultCodeName.setTextContent("faultCode");
        faultCode.appendChild(faultCodeName);

        Element faultCodeValue = doc.createElement("value");
        Element faultCodeInt = doc.createElement("int");
        faultCodeInt.setTextContent(code + "");
        faultCodeValue.appendChild(faultCodeInt);
        faultCode.appendChild(faultCodeValue);

        // Create the faultString element and add it to the struct
        Element faultString = doc.createElement("member");
        struct.appendChild(faultString);

        Element faultStringName = doc.createElement("name");
        faultStringName.setTextContent("faultString");
        faultString.appendChild(faultStringName);

        Element faultStringValue = doc.createElement("value");
        Element faultStringStr = doc.createElement("string");
        faultStringStr.setTextContent(message);
        faultStringValue.appendChild(faultStringStr);
        faultString.appendChild(faultStringValue);

        // Output the XML as a string
        String xmlString = toString(doc);
        return xmlString;
    }

    // Convert a Document object to a String
    public static String toString(Document doc) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer.toString();
    }
    

    public static Call extractXMLRPCCall(String requestBody) throws Exception {
        // System.out.println(requestBody);
        Call call = new Call();
        DocumentBuilder builder = dbf.newDocumentBuilder();

        // parse the request body into an XML document
        Document document = builder.parse(new InputSource(new StringReader(requestBody)));

        // get the root element of the XML document (should be <methodCall>)
        Element root = document.getDocumentElement();

        // get the method name (should be the content of the <methodName> element)
        String methodName = root.getElementsByTagName("methodName").item(0).getTextContent();
        // System.out.println(methodName);
        // get the parameters (should be the content of the <param> elements)
        NodeList paramNodes = root.getElementsByTagName("param");
        if (paramNodes.getLength() == 0) {
            if (methodName.equals("add")) {
                call.name = "add";
                call.args = null;
            } else {
                call.name = "multiply";
                call.args = null;
            }
            return call;
        }
        List<Object> params = new ArrayList<>();
        for (int i = 0; i < paramNodes.getLength(); i++) {
            Element param = (Element) paramNodes.item(i);
            Element value = (Element) param.getElementsByTagName("value").item(0);
            Node valueNode = value.getFirstChild();
           
            if (value.getElementsByTagName("i4").getLength() != 0) {
                String intValue = value.getElementsByTagName("i4").item(0).getTextContent();
                // LOG.info(intValue);
                params.add(Integer.parseInt(intValue));
            } else {
                throw new IllegalArgumentException("Illegal argument type");
            }
        }
        
        call.name = methodName;
        call.args = params;
        return call;
    }
    public static Object myHandlerMapping(String method, List<Object> params) throws ArithmeticException {
        Calc calc = new Calc();
        // System.out.println(params);
        if (method.equals("add")) {
            if (params == null) {
                return Integer.valueOf(0);
            }
            
            int[] intArray = new int[params.size()];
            for (int i = 0; i < params.size(); i++) {
                // System.out.println((int) params.get(i));
                intArray[i] = (int) params.get(i);
            }
            int result = calc.add(intArray);
            // System.out.println(result);
            return Integer.valueOf(result);
        } else if (method.equals("subtract")) {
            int result = calc.subtract((int) params.get(0), (int) params.get(1));
            return Integer.valueOf(result);
        } else if (method.equals("multiply")) {
            // System.out.println("aaa");
            if (params == null) {
                return Integer.valueOf(0);
            }
            
            int[] intArray = new int[params.size()];
            for (int i = 0; i < params.size(); i++) {
                intArray[i] = (int) params.get(i);
            }
            int result = calc.multiply(intArray);
            return Integer.valueOf(result);
        } else if (method.equals("modulo")) {
            if ((int) params.get(1) == 0) {
                throw new ArithmeticException("divide by zero");
            }
            int result = calc.modulo((int) params.get(0), (int) params.get(1));
            return Integer.valueOf(result);
        } else {
            if ((int) params.get(1) == 0) {
                throw new ArithmeticException("divide by zero");
            }
            int result = calc.divide((int) params.get(0), (int) params.get(1));
            return Integer.valueOf(result);
        }
    }
}
