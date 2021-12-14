# Project - Directed Weighted Graphs:
Written in: Java.  

## About:
This project which deals with the Abstraction, Design and Implementation of a Directed Weighted Graph. In addition, this projects includes a GUI which represents the graph, as dots and arrows, in a scale proportionate to the geographic locations of the Nodes in the graph.

This specific Implementation of a Directed Weighted Graph is revolved around the abstraction of road-maps and the way SAT-NAV's typically represent the world around us. each Node may refer to a city or a junction, and Edge may refer to a road between two places, and the Egde's weight may refer to the distance or time to be travelled via said road. 

### Dependencies: 

![dependencies](https://user-images.githubusercontent.com/73857923/146083048-ae051d91-211d-4361-847d-c1ced54e92ac.png)

### Data Structures:  

#### Each graph is defined by:  

1.  A collection of Nodes: Implemented by a Hashmap, which maps the Node's Integer ID (key) to the corresponding Node object (value).
2.	A collection of directed Edges: Implemented by a Hashmap, which maps a String representing an ordered pair (i.e: (a, b)) of two integer Node ID's, to the corresponding Edge object.  

#### Extra data structures designed to help with algorithms' implementations:  

3.	A collection of the out-going Edges from every Node: Implemented by a Hashmap, which maps the source-Node's integer ID (key1) to another Hashmap (value1), Which maps all of the source-Node's neighboring Nodes' integer ID's (key2) to the corresponding Edge object which connects them.
4.	A collection of the in-going Edges to every Node: Implemented by a Hashmap, which maps the destination-Node's integer ID (key1) to another Hashmap (value1), Which maps all of the source-Nodes' (which connect to said destination Node with a directed Edge from them to it) integer ID's (key2) to the corresponding Edge object which connects the two.
### Algorithms and Functions:  

#### Action to perform on a graph object:  

1.	*Add Node –* Adds a node to the graph with an integer ID and a Location (x, y, z coordinates) given by the user.
2.	*Remove Node –* Removes from the graph, the Node with the given integer ID. This method also automatically removes all in-going and out-going Edges connected to the removed node.
3.	*Add Edge –* Adds an Edge to the graph between user-specified source an destination Nodes, with a user-specified weight.
4.	*Remove Edge –* Removes an Edge from the graph, between user-specified source an destination Nodes.



#### Algorithms to be perform on a graph object:  

5.	*Is Connected? –* Determines if the graph is connected (returns true/false).
6.	*Shortest Path Distance –* Returns the distance of the shortest path between two user-specifies Nodes (based on the famous Dijkstra algorithm).
7.	*Shortest Path –* Returns a list of the Nodes which represent the shortest path between two user-specified Nodes (based on the famous Dijkstra algorithm).
8.	*Center -* Return the Node which has the smallest maximum distance from all other Nodes, assuming the graph is connected.
9.	*TSP –* Algorithm designed to solve a lenient variation of the well known problem from the fields of mathematics and computer sciences: The Travelling Salesman Problem. This is a greedy algorithm which calculates the shortest ("cheapest") route, which passes through user-specified cities (vertices on the graph. each city must be visited only once, and travelling through "other" cities (i.e: through vertices which are not from the original given collection) is permitted.
10.	*Save –* Saves the graph in a JSON file in a "pretty-printing" JSON format.
11.	*Load –* Loads and creates a graph from a JSON file.
### GUI
The Graphical User Interface of this project represents a given graph in a scale determined by the actual geographic locations that the Node of the graph represent. It also allows the user to use any of the above functions and algorithms.  

example:

![example](https://user-images.githubusercontent.com/73857923/146082818-a0e9ba8a-d7d7-4746-b3d3-c2194046a0a6.png)

## Runnig the Program using jar file via terminal:
java -jar Ex2.jar **<path of  JSON file to load as graph>**
