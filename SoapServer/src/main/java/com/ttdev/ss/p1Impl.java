package com.ttdev.ss;

import java.util.Scanner;

public class p1Impl implements SimpleService {

	public String concat(String s1, String s2) {
		//return s1.toUpperCase();
		System.out.println("     Introduce el nuevo titulo:");
		Scanner s = new Scanner(System.in);
		
		return s.nextLine() + " Esto es una string";
	}

}
