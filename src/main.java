
public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Load mapping to digital server
		LoadDigitalRecords loadDigitalRecords = new LoadDigitalRecords();
		//loadDigitalMapping("C:/opac_dp/Dataprep/DataIn/TrecSmal.mag.xml");
		loadDigitalRecords.run("/opac_dp/Dataprep/DataIn/TrecSmal.mag.xml");
		
		
	}

}
