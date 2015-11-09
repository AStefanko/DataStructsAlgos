import java.util.*;


public class MapMarkovModel extends AbstractModel{
	private String myString;
	private Random myRandom;
	public static final int DEFAULT_COUNT = 100;
	public static int RANDOM_SEED = 1234;
	
	public MapMarkovModel() {
		myRandom=new Random(RANDOM_SEED);
		//Map<String, ArrayList<Character>> daMap = new HashMap<String, ArrayList<Character>>();
			
	}
	
	 public void initialize(Scanner s) {
	        double start = System.currentTimeMillis();
	        int count = readChars(s);
	        double end = System.currentTimeMillis();
	        double time = (end - start) / 1000.0;
	        super.messageViews("#read: " + count + " chars in: " + time + " secs");
	    }
	
	protected int readChars(Scanner s) {
        myString = s.useDelimiter("\\Z").next();
        s.close();
        return myString.length();
    }
	
	//The only difference between this and the one from the brute force
	//is that I changed out my Ngram model 
	public void process(Object o) {
        String temp = (String) o;
        String[] nums = temp.split("\\s+");
        int k = Integer.parseInt(nums[0]);
        int lettahs = DEFAULT_COUNT;
        if (nums.length > 1) {
            lettahs = Integer.parseInt(nums[1]);
        }
        double stime = System.currentTimeMillis();
        String text = makeNGram(k, lettahs);
        double etime = System.currentTimeMillis();
        double time = (etime - stime) / 1000.0;
        this.messageViews("time to generate: " + time +" | chars generated:" + 
        		text.length()); //For benchmarking purposes
        this.notifyViews(text);
    
    }
	

	
	protected String makeNGram(int k, int lettahs) {
		//This is the pseudocode to a T
		//random starting index
		int start = myRandom.nextInt(myString.length() - k+1);
		//This is the seed string, same as in MarkovModel
        String seed1 = myString.substring(start, start + k);
        
        //Making da Map
        HashMap<String, ArrayList<Character>> daMap = new HashMap<String, ArrayList<Character>>();
        //If there is nothing in the map, then you build that ish 
		if (daMap.size()==0) {
			daMap=Mapper(k);
		}

		
		//because apparently this is faster than appending? 
		//This comes from Markov
		StringBuilder build = new StringBuilder();
        
        for(int i=0; i<lettahs; i++) {
        	if (daMap.get(seed1)==null) {
        		break;
        	}
        	ArrayList<Character> chars1 = daMap.get(seed1);
        	
        	int cSize = chars1.size();
        	int r1 = myRandom.nextInt(cSize);
        	Character n1 = chars1.get(r1);
        	
        	//trying to deal with EOF
        	if (n1 == 0)  {
            	break;
        	}
        	//Jacked this straight from MarkovModel
        	build.append(n1);
        	seed1 = seed1.substring(1) + n1;
        }
        String fina= build.toString();
        String fina2 = fina.trim();
        return fina2;
	}
	
	public HashMap<String, ArrayList<Character>> Mapper(int k) {
		
		HashMap<String, ArrayList<Character>> elMap = new HashMap<String, ArrayList<Character>>();
		for(int j=0; j<myString.length()-k+1; j++) {
			ArrayList<Character> lst1= new ArrayList<Character>();
			String ks = myString.substring(j, j+k);
			if(!elMap.containsKey(ks)) {
				lst1 = new ArrayList<Character>();
			} else {
				lst1=elMap.get(ks);
			}
			if (j + k >= myString.length()) {
				lst1.add((char) 0);
				break;
			}
			Character newt = myString.charAt(j+k);
			lst1.add(newt);
			elMap.put(ks, lst1);
			
		}
		return elMap;
		
	}
		

		
}


//Working on extrusion cooking
//trying to make rice flour a fibrous thing 
//Undergrad degree in biotech