#include "RedBlackTree.h"

RedBlackTree::RedBlackTree()
    : root(nullptr)
{
}

RedBlackTree::~RedBlackTree()
{
    clear();
}

void RedBlackTree::clear()
{
    clear(root);
    root = nullptr;
}

void RedBlackTree::clear(ResearcherNode *node)
{
    //TODO:
    if(node == nullptr){ //nothing to delete 
        return;
    }

    clear(node->left); // clear the left child (even though it doesnt have left child it will return when it says node == nullptr)

    clear(node->right); // clear the right child (even though it doesnt have right child it will return when it says node == nullptr)

    delete node; // deleting current node 

}

ResearcherNode *RedBlackTree::findResearcher(const std::string &fullName) const
{
    return find(root, fullName);
}

ResearcherNode *RedBlackTree::find(ResearcherNode *node, const std::string &fullName) const
{

//TODO: search and find researcher by name

    if(node == nullptr){
        return node;
    }

    //if found
    else if(node->data.fullName == fullName){
        return node;
    }

    //if researcher's name starts alphabetically later then go to left child
    else if(node->data.fullName > fullName){
        return find(node->left,fullName);
    }
    //if researcher's name starts alphabetically sooner then go to right child
    else{
        return find(node->right,fullName);
    }

}

bool RedBlackTree::insertResearcher(const Researcher &researcher)
{
    //attempt to insert, erturn true if success, false otherwise
    bool inserted = false;
    ResearcherNode *newNode = new ResearcherNode(researcher);


    root = bstInsert(root, newNode, inserted);

    //if inserted is true (no duplicate)
    if(inserted){
        insertFixup(newNode); // do rotations 
        return true;
    }
    else{
        delete newNode; //dont add the newNode
        return false;

    }
}

ResearcherNode *RedBlackTree::bstInsert(ResearcherNode *current, ResearcherNode *node, bool &inserted)
{
    // TODO:
    // 1) Standard BST insert by node->data.fullName.
    // 2) If tree is empty, node becomes root (set inserted = true). + 
    // 3) If name already exists, inserted = false and return current without inserting.
    // 4) Otherwise insert and set inserted = true.
    // 5) Do not modify colors here (node->color already RED).


    if(current == nullptr){
        inserted = true;
        return node;
    }

    //if researcher's name is same (found)
    if(node->data.fullName == current->data.fullName){
        inserted = false;
        return current;
    }

    //if researcher's name starts alphabetically later then go to left child
    if (node->data.fullName < current->data.fullName) {

        current->left = bstInsert(current->left, node, inserted);
        
        if (current->left) {
            current->left->parent = current;
        } 
    } 

    //if researcher's name starts alphabetically sooner then go to left child
    else{

        current->right = bstInsert(current->right, node, inserted);

        if (current->right) {
            current->right->parent = current;
        }
    }
    
    return current;
}

void RedBlackTree::insertFixup(ResearcherNode *node)
{
    // TODO: Standard Red-Black insertion fixup.
    // While parent is RED, handle cases based on uncle's color and position.
    // Ensure root becomes BLACK at the end.

    // Loop runs while parent is red, which violates RB-tree rules.
    while (node != root && node->parent->color == RED) {

         // Parent is a left child
        if (node->parent == node->parent->parent->left) {
            ResearcherNode* uncle = node->parent->parent->right;
            //Uncle is red (recolor and move up the tree)
            if (uncle && uncle->color == RED) {
                node->parent->color = BLACK;
                uncle->color = BLACK;
                node->parent->parent->color = RED;
                node = node->parent->parent;
            } 
            //Uncle is black
            else {

                if (node == node->parent->right) {
                    node = node->parent;
                    rotateLeft(node);
                }

                //Recolor and rotate
                node->parent->color = BLACK;
                node->parent->parent->color = RED;
                rotateRight(node->parent->parent);
            }
        } 
        // if parent is a right child
        else {
            ResearcherNode* uncle = node->parent->parent->left;
            //Uncle is red (recolor and move up the tree)
            if (uncle && uncle->color == RED) {
                node->parent->color = BLACK;
                uncle->color = BLACK;
                node->parent->parent->color = RED;
                node = node->parent->parent;
            } 
            //Uncle is black
            else {
                if (node == node->parent->left) {
                    node = node->parent;
                    rotateRight(node);
                }

                //Recolor and rotate
                node->parent->color = BLACK;
                node->parent->parent->color = RED;
                rotateLeft(node->parent->parent);
            }
        }
    }
    // Root must always be black
    root->color = BLACK;
}

bool RedBlackTree::removeResearcher(const std::string &fullName)
{
    // TODO:
    // 1) Find node z with data.fullName == fullName.
    // 2) If not found, return false.
    // 3) Perform standard RBT delete:
    //    - Track original color of removed node.
    //    - If a black node is removed, call deleteFixup on the appropriate child.
    // 4) Free node memory.
    // 5) Return true.
    // 1) Find node z with data.fullName == fullName.

    
    ResearcherNode* z = findResearcher(fullName);
    if (!z) return false;

    // removedNode is the node to be physically unlinked from the tree
    ResearcherNode* removedNode = z; 
    //replacementNode is the child of removedNode that moves into its old spot
    ResearcherNode* replacementNode; 
    
    Color y_original_color = removedNode->color;
    ResearcherNode* x_parent = nullptr;

    //if z has no left child (replace z with its right subtree)
    if (z->left == nullptr) {
        replacementNode = z->right;
        x_parent = z->parent; 
        
        // Update parent link or root
        if (z->parent == nullptr) root = z->right;
        else if (z == z->parent->left) z->parent->left = z->right;
        else z->parent->right = z->right;
        
        // Update replacement node's parent
        if (z->right != nullptr) z->right->parent = z->parent;
    }
    // if z has no right child (replace z with its left subtree)
    else if (z->right == nullptr) {
        replacementNode = z->left;
        x_parent = z->parent;
        
        // Update parent link or root
        if (z->parent == nullptr) root = z->left;
        else if (z == z->parent->left) z->parent->left = z->left;
        else z->parent->right = z->left;
        
         // Update replacement node's parent
        if (z->left != nullptr) z->left->parent = z->parent;
    } 
    else {
        //minimum of right subtree
        removedNode = minimum(z->right);
        y_original_color = removedNode->color;
        replacementNode = removedNode->right;
        
        // if Successor is direct child of z
        if (removedNode->parent == z) {
            x_parent = removedNode; 
        } 
        
        //if Successor is deeper in the tree
        else {
            x_parent = removedNode->parent;

            // Remove successor from its original position
            if (removedNode->parent == nullptr) root = removedNode->right; 
            else if (removedNode == removedNode->parent->left) removedNode->parent->left = removedNode->right;
            else removedNode->parent->right = removedNode->right;
            
            if (removedNode->right != nullptr) removedNode->right->parent = removedNode->parent;
            
            // Move z's right subtree under successor
            removedNode->right = z->right;
            removedNode->right->parent = removedNode;
        }
        
        // Replace z with successor
        if (z->parent == nullptr) root = removedNode;
        else if (z == z->parent->left) z->parent->left = removedNode;
        else z->parent->right = removedNode;
        
        removedNode->parent = z->parent;
        removedNode->left = z->left;
        removedNode->left->parent = removedNode;
        removedNode->color = z->color;
    }

    //Free deleted node memory.
    delete z;

    //Fixup if a black node was removed.
    if (y_original_color == BLACK) {
        deleteFixup(replacementNode, x_parent);
    }
    
    return true;
}



void RedBlackTree::deleteFixup(ResearcherNode *node, ResearcherNode *parent)
{
    ResearcherNode* x = node;
    ResearcherNode* xParent = parent;

    while (x != root && (x == nullptr || x->color == BLACK)) {
        if (xParent == nullptr) break; // Safety break

        //if x is left child 
        if (x == xParent->left) {
            ResearcherNode* sibling = xParent->right;
            //if sibling is red
            if (sibling && sibling->color == RED) {
                sibling->color = BLACK;
                xParent->color = RED;
                rotateLeft(xParent);
                sibling = xParent->right;
            }

            //if sibling and its children are black
            if (!sibling || ((!sibling->left || sibling->left->color == BLACK) && 
                (!sibling->right || sibling->right->color == BLACK))) {
                if (sibling) sibling->color = RED;
                x = xParent;
                xParent = x->parent;
            } 
            //at least one red child
            else {
                if (!sibling->right || sibling->right->color == BLACK) {
                    if (sibling->left) sibling->left->color = BLACK;
                    sibling->color = RED;
                    rotateRight(sibling);
                    sibling = xParent->right;
                }
                if (sibling) {
                    sibling->color = xParent->color;
                    if (sibling->right) sibling->right->color = BLACK;
                }
                xParent->color = BLACK;
                rotateLeft(xParent);
                x = root;
            }
        } else {
            // if x is the right child
            ResearcherNode* sibling = xParent->left;
            //if sibling is red
            if (sibling && sibling->color == RED) {
                sibling->color = BLACK;
                xParent->color = RED;
                rotateRight(xParent);
                sibling = xParent->left;
            }
            //if sibling and its children are black
            if (!sibling || ((!sibling->right || sibling->right->color == BLACK) && 
                (!sibling->left || sibling->left->color == BLACK))) {
                if (sibling) sibling->color = RED;
                x = xParent;
                xParent = x->parent;
            } 
            //at least one red child
            else {
                if (!sibling->left || sibling->left->color == BLACK) {
                    if (sibling->right) sibling->right->color = BLACK;
                    sibling->color = RED;
                    rotateLeft(sibling);
                    sibling = xParent->left;
                }
                if (sibling) {
                    sibling->color = xParent->color;
                    if (sibling->left) sibling->left->color = BLACK;
                }
                xParent->color = BLACK;
                rotateRight(xParent);
                x = root;
            }
        }
    }
    //to ensure x is black at the end
    if (x) x->color = BLACK;
}

ResearcherNode *RedBlackTree::minimum(ResearcherNode *node) const
{
    // TODO: Return leftmost node in subtree.

    while(node->left != nullptr){
        node = node->left;
    }
    return node;
}

void RedBlackTree::rotateLeft(ResearcherNode *x)
{
    // TODO: Standard left rotation.

    if(x == nullptr || x->right == nullptr){
        return;
    }

    ResearcherNode *newRoot = x->right; // if we return to left right child will become newRoot
    ResearcherNode *leftSubtree = newRoot->left; // left child of newRoot will be the right child of old root

    x->right = leftSubtree;

    if (leftSubtree != nullptr) {
        leftSubtree->parent = x;
    }

    newRoot->parent = x->parent;
    if (x->parent == nullptr) {

        root = newRoot; // x was the root of the entire tree
    } 
    else if (x == x->parent->left) {

        x->parent->left = newRoot;
    } 
    else {
        x->parent->right = newRoot;
    }

    newRoot->left = x;
    x->parent = newRoot;

}

void RedBlackTree::rotateRight(ResearcherNode *y)
{
    // TODO: Standard right rotation.
    if(y == nullptr || y->left == nullptr){
       return;
    }

    ResearcherNode *newRoot = y->left; // if we return to right  left child will become newRoot
    ResearcherNode *rightSubtree = newRoot->right; // right child of newRoot will be the left child of old root 

    y->left = rightSubtree;

    if (rightSubtree != nullptr) {
        rightSubtree->parent = y;
    }

    newRoot->parent = y->parent;
    if (y->parent == nullptr) {

        root = newRoot; // x was the root of the entire tree
    } 
    else if (y == y->parent->right) {

        y->parent->right = newRoot;
    } 
    else {
        y->parent->left = newRoot;
    }

    newRoot->right = y;
    y->parent = newRoot;
}

int RedBlackTree::getResearcherCount() const
{
    return getResearcherCount(root);
}

int RedBlackTree::getResearcherCount(ResearcherNode *node) const
{
    // TODO: return size of subtree.
    if(node == nullptr){
        return 0;
    }
    
    int count = 1 + getResearcherCount(node->right) + getResearcherCount(node->left);
    return count;
}

int RedBlackTree::getTotalLoad() const
{
    return getTotalLoad(root);
}

int RedBlackTree::getTotalLoad(ResearcherNode *node) const
{
    // TODO: sum of data.numAssigned in subtree.
    if(node == nullptr){
        return 0;
    }

    int totalLoad = node->data.numAssigned + getTotalLoad(node->left) + getTotalLoad(node->right);
    return totalLoad;
}

void RedBlackTree::traversePreOrderForStats() const
{
    traversePreOrderForStats(root);
}

void RedBlackTree::traversePreOrderForStats(ResearcherNode *node) const
{
    // TODO:
    // Pre-order traversal (node, left, right).
    // Students will decide what exactly to print in PRINT_STATS.

    if(node == nullptr){
        return;
    }

    Researcher curr = node->data;

    std::cout<< curr.fullName << " " << curr.capacity << " " << curr.numAssigned << std::endl;

    traversePreOrderForStats(node->left);
    traversePreOrderForStats(node->right);


}
