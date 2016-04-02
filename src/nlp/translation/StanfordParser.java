package nlp.translation;

import java.io.StringReader;
import java.util.List;
import java.util.*;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.Tree;



public class StanfordParser {
	
    private final static String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";    
    private final static String PCG_MODEL2 = "edu/stanford/nlp/models/lexparser/arabicFactored.ser.gz";        


    private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");

    private final LexicalizedParser englishParser = LexicalizedParser.loadModel(PCG_MODEL);
    private final LexicalizedParser arabicParser = LexicalizedParser.loadModel(PCG_MODEL2);
    
    private List<CoreLabel> tokenize(String str) {
        Tokenizer<CoreLabel> tokenizer =
            tokenizerFactory.getTokenizer(
                new StringReader(str));    
        return tokenizer.tokenize();
    }
    
    public Tree englishParse(String str) {                
        List<CoreLabel> tokens = tokenize(str);
        Tree tree = englishParser.apply(tokens);
        return tree;
    }
    
    public Tree arabicParse(String str){
    	 List<CoreLabel> tokens = tokenize(str);
         Tree tree = arabicParser.apply(tokens);
         return tree;
    }
    
    public List<Pair<String,String>> parseEnglishPhrase(String s) {
    	List<Pair<String,String>> myList = new ArrayList<Pair<String,String>>();
    	
    	   StanfordParser parser = new StanfordParser(); 
           Tree tree = parser.englishParse(s);
           
           //------------------------------------------------------------------
           String temp = goShow(tree, 0);
           System.out.println(temp);
           //------------------------------------------------------------------
           
           List<Tree> leaves = tree.getLeaves();
           
           for (Tree leaf : leaves) { 
               Tree parent = leaf.parent(tree);
               //System.out.print(leaf.label().value() + "-" + parent.label().value() + " ");
               Pair<String,String>p = new Pair(leaf.label().value(), parent.label().value());
               myList.add(p);
           }
    	
    	return myList;
    }
    
    public List<Pair<String,String>> parseArabicPhrase(String s) {
    	List<Pair<String,String>> myList = new ArrayList<Pair<String,String>>();
    	
 	   StanfordParser parser = new StanfordParser(); 
       Tree tree = parser.arabicParse(s);
       //------------------------------------------------------------------
       String temp = goShow(tree, 0);
       System.out.println(temp);
       //------------------------------------------------------------------
       List<Tree> leaves = tree.getLeaves();
        
        for (Tree leaf : leaves) { 
            Tree parent = leaf.parent(tree);
            //System.out.print(leaf.label().value() + "-" + parent.label().value() + " ");
            Pair<String,String>p = new Pair(leaf.label().value(), parent.label().value());
            myList.add(p);
        }
 	
        return myList;
    }

	private String goShow(Tree tree, int d) {
		Tree []ch = tree.children();
		int i;
		String ret = "(" + tree.label().value() + " ";
		if (ch.length == 0) ret = tree.label().value();
		for (i = 0; i < d; ++i) System.out.print(" ");
		System.out.println(tree.label().value());
		
		for (Tree node : ch) {
			ret += goShow(node, d + 3);
		}
		ret += ")";
		return ret;
	}



}
