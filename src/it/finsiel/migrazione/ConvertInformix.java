package it.finsiel.migrazione;

public class ConvertInformix extends ConvertDb{
	String codPolo;
    
	public ConvertInformix(String aCodPolo, char fileSystemSeparator)
    {
//    	db_type = aDb_type;
    	codPolo = aCodPolo;
    	this.fileSystemSeparator = fileSystemSeparator;
    }	
	
	public void completaTbTitolo(String ar1[])
	{
		 // Informix
		System.out.println("completaTbTitolo INFORMIX");
	} // End completaTb
	
	public void completaTbfBiblioteca(String ar1[])
	{
		 // Informix
//				ar1[28-1] = ConvertConstants.MIGRATION_SEQUENCE;
//	    	ar1[28-1] = Integer.toString(sequenceIdBiblioteca++);
//			ar1[29-1] = ConvertConstants.MIGRATION_CD_ANA_BIBLIOTECA_TEMP;
//			ar1[30-1] = "N"; //fl_canc
	} // End completaTb

}
