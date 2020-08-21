import java.io.UnsupportedEncodingException;

import org.marc4j.converter.*;


/**
 * Implement this class to create a character converter.
 * 
 * @author Bas Peters
 * @version $Revision: 1.1 $
 */
public class Utf16ToUtf8Converter implements CharConverter {
    public static final int MAX_BYTES_PER_UTF8_CHARACTER = 4;

  /**
   * Converts the dataElement and returns the result as a <code>String</code>
   * object.
   * 
   * @param dataElement the data to convert
   * @return String the conversion result
   */
  public String convert(String dataElement)
  {
	  
      byte arUtf8[] = myGetBytesUtf8(dataElement);
      String s1 = "";
      try {
		s1 =  new String(arUtf8, 0, arUtf8.length, "UTF8");  // doesn't work
//		s1 =  new String(arUtf8, 0, arUtf8.length, "ASCII");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return s1;
  }

  public byte[] convertToBytes(String dataElement)
  {
	  
      byte arUtf8[] = myGetBytesUtf8(dataElement);
      return arUtf8;
  }
  
  
  private byte [] myGetBytesUtf8(String s)
  {
	int len = s.length();    
   int en = MAX_BYTES_PER_UTF8_CHARACTER * len;
   byte[] ba = new byte[en];
   if (len == 0)
	return ba;

   int ctr = 0;

   for (int i=0; i < len; i++)
   {
   	char c = s.charAt(i);
   	if (c < 0x80)
   	{
   		ba[ctr++] = (byte)c;
   	}
   	else if (c < 0x800)
   	{
   		ba[ctr++] = (byte)(0xC0 | c >> 6);
   		ba[ctr++] = (byte)(0x80 | c & 0x3F);
   	}
   	else if (c < 0x10000)
   	{
   		ba[ctr++] = (byte)(0xE0 | c >> 12);
   		ba[ctr++] = (byte)(0x80 | c >> 6 & 0x3F);
   		ba[ctr++] = (byte)(0x80 | c & 0x3F);
   	}
   	else if (c < 0x200000)
   	{
   		ba[ctr++] = (byte)(0xE0 | c >> 18);
   		ba[ctr++] = (byte)(0x80 | c >> 12 & 0x3F);
   		ba[ctr++] = (byte)(0x80 | c >> 6 & 0x3F);
   		ba[ctr++] = (byte)(0x80 | c & 0x3F);
   	}
   	else if (c < 0x800)
   	{
   		
   	}
   } // end for
   
   return trim(ba, ctr);
  } // End myGetBytesUtf8 

  private static byte[] trim(byte[] ba, int len) {
     	if (len == ba.length)
     	    return ba;
     	byte[] tba = new byte[len];
     	System.arraycopy(ba, 0, tba, 0, len);
     	return tba;
         }
  
}