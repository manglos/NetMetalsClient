public class Main{
	
	public static Long S, T;
	public static double C[];
	public static int M_HEIGHT = 4;
	public static int M_WIDTH = 16;
	public static volatile TempPoint rMap[][], wMap[][];
	public static Long MAX_TEMP=(long)(10000 * 100);
	
	public Main(Long sVal, Long tVal, double cVal[], int hVal, int wVal, Long mVal, TempPoint[][] rM, TempPoint[][] wM){
		S=sVal;T=tVal;C=cVal;M_HEIGHT=hVal;M_WIDTH=wVal;MAX_TEMP=mVal;rMap=rM;wMap=wM;
	} 
	

}

