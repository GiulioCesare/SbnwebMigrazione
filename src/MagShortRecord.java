
public class MagShortRecord {

	private String thumbnail, identifier, identifierSite, dcIdentifierType, library, year, issue, bid ;
	private char bibLevel; 
	
	public MagShortRecord (){}


	public String getIdentifier() {
		return identifier;
	}


	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	public String getIssue() {
		return issue;
	}


	public void setIssue(String issue) {
		this.issue = issue;
	}


	public String getLibrary() {
		return library;
	}


	public void setLibrary(String library) {
		this.library = library;
	}


	public String getThumbnail() {
		return thumbnail;
	}


	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}


	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
	};
	
	public String toString() {
		String s = 
			"\nbibLevel = " + bibLevel + 
			"\nidentifier = " + identifier + 
			"\nLibrary = " + library + 
			"\nYear = " + year + 
			"\nIssue = " + issue + 
			"\nThumbnail = " + thumbnail +
			"\nBid = " + bid;
		return s;
	}


	public String getBid() {
		return bid;
	}


	public void setBid(String bid) {
		this.bid = bid;
	}


	public char getBibLevel() {
		return bibLevel;
	}


	public void setBibLevel(char bibLevel) {
		this.bibLevel = bibLevel;
	}


	public String getDcIdentifierType() {
		return dcIdentifierType;
	}


	public void setDcIdentifierType(String identifierType) {
		this.dcIdentifierType = identifierType;
	}


	public String getIdentifierSite() {
		return identifierSite;
	}


	public void setIdentifierSite(String identifierSite) {
		this.identifierSite = identifierSite;
	};
	
}
