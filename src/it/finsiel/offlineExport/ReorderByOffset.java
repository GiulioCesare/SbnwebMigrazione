/***
 * Author: Argentino Trombin 19/04/2010 
 * 
 * Riordiniamo tabelle molto grandi tramite il file degli offset 
 * Il file degli offset viene rigenerato con chiavi univoche
 */


package it.finsiel.offlineExport;

import it.finsiel.misc.DateUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class ReorderByOffset {
	String inTableName, inOffsetName;
	String outTableName, outOffsetName;
	
	RandomAccessFile fhInTable = null, fhOutTable = null ;
	BufferedReader brInOffset;
	BufferedWriter bwOutOffset;

	int offsetStartPos = 0;
	int logEveryNrecords = 0;	// 0x0f = 15, 0xff = 255, 0xfff = 4095, 0x1fff = 8191, 0xffff = 65535
	
	/**
	 * @param args
	 */
	public static void main(String args[]) {


		ReorderByOffset reorderByOffset = new ReorderByOffset();

		if (args.length < 6) {
			System.out.println("Uso: ReorderByOffset inTablename.out, inOffsetFile.srt, outTablename.out.srt, outOffsetFile.srt.off, offsetStartPos, logEveryNrecords");
			System.exit(1);
		}
		System.out.println( 
	     "\nReorderByOffset Versione 1.0.0" + 	// aggiunta gestione tr_tit_aut non ordinata per indice
	     					 	// aggiunta tr_tit_bib no ordinata per indice 
		 "\n====================================="
		);

		reorderByOffset.inTableName = args[0];
		reorderByOffset.inOffsetName = args[1]; 

		reorderByOffset.outTableName = args[2];
		reorderByOffset.outOffsetName = args[3]; 
		reorderByOffset.offsetStartPos = Integer.parseInt(args[4]);
		
		reorderByOffset.logEveryNrecords = Integer.parseInt(args[5]);
		
		
		System.out.println("Inizio elaborazione "
				+ reorderByOffset.inTableName + " " + DateUtil.getDate()
				+ " " + DateUtil.getTime());
		
		reorderByOffset.run();
		
		System.out.println("Fine elaborazione "
				+ " " + DateUtil.getDate()
				+ " " + DateUtil.getTime());
	} // End main

	void run()
	{
		try {
			// Apriamo i file in lettura
			fhInTable = new RandomAccessFile(inTableName, "r");
//			fisInOffsetName = new FileInputStream(inOffsetName);
			brInOffset = new BufferedReader(new FileReader(inOffsetName));
			
			
			
			// Apriamo i file in scrittura
//			bwOutTable = new BufferedWriter(new FileWriter(this.outTableName));
		   File f = new File(outTableName);
		   if (f.exists())
			   f.delete(); // Questo perche' devo aprire per forza in lettura e scrittura
			fhOutTable = new RandomAccessFile(outTableName, "rw");
			
			
			bwOutOffset = new BufferedWriter(new FileWriter(this.outOffsetName));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		elabora();
		
		// chiudiamo tutti i file
		try {
			fhInTable.close();
			brInOffset.close();
			fhOutTable.close();
			bwOutOffset.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // End run
	
	
	 /** 
	  * pad a string S with a size of N with char C * on the left (True) or on
	 * the right(flase)
	 */
	public String padString(String s, int n, char c, boolean paddingLeft) {
		StringBuffer str = new StringBuffer(s);
		int strLength = str.length();
		if (n > 0 && n > strLength) {
			for (int i = 0; i <= n; i++) {
				if (paddingLeft) {
					if (i < n - strLength)
						str.insert(0, c);
				} else {
					if (i > strLength)
						str.append(c);
				}
			}
		}
		return str.toString();
	}
	
	
	
void elabora()
{
	String s;
	long readingOffset=0;
	long writingOffset;
	long ctr = 0;
	String lastBid = "", bid;
	String writeStrOffset;
	String readStrOffset;
    try {
		while ((s = brInOffset.readLine()) != null)
		{
			
//if(ctr == 1000)
//	break;
			bid = s.substring(0,10);

			// Are we changing bid?
			if (!lastBid.equals(bid))
			{	// SAVE UNIQUE KEY OFFSET
				// Get currrent write offset
				writingOffset = fhOutTable.getFilePointer();				
				writeStrOffset = padString(Long.toString(writingOffset), 11, '0', true) + '\n';
				
				// Save new offset
				bwOutOffset.write(bid + writeStrOffset);
				lastBid = bid;
			}
			
			
			// Get the offset of source table
			readStrOffset = s.substring(offsetStartPos);
			readingOffset = Long.parseLong(readStrOffset);

			// Get positioned
			fhInTable.seek(readingOffset);

			// Read line
			s = fhInTable.readLine();

			// Write string 
			fhOutTable.writeBytes(s+'\n');
			
//			System.out.println(s);

			ctr++;
			if ((ctr % logEveryNrecords) == 0)
				System.out.println(ctr);
			
		}
		
		System.out.println("Finito, fatti " + ctr + "record");
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} // end while
	
} // End elabora
	
} // end ReorderByOffset
