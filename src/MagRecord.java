import java.util.ArrayList;



public class MagRecord {
	ArrayList <MagShortRecord> magShortRecordList;	
	String bidBib;
	
	public MagRecord(){
		magShortRecordList = new ArrayList<MagShortRecord>();		
	}

	
	public void addMagShortRecord(MagShortRecord aMag) {
		magShortRecordList.add(aMag);
	}

	public String getBidBib() {
		return bidBib;
	}

	public void setBidBib(String bid) {
		this.bidBib = bid;
	}

	public ArrayList<MagShortRecord> getMagShortRecordList() {
		return magShortRecordList;
	}

	public void setMagShortRecordList(ArrayList<MagShortRecord> magShortRecordList) {
		this.magShortRecordList = magShortRecordList;
	}
	
	
}
