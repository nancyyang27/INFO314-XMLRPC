package edu.uw.info314.xmlrpc.server;

public class Calc {
    public int add(int... args) {
        int result = 0;
       
        for (int arg : args) { 
            if ((long) result + arg > Integer.MAX_VALUE || (long) result + arg < Integer.MIN_VALUE) {
                throw new IllegalArgumentException("number too large");
            }
            result += arg; 
        }
       
        return result;
    }
    public int subtract(int lhs, int rhs) { return lhs - rhs; }
    public int multiply(int... args) {
        int result = 1;
        for (int arg : args) { 
            if ((long) result * arg > Integer.MAX_VALUE || (long) result * arg < Integer.MIN_VALUE) {
                throw new IllegalArgumentException("number too large");
            }
            result *= arg; 
        }
        return result;
    }
    public int divide(int lhs, int rhs) { return lhs / rhs; }
    public int modulo(int lhs, int rhs) { return lhs % rhs; }
}
