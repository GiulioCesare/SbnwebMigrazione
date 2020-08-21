
public class CharMap {
		String codiceEsabyte;
		String codiceUnimarc;
		String codiceUnicodeUTF16;
		String nome;

		CharMap(String codiceEsabyte, String codiceUnimarc,
				String codiceUnicode, String nome) {
			this.codiceEsabyte = new String(codiceEsabyte);
			this.codiceUnimarc = new String(codiceUnimarc);
			this.codiceUnicodeUTF16 = new String(codiceUnicode);
			this.nome = nome;
		}

}
