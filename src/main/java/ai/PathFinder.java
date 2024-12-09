package ai;

import org.example.Gamepanel;

import java.util.ArrayList;

public class PathFinder {
    Gamepanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(Gamepanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    public void instantiateNodes() {
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];
        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row] = new Node(col, row);
            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes() {
        int col = 0;
        int row = 0;
        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;
            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;
        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
            // check if the node is solid or not
            // first tiles
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
            if (gp.tileM.tile[tileNum].collision) {
                // if it is mark it as solid
                node[col][row].solid = true;
            }
            // interactive tiles
            for (int i = 0; i < gp.interactiveTile[1].length; i++) {
                if(gp.interactiveTile[gp.currentMap][i] != null &&
                        gp.interactiveTile[gp.currentMap][i].destructible) {
                    // if it can be destroyed it must be solid (maybe fix later)
                    int itCol = gp.interactiveTile[gp.currentMap][i].worldX/gp.tilesize;
                    int itRow = gp.interactiveTile[gp.currentMap][i].worldY/gp.tilesize;
                    node[itCol][itRow].solid = true;
                }
            }
            getCost(node[col][row]);


            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }
    public void getCost(Node node) {
        // G cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // H cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // F cost
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {
        while(!goalReached && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            // mark the current node as checked
            currentNode.checked = true;
            openList.remove(currentNode);

            // open the node above the entity
            if(row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }
            // now the one left to it
            if (col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }
            // down
            if (row + 1 < gp.maxWorldRow) {
                openNode(node[col][row + 1]);
            }
            // right
            if (col + 1 < gp.maxWorldCol) {
                openNode(node[col + 1][row]);
            }

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++) {
                // compare fCosts to see which node is the fastest
                if(openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                // if fCost is same check gCost
                else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            // if the openList is empty break
            if(openList.size() == 0) {
                break;
            }

            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }

    public void openNode(Node node) {
        if(!node.open && !node.checked && !node.solid) {
            node.open = true; // mark as a candidate for path
            node.parent = currentNode;
            openList.add(node);
        }
    }

    public void trackThePath() {
        Node current = goalNode;

        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
}
