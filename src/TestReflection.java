
import java.lang.reflect.*;

//Example class
class MyTest{
 public void oneDExample(String[] strs){
     System.out.println(strs[0]);
 }
 
 public void twoDExample(String[][] strs){
     System.out.println(strs[0][0]);
 }
 
 public void threeDExample(int[][][] ints){
     System.out.println("Three Dimension Example Invoked: " + ints[0][0][0]);
 }
}


public class TestReflection{
 static public void main(String []args){
     try{
         MyTest aTest  = new MyTest();            // Instance of MyTest
         Class  aClass = Class.forName("MyTest"); // Class object of MyTest
         Class[] para  = new Class[1];            // Parameter List
         Object[] arg  = new Object[1];           // Argument List
         Method m = null;                         // Method reference
         
         // One Dimension String Example
         String[] oneDStr   = new String[1];
         oneDStr[0] = "One Dimension Example Invoked";
         para[0] = Class.forName("[Ljava.lang.String;");  
         m = aClass.getMethod("oneDExample", para);
         arg[0] = oneDStr;
         m.invoke(aTest, arg);
         
         // Two Dimension String Example
         String[][] twoDStr   = new String[1][1];
         twoDStr[0][0] = "Two Dimension Example Invoked";
         para[0] = Class.forName("[[Ljava.lang.String;");
         m = aClass.getMethod("twoDExample", para);
         arg[0] = twoDStr;
         m.invoke(aTest, arg);

         // Three Dimension Int Example
         int[][][] threeDInt   = new int[1][1][1];
         threeDInt[0][0][0] = 10000;
         para[0] = Class.forName("[[[I");  // note no ';' at the end of name!
         m = aClass.getMethod("threeDExample", para);
         arg[0] = threeDInt;
         m.invoke(aTest, arg);
     }
     catch(Exception e){System.out.println(e); } 
 }
}

