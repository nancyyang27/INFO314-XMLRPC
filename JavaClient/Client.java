// package edu.uw.info314.xmlrpc.server;
import java.util.*;
import java.util.logging.*;

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

/**
 * This approach uses the java.net.http.HttpClient classes, which
 * were introduced in Java11.
 */
public class Client {
    private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private static String port = "";
    private static String host = "";
    public static void main(String... args) throws Exception {
        try {
            port = args[1];
            host = args[0];
            
            // System.out.println(add() == 0);
            // System.out.println(add(1, 2, 3, 4, 5) == 15);
            // // System.out.println(add(2, 2147483647) == 2);
            // System.out.println(subtract(12, 6) == 6);
            // System.out.println(multiply(3, 4) == 12);
            // System.out.println(multiply(1, 2, 3, 4, 5) == 120);
            // System.out.println(modulo(10, 0));
            // try {
            //     divide(10, 0);
            // }
            // System.out.println(add() == 0);
            // System.out.println(add(1, 2, 3, 4, 5) == 15);
            System.out.println(add(2, 4) == 6);
            // System.out.println(subtract(12, 6) == 6);
            // System.out.println(multiply(3, 4) == 12);
            // System.out.println(multiply(1, 2, 3, 4, 5) == 120);
            // System.out.println(divide(10, 5) == 2);
            // System.out.println(modulo(10, 5) == 0);
            // modulo(10, 0);
            // System.out.println(modulo(10, 5) == 0);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        
    }

    public static void checkInteger(int val) {
        if ((long) val > Integer.MAX_VALUE || (long) val < Integer.MIN_VALUE) {
            throw new IllegalArgumentException("number too large");
        }
    }

    public static int add(int lhs, int rhs) throws Exception {
        checkInteger(lhs);
        checkInteger(rhs);
        Document doc = dbf.newDocumentBuilder().newDocument();
        Element methodCall = doc.createElement("methodCall");
        doc.appendChild(methodCall);
        Element methodName = doc.createElement("methodName");
        methodName.setTextContent("add");
        methodCall.appendChild(methodName);
        Element params = doc.createElement("params");
        methodCall.appendChild(params);
        Element param1 = doc.createElement("param");
        params.appendChild(param1);
        Element value1 = doc.createElement("value");
        param1.appendChild(value1);
        Element i4_1 = doc.createElement("i4");
        i4_1.setTextContent(String.valueOf(lhs));
        value1.appendChild(i4_1);
        Element param2 = doc.createElement("param");
        params.appendChild(param2);
        Element value2 = doc.createElement("value");
        param2.appendChild(value2);
        Element i4_2 = doc.createElement("i4");
        i4_2.setTextContent(String.valueOf(rhs));
        value2.appendChild(i4_2);
        // convert the XML document to a string
        String requestBody = toString(doc);
        return getResult(requestBody);
        // return 6;
    }
    public static int add(Integer... params) throws Exception {
        for (Integer num : params) {
            checkInteger((num.intValue()));
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = dbf.newDocumentBuilder().newDocument();
        Element methodCall = doc.createElement("methodCall");
        doc.appendChild(methodCall);
        Element methodName = doc.createElement("methodName");
        methodName.setTextContent("add");
        methodCall.appendChild(methodName);
        Element paramsDoc = doc.createElement("params");
        methodCall.appendChild(paramsDoc);

        for (Integer param : params) {
            Element paramElement = doc.createElement("param");
            paramsDoc.appendChild(paramElement);
            Element valueElement = doc.createElement("value");
            paramElement.appendChild(valueElement);
            Element i4Element = doc.createElement("i4");
            i4Element.setTextContent(param.toString());
            valueElement.appendChild(i4Element);
        }

        String requestBody = toString(doc);
        return getResult(requestBody);
    }
    public static int subtract(int lhs, int rhs) throws Exception {
        checkInteger(lhs);
        checkInteger(rhs);
        Document doc = dbf.newDocumentBuilder().newDocument();
        Element methodCall = doc.createElement("methodCall");
        doc.appendChild(methodCall);
        Element methodName = doc.createElement("methodName");
        methodName.setTextContent("subtract");
        methodCall.appendChild(methodName);
        Element params = doc.createElement("params");
        methodCall.appendChild(params);
        Element param1 = doc.createElement("param");
        params.appendChild(param1);
        Element value1 = doc.createElement("value");
        param1.appendChild(value1);
        Element i4_1 = doc.createElement("i4");
        i4_1.setTextContent(String.valueOf(lhs));
        value1.appendChild(i4_1);
        Element param2 = doc.createElement("param");
        params.appendChild(param2);
        Element value2 = doc.createElement("value");
        param2.appendChild(value2);
        Element i4_2 = doc.createElement("i4");
        i4_2.setTextContent(String.valueOf(rhs));
        value2.appendChild(i4_2);
        // convert the XML document to a string
        String requestBody = toString(doc);
        return getResult(requestBody);

        
    }
    public static int multiply(int lhs, int rhs) throws Exception {
        checkInteger(lhs);
        checkInteger(rhs);
        Document doc = dbf.newDocumentBuilder().newDocument();
        Element methodCall = doc.createElement("methodCall");
        doc.appendChild(methodCall);
        Element methodName = doc.createElement("methodName");
        methodName.setTextContent("multiply");
        methodCall.appendChild(methodName);
        Element params = doc.createElement("params");
        methodCall.appendChild(params);
        Element param1 = doc.createElement("param");
        params.appendChild(param1);
        Element value1 = doc.createElement("value");
        param1.appendChild(value1);
        Element i4_1 = doc.createElement("i4");
        i4_1.setTextContent(String.valueOf(lhs));
        value1.appendChild(i4_1);
        Element param2 = doc.createElement("param");
        params.appendChild(param2);
        Element value2 = doc.createElement("value");
        param2.appendChild(value2);
        Element i4_2 = doc.createElement("i4");
        i4_2.setTextContent(String.valueOf(rhs));
        value2.appendChild(i4_2);
        // convert the XML document to a string
        String requestBody = toString(doc);
        return getResult(requestBody);
        
    }
    public static int multiply(Integer... params) throws Exception {
        for (Integer num : params) {
            checkInteger((num.intValue()));
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = dbf.newDocumentBuilder().newDocument();
        Element methodCall = doc.createElement("methodCall");
        doc.appendChild(methodCall);
        Element methodName = doc.createElement("methodName");
        methodName.setTextContent("multiply");
        methodCall.appendChild(methodName);
        Element paramsDoc = doc.createElement("params");
        methodCall.appendChild(paramsDoc);

        for (Integer param : params) {
            Element paramElement = doc.createElement("param");
            paramsDoc.appendChild(paramElement);
            Element valueElement = doc.createElement("value");
            paramElement.appendChild(valueElement);
            Element i4Element = doc.createElement("i4");
            i4Element.setTextContent(param.toString());
            valueElement.appendChild(i4Element);
        }
        
        String requestBody = toString(doc);
        return getResult(requestBody);
    }
    public static int divide(int lhs, int rhs) throws Exception {
        checkInteger(lhs);
        checkInteger(rhs);
        Document doc = dbf.newDocumentBuilder().newDocument();
        Element methodCall = doc.createElement("methodCall");
        doc.appendChild(methodCall);
        Element methodName = doc.createElement("methodName");
        methodName.setTextContent("divide");
        methodCall.appendChild(methodName);
        Element params = doc.createElement("params");
        methodCall.appendChild(params);
        Element param1 = doc.createElement("param");
        params.appendChild(param1);
        Element value1 = doc.createElement("value");
        param1.appendChild(value1);
        Element i4_1 = doc.createElement("i4");
        i4_1.setTextContent(String.valueOf(lhs));
        value1.appendChild(i4_1);
        Element param2 = doc.createElement("param");
        params.appendChild(param2);
        Element value2 = doc.createElement("value");
        param2.appendChild(value2);
        Element i4_2 = doc.createElement("i4");
        i4_2.setTextContent(String.valueOf(rhs));
        value2.appendChild(i4_2);
        // convert the XML document to a string
        String requestBody = toString(doc);
        // create an HTTP client and post the request to the server
        return getResult(requestBody);
    }
    public static int modulo(int lhs, int rhs) throws Exception {
        checkInteger(lhs);
        checkInteger(rhs);
        Document doc = dbf.newDocumentBuilder().newDocument();
        Element methodCall = doc.createElement("methodCall");
        doc.appendChild(methodCall);
        Element methodName = doc.createElement("methodName");
        methodName.setTextContent("modulo");
        methodCall.appendChild(methodName);
        Element params = doc.createElement("params");
        methodCall.appendChild(params);
        Element param1 = doc.createElement("param");
        params.appendChild(param1);
        Element value1 = doc.createElement("value");
        param1.appendChild(value1);
        Element i4_1 = doc.createElement("i4");
        i4_1.setTextContent(String.valueOf(lhs));
        value1.appendChild(i4_1);
        Element param2 = doc.createElement("param");
        params.appendChild(param2);
        Element value2 = doc.createElement("value");
        param2.appendChild(value2);
        Element i4_2 = doc.createElement("i4");
        i4_2.setTextContent(String.valueOf(rhs));
        value2.appendChild(i4_2);
        String requestBody = toString(doc);
        return getResult(requestBody);
    }

    public static int getResult(String xmlRequestBody) throws Exception{
        // System.out.println(xmlRequestBody);
        String url = "http://" + host + ":" + port + "/RPC";
        URL uri = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml");
        OutputStream output = connection.getOutputStream();
        output.write(xmlRequestBody.getBytes());
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null) {
            responseBuilder.append(line);
        }
        String responseBody = responseBuilder.toString();
        System.out.println(responseBody);
        // do something with the response body
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(responseBody)));

        Element root = document.getDocumentElement();
        if (root.getElementsByTagName("fault").getLength() != 0) {
            if (root.getElementsByTagName("int").item(0).getTextContent().equals("3")) {
                throw new IllegalArgumentException(root.getElementsByTagName("string").item(0).getTextContent());
            } else if (root.getElementsByTagName("int").item(0).getTextContent().equals("1")) {
                throw new ArithmeticException("divide by zero");
            }
        }
        NodeList nodeList = root.getElementsByTagName("i4");
        int value = Integer.parseInt(nodeList.item(0).getTextContent());
        return value;
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
}
