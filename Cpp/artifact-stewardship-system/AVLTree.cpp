#include "AVLTree.h"
#include <iostream>

AVLTree::AVLTree() : root(nullptr)
{
}

AVLTree::~AVLTree()
{
    clear();
}

void AVLTree::clear()
{
    clear(root);
    root = nullptr;
}

void AVLTree::clear(ArtifactNode *node)
{
    //clear by recursion
    if (node == nullptr)
    {
        return;
    }

    clear(node->left);
    clear(node->right);
    delete node;
}

int AVLTree::height(ArtifactNode *node) const
{
    return node ? node->height : 0;
}

int AVLTree::getBalance(ArtifactNode *node) const
{
    if (!node) return 0;
    return height(node->left) - height(node->right);
}

ArtifactNode *AVLTree::rotateLeft(ArtifactNode *x)
{
    // TODO: Standard AVL left rotation.
    //return new root

    ArtifactNode *newRoot = x->right; // if we return to left right child will become newRoot
    ArtifactNode *leftSubtree = newRoot->left; // left child of newRoot will be the right child of old root

    newRoot->left = x;
    x->right = leftSubtree;

    x->height = 1 + std::max(height(x->left), height(x->right)); //updating height 
    newRoot->height = 1 + std::max(height(newRoot->left), height(newRoot->right));  //updating height 

    return newRoot;
}

ArtifactNode *AVLTree::rotateRight(ArtifactNode *y)
{
    // TODO: Standard AVL right rotation (mirror of rotateLeft).

    ArtifactNode *newRoot = y->left; // if we return to right  left child will become newRoot
    ArtifactNode *rightSubtree = newRoot->right; // right child of newRoot will be the left child of old root

    newRoot->right = y;
    y->left = rightSubtree;

    y->height = 1 + std::max(height(y->left), height(y->right));  //updating height 
    newRoot->height = 1 + std::max(height(newRoot->left), height(newRoot->right));  //updating height 

    return newRoot;
}

ArtifactNode *AVLTree::findMinNode(ArtifactNode *node) const
{
    // TODO: Return leftmost node in this subtree.

    ArtifactNode *curr = node;
    while (curr && curr->left != nullptr) // going to left child until we can't no more
    {
        curr = curr->left;
    }
    return curr;
}

ArtifactNode *AVLTree::insert(ArtifactNode *node, const Artifact &artifact, bool &inserted)
{
    // TODO:
    // 1) Standard BST insert by artifactID.
    // 2) If duplicate ID, set inserted = false and return node unchanged.
    // 3) On actual insertion, update node->height.
    // 4) Compute balance and apply AVL rotations if needed.
    // 5) Return the (possibly new) root of this subtree.

    //if there is no node, then added artifact will be the root
    if (node == nullptr)
    {
        inserted = true;
        ArtifactNode* newNode = new ArtifactNode(artifact);
        newNode->height = 1; 
        return newNode;
    }

    //if artifact's ID is larger then go to right child
    if (artifact.artifactID > node->data.artifactID)
    {
        node->right = insert(node->right, artifact, inserted);
    }
    //if artifact's ID is smaller then go to left child
    else if (node->data.artifactID > artifact.artifactID)
    {
        node->left = insert(node->left, artifact, inserted);
    }
    else
    {
        //duplicate id
        inserted = false;
        return node;
    }

    node->height = 1 + std::max(height(node->left), height(node->right)); //updating height
    int balance = getBalance(node); // height of left child - height of right child

    if (balance > 1 ) // if left side is heavier rotate to rigt 
    {
        if (artifact.artifactID < node->left->data.artifactID)
        {
            return rotateRight(node);
        }
        else
        {
            node->left = rotateLeft(node->left);
            return rotateRight(node);
        }
    }
    else if (balance < -1 ) // if right side is heavier rotate to left 
    {
        if (artifact.artifactID > node->right->data.artifactID)
        {
            return rotateLeft(node);
        }
        else
        {
            node->right = rotateRight(node->right);
            return rotateLeft(node);
        }
    }

    return node;
}

ArtifactNode *AVLTree::remove(ArtifactNode *node, int artifactID, bool &removed)
{
    // TODO:
    // 1) Standard BST deletion by artifactID.
    // 2) If node not found, leave removed = false.
    // 3) Upon deletion, update heights and rebalance with rotations.
    // 4) Return (possibly new) root of this subtree.

    //if not found
    if (node == nullptr)
    {
        removed = false;
        return nullptr;
    }

    //if artifact's ID is greater then go to right child
    if (artifactID > node->data.artifactID)
    {
        node->right = remove(node->right, artifactID, removed);
    }
    //if artifact's ID is smaller then go to left child
    else if (node->data.artifactID > artifactID)
    {
        node->left = remove(node->left, artifactID, removed);
    }
    else
    {
        //if they are same removed = true
        removed = true;

        //only right child
        if (node->left == nullptr)
        {
            ArtifactNode *temp = node->right;
            delete node;
            return temp;
        }

        //only left child
        else if (node->right == nullptr)
        {
            ArtifactNode *temp = node->left;
            delete node;
            return temp;
        }
        else
        {
            // node with 2 children
            ArtifactNode *temp = findMinNode(node->right);
            node->data = temp->data;
            node->right = remove(node->right, temp->data.artifactID, removed);
        }
    }

    //only one node (root)
    if (node == nullptr)
    {
        return node;
    }

    node->height = 1 + std::max(height(node->left), height(node->right)); //updating height 
    int balance = getBalance(node); // height of left child - height of right child

    // Left Left
    if(balance > 1){
        if(getBalance(node->left) >= 0){
            return rotateRight(node);
        }
    // Left Right
    else{
        node->left = rotateLeft(node->left);
        return rotateRight(node);
        }
    }

    // Right Right
    else if (balance <-1){
        if(getBalance(node->right) <= 0){
            return rotateLeft(node);
        }
    
    //Right Left
        else{
        node->right = rotateRight(node->right);
        return rotateLeft(node);
        }
    }

    return node;
}


bool AVLTree::insertArtifact(const Artifact &artifact)
{
    bool inserted = false; //tracks whether operation is succesful
    root = insert(root, artifact, inserted);
    return inserted;
}

bool AVLTree::removeArtifact(int artifactID)
{
    bool removed = false; //tracks whether operation is succesful
    root = remove(root, artifactID, removed);
    return removed;
}

ArtifactNode *AVLTree::findArtifact(int artifactID) const
{
    return find(root, artifactID);
}

ArtifactNode *AVLTree::find(ArtifactNode *node, int artifactID) const
{
    if (node == nullptr)
    {
        return nullptr;
    }
    //if artifact's ID is same (found)
    else if (node->data.artifactID == artifactID)
    {
        return node;
    }
    //if artifact's ID is smaller then go to left child
    else if (node->data.artifactID > artifactID)
    {
        return find(node->left, artifactID);
    }
    else
    //if artifact's ID is greater then go to right child
    {
        return find(node->right, artifactID);
    }
}

void AVLTree::setAssignedTo(int artifactID, const std::string &researcherName)
{
     //assign artifact to researcher

    ArtifactNode *artifact = findArtifact(artifactID);
    if (artifact != nullptr)
    {
        artifact->data.assignedToName = researcherName;
    }
}

void AVLTree::clearAssignedTo(int artifactID)
{
    ArtifactNode *artifact = findArtifact(artifactID);
    if (artifact != nullptr)
    {
        artifact->data.assignedToName = "";
    }
}

void AVLTree::printUnassigned() const
{
    printUnassigned(root);
}

void AVLTree::printUnassigned(ArtifactNode *node) const
{
    // TODO:
    // Inorder traversal.
    // For each node with data.assignedToName == "", print in required format, e.g.:
    // <id> <name> <rarity> <value>

    if (node == nullptr)
    {
        return;
    }

    printUnassigned(node->left);

    Artifact artifact = node->data;
    if (artifact.assignedToName == "")
    {
        std::cout << artifact.artifactID << " "
                  << artifact.name << " "
                  << artifact.rarityLevel << " "
                  << artifact.researchValue << std::endl;
    }

    printUnassigned(node->right);
}

int AVLTree::getArtifactCount() const
{
    return getArtifactCount(root);
}

int AVLTree::getArtifactCount(ArtifactNode *node) const
{
    // TODO: return size of subtree.

    if (node == nullptr)
    {
        return 0;
    }

    return 1 + getArtifactCount(node->left) + getArtifactCount(node->right);
}

int AVLTree::getTotalRarity() const
{
    return getTotalRarity(root);
}

int AVLTree::getTotalRarity(ArtifactNode *node) const
{
    // TODO: sum of rarityLevel over subtree.

    if (node == nullptr)
    {
        return 0;
    }

    //return all nodes rarity
    return node->data.rarityLevel +
           getTotalRarity(node->left) +
           getTotalRarity(node->right);
}

void AVLTree::traversePostOrderForStats() const
{
    traversePostOrderForStats(root);
}

void AVLTree::traversePostOrderForStats(ArtifactNode *node) const
{
    // TODO:
    // Post-order traversal (left, right, node).
    // Students will decide what exactly to print in PRINT_STATS.

    if (node == nullptr)
    {
        return;
    }

    //prints left child, right child then node

    traversePostOrderForStats(node->left);
    traversePostOrderForStats(node->right);

    std::string status = (node->data.assignedToName == "") ? "UNASSIGNED" : node->data.assignedToName;

    std::cout << node->data.artifactID << " "
              << node->data.name << " "
              << node->data.rarityLevel << " "
              << node->data.researchValue << " "
              << status << std::endl;
}

//added helper funciton to print artifacts with rarity 
void AVLTree::printArtifactsWithRarity(int minRarity) const
{
    printArtifactsWithRarity(root, minRarity);
}

void AVLTree::printArtifactsWithRarity(ArtifactNode *node, int minRarity) const
{
    //prints left child, node then right child
    if (node)
    {
        printArtifactsWithRarity(node->left, minRarity);

        if (node->data.rarityLevel >= minRarity)
        {
            std::string status =
                (node->data.assignedToName == "") ? "UNASSIGNED" : "ASSIGNED:" + node->data.assignedToName;

            std::cout << node->data.artifactID << " "
                      << node->data.name << " "
                      << node->data.rarityLevel << " "
                      << node->data.researchValue << " "
                      << status << std::endl;
        }

        printArtifactsWithRarity(node->right, minRarity);
    }
}
