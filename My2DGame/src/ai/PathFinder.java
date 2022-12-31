package ai;

import entity.Entity;
import main.GamePanel;

import java.util.ArrayList;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp)
    {
        this.gp = gp;
        instantiateNodes();
    }
    public void instantiateNodes()
    {
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow)
        {
            node[col][row] = new Node(col,row);

            col++;
            if(col == gp.maxWorldCol)
            {
                col = 0;
                row++;
            }
        }
    }

    //reset previous pathfinding result
    public void resetNodes()
    {
        int col = 0;
        int row = 0;
        while(col < gp.maxWorldCol && row < gp.maxWorldRow)
        {
            //reset open, checked and solid state
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if(col == gp.maxWorldCol)
            {
                col = 0;
                row++;
            }
        }
        //reset other settings
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity)
    {
        resetNodes();
        //set Start and Goal node
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;
        while(col < gp.maxWorldCol && row < gp.maxWorldRow)
        {
            //SET SOLID NODE
            //CHECK TILES
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
            if(gp.tileM.tile[tileNum].collision == true)
            {
                node[col][row].solid = true;

            }
            //CHECK INTERACTIVE TILES
            for(int i = 0; i < gp.iTile[1].length; i++)
            {
                if(gp.iTile[gp.currentMap][i] != null &&
                        gp.iTile[gp.currentMap][i].destructible == true)
                {
                    int itCol = gp.iTile[gp.currentMap][i].worldX / gp.tileSize;
                    int itRow = gp.iTile[gp.currentMap][i].worldY / gp.tileSize;
                    node[itCol][itRow].solid = true;
                }
            }
            //SET COST
            getCost(node[col][row]);

            col++;
            if(col == gp.maxWorldCol)
            {
                col = 0;
                row++;
            }
        }
    }
    public void getCost(Node node)
    {
        // G Cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // H Cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // F Cost
        node.fCost = node.gCost + node.hCost;
    }
    public boolean search()
    {
        while(goalReached == false && step < 500)
        {
            int col = currentNode.col;
            int row = currentNode.row;

            //check the current node
            currentNode.checked = true;
            openList.remove(currentNode);

            //open the UP node
            if(row-1 >= 0)
            {
                 openNode(node[col][row-1]);
            }
            //open the LEFT node
            if(col - 1 >= 0)
            {
                openNode(node[col-1][row]);
            }
            //open the DOWN node
            if(row + 1 <= gp.maxWorldRow)
            {
                openNode(node[col][row+1]);
            }
            //open the RIGHT node
            if(col + 1 <= gp.maxWorldCol)
            {
                openNode(node[col+1][row]);
            }

            //Find the best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++)
            {
                //Check if this node's F cost is better
                if(openList.get(i).fCost < bestNodefCost)
                {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                //If F cost is equal, check the G cost
                else if(openList.get(i).fCost == bestNodefCost)
                {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost)
                    {
                        bestNodeIndex = i;

                    }
                }
            }

            //If there is no node in the openList, end the loop
            if(openList.size() == 0)
            {
                break;
            }

            //After the loop, openList(bestNodeIndex] is the next step (= currentNode)
            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode)
            {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }
    public void openNode(Node node)
    {
        if(node.open == false && node.checked == false && node.solid == false)
        {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
    public void trackThePath()
    {
        Node current = goalNode;

        while(current != startNode)
        {
            pathList.add(0,current); //last added node is in the [0]
            current = current.parent;
        }
    }
}
