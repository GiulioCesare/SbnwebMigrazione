package it.finsiel.offlineExport;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test test = new Test();
		
		String numStd = test.gestisciTrattiniNumStandard("8818220357");
		
	}


	   
	   public static String gestisciTrattiniNumStandard(String numero) {
		 String ret = "";
		 if (numero.length() == 13 && (numero.startsWith("978") || numero.startsWith("979"))) {
		   ret = numero.substring(0,3) + "-";
		   numero = numero.substring(3);
		 }
		 ret += numero.substring(0,2);
		 numero = numero.substring(2);
		 
//	 Arge 26/05/09
	/*
		if (primiDue<20) {
		  ret += "-" + numero.substring(0, 2) + "-" + numero.substring(2, 7) + "-" + numero.substring(7);
		} else if (primiDue<70) {
		  ret += "-" + numero.substring(0, 3) + "-" + numero.substring(3, 7) + "-" + numero.substring(7);
		} else if (primiDue<85) {
		  ret += "-" + numero.substring(0, 4) + "-" + numero.substring(4, 7) + "-" + numero.substring(7);
		} else if (primiDue<90) {
		  ret += "-" + numero.substring(0, 5) + "-" + numero.substring(5, 7) + "-" + numero.substring(7);
		} else {
		  ret += "-" + numero.substring(0, 6) + "-" + numero.substring(6, 7) + "-" + numero.substring(7);
		}
	*/	 

	String pd = numero.substring(0,2); // Arge 26/05/09
	int len = numero.length() >= 7 ? 7 : numero.length();

	int primiDue;
	try{
		primiDue = Integer.parseInt(pd);
	}
	catch (NumberFormatException e)
	{
//		log.error("ERRORE gestisciTrattiniNumStandard: ",e);

		ret += "-" + numero.substring(0, 2) + "-" + numero.substring(2, len) + "-" + numero.substring(len);
		return ret;
	}

		 if (primiDue<20) {
		   ret += "-" + numero.substring(0, 2) + "-" + numero.substring(2, len) + "-" + numero.substring(len);
		 } else if (primiDue<70) {
		   ret += "-" + numero.substring(0, 3) + "-" + numero.substring(3, len) + "-" + numero.substring(len);
		 } else if (primiDue<85) {
		   ret += "-" + numero.substring(0, 4) + "-" + numero.substring(4, len) + "-" + numero.substring(len);
		 } else if (primiDue<90) {
		   ret += "-" + numero.substring(0, 5) + "-" + numero.substring(5, len) + "-" + numero.substring(len);
		 } else {
		   ret += "-" + numero.substring(0, 6) + "-" + numero.substring(6, len) + "-" + numero.substring(len);
		 }
//		End Arge 26/05/09

		 return ret;
	   }

}
