
import java.util.*;

public class WordMarkovModel extends AbstractModel {
	
	protected String myString;
    protected Random myRandom;
    private String[] myWords;
    public static final int DEFAULT_COUNT = 100; // default # random letters generated
    public static int RANDOM_SEED = 1234; 
    //public HashMap<WordNgram, ArrayList<WordNgram>> daMap;
    
    public WordMarkovModel() {
        myRandom = new Random(RANDOM_SEED);
    }
    
    public void initialize(Scanner s) {
        double start = System.currentTimeMillis();
        int count = readChars(s);
        myWords = myString.split("\\s+");
        double end = System.currentTimeMillis();
        double time = (end - start) / 1000.0;
        super.messageViews("#read: " + myWords.length + " chars in: " + time + " secs");
    }
    
    protected int readChars(Scanner s) {
        myString = s.useDelimiter("\\Z").next();
        s.close();
        return myString.length();
    }
    
    public void process(Object o) {
        String temp = (String) o;
        String[] nums = temp.split("\\s+");
        int k = Integer.parseInt(nums[0]);
        int nWerds = DEFAULT_COUNT;
        if (nums.length > 1) {
            nWerds = Integer.parseInt(nums[1]);
        }
        
        double stime = System.currentTimeMillis();
        String text = makeNGram(k, nWerds);
        double etime = System.currentTimeMillis();
        double time = (etime - stime) / 1000.0;
        this.messageViews("time to generate: " + time +" | chars generated:" + 
        		text.length()); //For benchmarking purposes
        this.notifyViews(text);
        
    }
    
    protected String makeNGram(int k, int nWerds) {
    	
    	int start = myRandom.nextInt(myWords.length-k+1);
    	//Pretty much the same thing as the seed from MapMarkovModel 
    	WordNgram str1 = new WordNgram(myWords, start, k);
    	
    	HashMap<WordNgram, ArrayList<WordNgram>> daMap = new HashMap<WordNgram, ArrayList<WordNgram>>();
    	//Make the map if it ain't there 
    	if (daMap.size()==0) {
			daMap=Mapper(k);
		}
    	
    	//Same with the MapMarkovModel equivalent of this 
    	StringBuilder build = new StringBuilder();
    	
        
        for(int i=0; i<nWerds; i++) {
        	if (daMap.get(str1)==null) {
        		break;
        	}
        	ArrayList<WordNgram> newWs = daMap.get(str1);
        	int nSize = newWs.size();
        	int r1 = myRandom.nextInt(nSize);
        	WordNgram n1 = newWs.get(r1);
        	
        	//dealing with EOF
        	if (n1==null) {
        		break;
        	}
        	//String after = newWs.wordAt(i+k);
        	String n2 = n1.wordAt();
        	
            build.append(n2);
            if(n2!= " ") {
            	build.append(" ");
            }
            str1 = n1;
        }
        String fina= build.toString();
        String fina2 = fina.trim();
        return fina2;
    }
    
    public HashMap<WordNgram, ArrayList<WordNgram>> Mapper(int k) {
		HashMap<WordNgram, ArrayList<WordNgram>> elMap = new HashMap<WordNgram, ArrayList<WordNgram>>();
		
		//I stop it at length-k so I don't get an error toward the end of the array
		for(int b=0; b<myWords.length-k+1; b++) {
			ArrayList<WordNgram> lst1= new ArrayList<WordNgram>();
			WordNgram kws = new WordNgram(myWords, b, k);
			if(!elMap.containsKey(kws)) {
				lst1 = new ArrayList<WordNgram>();
			} 
			else {
				lst1=elMap.get(kws);
			}
			//Deal with EOF
			if (b + k >= myWords.length) {
				lst1.add(null);
				elMap.put(kws, lst1);
				break;
			} else{
				WordNgram n2 = new WordNgram(myWords, b+1, k);
				lst1.add(n2);
				elMap.put(kws, lst1);
			}

		}

		return elMap;
		
	}

}
