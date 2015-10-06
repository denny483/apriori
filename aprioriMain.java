import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Set;
//
//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//May Budda bless you no bug forever
//
//
//
public class aprioriMain {
	public static String[] SpiltedString, SpiltedLarge, temp1, cantemp,
			entrytemp,input2tempString;
	public static int largeMax = 2, largeValue = 2, vectorIndex = 0,
			frequency = 0, totalitemset = 0, count, templinecount = 0,
			tempvalue;
	public static Map<String, Integer> result = new HashMap<String, Integer>();
	public static Map<String, Integer> candidates = new HashMap<String, Integer>();
	public static Map<String, Integer> preLarge = new HashMap<String, Integer>();
	public static List<String[]> tmppreLarge = new LinkedList<String[]>();
	public static List<String[]> input2list = new LinkedList<String[]>();
	public static List<String[]> inputlist = new LinkedList<String[]>();
	public static List<String[]> candilist = new LinkedList<String[]>();
	public static Set<String> maptemp = new HashSet<String>();
	public static Set<String> maptemp2 = new HashSet<String>();
	public static HashSet<String> set = new HashSet<String>();
	public static double confidence = 0.5, linecount = 0;
	public static String Temp, temp2, coin, tempkey;
	public static double endTime,startTime;
	public static Scanner input, input2;
	private static boolean findcand;
	public static FileWriter fw;
	
	public static boolean canbeCandidate(String[] canbeCandidateString) {
		cantemp = new String[largeValue];
		for (int i = 0; i < largeValue; i++) {
			cantemp = Arrays.copyOf(canbeCandidateString,canbeCandidateString.length);
			cantemp[i] = " ";
			coin = "";
			for (int j = 0; j < largeValue; j++) {
				if (cantemp[j].equals(" "))
					continue;
				coin += cantemp[j] + " ";
			}
			if (!preLarge.containsKey(coin))
				return false;
		}
		return true;
	}

	public static void findCandidates() {
		Iterator<String[]> it = candilist.iterator();
		tmppreLarge.clear();
		tmppreLarge.addAll(candilist);
		Iterator<String[]> it2 = tmppreLarge.iterator();
		candidates = new HashMap<String, Integer>();
		while (it.hasNext()) {
			String[] findcanarray1 = it.next();
			it2 = tmppreLarge.iterator();
			while (it2.hasNext()) {
				String[] findcanarray2 = it2.next();
				if ( findcanarray1.equals( findcanarray2)) {
					it2.remove();
					continue;
				}
				if (largeValue == 2){
					temp1=new String[2];
					temp1[0] = findcanarray1[0];
					temp1[1] = findcanarray2[0];
					}
				if (largeValue > 2) {
					boolean canbe = true;
					for(int i=0;i<largeValue-2;i++){
						if(!findcanarray1[i].equals(findcanarray2[i])){
							canbe=false;
						}
					}
					if(!canbe)continue;
					String[] temp1afterdisc = new String[largeValue];
					for(int i=0;i<largeValue-1;i++)
					{
						temp1afterdisc[i]=findcanarray1[i];
					}
					temp1afterdisc[largeValue-1]=findcanarray2[largeValue-2];
					Arrays.sort(temp1afterdisc);
					if (temp1afterdisc.length > largeValue)
						continue;
					if (canbeCandidate(temp1afterdisc)) {
						temp2 = "";
						for (String tempsort : temp1afterdisc)
							temp2 += tempsort + " ";
						candidates.put(temp2, 0);
					}
				} else {
					Arrays.sort(temp1);
					temp2 = "";
					for (String tempsort : temp1)
						temp2 += tempsort + " ";
					candidates.put(temp2, 0);
				}
			}
		}
	}

	public static void printitemset(Iterator<Map.Entry<String, Integer>> it)
			throws IOException {
		FileWriter fw = new FileWriter(new File("itemset.txt"));
		while (it.hasNext()) {
			Map.Entry<String, Integer> entry = it.next();
			fw.write("{" + entry.getKey() + "} :" + entry.getValue() + "\n");
		}
		fw.write("total Itemsets:" + result.size()+ "\n");
		fw.write("linecount:"+linecount+ "\n");
		fw.write("confidence:" + confidence+ "\n");
		fw.write("Database created in " + (endTime - startTime)/ 1000.0 + " seconds\n");
	//	System.out.println("total Itemsets:" + result.size());
		fw.flush();
		fw.close();
	}

	public static void deleteconfidence(Iterator<Map.Entry<String, Integer>> it) {
		candilist.clear();
		while (it.hasNext()) {
			Map.Entry<String, Integer> entry = it.next();
			if (entry.getValue() < confidence) {
				it.remove();
			}
			else{
				candilist.add(entry.getKey().split(" +"));
			}
		}
	}

	public static void getLarge() throws IOException {
		entrytemp = new String[largeValue];
		String[] inputStringarray;
		Iterator<String[]> inputit = inputlist.iterator();
		while (inputit.hasNext()) {
			maptemp.clear();
			String[] inputitarray = inputit.next();
			for (int i = 0; i < inputitarray.length; i++) {
				maptemp.add(inputitarray[i]);
			}
			Iterator<String[]> it = input2list.iterator();
			while (it.hasNext()) {
				inputStringarray = it.next();
				maptemp2.clear();
				for (String s : inputStringarray) {
					maptemp2.add(s);
				}
				findcand = false;
				if (maptemp.containsAll(maptemp2)) {
					findcand = true;
				}
				if (findcand) {
					coin = "";
					for (String s : inputStringarray)
						coin += s + " ";
					count = candidates.get(coin);
					candidates.put(coin, count + 1);
				}
			}
		}
		deleteconfidence(candidates.entrySet().iterator());
	}

	public static void main(String[] args) throws IOException {
		
		String inputstring;
		Scanner scanner = new Scanner(System.in);
//		System.out.println("請輸入要讀取的文件名稱：");
	//	inputstring= scanner.next();
	//	 System.out.println("請輸入MinSup：");
	//	 confidence = scanner.nextInt();
			input = new Scanner(new File("T15I6N0.5KD1K.txt"));
			startTime = System.currentTimeMillis();
		while (input.hasNext()) {// 掃描資料並且建出C1 並且將切割開的資料寫到temp.txt當中
			Temp = input.nextLine();
			linecount++;
			SpiltedString = Temp.split("[ ]*[,][ ]*");
			inputlist.add(SpiltedString);
			for (int i = 0; i < SpiltedString.length; i++) {// 製作出C1
				if (candidates.containsKey(SpiltedString[i])) {
					count = candidates.get(SpiltedString[i]);
					candidates.put(SpiltedString[i], count + 1);
				} // end if
				else
					candidates.put(SpiltedString[i], 1);
			}
		}
		input.close();
		endTime = System.currentTimeMillis();
		System.out.println("Database created in " + (endTime - startTime)/ 1000.0 + " seconds");
		confidence = (confidence / 100.00) * linecount;
		//confidence =60000;
		//System.out.println("large: 1"+"  candidates size:"+candidates.size());
		deleteconfidence(candidates.entrySet().iterator());// deleteconfidence
	//	System.out.println("large: 1"+"  Large size:"+candidates.size());
		result.putAll(candidates);
		preLarge.putAll(candidates);
		while (true) {
			findCandidates();
			
			//System.out.println("large: "+largeValue+"  candidates size:"+candidates.size());
			Iterator<Entry<String, Integer>> it = candidates.entrySet().iterator();
			input2list.clear();
			while (it.hasNext()) {
				Entry<String, Integer> entry = it.next();
				input2tempString = entry.getKey().split(" +");
				input2list.add(input2tempString);
			}
			getLarge();
			//System.out.println("large: "+largeValue+" large  size:"+candidates.size());
			preLarge.clear();
			preLarge.putAll(candidates);
			result.putAll(candidates);
			if (candidates.size() == 0) {
				break;
			}
			candidates.clear();
			largeValue++;
		}
		endTime = System.currentTimeMillis();
		printitemset(result.entrySet().iterator());
		System.out.println("Database created in " + (endTime - startTime)/ 1000.0 + " seconds");
	}
}
