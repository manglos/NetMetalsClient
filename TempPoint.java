import java.util.Random;
import java.io.Serializable;

public class TempPoint implements Serializable{

	private static final long serialVersionUID = 2012546879875911708L;
	final int xPos, yPos;
	volatile Long temperature;
	final double p[];

	TempPoint(int x, int y, Long t, double ps[]){
		xPos = x;
		yPos = y;
		p=new double[3];
		p[0]=ps[0];
		p[1]=ps[1];
		p[2]=ps[2];
		temperature = t;
	}

	String getKey(){
		return xPos+"-"+yPos;
	}

	int getCol(){
		return xPos;
	}

	int getRow(){
		return yPos;
	}

	void setTemp(Long t){
		temperature = t;
	}

	double[] getPtgs(){
		return p;
	}
	
	public Long getTemp(){
		return temperature;
	}

	
	public Long calcTemp() {
		TempPoint neighbors[]=getNeighbors();
 
		//if(MyClient.debug)System.out.println("CALC TEMP");

		Long temps[] = new Long[4];
		double mPtgs[][] = new double[4][3];
		
		for(int i=0;i<neighbors.length;i++){
			if(neighbors[i]!=null){
				temps[i]=neighbors[i].getTemp();
				mPtgs[i]=neighbors[i].getPtgs();
			}
			else{
				temps[i]=null;
				mPtgs[i]=null;
			}
		}

		if(!(getCol()==0 && getRow()==0) && !(getCol()==MyClient.M_WIDTH-1 && getRow()==MyClient.M_HEIGHT-1))
			return calculateTemp(temps, mPtgs);
		
		return temperature;

	}

	TempPoint[] getNeighbors(){
		TempPoint result[] = new TempPoint[4];
		
		for(int i=0;i<result.length;i++)
			result[i]=null;

		int startX = MyClient.myQuad[0][0].xPos;
		int endX = MyClient.myQuad[MyClient.myQuad.length-1][0].xPos;
		
		//System.out.println(startX + " : " + endX);
	

		if(xPos>startX){
			result[3] = MyClient.myQuad[xPos-startX-1][yPos];
		}
		else if(xPos==startX && MyClient.leftNeighbors!=null){
			result[3] = MyClient.leftNeighbors[0][yPos];
			//System.out.println("left n temp = " +  result[3].getTemp());
		}
		
		if(xPos<endX){
			result[1] = MyClient.myQuad[xPos-startX+1][yPos];
		}
		else if(xPos==endX && MyClient.rightNeighbors!=null){
			result[1] = MyClient.rightNeighbors[0][yPos];
		}
		
		if(yPos>0)
			result[0] = MyClient.myQuad[xPos-startX][yPos-1];
		if(yPos<MyClient.M_HEIGHT-1)
			result[2] = MyClient.myQuad[xPos-startX][yPos+1];
		
		return result;
	}
	
	long calculateTemp(Long nTemps[], double mPtgs[][]){

		double result = 0;

		for(int i=MyClient.C.length-1;i>=0;i--){
			double sumNeighbors = 0;
			int numNeighbors = 0;
			
			for(int j=nTemps.length-1;j>=0;j--){
				if(nTemps[j]!=null){
					sumNeighbors+=(double)(nTemps[j] * mPtgs[j][i]);
					numNeighbors++;
				}
			}
			
			if(numNeighbors!=0)
				result += (double)(MyClient.C[i] * (double)(sumNeighbors/numNeighbors));
		}

		if(result>MyClient.MAX_TEMP){
			return MyClient.MAX_TEMP;
		}
		return (long)result;
	}
	
}

