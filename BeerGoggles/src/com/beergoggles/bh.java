package com.beergoggles;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



public class bh {
	public static void main(String[] args){

		testSorting();


	}
	public static void testSorting(){
		Beer a = new Beer("Sierra Nevada Pale Ale","N/A","91","92","083783375220");
		Beer b = new Beer("Arrogant Bastard Ale","7.20%","N/A","98","636251780004");
		Beer c = new Beer("Newcastle Brown Ale","4.70%","76","N/A","088345100555");
		Beer d = new Beer("Guinness Draught","4.20%","79","65","083820234800");
		Beer e = new Beer("Sierra Nevada Torpedo","7.20%","93","99","083783675054");
		
		ArrayList<Beer> beers = new ArrayList<Beer>();
		beers.add(a); beers.add(b); beers.add(c); beers.add(d); beers.add(e);
		sort(beers,"ba");
	}

	
	
	
	public static ArrayList<Beer> sort(ArrayList<Beer> beers, String sortby){
		//beers.
		Comparator<Beer> comp;
		if(sortby=="ba"){
			comp = new BAComparator();
		}
		else if(sortby =="abv"){
			comp = new ABVComparator();
		}
		else if(sortby == "bro"){
			comp = new BroComparator();
		}
		else if(sortby == "price"){
			comp = new PriceComparator();
		}
		else if(sortby == "bang"){
			comp = new BangComparator();
		}
		else{
			comp = null;
		}
		
//		for(Beer b : beers){
//			System.out.println(b.toString());
//		}
		//System.out.println(beers);
		Collections.sort(beers,comp);
		return beers;
//		System.out.println();
//		for(Beer b : beers){
//			System.out.println(b.toString());
//		}
	}
}
	
	