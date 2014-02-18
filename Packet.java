import java.io.Serializable;


public class Packet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public volatile TempPoint rQuad[][], leftNeighbors[][], rightNeighbors[][];
	
	public Packet(TempPoint[][] ln, TempPoint[][] rq, TempPoint[][] rn){
		leftNeighbors=ln;rQuad=rq;rightNeighbors=rn;
	}

}
