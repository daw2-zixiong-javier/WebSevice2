package daw2.javaClient;

import java.util.Scanner;

public class SoapAsyncRequest extends Thread {
	
	private String toModifyTile;
    private String modifiedTitle;

	public SoapAsyncRequest (String toModifyTile) {
		this.toModifyTile = toModifyTile;
		this.modifiedTitle = "";
	}
	
	public String getModifiedTitle() {
		System.out.println("     Introduce el nuevo titulo:");
		Scanner s = new Scanner(System.in);
		return s.nextLine();
	}
	
	public void run() {
		SimpleService_P1_Client soapClient = new SimpleService_P1_Client();
		//this.modifiedTitle = soapClient.modifyDrupalTitle(toModifyTile);
	}
}
