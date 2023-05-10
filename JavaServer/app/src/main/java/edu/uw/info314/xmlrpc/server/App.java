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
                // String responseBody = XmlRpcResponse.serialize(result);
                // response.type("text/xml");
                // response.status(200);
                // response.header("Host", InetAddress.getLocalHost().getHostName()); // set the Host header
                // return responseBody;
                return "";
            } catch (IllegalArgumentException e) {
                // String faultCode = "3";
                // String faultString = "illegal argument type";
                // String responseBody = XmlRpcResponse.serializeFault(faultCode, faultString);
                // response.type("text/xml");
                // response.status(500);
                // response.header("Host", InetAddress.getLocalHost().getHostName()); // set the Host header
                // // return responseBody;
                return "";
            } catch (ArithmeticException e) {
                return "";
            } catch (LargeNumberException e) {
                return "";
            }
        });
        notFound((request, response) -> {
            response.status(404);
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
    

    public static Call extractXMLRPCCall(String requestBody) throws Exception {
        Call call = new Call();
        DocumentBuilder builder = dbf.newDocumentBuilder();

        // parse the request body into an XML document
        Document document = builder.parse(new InputSource(new StringReader(requestBody)));

        // get the root element of the XML document (should be <methodCall>)
        Element root = document.getDocumentElement();

        // get the method name (should be the content of the <methodName> element)
        String methodName = root.getElementsByTagName("methodName").item(0).getTextContent();

        // get the parameters (should be the content of the <param> elements)
        NodeList paramNodes = root.getElementsByTagName("param");
        if (paramNodes.getLength() == 0) {
            if (methodName.equals("add")) {
                call.name = "add";
                call.args = new ArrayList<>();
            } else {
                call.name = "multiply";
                call.args = new ArrayList<>();
            }
            return call;
        }
        Object[] params = new Object[paramNodes.getLength()];
        for (int i = 0; i < paramNodes.getLength(); i++) {
            Element paramElement = (Element) paramNodes.item(i);
            Node valueNode = paramElement.getElementsByTagName("value").item(0);
            Node childNode = valueNode.getFirstChild();
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                String nodeName = childElement.getNodeName();
                if ("i4".equals(nodeName)) {
                    String value = childElement.getTextContent();
                    try {
                        params[i] = Integer.parseInt(value);
                    } catch (Exception e) {
                        throw new LargeNumberException("Number is too large");
                    }
                } else {
                    throw new IllegalArgumentException("Illegal argument type");
                }
            } else if (childNode.getNodeType() == Node.TEXT_NODE) {
                String value = childNode.getNodeValue();
                params[i] = Integer.parseInt(value);
            } else {
                throw new IllegalArgumentException("Illegal argument type");
            }
        }
        
        call.name = methodName;
        call.args = Arrays.asList(params);
        return call;
    }


    public static Object myHandlerMapping(String method, List<Object> params) throws ArithmeticException {
        Calc calc = new Calc();
        if (method.equals("add")) {
            if (params.size() != 2) {
                int result = calc.add((int) params.get(0), (int) params.get(1));
                return Integer.valueOf(result);
            } 
            if (params.size() == 0) {
                return Integer.valueOf(0);
            }
            int[] intArray = new int[params.size()];
            for (int i = 0; i < params.size(); i++) {
                intArray[i] = (int) params.get(i);
            }
            int result = calc.add(intArray);
            return Integer.valueOf(result);
        } else if (method.equals("subtract")) {
            int result = calc.subtract((int) params.get(0), (int) params.get(1));
            return Integer.valueOf(result);
        } else if (method.equals("multiply")) {
            if (params.size() != 2) {
                int result = calc.multiply((int) params.get(0), (int) params.get(1));
                return Integer.valueOf(result);
            } 
            if (params.size() == 0) {
                return Integer.valueOf(1);
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
class LargeNumberException extends IllegalArgumentException {
    public LargeNumberException(String message) {
        super(message);
    }
}
