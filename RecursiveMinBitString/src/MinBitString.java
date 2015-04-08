
public class MinBitString 
{
	private static int[] nodeMap;
	private static Graph one;
	private static Graph two;
	private static int checkMapCounter=0;
	
	public static void main(String[] args)
	{
		boolean isomorphic;
		long end, start;
		
		GraphGenerator.generate(15, true);
		one = new Graph("first.txt");
		two = new Graph("second.txt");
		
		start= System.currentTimeMillis();
		isomorphic = FindIsomorphism();
		end = System.currentTimeMillis();
		
		if(isomorphic)
			System.out.println(one.getName()+" IS isomorphic to "+two.getName());
		else
			System.out.println(one.getName()+" IS NOT isomorphic to "+two.getName());
		System.out.println("Candidate Mappings Checked: "+checkMapCounter);
		System.out.println("Completion Time: "+ (end - start)/1000.0 +"s");
	}
	
	public static boolean FindIsomorphism()
	{
		//O(1)
		System.out.println("Counting nodes and edges...");
		if(one.nodeCount() != two.nodeCount())
			return false;
		if(one.edgeCount()!=two.edgeCount())
			return false;
		System.out.println("Number of Nodes: "+one.nodeCount()+ "\nNumber of Edges: "+one.edgeCount());
		System.out.println("Finished counting\n");
		
		//O(n)
		System.out.println("Counting degrees...");
		if(!DegreeCompare())
			return false;
		System.out.println("Finished counting\n");
		
		System.out.println("Finding isomorphism...");
		//O()
		if(!CompareMinimumBitStrings())
			return false;
		
		return true;
	}
	
	public static boolean DegreeCompare() //O(3n) 
	{
		int[] degList1 = one.countDegrees(); //O(n)
		int[] degList2 = two.countDegrees(); //O(n)
	
		if(degList1.length != degList2.length) //O(1)
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
		nodeMap = new int[one.nodeCount()];
		
		//O(n!)
		for(int i=0; i<nodeMap.length && !found; i++)
		{
			nodeMap[0] = i;
			one.visit(i);
			found = RecursiveCompare(1);
			one.unvisit(i);
		}
		
		return found;
	}
	
	private static boolean RecursiveCompare(int currNode)
	{
		boolean found = false;
		int length = one.nodeCount();
		
		if(one.numVisited() == length)
			found = true;
		
		if(found)
		{
			found = false;
			checkMapCounter++;
			if(one.compareMinBitString(two.getMinimumBitString(nodeMap)))
			{
				System.out.println(one.getMinimumBitString().toString());
				System.out.println(two.getMinimumBitString(nodeMap).toString());
				found = true;
			}
		}
		else
		{
			for(int i=0; i<length && !found; i++)
			{
				if(!one.isVisited(i))
				{
					nodeMap[currNode] = i;
					one.visit(i);
					found = RecursiveCompare(currNode+1);
					one.unvisit(i);
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
