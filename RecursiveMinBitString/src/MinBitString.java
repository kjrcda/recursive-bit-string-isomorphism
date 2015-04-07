
public class MinBitString 
{
	private static int[] mapping;
	private static Graph g1;
	private static Graph g2;
	private static int checkMapCounter=0;
	
	public static void main(String[] args)
	{
		boolean isomorphic;
		long endTime, startTime;
		
		GraphGenerator.generate(10, true);
		g1 = new Graph("first.txt");
		g2 = new Graph("second.txt");
		
		startTime= System.currentTimeMillis();
		isomorphic = FindIsomorphism();
		endTime = System.currentTimeMillis();
		
		if(isomorphic)
			System.out.println(g1.getName()+" IS isomorphic to "+g2.getName());
		else
			System.out.println(g1.getName()+" IS NOT isomorphic to "+g2.getName());
		System.out.println("Candidate Mappings Checked: "+checkMapCounter);
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
		//
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
		boolean found = false;
		mapping = new int[g1.nodeCount()];
		
		for(int i=0; i<mapping.length && !found; i++)
		{
			mapping[0] = i;
			g1.visit(i);
			found = RecursiveCompare(1);
			g1.unvisit(i);
		}
		
		return found;
	}
	
	private static boolean RecursiveCompare(int currNode)
	{
		boolean found = false;
		int length = g1.nodeCount();
		
		if(g1.numVisited() == length)
			found = true;
		
		if(found)
		{
			found = false;
			checkMapCounter++;
			if(g1.compareMinBitString(g2.getMinimumBitString(mapping)))
			{
				System.out.println(g1.getMinimumBitString().toString());
				System.out.println(g2.getMinimumBitString(mapping).toString());
				found = true;
			}
		}
		else
		{
			for(int i=0; i<length && !found; i++)
			{
				if(!g1.isVisited(i))
				{
					mapping[currNode] = i;
					g1.visit(i);
					found = RecursiveCompare(currNode+1);
					g1.unvisit(i);
				}
			}
		}
		
		return found;
	}
	
	public static void PrintMapping(int[] map) //O(n)
	{
		for(int i=0; i<map.length; i++)
			System.out.println((i+1)+" maps to "+(map[i]+1));
		System.out.println();
	}

}
