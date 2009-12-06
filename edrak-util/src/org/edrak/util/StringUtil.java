package org.edrak.util;



public class StringUtil
{
    public static final String AUTH_PARAMTER_NAME="pefdghj";
    public static final String AUTH_PREFIX="xcnryw63g";

    private static StringEncrypter stringEncrypter = null;

    public static String removeLastComa(String str)
    {
        if (str == null || str.equals("")) return "";
        str = str.trim();
        if (str.lastIndexOf(',') == str.length() - 1)
        {

            return str.substring(0, str.length() - 1);

        }

        return str;

    }

    public static String encrypt(String txt)
    {
        String ret = "";
        if (txt == null || txt.equals("")) return ret;

        initStringEncrypter();
        try
        {
            ret = stringEncrypter.encrypt(txt);
        }
        catch (Exception e)
        {
            ret = "";
        }
        return ret;
    }

    private static void initStringEncrypter()
    {
        if (stringEncrypter == null)
        {
            stringEncrypter = StringEncrypter.getInstance();
        }
    }

    public static String decrypt(String txt)
    {
        String ret = "";
        if (txt == null || txt.equals("")) return ret;

        initStringEncrypter();
        try
        {
            ret = stringEncrypter.decrypt(txt);
        }
        catch (Exception e)
        {
            ret = "";
        }

        return ret;
    }
    
    public static String testEnc(long i){
    	StringBuffer ret=new StringBuffer();
    	
    	char arr[]={'z','u','r','l','i','n','9','1','g','o'};
    	
    	String str=""+i;
    	
    	for (int x=0;x<str.length();x++){
    		
    	}
    	
    	return ret.toString();
    }
    
    
    public static void main(String[] args) {
    	
    	for (int i=1;i<100;i++){
    		pr(StringUtil.encrypt(""+i));
    	}
    }

    public  static void pr(String t){
    	System.out.println(t);
    }
}
