#include "WagonList.h"

#include <iostream>

WagonList::~WagonList() { clear(); }

void WagonList::clear()
{
    // TODO: Delete all Wagon objects in this list and reset pointers.

    Wagon *curr = front;
    Wagon *nextWagon = nullptr;

    while (curr != nullptr)
    {
        nextWagon = curr->getNext();
        
        // Delete the Wagon object pointed to by current
        delete curr; 
        
        curr = nextWagon; // pointer will be the next pointer
    }

    // Reset list state to an empty list
    front = nullptr;
    rear = nullptr;
    totalWeight = 0;
}

//Move Constructor: It is a special kind of constructor which is used to transfer dynomically allocated objects from one
// place to another place. This means the new object and other means which will be transmitted. "Other" object's parameters are moved 
//to "This". "This " receives and "Other" gives up their resources.
WagonList::WagonList(WagonList &&other) noexcept
{
    // TODO: Implement move constructor.
    // Transfer ownership of 'other' list’s nodes into this list
    // and leave 'other' in an empty but valid state.


    this->front = other.front;
    this->rear = other.rear;
    this->totalWeight = other.totalWeight;

    other.front = nullptr;
    other.rear = nullptr;
    other.totalWeight = 0;
}

WagonList &WagonList::operator=(WagonList &&other) noexcept
{
    // Operation version of the move constructor.
    // TODO: Implement it.

    if (this != &other)
    {
        // Destroy resources currently held by *this* to prevent memory leaks.
        clear(); 
        
        // 2. Transfer ownership from 'other' to 'this'
        this->front = other.front;
        this->rear = other.rear;
        this->totalWeight = other.totalWeight;

        // 3. Leave 'other' in a safe, empty state
        other.front = nullptr;
        other.rear = nullptr;
        other.totalWeight = 0;
    }
    return *this;
}

Wagon *WagonList::findById(int id)
{
    // TODO: Find and return the Wagon with given ID.
    // Return nullptr if not found.
    //iterating the train until we found a matching id 

    Wagon *curr = front; //first wagon
    
    while (curr != nullptr)
    {
        if (curr->getID() == id)
        {
            return curr;
        }
        else{
            curr = curr->getNext();
        }
        
    }

    return nullptr;
}

void WagonList::addWagonToRear(Wagon *w)
{
    // TODO: Add a Wagon to the rear (end) of the list.
    // This function does not respect the weight order
    // it inserts to end regardless of the weight

    if(w == nullptr){
        return; 
    }

    w->setNext(nullptr);
    w->setPrev(nullptr);

    if( isEmpty() ){
        //if the list is empty the added wagon will be the first and the last.
        front = w;
        rear = w;
    }
    else{
        rear->setNext(w); //set rear to the next of the added wagon
        w->setPrev(rear); // set the previous pointer to the old rear
        rear = w; // last wagon
    }

    totalWeight = totalWeight + w->getWeight();
    
}

int WagonList::getTotalWeight() const { return totalWeight; }

bool WagonList::isEmpty() const
{
    // TODO: Return true if the list has no wagons.

    if(front == nullptr){
        // if front of the pointer is nullptr this means there is no wagon which means its empty.
        return true;
    }
    return false;
}

void WagonList::insertSorted(Wagon *wagon)
{
    // TODO: Insert wagon into this list in descending order of weight.
    
    if(wagon == nullptr){ // if there is no wagon
        return;
    }

    wagon->setNext(nullptr); 
    wagon->setPrev(nullptr);

    int weightOfWagon = wagon->getWeight();

    if ( isEmpty() ) {

        this->front = wagon;
        this->rear = wagon;

        this->totalWeight += weightOfWagon;

        return;
    }

    if(weightOfWagon > this->front->getWeight()){
        // weight of the first wagon is less than the inserted wagon
       
        wagon->setNext(this->front);
        this->front->setPrev(wagon);
        this->front = wagon;
        this->totalWeight += weightOfWagon;

        return;
    } 


    Wagon *curr = this->front;

    while( curr->getNext() != nullptr && curr->getNext()->getWeight() > weightOfWagon){
            curr = curr->getNext();
        }

    if(curr->getNext()==nullptr){

            curr->setNext(wagon);
            wagon->setPrev(curr);
            this->rear = wagon;
        }
    else{
            wagon->setNext(curr->getNext());
            wagon->setPrev(curr);

            curr->getNext()->setPrev(wagon);
            curr->setNext(wagon);
        }

    this->totalWeight += weightOfWagon;
    
}

void WagonList::appendList(WagonList &&other)
{
   
   
   // TODO: Append another WagonList to this one (merge them).
   // Use move semantics to avoid deep copies. (Double && at the parameter already makes it so)
   // 'other' should end up empty after this operation
   // At merge lists (blocks) will be protected 
   // But the one with heavier wagon at the front will be before the other list

    if (other.front == nullptr) {
        return; // Nothing to append.
    }

    if (this->front == nullptr) {
        // Since 'this' is empty, we just move 'other' into 'this'.
        
        this->front = other.front;
        this->rear = other.rear;
        this->totalWeight = other.totalWeight;
        
        // Empty other
        other.front = nullptr;
        other.rear = nullptr;
        other.totalWeight = 0;
        return;
    }



    //
    WagonList* firstList = (other.front->getWeight() > this->front->getWeight()) ? &other : this;
    //WagonList* secondList = (firstList == &other) ? this : &other;

    if (firstList == this) 
    {
        
        this->rear->setNext(other.front);
        other.front->setPrev(this->rear);
        

        this->rear = other.rear;
        this->totalWeight += other.totalWeight;
        
        
        other.front = nullptr;
        other.rear = nullptr;
        other.totalWeight = 0;
    } 
    
    // If 'other' is the firstList and 'this' is the secondList:
    else 
    {
        
        other.rear->setNext(this->front);
        this->front->setPrev(other.rear);
        
        
        this->front = other.front;
        this->totalWeight += other.totalWeight;
        

        other.front = nullptr;
        other.rear = nullptr;
        other.totalWeight = 0;
    }
}
    


Wagon *WagonList::detachById(int id)
{
    // TODO: Remove a specific wagon (by ID) from this list and return it.
    // Use: std::cout << "Wagon " << toRemove->id << " detached from Wagon List. " << std::endl;
    // Return nullptr if wagon not found.
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    Wagon *removedWagon = findById(id); //finding the wagon that is going to be removed with its id 

    if(removedWagon==nullptr){
        return  nullptr;
    }

    Wagon *prevWagon = removedWagon->getPrev();// previous wagon of removed wagon
    Wagon *nextWagon = removedWagon->getNext(); // next wagon of removed wagon

    
    if(prevWagon==nullptr){
        front = nextWagon; //if the removed wagon is the first wagon, then next wagon will be the first wagon.
    }
    else{
        prevWagon->setNext(nextWagon); //else the next wagon will be the next wagon of the removed wagon.
    }

    if(nextWagon==nullptr){
        rear = prevWagon; //if the removed wagon is the last wagon, then previous wagon will be the last wagon.

    }
    else{
        nextWagon->setPrev(prevWagon);
    }

    totalWeight= totalWeight - removedWagon->getWeight(); //decreasing the total weight of removed wagon

    removedWagon->setNext(nullptr); //emptying the neighboor pointers of the wagon
    removedWagon->setPrev(nullptr);

    std::cout << "Wagon " << removedWagon->getID() << " detached from Wagon List. " << std::endl;

    return removedWagon;
}


WagonList WagonList::splitAtById(int id)
{
    // TODO: Split this list into two lists at the wagon with given ID.
    // The wagon with 'id' becomes the start of the new list.
    // Return the new WagonList (move return).
    // If 'id' not found, return an empty list.
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    WagonList newList; // return-by-value (will be moved automatically)

     Wagon *firstWagon = findById(id); // first wagon of the new list

     if(firstWagon==nullptr){
        return newList; // empty list 
     }

     int removedWeight = 0;
     Wagon *curr = firstWagon;

     while(curr!=nullptr){
        //calculating the new list's weight 
        removedWeight += curr->getWeight();
        curr = curr->getNext();
     }

     this->totalWeight -= removedWeight; //decreasing the removed weight from the first total weight
     newList.totalWeight = removedWeight;

     newList.front = firstWagon; //first wagon is the new front of the newList 
     newList.rear = this->rear; //last wagon's pointer will be the newList rear pointer
     this->rear = firstWagon->getPrev(); // old list's rear will be the first newList wagon's pointer


    if (this->rear != nullptr)
    {
        //if else statements are handling the cases if the list becomes empty which will result a crash.
        //original list  doesnt become empty.
        //old list last wagon's pointer is set to nullptr. 
        this->rear->setNext(nullptr);
    } 
    else { 
        //original list becomes empty.
        //old list first wagon's pointer set to nullptr.

        this->front = nullptr;
    }

    newList.front->setPrev(nullptr);

    return newList; // moved, not copied
}

// Print is already implemented
void WagonList::print() const
{

    std::cout << *this << std::endl;
    return;
}

// << operator is already implemented
std::ostream &operator<<(std::ostream &os, const WagonList &list)
{
    if (list.isEmpty())
        return os;

    Wagon *current = list.front;

    while (current)
    {
        os << "W" << current->getID() << "(" << current->getWeight() << "ton)";
        if (current->getNext())
            os << " - ";
        current = current->getNext();
    }
    return os;
}
