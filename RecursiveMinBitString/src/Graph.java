
public class Graph 
{	
	private String name = "";
	private boolean[] visited;
	private int[][] vertAdj;
	private int numNodes;
	private int numEdges;
	private int[] nodeDegrees = null;
	private int visitCounter = 0;
	private GraphInput.GraphResult original = null;
	
	public Graph() //O(0)
	{
		init(0);
	}
	
	public Graph(int nodes) //O(n^2)
	{
		init(nodes);
	}
	
	public Graph(String filename) //O(edges)
	{
		GraphInput graph = new GraphInput(filename); 
		original = graph.readInput(); //O(.5e) e = edges
		if(original == null)
			init(0); //O(0)
		else
		{
			visited = original.visited;
			vertAdj = original.matrix;
			numNodes = original.numVert;
			numEdges = original.numEdges;
			name = original.name;
		}
	}
	
	private void init(int nodes) //O(n^2) (could be O(1))
	{
		numNodes = nodes;
		numEdges = 0;
		visited = new boolean[numNodes];
		vertAdj = new int[numNodes][numNodes];
		nodeDegrees = null;
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
			nodeDegrees = null;
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
	
	public boolean createEdge(int node1, int node2) //O(1)
	{
		if(numNodes==0 || node1>=numNodes || node2>=numNodes || vertAdj[node1][node2]==1)
			return false;
		
		vertAdj[node1][node2] = 1;
		vertAdj[node2][node1] = 1;
		numEdges++;
				
		if(nodeDegrees == null)
			MakeDegreeList();
		else
		{
			nodeDegrees[node1]++;
			nodeDegrees[node2]++;
		}
		
		return true;
	}
	
	public boolean deleteEdge(int node1, int node2) //O(1)
	{
		if(numNodes==0 || node1>=numNodes || node2>=numNodes || vertAdj[node1][node2]==0)
			return false;
		
		vertAdj[node1][node2] = 0;
		vertAdj[node2][node1] = 0;
		numEdges--;
				
		if(nodeDegrees == null)
			MakeDegreeList();
		else
		{
			nodeDegrees[node1]--;
			nodeDegrees[node2]--;
		}
		
		return true;
	}
	
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
		if(nodeDegrees == null)
			MakeDegreeList();
		
		for(int i=0; i<numNodes; i++)
			deg[nodeDegrees[i]]++;
		
		return deg;
	}
	
	public int getDegree(int node) //O(1)
	{
		if(nodeDegrees == null)
			MakeDegreeList();
		
		return nodeDegrees[node];
	}
	
	private void MakeDegreeList() //O(n^2)
	{
		//TODO speed up
		nodeDegrees = new int[numNodes];
		for(int i=0; i<numNodes; i++)
		{
			for(int j=0; j<numNodes; j++)
				if(vertAdj[i][j] == 1)
					nodeDegrees[i]++;
		}
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
	
	public boolean compareMapping(int[][] second, int[] map) //)(.5n^2)
	{
		for(int i=0; i<numNodes; i++)
			for(int j=i+1; j<numNodes; j++)
				if(vertAdj[i][j]!=second[map[i]][map[j]])
					return false;
		return true;
	}
	

	/*
	public BitString getMinimumColBitString()
	{
		return new BitString();
	}
	
	public BitString getMinimumRowBitString()
	{
		BitString rows[] = new BitString[numNodes];
		BitString temp[] = new BitString[numNodes];
		int[] intRows = new int[numNodes];
		MergeSorter sort = new MergeSorter();
		
		//O(n) make rows
		for(int i=0; i< numNodes; i++)
		{
			rows[i] = new BitString(i,vertAdj[i], (i+1), numNodes);
			temp[i] = new BitString(i,vertAdj[i], (i+1), numNodes);
			intRows[i] = rows[i].value();
		}
		
		//get min ordering (smallest first)
		System.out.println(java.util.Arrays.toString(intRows));
		sort.set(intRows);
		intRows = sort.mergeSort(); //TODO stopped here
		System.out.println(java.util.Arrays.toString(intRows));
		for(int i=0; i<numNodes; i++)
		{
			int k=0;
			int value = temp[i].value();
			for(k=0; k<numNodes && value != intRows[k]; k++);
			rows[k] = temp[i];
		} //rows now has ordered bitstrings
		
		//make new string to return
		for(int i=0; i<numNodes; i++)
			rows[i].printString();
		
		
		
		return new BitString(); 
	}*/

}