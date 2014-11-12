package hu.gyerob.mihf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	public Main() {

	}

	public static void main(String args[]) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"res/wine.data"));
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
