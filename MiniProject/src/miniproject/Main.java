package miniproject;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
	
	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static int difficulty = 5;

	public static void main(String[] args) {
            
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/knight","root","");
                System.out.println("JDBC Connected");
                
                System.out.println("BLOCKCHAIN SUPPLY CHAIN");
//                LoginFrame lg = new LoginFrame();
//                lg.setVisible(true);
                
                System.out.println("Dairy Farm:");
                DairyFarm df = new DairyFarm();
                String query11 = "SELECT comments FROM `dairyfarm` WHERE id=(SELECT max(id) FROM dairyfarm);";
                PreparedStatement stmt1 = conn.prepareStatement(query11);
                ResultSet rs1= stmt1.executeQuery();
                while (rs1.next()) {
                   String result=rs1.getString(1);
                   addBlock(new Block(result, "0")); 
                   System.out.println(result);
                }
                
                    
                String query12="INSERT into dfblock (hash,prevhash,data,timestamp,nonce) values(?,?,?,?,?);";
                PreparedStatement state1 = conn.prepareStatement(query12);  
                state1.setString(1,blockchain.get(0).hash);
                state1.setString(2,blockchain.get(0).previousHash);
                state1.setString(3,blockchain.get(0).data);
                state1.setString(4,blockchain.get(0).timeStamp);
                state1.setInt(5,blockchain.get(0).nonce);
                state1.executeUpdate();
                
                
                
                System.out.println("Milk Distributer");
                String query21 = "SELECT comments FROM `milkdistributer` WHERE id=(SELECT max(id) FROM milkdistributer);";
                PreparedStatement stmt2 = conn.prepareStatement(query21);
                ResultSet rs2= stmt2.executeQuery();
                while (rs2.next()) {
                    String result=rs2.getString(1);
                    addBlock(new Block(result,blockchain.get(blockchain.size()-1).hash));  
                    System.out.println(result);
                }
                
                String query22="INSERT into mdblock (hash,prevhash,data,timestamp,nonce) values(?,?,?,?,?);";
                PreparedStatement state2 = conn.prepareStatement(query22);  
                state2.setString(1,blockchain.get(1).hash);
                state2.setString(2,blockchain.get(1).previousHash);
                state2.setString(3,blockchain.get(1).data);
                state2.setString(4,blockchain.get(1).timeStamp);
                state2.setInt(5,blockchain.get(1).nonce);
                state2.executeUpdate();
                
                System.out.println("Cheese Manufacturer");
                String query31 = "SELECT comments FROM `cheesemanufacturer` WHERE id=(SELECT max(id) FROM cheesemanufacturer);";
                PreparedStatement stmt3 = conn.prepareStatement(query31);
                ResultSet rs3= stmt3.executeQuery();
                while (rs3.next()) {
                    String result=rs3.getString(1);
                    addBlock(new Block(result,blockchain.get(blockchain.size()-1).hash));  
                    System.out.println(result);
                }
                
                String query32="INSERT into cmblock (hash,prevhash,data,timestamp,nonce) values(?,?,?,?,?);";
                PreparedStatement state3 = conn.prepareStatement(query32);  
                state3.setString(1,blockchain.get(2).hash);
                state3.setString(2,blockchain.get(2).previousHash);
                state3.setString(3,blockchain.get(2).data);
                state3.setString(4,blockchain.get(2).timeStamp);
                state3.setInt(5,blockchain.get(2).nonce);
                state3.executeUpdate();                
                
                System.out.println("Cheese Distributer");
                String query41 = "SELECT comments FROM `cheesedistributer` WHERE id=(SELECT max(id) FROM cheesedistributer);";
                PreparedStatement stmt4 = conn.prepareStatement(query41);
                ResultSet rs4= stmt4.executeQuery();
                while (rs4.next()) {
                    String result=rs4.getString(1);
                    addBlock(new Block(result,blockchain.get(blockchain.size()-1).hash));   
                    System.out.println(result);
                }
                
                String query42="INSERT into cdblock (hash,prevhash,data,timestamp,nonce) values(?,?,?,?,?);";
                PreparedStatement state4 = conn.prepareStatement(query42);  
                state4.setString(1,blockchain.get(3).hash);
                state4.setString(2,blockchain.get(3).previousHash);
                state4.setString(3,blockchain.get(3).data);
                state4.setString(4,blockchain.get(3).timeStamp);
                state4.setInt(5,blockchain.get(3).nonce);
                state4.executeUpdate();                
                
                System.out.println("\nBlockchain is Valid: " + isChainValid());
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            

	}
	
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);

			if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
				System.out.println("Current Hashes not equal");			
				return false;
			}

			if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}

			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
			
		}
		return true;
	}
	
	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
        
        
}
