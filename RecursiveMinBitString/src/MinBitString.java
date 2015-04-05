
public class MinBitString 
{
	private static Graph g1;
	private static Graph g2;
	
	public static void main(String[] args)
	{
		boolean isomorphic;
		long endTime, startTime;
		
		g1 = new Graph("TestA.txt");
		g2 = new Graph("TestB.txt");
		
		startTime= System.currentTimeMillis();
		isomorphic = FindIsomorphism();
		endTime = System.currentTimeMillis();
		
		if(isomorphic)
			System.out.println(g1.getName()+" IS isomorphic to "+g2.getName());
		else
			System.out.println(g1.getName()+" IS NOT isomorphic to "+g2.getName());
		System.out.println("Completion Time: "+ (endTime - startTime)/1000.0 +"s");
	}
	
	public static boolean FindIsomorphism()
	{
		//O(2)
		System.out.println("\nCounting nodes and edges...");
		if(g1.nodeCount() != g2.nodeCount())
			return false;
		if(g1.edgeCount()!=g2.edgeCount())
			return false;
		System.out.println("Number of Nodes: "+g1.nodeCount()+ "\nNumber of Edges: "+g1.edgeCount());
		System.out.println("Finished counting\n");
		
		//O(n^2)
		System.out.println("Counting degrees...");
		if(!MatchingDegrees(g1, g2))
			return false;
		System.out.println("Finished counting\n");
		
		System.out.println("Finding isomorphism...");
		//n!
		if(!CompareMinimumBitStrings())
			return false;
		
		return true;
	}
	
	public static boolean MatchingDegrees(Graph one, Graph two) //O(2n^2) + O(3n) 
	{
		int[] degList1 = one.countDegrees(); //O(n^2) + O(n)
		int[] degList2 = two.countDegrees(); //O(n^2) + O(n)
	
		if(degList1.length != degList2.length) //O(2)
			return false;
		
		for(int i=0; i<degList1.length; i++) //O(n)
		{
			if(degList1[i] != degList2[i])
				return false;
		}
		return true;
	}
	
	public static boolean CompareMinimumBitStrings()
	{
		BitString one = g1.getMinimumBitString();
		BitString two = g2.getMinimumBitString();
		System.out.println(one.getString()+" - "+two.getString());
		return one.getString().compareTo(two.getString()) ==0;
	}
	
	public static void PrintMapping(int[] map) //O(n)
	{
		for(int i=0; i<map.length; i++)
			System.out.println((i+1)+" maps to "+(map[i]+1));
		System.out.println();
	}

}
