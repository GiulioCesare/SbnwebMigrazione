import java.util.Hashtable;

public class ConvertMisc {
	public static final String RESERVED_ENCODING = "10";
	public static final String RESERVED_ESABYTE_ENCODING = "99";

	public static final byte INTRODUCE_ESC = 0x1B;
	public static final byte INTRODUCE_03_A = 0x0E; // 
	public static final byte INTRODUCE_05_A = INTRODUCE_ESC; //  
	public static final byte INTRODUCE_05_B = 0x6F; //
	public static final byte INTRODUCE_10_A = INTRODUCE_ESC; // UNICODE
	public static final byte INTRODUCE_10_B = 0x6E; // UNICODE
	public static final byte INTRODUCE_RIT_BASE = 0x0F; // 

	public static final byte INTRODUCE_99_W = 0x57; // W 

	public static final byte INTRODUCE_NSB_A = INTRODUCE_ESC; // Non
	// indicizzare
	// testo tra la
	// sequeza ESC H
	// e ESC I
	public static final byte INTRODUCE_NSB_B = 0x48; // H
	public static final byte INTRODUCE_NSE_A = INTRODUCE_ESC;
	public static final byte INTRODUCE_NSE_B = 0x49; // I

	
	public static Hashtable charTable;	
	public static java.util.Vector charSetsUsed;	
	
	/*
	 * private String fromUnimarc2Unicode(String textLine) { String
	 * messaggioTrasformato = ""; String messaggioTrasformato1 = ""; String
	 * codificaSBN = ""; String utf16Code = null; String delimiter =
	 * String.valueOf('\033'); int ndx = 38; StringBuffer sb = new
	 * StringBuffer(); StringBuffer sbEsabyte = new StringBuffer(); String Bid
	 * =""; boolean field140 = false;
	 * 
	 * //if (field140 == false) //{ // // Check id ANTICO // if (Bid.charAt(3) ==
	 * 'E') // { // DataField dataField = factory.newDataField("140",
	 * indicator1, indicator2); // String Antico = " E "; // Subfield sf = new
	 * SubfieldImpl('a', Antico); // dataField.addSubfield(sf); // } // //}
	 *  // Find out which character sets are used String etichetta = ""; if
	 * (emptyString(textLine)) { return textLine; } else { etichetta =
	 * textLine.substring(0, 4); }
	 * 
	 * if (etichetta.compareTo("=100") == 0) { String chrarset1 =
	 * textLine.substring(ndx, ndx + 2);// the second // entry. We // assume the //
	 * first one to // bee always 01 // (The ASCII // set) String chrarset2 =
	 * textLine.substring(ndx + 2, ndx + 4); String chrarset3 =
	 * textLine.substring(ndx + 4, ndx + 6); charSetsUsed.clear(); if
	 * (chrarset1.compareTo(" ") != 0) charSetsUsed.add(chrarset1); if
	 * (chrarset2.compareTo(" ") != 0) charSetsUsed.add(chrarset2); if
	 * (chrarset3.compareTo(" ") != 0) charSetsUsed.add(chrarset3);
	 * messaggioTrasformato = textLine.substring(0, ndx - 2) +
	 * UNICODE_UTF8_ENCODING + " " + textLine.substring(ndx - 2 + 6); } else if
	 * (etichetta.compareTo("=LDR") == 0) { messaggioTrasformato = textLine;
	 * recordCounter++; //} else if (etichetta.startsWith("=001")){ // Bid =
	 * "tmp"; //} //else if (etichetta.startsWith("=140")) //{ // field140 =
	 * true; //}
	 * 
	 * else if (etichetta.startsWith("=010") || etichetta.startsWith("=011") ||
	 * ((etichetta.compareTo("=012") > 0) && (textLine .compareTo("=073") < 0)) ||
	 * etichetta.startsWith("=101") || etichetta.startsWith("=102") ||
	 * etichetta.startsWith("=950")) { // NO TRANSLATION messaggioTrasformato =
	 * textLine; } // TRANSLATE else // if (textLine.startsWith("=712")) // per
	 * cominciare { messaggioTrasformato =
	 * fromUnimarc2UnicodeTranslate(textLine); } return messaggioTrasformato; } //
	 * End fromUnimarc2Unicode
	 */
//	public String fromUnimarc2UnicodeTranslate(String textLine)
	public static String fromUnimarc2UnicodeTranslate(String textLine)
	
	
			throws Exception { // throws Exception
		String messaggioTrasformato;
		StringBuffer sb = new StringBuffer();
		StringBuffer sbEsabyte = new StringBuffer();

//		byte[] ba = textLine.getBytes();
//		byte[] ba_Utef8 = textLine.getBytes("UTF-8");
//		byte[] ba_8859_1 = textLine.getBytes("8859_1");
//		byte[] ba_ISO8859_1 = textLine.getBytes("ISO8859_1");
//		byte[] ba_Cp1252 = textLine.getBytes("Cp1252");
//		byte[] ba_UTF_16 = textLine.getBytes("UTF-16");
//		byte[] ba_ISO_8859_1 = textLine.getBytes("ISO-8859-1");

		byte[] ba = textLine.getBytes("ISO8859_1");
		
		int iStart = -1;
		sb.setLength(0); // clear

		if (ba == null) {
			// System.out.println("\nSequenza non terminata: " + textLine);
			throw new Exception("Sequenza non terminata: " + textLine);
		}

				
/*		
		for ( int i = 0; i < textLine.length(); ++i ) {
			   char c = textLine.charAt( i );
			   int j = (int) c;
			   System.out.println(textLine.charAt(i) +"("+j+")"+"("+Integer.toHexString(j)+")");
			}
*/		
		
		// Work on a single line
		for (int i = 0; i < ba.length;) {
			if (i >= ba.length) {
				break;
			}

			if (ba[i] < 0) {
				Integer intChar = new Integer(-1);
				/*
				 * if (ba[i] == -32)// A_ACCENTO_GRAVE intChar =
				 * Integer.decode("0x00C0"); else if (ba[i] == -24)//
				 * E_CON_ACCENTO_GRAVE intChar = Integer.decode("0x00C8"); else
				 * if (ba[i] == -23) // E_CON_ACCENTO_ACUTO intChar =
				 * Integer.decode("0x00C9"); else if (ba[i] == -20)//
				 * I_CON_ACCENTO_GRAVE intChar = Integer.decode("0x00CC"); else
				 * if (ba[i] == -14)// O_CON_ACCENTO_GRAVE intChar =
				 * Integer.decode("0x00D2"); else if (ba[i] == -7)//
				 * U_CON_ACCENTO_GRAVE intChar = Integer.decode("0x00D9"); else
				 * if (ba[i] == -25)// C_CON_CEDIGLIA intChar =
				 * Integer.decode("0x00C7");
				 */
				if (ba[i] == -32)// a_ACCENTO_GRAVE
					intChar = Integer.decode("0x00E0");
				else if (ba[i] == -24)// e_CON_ACCENTO_GRAVE
					intChar = Integer.decode("0x00E8");
				else if (ba[i] == -23) // e_CON_ACCENTO_ACUTO
					intChar = Integer.decode("0x00E9");
				else if (ba[i] == -20)// i_CON_ACCENTO_GRAVE
					intChar = Integer.decode("0x00EC");
				else if (ba[i] == -14)// o_CON_ACCENTO_GRAVE
					intChar = Integer.decode("0x00F2");
				else if (ba[i] == -7)// u_CON_ACCENTO_GRAVE
					intChar = Integer.decode("0x00F9");
				else if (ba[i] == -25)// c_CON_CEDIGLIA
					intChar = Integer.decode("0x00E7");
				else if (ba[i] == -110)// � Sembra ma non � un apostrofo 
					intChar = Integer.decode("0x2019");

				if (intChar.compareTo(new Integer(-1)) != 0) {
					char Carattere = (char) intChar.intValue();
					sb.append(Carattere);
				} else
					sb.append("�"); // LETTERA ACCENTATA
				i++;
				continue;
			}

			if (ba[i] == INTRODUCE_RIT_BASE) {
				// sb.append("'0x0F BAD SEQUENCE'");
				i++;
				continue;
			}

			if (ba[i] == INTRODUCE_ESC && ba[i + 1] == INTRODUCE_NSB_B) {
				sb.append(""); // ##
				i += 2;
				continue;
			} else if (ba[i] == INTRODUCE_ESC && ba[i + 1] == INTRODUCE_NSE_B) {
				sb.append("*"); // #
				i += 2;
				continue;
			}

			iStart = i;
			for (int j = 0; j < charSetsUsed.size(); j++) {
				if (i >= ba.length)
					break;
				String charset = (String) charSetsUsed.get(j);

				if (charset.equals("03")) {
					sbEsabyte.setLength(0);
					if (ba[i] == INTRODUCE_03_A) {
						i++;
						
if (i >=  ba.length)
	throw new Exception("Sequenza non terminata: " + textLine);
	
						
						// Eg. 0x0E 0x41 0x0F 0x61 (Aa)
						char diacritico = (char) ba[i++];

						// if (ba[i] == INTRODUCE_RIT_BASE) {
						if (i < ba.length && ba[i] == INTRODUCE_RIT_BASE) {
							i++;
							if (diacritico >= 'A' && diacritico <= 'Z') {
								if (diacritico == 'L' || diacritico == 'N'
										|| diacritico == 'Q'
										|| diacritico == 'T'
										|| diacritico == 'W') {
									sb.append("'BAD CHARACTER:" + (char) ba[i] + "'");
									i++;
								} else {
									sbEsabyte.append("1" + diacritico);
									if (ba[i] == 'i') {
										if (ba[i] != 'S' && ba[i] != 'S')
											sbEsabyte.append("1u");
										else
											sbEsabyte.append("0"
													+ (char) ba[i++]);

									} else {
										sbEsabyte.append("0" + (char) ba[i++]);
										// Converti e scrivi
										char ucode = fromEsabyte2Unicode(sbEsabyte
												.toString());
										sb.append(ucode);
									}
								}
							} else {
								if (diacritico <= '?' || diacritico >= 'a') {
									// Componi esabyte
									sbEsabyte.append("0 1" + diacritico);
									// Converti e scrivi
									char ucode = fromEsabyte2Unicode(sbEsabyte
											.toString());
									sb.append(ucode);
								} else {
									sb.append("'BAD CHARACTER:" + diacritico
											+ "'");
									// i++;
								}
							}
						} else { // multicarattere?
							i--;
							if (ba == null) {
								System.out.println("Sequenza NULL");
								return "";
							}
							if (i >= ba.length) {
								// System.out.println("Sequenza non terminata: "
								// + textLine);
								throw new Exception("Sequenza non terminata: "
										+ textLine);
							}
							while (ba[i] != INTRODUCE_RIT_BASE) {
								// Componi l'esabyte
								sbEsabyte.append("0 1" + (char) ba[i++]);
								// Converti e scrivi
								// Converti e scrivi
								char ucode = fromEsabyte2Unicode(sbEsabyte
										.toString());
								sb.append(ucode);
								// Next character
								if (i >= ba.length) {
									// System.out.println("Sequenza non
									// terminata: " + textLine);
									throw new Exception(
											"Sequenza non terminata: "
													+ textLine);
								}

							} // End while
						}
					}
				} // End if (charset.equals("03"))
				else if (charset.equals(RESERVED_ESABYTE_ENCODING))
				{
					if (ba[i]== INTRODUCE_ESC && ba[i+1] == INTRODUCE_99_W)
					{
						sbEsabyte.append((char)ba[i+2]); 
						sbEsabyte.append((char)ba[i+3]); 
						sbEsabyte.append((char)ba[i+4]); 
						sbEsabyte.append((char)ba[i+5]); 
						char ucode = fromEsabyte2Unicode(sbEsabyte.toString());
						sb.append(ucode);
						i += 6;
					}
				}

				else if (charset.equals("05")
						|| charset.equals(RESERVED_ENCODING)) { // "10"
					if (ba[i] == INTRODUCE_05_A || ba[i] == INTRODUCE_10_A) {

						i++;
						if (i < ba.length && ba[i] == INTRODUCE_05_B) {
							i++;
							while (i < ba.length && ba[i] != INTRODUCE_RIT_BASE) {
								if (ba[i] < 'A') {
									// if (ba[i] >= '0' && ba[i] <= '9')
									if (ba[i] >= ' ' && ba[i] <= '9') // Arge
									// fix
									{
										// Componi esabyte
										sbEsabyte
												.append("0 4" + (char) ba[i++]);
										// Converti e scrivi
										char ucode = fromEsabyte2Unicode(sbEsabyte
												.toString());
										sb.append(ucode);
									} else {
										sb.append("'BAD CHARACTER:"
												+ (char) ba[i] + "'");
										i++;
									} // end else if numeric
								} else {
									// Componi esabyte
									sbEsabyte.append("0 3" + (char) ba[i++]);
									// Converti e scrivi
									char ucode = fromEsabyte2Unicode(sbEsabyte
											.toString());
									sb.append(ucode);
								} // End else if (ba[i] < 'A')
							} // end while
							i++; // move past INTRODUCE_RIT_BASE
						} // End if (ba[i] == INTRODUCE_05_B)

						else if (ba[i] == INTRODUCE_10_B) // Unicode
						{
							i++;
							// Arge 24/04/07
							while (i < ba.length && ba[i] != INTRODUCE_RIT_BASE) {
								// Componi esabyte
								sbEsabyte.append("0 2" + (char) ba[i++]);
								// Converti e scrivi
								char ucode = fromEsabyte2Unicode(sbEsabyte.toString());
								sb.append(ucode);
							} // end while
							i++; // move past INTRODUCE_RIT_BASE

						} // End if (ba[i] == INTRODUCE_05_B)

						else {
							sb.append("'BAD CHARACTER:" + (char) ba[i] + "'");
							i++;
						} // End else if (ba[i] == INTRODUCE_05_B)

					} // End if (ba[i] == INTRODUCE_05_A)
				} // end if (charset.equals("05"))

			} // End for (int j=0; j <charSetsUsed.size(); j++)
			if (iStart == i) {
				sb.append((char) ba[i++]);
			}
		} // End for (int i=0; i<ba.length; )
		messaggioTrasformato = sb.toString();
		return messaggioTrasformato;
	} // End fromUnimarc2UnicodeTranslate

	
	public static char fromEsabyte2Unicode(String esabyte) {
		char Carattere;
		Integer prova;
		String utf16Code;
		try {
			CharMap cm = (CharMap) charTable.get(esabyte);
			new Integer(0);
			if (cm == null)

				prova = Integer.decode("0x00BF"); // ?
			else
				prova = Integer.decode("0x" + cm.codiceUnicodeUTF16);
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("*** Codice ESABYTE errato *** Codice ESABYTE: "
					+ esabyte);
			return ' ';
		} catch (NumberFormatException nfe) {
			System.out
					.println("*** Codice UTF-8 corrispondente non trovato *** Codice ESABYTE: "
							+ esabyte);
			return ' ';
		} catch (Exception ex) {
			ex.printStackTrace();
			return ' ';
		}

		Carattere = (char) prova.intValue();
		return Carattere;
	} // End fromEsabyte2Unicode


	public static java.util.Vector getCharSetsUsed() {
		return charSetsUsed;
	}


	public static void setCharSetsUsed(java.util.Vector charSetsUsed) {
		ConvertMisc.charSetsUsed = charSetsUsed;
	}


	public static Hashtable getCharTable() {
		return charTable;
	}


	public static void setCharTable(Hashtable charTable) {
		ConvertMisc.charTable = charTable;
	}
	




	public static String replaceNoSortSequence(String textLine)
	
	
	throws Exception { // throws Exception
	String messaggioTrasformato;
	StringBuffer sb = new StringBuffer();
	sb.setLength(0); // clear
	int i = 0;
	for (; i < textLine.length();) {
		if (textLine.charAt(i) == INTRODUCE_ESC && textLine.charAt(i+1) == INTRODUCE_NSB_B) {
			sb.append(""); // ##
			i += 2;
			continue;
		} else if (textLine.charAt(i) == INTRODUCE_ESC && textLine.charAt(i+1) == INTRODUCE_NSE_B) {
			sb.append("*"); // #
			i += 2;
			break;
		}
		else
		{
			sb.append(textLine.charAt(i));
			i++;
		}
} // End for (int i=0; i<ba.length; )
messaggioTrasformato = sb.toString() + textLine.substring(i);
return messaggioTrasformato;
} // End replaceNoSortSequence







}


