#include "TrainTrack.h"
#include <iostream>

bool TrainTrack::autoDispatch = false;

TrainTrack::TrainTrack()
    : firstLocomotive(nullptr),
      lastLocomotive(nullptr),
      destination(Destination::OTHERS),
      totalWeight(0), trainCounter(0)
{
}

TrainTrack::TrainTrack(Destination _dest)
    : firstLocomotive(nullptr),
      lastLocomotive(nullptr),
      destination(_dest),
      totalWeight(0), trainCounter(0)
{
}

TrainTrack::~TrainTrack()
{
    // TODO: If track is deconstucting, 
    // depart all of the trains
    // Handle pointers as necessary

    Train * curr = firstLocomotive;
    Train *nextTrain = nullptr;

    while(curr != nullptr){
        nextTrain = curr->getNext();
        delete curr;
        curr = nextTrain;
    }

   // Reset list state to an empty list
    firstLocomotive = nullptr;
    lastLocomotive = nullptr;
    totalWeight = 0;
    trainCounter = 0;

}

// Given to you ready
std::string TrainTrack::generateTrainName()
{
    ++trainCounter;
    return "Train_" + destinationToString(destination) + "_" + std::to_string(trainCounter);
}

void TrainTrack::addTrain(Train *train)
{
    // TODO: Add a train to the end (rear) of this track

    if(train == nullptr){
        return;
    }

    train->setNext(nullptr);

    if(isEmpty()){
        firstLocomotive = train;
        lastLocomotive = train;
    }

    else {
        lastLocomotive->setNext(train);
        lastLocomotive = train;
    }

    totalWeight += train->getTotalWeight(); 

    // TODO: Hadle Auto-dispatch rule:
    //   If adding this train causes AUTO_DISPATCH_LIMIT to overflow
    //   and auto-dispatch is enabled, repeatedly dispatch (depart) trains
    //   from the front until there is enough capacity.
    //      use: std::cout << "Auto-dispatch: departing " << departed->getName() << " to make room.\n";
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    while(autoDispatch && totalWeight > AUTO_DISPATCH_LIMIT && !isEmpty()){

        Train *departed = departTrain();

        if(departed){
            std::cout << "Auto-dispatch: departing " << departed->getName() << " to make room.\n";
            delete departed;
        }
    }

}

Train *TrainTrack::departTrain()
{
    // TODO: Remove the first train (front of the track) and return it.
    // use: std::cout << "Train " << removed->name << " departed from Track " << destinationToString(destination) << "." << std::endl;
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    if (isEmpty()) {
        return nullptr;
    }

    Train *removed = firstLocomotive;

    std::cout << "Train " << removed->name << " departed from Track " << destinationToString(destination) << "." << std::endl;

    firstLocomotive = removed->getNext();
    removed->setNext(nullptr);

    if(firstLocomotive == nullptr){
        //there is no more locomotive left.
        lastLocomotive=nullptr;
    }

    totalWeight -= removed->getTotalWeight();
    trainCounter--;
    
    return removed;
}

bool TrainTrack::isEmpty() const
{
    // TODO: Return true if there are no trains on this track.
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

     return firstLocomotive == nullptr;
}


Train *TrainTrack::findTrain(const std::string &name) const
{
    // TODO: Search for a train by name.
    // Return pointer to train if found, nullptr otherwise.
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    Train *curr = firstLocomotive;

    while(curr!= nullptr) {

        if(curr->getName() == name){

            return curr;
        }

        curr = curr-> getNext();
    }

    return nullptr;
}

// Given to you ready
void TrainTrack::printTrack() const
{

    if (isEmpty())
        return;

    Train *current = firstLocomotive;

    std::cout << "[Track " << static_cast<int>(firstLocomotive->destination) << "] ";
    while (current)
    {
        std::cout << current->getName() << "(" << current->getTotalWeight() << "ton)-" << current->wagons << " -> ";
        current = current->getNext();
    }
    std::cout << std::endl;
    return;
}

void TrainTrack::clear()
{
  while(!isEmpty()){
        // We call departTrain to print the "departed" message and remove
        Train* departed = departTrain();
        if (departed) {
            delete departed;
        }
    }
    // ***************************
    
    firstLocomotive = nullptr;
    lastLocomotive = nullptr;
    totalWeight = 0;
    trainCounter = 0;
}

