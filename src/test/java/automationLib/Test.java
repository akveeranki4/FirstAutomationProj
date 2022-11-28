package automationLib;

import java.util.Arrays;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str2 = "2+2?";
		String splitStr = str2.replace("?", "");
		System.out.println(splitStr);
		String[] arr = splitStr.split("\\+");
		int[] values = Arrays.stream(arr).mapToInt(Integer::parseInt).toArray();
		for (int k = 0; k < values.length; k++)
			System.out.println(values[k]);

	}

}
