package solarex.util;

/**
 * Status objects for methods which need to return a message as
 * well as a status code.
 * 
 * @author Hj. Malthaner
 */
public class Status
{
    /** 
     * If there isn't a problem, this instance should be returned 
     */
    public static final Status OK = new Status(0, "Ok");
    
    public final int problemCode;
    public final String message;
    
    public Status(int code, String message)
    {
        this.problemCode = code;
        this.message = message;
    }
}
