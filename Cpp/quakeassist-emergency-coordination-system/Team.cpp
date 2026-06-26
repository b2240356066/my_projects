#include "Team.h"

Team::Team()
    : teamID(-1),
      maxLoadCapacity(0),
      currentWorkload(0),
      missionStack(4) {
}

Team::Team(int id, int maxLoad)
    : teamID(id),
      maxLoadCapacity(maxLoad),
      currentWorkload(0),
      missionStack(4) {
}

int Team::getId() const {
    return teamID;
}

int Team::getMaxLoadCapacity() const {
    return maxLoadCapacity;
}

int Team::getCurrentWorkload() const {
    return currentWorkload;
}

void Team::setId(int id) {
    teamID = id;
}

void Team::setMaxLoadCapacity(int maxLoad) {
    maxLoadCapacity = maxLoad;
}

bool Team::hasActiveMission() const {
    return !missionStack.isEmpty();
}

bool Team::tryAssignRequest(const Request& req) {
    //Implement tryAssignRequest function as explained in the PDF.

    if(currentWorkload + req.computeWorkloadContribution() > maxLoadCapacity){
        return false; //exceeds the capacity 
    }
    
    currentWorkload += req.computeWorkloadContribution(); //adding workload from the request
    missionStack.push(req); //pushing it to the stack
    return true;
}

void Team::rollbackMission(RequestQueue& supplyQueue, RequestQueue& rescueQueue) {
    //Implement rollbackMission function as explained in the PDF.

    if (missionStack.isEmpty()){
        return; // if stack is empty nothing to rollback
    }
        

     // beacause it is a stack when we pop we dont get the same order so we use another stack like array to reverse the order 
    Request req;
    int stackSize = missionStack.size();
    Request* tempArray = new Request[stackSize]; // new stack like array 
    int count = 0;


    while(missionStack.pop(req)){

        tempArray[count++] = req; // adding it all to the new array 

        currentWorkload -= req.computeWorkloadContribution();
        if (currentWorkload < 0)
            currentWorkload = 0;
    }

    for(int i = count -1; i >=0; i--){

        std::string reqType = tempArray[i].getType(); //getting the type of request

        if(reqType == "RESCUE"){

            rescueQueue.enqueue(tempArray[i]); //adding to rescue queue
        }

        else if(reqType == "SUPPLY"){

            supplyQueue.enqueue(tempArray[i]); //adding to supply queue
        }

    }

    delete[] tempArray; // delete temporary stack like array
    clearMission(); // clearing and updating workload 
        
}

void Team::clearMission() {
    missionStack.clear();
    currentWorkload = 0;
}

const MissionStack& Team::getMissionStack() const {
    return missionStack;
}
