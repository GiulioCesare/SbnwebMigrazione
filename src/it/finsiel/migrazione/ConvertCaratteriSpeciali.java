package it.finsiel.migrazione;

/**
 *  utility per convertire UCS/Unicode in caratteri per ordinamento tutti Maiuscoli
 *
 */
public class ConvertCaratteriSpeciali {

    public static String convert(String data) {
    	return new String(convert(data.toCharArray()));
    }

    public static String convertAutore(String nome)	{
        char[] data = nome.toCharArray();
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < data.length; i++) {
            char c = data[i];
            int d;
            if (":<>;-*#_,".indexOf(data[i])>=0)
                d = data[i];
            else
                d = convert(c);
//          System.out.println("carattere "+d +">>"+ (char)d);
            if (d < 0) {
//              System.out.println("carattere da escludere "+d);
            } else {
                if (d < 256) {
                    sb.append((char)d);
                } else {
                    sb.append((char)(d / 256));
                    sb.append((char)(d % 256));
                }       }
        }
     return (sb.toString()).toUpperCase();
    }

    /**
     * @param data      UCS/Unicode data
     * @return char[] - caratteri per odinamento
     */

    public static char[] convert(char[] data)  {
	StringBuffer sb = new StringBuffer();
	for(int i = 0; i < data.length; i++) {
		char c = data[i];
		int d = convert(c);
//		System.out.println("carattere "+d +">>"+ (char)d);
		if (d < 0) {
//			System.out.println("carattere da escludere "+d);
		} else {
			if (d < 256) {
			    sb.append((char)d);
			} else {
			    sb.append((char)(d / 256));
				sb.append((char)(d % 256));
			}		}
	}
	return ((sb.toString()).toUpperCase()).toCharArray();
    }
    
    private static int convert(int i) {
	switch(i) {
        case 0x00A1: return -1;      // inverted exclamation mark
        case 0x00A3: return 0x20;    // pound sign
        case 0x00A9: return 0x43;    // copyright sign
        case 0x00AE: return 0x52;    // registered trade mark sign
        case 0x00B0: return 0x20;    // degree sign, ring above
        case 0x00B1: return 0x20;    // plus-minus sign
        case 0x00B7: return 0x58;    // middle dot
        case 0x00B8: return -1;      // cedilla
        case 0x00BF: return -1;      // inverted question mark
        case 0x00C0: return 0x41;    // capital A with grave accent
        case 0x00C1: return 0x41;    // capital A with acute accent
        case 0x00C2: return 0x41;    // capital A with circumflex accent
        case 0x00C3: return 0x41;    // capital A with tilde
        case 0x00C4: return 0x41;    // capital A with diaeresis
        case 0x00C5: return 0x41;    // capital A with ring above
        case 0x00C6: return 0x4145;  // capital diphthong A with E
        case 0x00C7: return 0x43;    // capital C with cedilla
        case 0x00C8: return 0x45;    // capital E with grave accent
        case 0x00C9: return 0x45;    // capital E with acute accent
        case 0x00CA: return 0x45;    // capital E with circumflex accent
        case 0x00CB: return 0x45;    // capital E with diaeresis
        case 0x00CC: return 0x49;    // capital I with grave accent
        case 0x00CD: return 0x49;    // capital I with acute accent
        case 0x00CE: return 0x49;    // capital I with circumflex accent
        case 0x00CF: return 0x49;    // capital I with diaeresis
        case 0x00D0: return 0x44;    // capital icelandic letter Eth
        case 0x00D1: return 0x4E;    // capital N with tilde
        case 0x00D2: return 0x4F;    // capital O with grave accent
        case 0x00D3: return 0x4F;    // capital O with acute accent
        case 0x00D4: return 0x4F;    // capital O with circumflex accent
        case 0x00D5: return 0x4F;    // capital O with tilde
        case 0x00D6: return 0x4F;    // capital O with diaeresis
        case 0x00D8: return 0x4F;    // capital O with oblique stroke
        case 0x00D9: return 0x55;    // capital U with grave accent
        case 0x00DA: return 0x55;    // capital U with acute accent
        case 0x00DB: return 0x55;    // capital U with circumflex
        case 0x00DC: return 0x55;    // capital U with diaeresis
        case 0x00DD: return 0x59;    // capital Y with acute accent
        case 0x00DE: return 0x5448;  // capital Icelandic letter Thorn
        case 0x00DF: return 0x5353;  // small German letter sharp s
        case 0x00E0: return 0x41;    // small a with grave accent
        case 0x00E1: return 0x41;    // small a with acute accent
        case 0x00E2: return 0x41;    // small a with circumflex accent
        case 0x00E3: return 0x41;    // small a with tilde
        case 0x00E4: return 0x41;    // small a with diaeresis
        case 0x00E5: return 0x41;    // small a with ring above
        case 0x00E6: return 0x4145;  // small diphthong a with e
        case 0x00E7: return 0x43;    // small c with cedilla
        case 0x00E8: return 0x45;    // small e with grave accent
        case 0x00E9: return 0x45;    // small e with acute accent
        case 0x00EA: return 0x45;    // small e with circumflex accent
        case 0x00EB: return 0x45;    // small e with diaeresis
        case 0x00EC: return 0x49;    // small i with grave accent
        case 0x00ED: return 0x49;    // small i with acute accent
        case 0x00EE: return 0x49;    // small i with circumflex accent
        case 0x00EF: return 0x49;    // small i with diaeresis
        case 0x00F0: return 0x44;    // small Icelandic letter Eth
        case 0x00F1: return 0x4E;    // small n with tilde
        case 0x00F2: return 0x4F;    // small o with grave accent
        case 0x00F3: return 0x4F;    // small o with acute accent
        case 0x00F4: return 0x4F;    // small o with circumflex accent
        case 0x00F5: return 0x4F;    // small o with tilde
        case 0x00F6: return 0x4F;    // small o with diaeresis
        case 0x00F8: return 0xB2;    // small o with oblique stroke
        case 0x00F9: return 0x55;    // small u with grave accent
        case 0x00FA: return 0x55;    // small u with acute accent
        case 0x00FB: return 0x55;    // small u with circumflex
        case 0x00FC: return 0x55;    // small u with diaeresis
        case 0x00FD: return 0x59;    // small y with acute accent
        case 0x00FE: return 0x5448;  // small Icelandic letter Thorn
        case 0x00FF: return 0x59;    // small y with diaeresis
        case 0x0100: return 0x41;    // capital a with macron
        case 0x0101: return 0x41;    // small a with macron
        case 0x0102: return 0x41;    // capital A with breve
        case 0x0103: return 0x41;    // small a with breve
        case 0x0104: return 0x41;    // capital A with ogonek
        case 0x0105: return 0x41;    // small a with ogonek
        case 0x0106: return 0x43;    // capital C with acute accent
        case 0x0107: return 0x43;    // small c with acute accent
        case 0x0108: return 0x43;    // capital c with circumflex
        case 0x0109: return 0x43;    // small c with circumflex
        case 0x010A: return 0x43;    // capital c with dot above
        case 0x010B: return 0x43;    // small c with dot above
        case 0x010C: return 0x43;    // capital C with caron
        case 0x010D: return 0x43;    // small c with caron
        case 0x010E: return 0x44;    // capital D with caron
        case 0x010F: return 0x44;    // small d with caron
        case 0x0110: return 0x44;    // capital D with stroke
        case 0x0111: return 0x44;    // small D with stroke
        case 0x0112: return 0x45;    // capital e with macron
        case 0x0113: return 0x45;    // small e with macron
        case 0x0114: return 0x45;    // capital e with breve
        case 0x0115: return 0x45;    // small e with breve
        case 0x0116: return 0x45;    // capital e with dot above
        case 0x0117: return 0x45;    // small e with dot above
        case 0x0118: return 0x45;    // capital E with ogonek
        case 0x0119: return 0x45;    // small e with ogonek
        case 0x011A: return 0x45;    // capital E with caron
        case 0x011B: return 0x45;    // small e with caron
        case 0x011C: return 0x47;    // capital g with circumflex
        case 0x011D: return 0x47;    // small g with circumflex
        case 0x011E: return 0x47;    // capital g with breve
        case 0x011F: return 0x47;    // small g with breve
        case 0x0120: return 0x47;    // capital g with dot above
        case 0x0121: return 0x47;    // small g with dot above
        case 0x0122: return 0x47;    // capital g with cedilla
        case 0x0123: return 0x47;    // small g with cedilla
        case 0x0124: return 0x48;    // capital h with circumflex
        case 0x0125: return 0x48;    // small h with circumflex
        case 0x0128: return 0x49;    // capital i with tilde
        case 0x0129: return 0x49;    // small i with tilde
        case 0x012A: return 0x49;    // capital i with macron
        case 0x012B: return 0x49;    // small i with macron
        case 0x012C: return 0x49;    // capital i with breve
        case 0x012D: return 0x49;    // small i with breve
        case 0x012E: return 0x49;    // capital i with ogonek
        case 0x012F: return 0x49;    // small i with ogonek
        case 0x0130: return 0x49;    // capital i with dot above
        case 0x0131: return 0x49;    // small dotless i
        case 0x0134: return 0x4A;    // capital j with circumflex
        case 0x0135: return 0x4A;    // small j with circumflex
        case 0x0136: return 0x4B;    // capital k with cedilla
        case 0x0137: return 0x4B;    // small k with cedilla
        case 0x0139: return 0x4C;    // capital L with acute accent
        case 0x013A: return 0x4C;    // small l with acute accent
        case 0x013B: return 0x4C;    // capital l with cedilla
        case 0x013C: return 0x4C;    // small l with cedilla
        case 0x013D: return 0x4C;    // capital L with caron
        case 0x013E: return 0x4C;    // small l with caron
        case 0x0141: return 0x4C;    // capital L with stroke
        case 0x0142: return 0x4C;    // small l with stroke
        case 0x0143: return 0x4E;    // capital N with acute accent
        case 0x0144: return 0x4E;    // small n with acute accent
        case 0x0145: return 0x4E;    // capital n with cedilla
        case 0x0146: return 0x4E;    // small n with cedilla
        case 0x0147: return 0x4E;    // capital N with caron
        case 0x0148: return 0x4E;    // small n with caron
        case 0x014C: return 0x4F;    // capital o with macron
        case 0x014D: return 0x4F;    // small o with macron
        case 0x014E: return 0x4F;    // capital o with breve
        case 0x014F: return 0x4F;    // small o with breve
        case 0x0150: return 0x4F;    // capital O with double acute
        case 0x0151: return 0x4F;    // small o with double acute
        case 0x0152: return 0x4F45;  // capital ligature OE
        case 0x0153: return 0x4F45;  // small ligature OE
        case 0x0154: return 0x52;    // capital R with acute accent
        case 0x0155: return 0x52;    // small r with acute accent
        case 0x0156: return 0x52;    // capital r with cedilla
        case 0x0157: return 0x52;    // small r with cedilla
        case 0x0158: return 0x52;    // capital R with caron
        case 0x0159: return 0x52;    // small r with caron
        case 0x015A: return 0x53;    // capital S with acute accent
        case 0x015B: return 0x53;    // small s with acute accent
        case 0x015C: return 0x53;    // capital s with circumflex
        case 0x015D: return 0x53;    // small s with circumflex
        case 0x015E: return 0x53;    // capital S with cedilla
        case 0x015F: return 0x53;    // small s with cedilla
        case 0x0160: return 0x53;    // capital S with caron
        case 0x0161: return 0x53;    // small s with caron
        case 0x0162: return 0x54;    // capital T with cedilla
        case 0x0163: return 0x54;    // small t with cedilla
        case 0x0164: return 0x54;    // capital T with caron
        case 0x0165: return 0x54;    // small t with caron
        case 0x0168: return 0x55;    // capital u with tilde
        case 0x0169: return 0x55;    // small u with tilde
        case 0x016A: return 0x55;    // capital u with macron
        case 0x016B: return 0x55;    // small u with macron
        case 0x016C: return 0x55;    // capital u with breve
        case 0x016D: return 0x55;    // small u with breve
        case 0x016E: return 0x55;    // capital U with ring above
        case 0x016F: return 0x55;    // small u with ring above
        case 0x0170: return 0x55;    // capital U with double acute
        case 0x0171: return 0x55;    // small u with double acute
        case 0x0172: return 0x55;    // capital u with ogonek
        case 0x0173: return 0x55;    // small u with ogonek
        case 0x0174: return 0x57;    // capital w with circumflex
        case 0x0175: return 0x57;    // small w with circumflex
        case 0x0176: return 0x59;    // capital y with circumflex
        case 0x0177: return 0x59;    // small y with circumflex
        case 0x0178: return 0x59;    // capital y with diaeresis
        case 0x0179: return 0x5A;    // capital Z with acute accent
        case 0x017A: return 0x5A;    // small z with acute accent
        case 0x017B: return 0x5A;    // capital Z with dot above
        case 0x017C: return 0x5A;    // small z with dot above
        case 0x017D: return 0x5A;    // capital Z with caron
        case 0x017E: return 0x5A;    // small z with caron
        case 0x01A0: return 0x4F;    // capital O with horn
        case 0x01A1: return 0x4F;    // small o with horn
        case 0x01AF: return 0x55;    // capital U with horn
        case 0x01B0: return 0x55;    // small u with horn
        case 0x01CD: return 0x41;    // capital a with caron
        case 0x01CE: return 0x41;    // small a with caron
        case 0x01CF: return 0x49;    // capital i with caron
        case 0x01D0: return 0x49;    // small i with caron
        case 0x01D1: return 0x4F;    // capital o with caron
        case 0x01D2: return 0x4F;    // small o with caron
        case 0x01D3: return 0x55;    // capital u with caron
        case 0x01D4: return 0x55;    // small u with caron
        case 0x01E2: return 0x4145;  // capital ae with macron
        case 0x01E3: return 0x4145;  // small ae with macron
        case 0x01E6: return 0x47;    // capital g with caron
        case 0x01E7: return 0x47;    // small g with caron
        case 0x01E8: return 0x4B;    // capital k with caron
        case 0x01E9: return 0x4B;    // small k with caron
        case 0x01EA: return 0x4F;    // capital o with ogonek
        case 0x01EB: return 0x4F;    // small o with ogonek
        case 0x01F0: return 0x4A;    // small j with caron
        case 0x01F4: return 0x47;    // capital g with acute
        case 0x01F5: return 0x47;    // small g with acute
        case 0x01FC: return 0x4145;  // capital ae with acute
        case 0x01FD: return 0x4145;  // small ae with acute
//--------------------------------------------------------------------------------------
        case 0x02B9: return -1;      // modified letter prime
        case 0x02BA: return -1;      // modified letter double prime
        case 0x02BE: return -1;      // modifier letter right half ring
        case 0x02BF: return -1;      // modifier letter left half ring
        case 0x0300: return -1;      // grave accent
        case 0x0301: return -1;      // acute accent
        case 0x0302: return -1;      // circumflex accent
        case 0x0303: return -1;      // tilde
        case 0x0304: return -1;      // combining macron
        case 0x0306: return -1;      // breve
        case 0x0307: return -1;      // dot above
		case 0x0308: return -1;      // combining diaeris (Dialytica)
        case 0x0309: return -1;      // hook above
        case 0x030A: return -1;      // ring above
        case 0x030B: return -1;      // double acute accent
        case 0x030C: return -1;      // caron
        case 0x0310: return -1;      // candrabindu
        case 0x0313: return -1;      // comma above
        case 0x0315: return -1;      // comma above right
        case 0x031C: return -1;      // combining half ring below
        case 0x0323: return -1;      // dot below
        case 0x0324: return -1;      // diaeresis below
        case 0x0325: return -1;      // ring below
        case 0x0326: return -1;      // comma below
        case 0x0327: return -1;      // combining cedilla
        case 0x0328: return -1;      // ogonek
        case 0x032E: return -1;      // breve below
        case 0x0332: return -1;      // low line (= line below?)
        case 0x0333: return -1;      // double low line
//--------------------------------------------------------------------------------------
        case 0x1E00: return 0x41;    // capital a with ring below
        case 0x1E01: return 0x41;    // small a with ring below
        case 0x1E02: return 0x42;    // capital b with dot above
        case 0x1E03: return 0x42;    // small b with dot above
        case 0x1E04: return 0x42;    // capital b with dot below
        case 0x1E05: return 0x42;    // small b with dot below
        case 0x1E0A: return 0x44;    // capital d with dot above
        case 0x1E0B: return 0x44;    // small d with dot above
        case 0x1E0C: return 0x44;    // capital d with dot below
        case 0x1E0D: return 0x44;    // small d with dot below
        case 0x1E10: return 0x44;    // capital d with cedilla
        case 0x1E11: return 0x44;    // small d with cedilla
        case 0x1E1E: return 0x46;    // capital f with dot above
        case 0x1E1F: return 0x46;    // small f with dot above
        case 0x1E20: return 0x47;    // capital g with macron
        case 0x1E21: return 0x47;    // small g with macron
        case 0x1E22: return 0x48;    // capital h with dot above
        case 0x1E23: return 0x48;    // small h with dot above
        case 0x1E24: return 0x48;    // capital h with dot below
        case 0x1E25: return 0x48;    // small h with dot below
        case 0x1E26: return 0x48;    // capital h with diaeresis
        case 0x1E27: return 0x48;    // small h with diaeresis
        case 0x1E28: return 0x48;    // capital h with cedilla
        case 0x1E29: return 0x48;    // small h with cedilla
        case 0x1E2A: return 0x48;    // capital h with breve below
        case 0x1E2B: return 0x48;    // small h with breve below
        case 0x1E30: return 0x4B;    // capital k with acute
        case 0x1E31: return 0x4B;    // small k with acute
        case 0x1E32: return 0x4B;    // capital k with dot below
        case 0x1E33: return 0x4B;    // small k with dot below
        case 0x1E36: return 0x4C;    // capital l with dot below
        case 0x1E37: return 0x4C;    // small l with dot below
        case 0x1E3E: return 0x4D;    // capital m with acute
        case 0x1E3F: return 0x4D;    // small m with acute
        case 0x1E40: return 0x4D;    // capital m with dot above
        case 0x1E41: return 0x4D;    // small m with dot above
        case 0x1E42: return 0x4D;    // capital m with dot below
        case 0x1E43: return 0x4D;    // small m with dot below
        case 0x1E44: return 0x4E;    // capital n with dot above
        case 0x1E45: return 0x4E;    // small n with dot above
        case 0x1E46: return 0x4E;    // capital n with dot below
        case 0x1E47: return 0x4E;    // small n with dot below
        case 0x1E54: return 0x50;    // capital p with acute
        case 0x1E55: return 0x50;    // small p with acute
        case 0x1E56: return 0x50;    // capital p with dot above
        case 0x1E57: return 0x50;    // small p with dot above
        case 0x1E58: return 0x52;    // capital r with dot above
        case 0x1E59: return 0x52;    // small r with dot above
        case 0x1E5A: return 0x52;    // capital r with dot below
        case 0x1E5B: return 0x52;    // small r with dot below
        case 0x1E60: return 0x53;    // capital s with dot above
        case 0x1E61: return 0x53;    // small s with dot above
        case 0x1E62: return 0x53;    // capital s with dot below
        case 0x1E63: return 0x53;    // small s with dot below
        case 0x1E6A: return 0x54;    // capital t with dot above
        case 0x1E6B: return 0x54;    // small t with dot above
        case 0x1E6C: return 0x54;    // capital t with dot below
        case 0x1E6D: return 0x54;    // small t with dot below
        case 0x1E72: return 0x55;    // capital u with diaeresis below
        case 0x1E73: return 0x55;    // small u with diaeresis below
        case 0x1E7C: return 0x56;    // capital v with tilde
        case 0x1E7D: return 0x56;    // small v with tilde
        case 0x1E7E: return 0x56;    // capital v with dot below
        case 0x1E7F: return 0x56;    // small v with dot below
        case 0x1E80: return 0x57;    // capital w with grave
        case 0x1E81: return 0x57;    // small w with grave
        case 0x1E82: return 0x57;    // capital w with acute
        case 0x1E83: return 0x57;    // small w with acute
        case 0x1E84: return 0x57;    // capital w with diaeresis
        case 0x1E85: return 0x57;    // small w with diaeresis
        case 0x1E86: return 0x57;    // capital w with dot above
        case 0x1E87: return 0x57;    // small w with dot above
        case 0x1E88: return 0x57;    // capital w with dot below
        case 0x1E89: return 0x57;    // small w with dot below
        case 0x1E8A: return 0x58;    // capital x with dot above
        case 0x1E8B: return 0x58;    // small x with dot above
        case 0x1E8C: return 0x58;    // capital x with diaeresis
        case 0x1E8D: return 0x58;    // small x with diaeresis
        case 0x1E8E: return 0x59;    // capital y with dot above
        case 0x1E8F: return 0x59;    // small y with dot above
        case 0x1E90: return 0x5A;    // capital z with circumflex
        case 0x1E91: return 0x5A;    // small z with circumflex
        case 0x1E92: return 0x5A;    // capital z with dot below
        case 0x1E93: return 0x5A;    // small z with dot below
        case 0x1E97: return 0x54;    // small t with diaeresis
        case 0x1E98: return 0x57;    // small w with ring above
        case 0x1E99: return 0x59;    // small y with ring above
        case 0x1EA0: return 0x41;    // capital a with dot below
        case 0x1EA1: return 0x41;    // small a with dot below
        case 0x1EA2: return 0x41;    // capital a with hook above
        case 0x1EA3: return 0x41;    // small a with hook above
        case 0x1EB8: return 0x45;    // capital e with dot below
        case 0x1EB9: return 0x45;    // small e with dot below
        case 0x1EBA: return 0x45;    // capital e with hook above
        case 0x1EBB: return 0x45;    // small e with hook above
        case 0x1EBC: return 0x45;    // capital e with tilde
        case 0x1EBD: return 0x45;    // small e with tilde
        case 0x1EC8: return 0x49;    // capital i with hook above
        case 0x1EC9: return 0x49;    // small i with hook above
        case 0x1ECA: return 0x49;    // capital i with dot below
        case 0x1ECB: return 0x49;    // small i with dot below
        case 0x1ECC: return 0x4F;    // capital o with dot below
        case 0x1ECD: return 0x4F;    // small o with dot below
        case 0x1ECE: return 0x4F;    // capital o with hook above
        case 0x1ECF: return 0x4F;    // small o with hook above
        case 0x1EE4: return 0x55;    // capital u with dot below
        case 0x1EE5: return 0x55;    // small u with dot below
        case 0x1EE6: return 0x55;    // capital u with hook above
        case 0x1EE7: return 0x55;    // small u with hook above
        case 0x1EF2: return 0x59;    // capital y with grave
        case 0x1EF3: return 0x59;    // small y with grave
        case 0x1EF4: return 0x59;    // capital y with dot below
        case 0x1EF5: return 0x59;    // small y with dot below
        case 0x1EF6: return 0x59;    // capital y with hook above
        case 0x1EF7: return 0x59;    // small y with hook above
        case 0x1EF8: return 0x59;    // capital y with tilde
        case 0x1EF9: return 0x59;    // small y with tilde
//--------------------------------------------------------------------------------------
        case 0x200C: return -1;      // zero width non-joiner
        case 0x200D: return -1;      // zero width joiner
        case 0x2113: return -1;      // script small l
        case 0x2117: return -1;      // sound recording copyright
        case 0x266D: return -1;      // music flat sign
        case 0x266F: return -1;      // music sharp sign
        case 0xFE20: return -1;      // ligature left half
        case 0xFE21: return -1;      // ligature right half
        case 0xFE22: return -1;      // double tilde left half
        case 0xFE23: return -1;      // double tilde right half
//--------------------------------------------------------------------------------------
        case 0x00A7: return -1;      // paragrafo
        case 0x007B: return -1;      // {
        case 0x007D: return -1;      // }
        case 0x005B: return -1;      // [
        case 0x005D: return -1;      // ]
        case 0x0028: return -1;      // (
        case 0x0029: return -1;      // )
        case 0x002E: return -1;      // .
        case 0x002F: return -1;      // /
        case 0x003B: return -1;      // ;
        case 0x0021: return -1;      // !
        case 0x003F: return -1;      // ?
        case 0x002D: return -1;      // -
        case 0x003A: return -1;      // :
        case 0x005F: return -1;      // _
        case 0x00A0: return -1;      //  
        case 0x002A: return -1;      // *
        case 0x007C: return -1;      // |
        case 0x0027: return 0x20;    // ' ->ritorna spazio
        case 0x0023: return 0x20;    // # ->ritorna spazio
        case 0x0026: return 0x20;    // & ->ritorna spazio
        case 0x003C: return 0x3C;    // <
        case 0x003E: return 0x3E;    // >
        case 0x005E: return 0x20;    // ^ ->ritorna spazio
        case 0x0060: return 0x20;    // ` ->ritorna spazio
        case 0x00A2: return 0x20;    // cent
        case 0x00B9: return 0x31;    // 1
        case 0x00B2: return 0x32;    // 2
        case 0x00B3: return 0x33;    // 3
        case 0x00AA: return 0x41;    // ordinale femminile
        case 0x00BA: return 0x4F;    // ordinale maschile
        case 0x00B5: return 0x4D4D;  // micron
        case 0x00D7: return 0x58;    // moltiplicato
        case 0x002C: return -1;      // ,
        case 0x0022: return 0x20;    // virgolette ->ritorna spazio
        case 0x00AB: return -1;      // virgolette ad angolo aperte
        case 0x00BB: return -1;      // virgolette ad angolo chiuse
        case 0x00F7: return 0x20;    // da a
//		----------------------------FINSIEL---------------------------------------------------
		case 0x0226: return 0x41;  	 // A_MAIUSCOLA_CON_PUNTO_SOPRASCRITTO
		case 0x1E0E: return 0x44;  	 // D_MAIUSCOLA_CON_SOTTOLINEATURA
		case 0x1E34: return 0x4B;  	 // K_MAIUSCOLA_CON_SOTTOLINEATURA
		case 0x1E6E: return 0x54;  	 // T_MAIUSCOLA_CON_SOTTOLINEATURA
		case 0x1E94: return 0x5A;  	 // Z_MAIUSCOLA_CON_SOTTOLINEATURA
		case 0x0227: return 0x41;  	 // A_MINUSCOLA_CON_PUNTO_SOPRASCRITTO
		case 0x1E0F: return 0x44;  	 // D_MINUSCOLA_CON_SOTTOLINEATURA
		case 0x1E96: return 0x48;  	 // H_MINUSCOLA_CON_SOTTOLINEATURA
		case 0x1E35: return 0x4B;  	 // K_MINUSCOLA_CON_SOTTOLINEATURA
		case 0x0282: return 0x53;  	 // S_MINUSCOLA_CON_GANCIO_A_SINISTRA
		case 0x1E6F: return 0x54;  	 // T_MINUSCOLA_CON_SOTTOLINEATURA
		case 0x1E95: return 0x5A;  	 // Z_MINUSCOLA_CON_SOTTOLINEATURA
		case 0x201E: return 0x20;    // VIRGOLETTE_SOTTO_LINEA_APERTE
		case 0x0024: return 0x20;    // DOLLARO
		case 0x00A5: return 0x20;    // YEN
		case 0x271D: return 0x20;    // CROCE_SEMPLICE
		case 0x2018: return -1;  	 // VIRGOLETTA_SOPRA_LINEA_APERTA
		case 0xFEC9: return -1;  	 // AIN
		case 0xFE84: return -1;  	 // ALIF
		case 0x201A: return -1;  	 // VIRGOLETTE_SOTTO_LINEA_APERTA
		case 0x271F: return -1;  	 // CROCE_DOPPIA
		case 0x2019: return -1;  	 // VIRGOLETTA_SOPRA_LINEA_CHIUSA
		case 0x044C: return -1;  	 // SEGNO_DI_DEBOLE
		case 0x044A: return -1;  	 // SEGNO_DI_FORTE
		case 0x0669: return 0x20;  	 // NOTA_DI_MUSICA
		case 0x0126: return 0x48;  	 // H_TAGLIATA_MAIUSCOLA
		case 0x0127: return 0x48;  	 // H_TAGLIATA_MINUSCOLA
		case 0x013F: return 0x4C;  	 // L_CON_PUNTO_MAIUSCOLA
		case 0x0140: return 0x4C;  	 // L_CON_PUNTO_MINUSCOLA
		case 0x0166: return 0x54;  	 // T_TAGLIATA_MAIUSCOLA
		case 0x0167: return 0x54;  	 // T_TAGLIATA_MINUSCOLA
		case 0x0149: return 0x4E;  	 // N_CON_APOSTROFO
		case 0x014A: return 0x4E;  	 // ENG_MAIUSCOLO
		case 0x014B: return 0x4E;  	 // ENG_MINUSCOLO
		case 0x2080: return 0x30;  	 // 0
		case 0x2081: return 0x31;  	 // 1
		case 0x2082: return 0x32;  	 // 2
		case 0x2083: return 0x33;  	 // 3
		case 0x2084: return 0x34;  	 // 4
		case 0x2085: return 0x35;  	 // 5
		case 0x2086: return 0x36;  	 // 6
		case 0x2087: return 0x37;  	 // 7
		case 0x2088: return 0x38;  	 // 8
		case 0x2089: return 0x39;  	 // 9
		case 0x2070: return 0x30;  	 // 0
		case 0x2071: return 0x31;  	 // 1
		case 0x2072: return 0x32;  	 // 2 
		case 0x2073: return 0x33;  	 // 3
		case 0x2074: return 0x34;  	 // 4
		case 0x2075: return 0x35;  	 // 5
		case 0x2076: return 0x36;  	 // 6
		case 0x2077: return 0x37;  	 // 7
		case 0x2078: return 0x38;  	 // 8
		case 0x2079: return 0x39;  	 // 9
		case 0x0132: return 0x494A;	 // IJ_OLANDESE_MAIUSCOLA
		case 0x0133: return 0x494A;	 // IJ_OLANDESE_MINUSCOLA
		case 0x2126: return 0x4F484D;// OHM
		case 0x1D12C: return 0x42;   // BEMOLLE
		case 0x03B1: return 0x41;    // DAL GRECO AL LATINO
		case 0x03B2: return 0x42;    //   
		case 0x03B3: return 0x44;    //   
		case 0x03B4: return 0x45;    //   
		case 0x03B5: return 0x46;    //   
		case 0x03DB: return 0x47;    //   
		case 0x03DD: return 0x48;    //   
		case 0x03B6: return 0x49;    //   
		case 0x03B7: return 0x4A;    //   
		case 0x03B8: return 0x4B;    //   
		case 0x03B9: return 0x4C;    //   
		case 0x03BA: return 0x4D;    //   
		case 0x03BB: return 0x4E;    //   
		case 0x03BC: return 0x4E;    //   
		case 0x03BD: return 0x50;    //   
		case 0x03BE: return 0x51;    //   
		case 0x03BF: return 0x52;    //   
		case 0x03C0: return 0x53;    //   
		case 0x03C1: return 0x55;    //   
		case 0x03C3: return 0x56;    //   
		case 0x03C4: return 0x58;    //   
		case 0x03C5: return 0x59;    //  
		case 0x03C6: return 0x5A;    //  
		case 0x03C8: return 0x5053;  //  
		case 0x03A7: return 0x4348;  //  
		case 0x03A9: return 0x4F;    //  
		case 0x03C7: return 0x4348;  //  
		case 0x03A8: return 0x5053;  //  
		case 0x03C9: return 0x4F;    //  
		case 0x0391: return 0x41;    //
		case 0x0392: return 0x42;    //   
		case 0x0393: return 0x44;    //   
		case 0x0394: return 0x45;    //   
		case 0x0395: return 0x46;    //   
		case 0x03DA: return 0x47;    //   
		case 0x03DC: return 0x48;    //   
		case 0x0396: return 0x49;    //   
		case 0x0397: return 0x4A;    //   
		case 0x0398: return 0x4B;    //   
		case 0x0399: return 0x4C;    //   
		case 0x039A: return 0x4D;    //   
		case 0x039B: return 0x4E;    //   
		case 0x039C: return 0x4E;    //   
		case 0x039D: return 0x50;    //   
		case 0x039E: return 0x51;    //   
		case 0x039F: return 0x52;    //   
		case 0x03A0: return 0x53;    //   
		case 0x03A1: return 0x55;    //   
		case 0x03A3: return 0x56;    //   
		case 0x03A4: return 0x58;    //   
		case 0x03A5: return 0x59;    //  
		case 0x03A6: return 0x5A;    //  	      
//		--------------------------------------------------------------------------------------
   default:
        {
            if (i==0x20 || (i>=0x41 && i<=0x5A) || (i>=0x61 && i<=0x7A) 
            	|| (i>=0x30 && i<=0x39)) 
            	return i;
//            else {
//                EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3099,"Carattere non valido");
//                ecc.appendMessaggio(":"+(char)i+" codice:"+i);
//                throw ecc;
//            }
        } 
	    return -1;             // if no match, return carattere
	}

    }

}