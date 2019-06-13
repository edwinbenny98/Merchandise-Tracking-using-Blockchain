package miniproject;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar;

interface Ledger{
	
	public String calculateHash();
	public String calculateDate();
	
}



public class Block implements Ledger{
	
	public String hash;
	public String previousHash;	
	public String data;
	public String timeStamp;
	public int nonce;
	 
	public Block(String data,String previousHash ) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = calculateDate();
		this.hash = calculateHash();
	}
	

	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256( 
				previousHash +
				timeStamp +
				Integer.toString(nonce) + 
				data 
				);
		return calculatedhash;
	}
	
	public String calculateDate() {
		 Date date = Calendar.getInstance().getTime();  
         DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss.SSS");  
         String strDate = dateFormat.format(date);  
         return strDate;
	}
	

	public void mineBlock(int difficulty) {
		String target = StringUtil.getDificultyString(difficulty);
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}
	
}
