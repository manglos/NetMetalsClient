
import java.util.concurrent.RecursiveAction;

@SuppressWarnings("serial")
class QuadrantWorker extends RecursiveAction{

	TempPoint rQuad[][], wQuad[][];
	final int THRESHOLD = MyClient.M_HEIGHT/4;
	
	QuadrantWorker(TempPoint[][] r, TempPoint[][] w){
		rQuad = r;
			TempPoint[][] temp = r;
			wQuad = temp;
	}
	
	TempPoint[][] split(TempPoint m[][], int q){
		TempPoint result[][];
		int width, height, startX, startY;
		
		if(m.length == m[0].length){
			width = m.length/2;
			height = m[0].length/2;
			startX = 0;
			startY = 0;
			
			if(q==2){
				startX = width;
				startY = 0;
			}
			else if(q==3){
				startX = 0;
				startY = height;
			}
			else if(q==4){
				startX = width;
				startY = height;
			}
			
		}
		else{
			width = m.length/4;
			height = m[0].length;
			startX = width * (q - 1);
			startY = 0;	
		}
		
		result = new TempPoint[width][height];
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				result[i][j] = m[startX+i][startY+j];
			}
		}
		
		return result;
	}

	
	
	@Override
	protected void compute() {
		//if(Main.debug)System.out.println("My quad is " + rQuad.length + "x" + rQuad[0].length);
		
		if(rQuad.length <= THRESHOLD){
			computeDirectly();
		}
		else{
			TempPoint rOne[][], rTwo[][], rThree[][], rFour[][];
			TempPoint wOne[][], wTwo[][], wThree[][], wFour[][];
			
			rOne = split(rQuad, 1);
			rTwo = split(rQuad, 2);
			rThree = split(rQuad, 3);
			rFour = split(rQuad, 4);
			
			wOne = split(wQuad, 1);
			wTwo = split(wQuad, 2);
			wThree = split(wQuad, 3);
			wFour = split(wQuad, 4);
			
			QuadrantWorker q[] = new QuadrantWorker[4];
			q[0] = new QuadrantWorker(rFour, wFour);
			q[3] = new QuadrantWorker(rTwo, wTwo);
			q[1] = new QuadrantWorker(rThree, wThree);
			q[2] = new QuadrantWorker(rOne, wOne);
			
			invokeAll(q);
			
		}
	}

	private void computeDirectly() {
		for(int i=0;i<rQuad.length;i++){
			for(int j=0;j<rQuad[0].length;j++){
				wQuad[i][j].setTemp(rQuad[i][j].calcTemp());
			}
		}		
	}

	public TempPoint[][] getWriteQuad(){
		return wQuad;
	}
}
