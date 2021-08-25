package study;




import java.awt.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import study1.tset1;

public class test {

	public static void main(String[] args) {
		//오버로드
		System.out.println("-------------------오버로드------------------");
		tset1 probuct1= new tset1();
		probuct1.setProductNo("1");
		probuct1.setProductName("아메리카노");
		probuct1.setPrice(4000);
		tset1 probuct2= new tset1();
		probuct2.setProductInfo("2", "카푸치노");
		tset1 probuct3= new tset1();
		probuct3.setProductInfo("2", "카푸치노", 5);
		
		//배열
		int[] arry = new int[]{1,55,3,2,12};
		for(int i=0; i<arry.length;i++) {
			System.out.println(arry[i]);
		}
		System.out.println("-------------------다차원배열------------------");
		//다차원 배열
		String [][] arr = {{"자료1","자료2","자료3"},{"자료4","자료5","자료6"}};
		System.out.println(arr[1][0]);
		//정렬
		Arrays.sort(arry);
		
		System.out.println(Arrays.toString(arry));
		//향상된 for문 
		System.out.println("-------------------향상된 for문------------------");
		for(int a : arry) {
			System.out.println(a);
		}
		
		//구구단
		System.out.println("-------------------for문 구구단------------------");
//		for(int i = 1; i<=9; i++) {
//			System.out.println(i+"단");
//			for(int j = 1; j<=9; j++) {
//				System.out.println(i+"*"+j+"="+i*j);
//			}
//		}
		
		//ArrayList
		System.out.println("-------------------ArrayList------------------");
		ArrayList<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		System.out.println(list.get(0));
		
		
		//Map
		System.out.println("-------------------Map------------------");
		HashMap<String ,String> map = new HashMap <String ,String>();
		map.put("1","value1");
		map.put("2","value2");
		map.put("3","value3");
		System.out.println(map.get("3"));
	
		
	}
}
