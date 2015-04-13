import java.util.ArrayList;

public class Graph 
{	
	private String name = "";
	private boolean[] visited;
	private int[][] vertAdj;
	private int numNodes;
	private int numEdges;
	private int[] nodeDegrees;
	private int visitCounter = 0;
	private BitString min = null;
	private GraphInput.GraphResult original = null;
	
	public Graph() //O(0)
	{
		init(0);
	}
	
	public Graph(int nodes) //O(1)
	{
		init(nodes);
	}
	
	public Graph(String filename) //O(edges)
	{
		GraphInput graph = new GraphInput(filename); 
		original = graph.readInput(); //O(.5e) e = edges
		if(original == null)
			init(0); 
		else
		{
			visited = original.visited;
			vertAdj = original.matrix;
			numNodes = original.numVert;
			numEdges = original.numEdges;
			name = original.name;
			nodeDegrees = original.degrees;
		}
	}
	
	private void init(int nodes) //O(1)
	{
		numNodes = nodes;
		numEdges = 0;
		visited = new boolean[numNodes];
		vertAdj = new int[numNodes][numNodes];
		nodeDegrees = new int[numNodes];
		visitCounter = 0;
	}
	 
	public void markAllUnvisited() //O(n)
	{
		for(int i=0; i<numNodes; i++)
			visited[i] = false;
		visitCounter = 0;
	}
	
	public void reset() //O(1) when built from file
	{
		if(original!=null)
		{
			visited = original.visited;
			vertAdj = original.matrix;
			numNodes = original.numVert;
			numEdges = original.numEdges;
			nodeDegrees = original.degrees;
			visitCounter = 0;
		}
		else
			init(numNodes);
	}

	public int nodeCount() //O(1)	
	{
		return numNodes;
	}
	
	public int edgeCount()
	{
		return numEdges;
	}
	
	public boolean isVisited(int node) //O(1)
	{
		if(numNodes==0 || node >= numNodes)
			return false;
		return visited[node];
	}

	public boolean visit(int node) //O(1)
	{
		if(numNodes==0 || node >= numNodes)
			return false;
		visited[node] = true;
		visitCounter++;
		return true;
	}
	
	public boolean unvisit(int node) //O(1)
	{
		if(numNodes==0 || node >= numNodes)
			return false;
		visited[node] = false;
		visitCounter--;
		return true;
	}
	
	public int numVisited() //O(1)
	{
		return visitCounter;
	}
	
	/**
	public boolean createEdge(int node1, int node2) //O(1)
	{
		if(numNodes==0 || node1>=numNodes || node2>=numNodes || vertAdj[node1][node2]==1)
			return false;
		
		vertAdj[node1][node2] = 1;
		vertAdj[node2][node1] = 1;
		numEdges++;

		nodeDegrees[node1]++;
		nodeDegrees[node2]++;
		
		return true;
	}
	
	public boolean deleteEdge(int node1, int node2) //O(1)
	{
		if(numNodes==0 || node1>=numNodes || node2>=numNodes || vertAdj[node1][node2]==0)
			return false;
		
		vertAdj[node1][node2] = 0;
		vertAdj[node2][node1] = 0;
		numEdges--;

		nodeDegrees[node1]--;
		nodeDegrees[node2]--;
		
		return true;
	}
	*/
	
	public boolean isEdge(int from, int to) //O(4)
	{
		return (numNodes==0 || from>=numNodes || to>=numNodes || vertAdj[from][to]==0);
	}
	
	public String getName() //O(1)
	{
		return name;
	}
	
	public int[] countDegrees() //O(n)
	{
		int[] deg = new int[numNodes];
		
		for(int i=0; i<numNodes; i++)
			deg[nodeDegrees[i]]++;
		
		return deg;
	}
	
	public int getDegree(int node) //O(1)
	{
		if(numNodes==0 || node<0 || node>numNodes-1)
			return  0;
		return nodeDegrees[node];
	}
	
	public int[] getAdjacentNodes(int node) //O(n)
	{
		PathList adj = new PathList();
		for(int i=0; i<numNodes; i++)
			if(vertAdj[node][i] == 1)
				adj.add(i);
		return adj.toArray();
	}
	
	public int[][] getAdjacencyMatrix() //O(1)
	{
		return vertAdj;
	}
	//can use rightmost top half of matrix because this is an undirected
	//graph so we will have repeated edges in the leftmost lower half 
	//of our matrix
	public boolean compareMatrix(Graph toCheck) //O(.5n^2)
	{
		int[][] check = toCheck.getAdjacencyMatrix();
		for(int i=0; i<numNodes; i++)
		{
			for(int j=i+1; j<numNodes; j++)
			{
				if(check[i][j]!=vertAdj[i][j])
					return false;
			}
		}
		return true;
	}
	
	private int[][] constructMappedMatrix(int[] mapping) //O(.5n^2)
	{
		int[][] ret = new int[numNodes][numNodes];
		
		for(int i=0; i<numNodes; i++)
		{
			for(int j=i+1; j<numNodes; j++)
			{
				if(vertAdj[i][j]==1)
				{
					ret[mapping[i]][mapping[j]] = 1;
					ret[mapping[j]][mapping[i]] = 1;
				}
			}
		}
		
		return ret;
	}
	
	public boolean compareMapping(int[][] second, int[] map) //)(.5n^2)
	{
		for(int i=0; i<numNodes; i++)
			for(int j=i+1; j<numNodes; j++)
				if(vertAdj[i][j]!=second[map[i]][map[j]])
					return false;
		return true;
	}
	
	public BitString getMinimumBitString()
	{
		if(min == null)
			min = getMinimumBitString(vertAdj);
		return min;
	}
	
	public BitString getMinimumBitString(int[] mapping)
	{
		return getMinimumBitString(constructMappedMatrix(mapping));
	}
	
	//TODO: optimize this function so that it takes a mapping as well
	//and can create the new BitString in here using g2 matrix + mapping
	//instead of having the constructMappedMatrix call. Would reduce
	//O(1.5n^2) work down to O(.5n^2) work for every comparison
	private BitString getMinimumBitString(int[][] matrix)
	{
		ArrayList<BitString> list = new ArrayList<BitString>();
		
		//O(.5n^2) make rows
		for(int i=0; i< numNodes-1; i++)
		{
			BitString temp = new BitString(i,matrix[i], (i+1), numNodes);
			if(temp.value()!=0)
				list.add(temp);
		}

		//get min ordering (smallest first lexicographically)
		//System.out.println(java.util.Arrays.toString(list.toArray()));
		BitString[] arr = new BitString[list.size()];
		
		//O(nlogn)
		arr = MergeSorter.mergeSort((list.toArray(arr)));
		//System.out.println(java.util.Arrays.toString(arr)+"\n");
		
		boolean flip = false;
		int flipIndex = -1;
		BitString retVal = new BitString();
		//O(n)
		for(int i=0; i<arr.length && !flip;i++)
		{
			if(!arr[i].getString().contains("0"))
			{
				flip = true;
				flipIndex = i;
			}
			else
				retVal.append(arr[i]);
		}
		
		for(int i=arr.length-1; i>=flipIndex && flip; i--)
			retVal.append(arr[i]);
		
		return retVal;
	}
	
	public boolean compareMinBitString(BitString toCompare)
	{
		
		BitString now = getMinimumBitString();
		return compareString(toCompare.getString(), now.getString());

	}
	
	private boolean compareString(String one, String two)
	{
		int minLength = one.length()<two.length() ? one.length() : two.length();
		boolean longer = minLength == two.length() ? true : false;
		
		for(int i=0; i<minLength; i++)
			if(one.charAt(one.length()-1-i) != two.charAt(two.length()-1-i))
				return false;
		
		if(longer) //second is longer, switch them
			one = two;
		
		for(int i=0; i+minLength<one.length(); i++)
			if(one.charAt(i) == '1')
				return false;
		
		return true;
	}
}
