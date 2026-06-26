#include "QuakeAssistController.h"
#include <iostream>
#include <sstream>

QuakeAssistController::QuakeAssistController()
    : teams(nullptr),
      teamCount(0),
      supplyQueue(4),
      rescueQueue(4) {
}

QuakeAssistController::~QuakeAssistController() {
    delete[] teams;
}

bool QuakeAssistController::parseAndExecute(const std::string& line) {
    //Read the input file line by line and execute realtime.
    std::stringstream ss(line);
    std::string command;

    ss >> command;

    if(command == "INIT_TEAMS"){

        int numTeams;
        ss >> numTeams;

        if(initializeTeams(numTeams)){ // returns true if teams are succesfully initialized

            std::cout << "Initialized " << numTeams << " teams." << std::endl;
            return true;

        }

        return false;
        
    }

    else if(command == "SET_TEAM_CAPACITY"){

        int teamID, maxLoadCapacity;
        ss >> teamID >> maxLoadCapacity;

        if(handleSetTeamCapacity(teamID, maxLoadCapacity)){ // returns true if team is succesfully settled capacity
            std::cout << "Team " << teamID <<" capacity set to " << maxLoadCapacity <<"." << std::endl;
            return true;
        }

        return false;
    }

    else if(command == "ADD_SUPPLY"){

        int amount, emergencyLevel;
        std::string id, city, supplyType;

        ss >> id >> city >> supplyType >> amount >> emergencyLevel; 

        if(handleAddSupply(id, city, supplyType, amount, emergencyLevel)){ // returns true if request is succesfully added to queue 

            std::cout << "Request " << id << " added to SUPPLY queue." << std::endl;
            return true;
        }

        return false;
    }

    else if(command == "ADD_RESCUE"){

        int numPeople, emergencyLevel;
        std::string id, city, buildingRisk;

        ss >> id >> city >> numPeople >> buildingRisk >> emergencyLevel; 

        if(handleAddRescue(id, city, numPeople, buildingRisk, emergencyLevel)){ // returns true if request is succesfully added to queue 

            std::cout << "Request " << id << " added to RESCUE queue." << std::endl;
            return true;
        }

        return false;
    }

    else if(command == "REMOVE_REQUEST"){
        std::string id;

        ss >> id;

        if(handleRemoveRequest(id)){ // returns true if request is succesfully removed from queue 
            return true;
        }

        return false;
    }

    else if(command == "HANDLE_EMERGENCY"){

        int teamID, k;

        ss >> teamID >> k;

        if(handleHandleEmergency(teamID,k)){ // returns true if team is succesfully handled emergency and mission stack
            return true;
        }

        return false;

    }

    else if(command == "DISPATCH_TEAM"){
        int id;

        ss >> id;

        if(handleDispatchTeam(id)){ // returns true if team is succesfully dispatched
            return true;
        }

        return false;
    }

    else if(command == "PRINT_QUEUES"){

        printQueues(); //printing supply and rescue queues
        return true;
    }

    else if(command == "PRINT_TEAM"){

        int id;

        ss >> id;

        printTeam(id); //prints mission stack of the specified team
        return true;
    }

    else if(command == "CLEAR"){

        clear(); // clears mission stacks and queues
        return false; 
    }

    else{

        std::cout << "Error: Unknown command '" << command << "'." << std::endl;
        return true;
    }
}

bool QuakeAssistController::initializeTeams(int numTeams) {
    //Create a team array and initialize it with teams.
    if(numTeams < 0){
        return false;
    }

    if(teams !=nullptr){ // if teams is not empty before delete the content
        delete[] teams; 
        teams = nullptr;
    }

    teams = new Team[numTeams]; // new teams array 
    teamCount = numTeams; // updating number of teams

    for(int i=0; i < numTeams; i++){
        teams[i].setId(i); // setting id's of teams
    }

    supplyQueue.clear(); // clearing queues for not to have some memory leak
    rescueQueue.clear();

    return true;
}

int QuakeAssistController::findTeamIndexById(int teamId) const {
    //Find the index of the team using teamId.
    for(int i=0; i< teamCount; i++){

        if(teams[i].getId() == teamId){

            return i; // return the index of team array that team is found
        }
    }
    
    return -1;
    
}

bool QuakeAssistController::handleSetTeamCapacity(int teamId, int capacity) {
    //Find the index of team in the array, update the capacity value of the team.

    int handledIdx = findTeamIndexById(teamId); //finding the team with given id

    if(handledIdx == -1){
        std::cout << "Error: Team " << teamId << " not found." << std::endl;
        return true;
    }

    teams[handledIdx].setMaxLoadCapacity(capacity); // setting max capacity
    return true;
}

bool QuakeAssistController::handleAddSupply(const std::string& id,
                                            const std::string& cityStr,
                                            const std::string& supplyTypeStr,
                                            int amount,
                                            int emergencyLevel) {
    //Create new supply request, and add it to the SUPPLY queue.

    Request req(id,cityStr, supplyTypeStr, amount, emergencyLevel); // creating request with given parameters
    supplyQueue.enqueue(req); // adding it to the queue
    return true;
}

bool QuakeAssistController::handleAddRescue(const std::string& id,
                                            const std::string& cityStr,
                                            int numPeople,
                                            const std::string& riskStr,
                                            int emergencyLevel) {
    //Create new rescue request, and add it to the RESCUE queue.

    Request req(id,cityStr, numPeople, riskStr, emergencyLevel);// creating request with given parameters
    rescueQueue.enqueue(req); // adding it to the queue
    return true;
}

bool QuakeAssistController::handleRemoveRequest(const std::string& id) {
    //Remove request using request ID from request(SUPPLY, RESCUE) queue. 
    bool flagRescue = false;
    bool flagSupply = false;

    if(supplyQueue.removeById(id)){
        flagSupply = true; // if supplyQueue has the request
    }

    else if(rescueQueue.removeById(id)){
        flagRescue = true;  // if rescueQueue has the request
    }

    if(flagRescue || flagSupply){ // if one of the queues have it 
        std::cout << "Request " << id << " removed from queues." << std::endl;
        return true; 
    }

    else{
        std::cout << "Error: Request " << id <<" not found." << std::endl;
        return true; 
    }

}

bool QuakeAssistController::handleHandleEmergency(int teamId, int k) {
    // TODO: Implement:
    // 1) Find team by id.
    // 2) For up to k steps:
    //    - Look at front of supplyQueue and rescueQueue using peek().
    //    - Use Request::computeEmergencyScore() to decide which to take.
    //    - If both empty -> break.
    //    - Try teams[teamIdx].tryAssignRequest(chosenRequest).
    //       * If this returns false, print overload message and
    //         call teams[teamIdx].rollbackMission(supplyQueue, rescueQueue),
    //         then break.
    //       * Else, dequeue chosen request from its queue and continue.

    int teamIdx = findTeamIndexById(teamId); // finding the team with given id

    int requestCount = 0;
    int rescueCount = 0;
    int supplyCount = 0;

    if(teamIdx==-1){

        std::cout << "Error: Team "<< teamId << " not found." << std::endl;
        return true;
    }

    for(int i=0; i < k; i++){ // if both of the queues are empty

        if(supplyQueue.isEmpty() && rescueQueue.isEmpty()){
        break;
    }

    Request chosenRequest;
    Request frontSupply;
    Request frontRescue;

    if(supplyQueue.peek(frontSupply) && rescueQueue.peek(frontRescue)){ // if both queues are not empty 

        if(frontSupply.computeEmergencyScore() > frontRescue.computeEmergencyScore()){

            chosenRequest = frontSupply; // supply emergency is higher so we add it first
        }

        else if( frontRescue.computeEmergencyScore() > frontSupply.computeEmergencyScore()){

            chosenRequest = frontRescue; // rescue emergency is higher so we add it first
        }

        else{

            chosenRequest = frontRescue; // they are both equal but we are adding rescue request.
        }

    }

    else if(supplyQueue.peek(frontSupply)){ // if rescueQueue is empty 

        chosenRequest = frontSupply;
    }

    else if(rescueQueue.peek(frontRescue)){ // if supplyQueue is empty 

        chosenRequest = frontRescue;
    }

    if(chosenRequest.getType() == "RESCUE"){

         rescueQueue.dequeue(chosenRequest); // get the front of rescueQueue

    }

    else{

        supplyQueue.dequeue(chosenRequest); // get the front of supplyQueue
    }


    if(teams[teamIdx].tryAssignRequest(chosenRequest)){ // if request doesnt exceed the workload increment the counts

        if(chosenRequest.getType() == "RESCUE"){ 
           
            rescueCount++;
            requestCount++;
            continue;
        }

        else if(chosenRequest.getType() == "SUPPLY" ){
            
            supplyCount++;
            requestCount++;
            continue;
        }

    }
    
    //if it exceeds call rollback and print overload message 
    std::cout<<"Overload on Team "<< teamId <<": rolling back mission." << std::endl;
    teams[teamIdx].rollbackMission(supplyQueue, rescueQueue);

     if(chosenRequest.getType() == "RESCUE"){

         rescueQueue.enqueue(chosenRequest); // put back the request 

    }

    else{

        supplyQueue.enqueue(chosenRequest); // put back the request 
    }

    return true;
    

    }
    
    // if no overload occur print the total count message 
    std::cout << "Team " << teamId <<" assigned " << requestCount<<" requests (" << supplyCount <<" SUPPLY, " << 
        rescueCount << " RESCUE), total workload " << teams[teamIdx].getCurrentWorkload() << "." << std::endl;

    return true;
}

bool QuakeAssistController::handleDispatchTeam(int teamId) {
    int idx = findTeamIndexById(teamId);
    if (idx == -1) {
        std::cout << "Error: Team " << teamId << " not found." << std::endl;
        return true;
    }
    Team& t = teams[idx];
    if (!t.hasActiveMission()) {
        std::cout << "Error: Team " << teamId << " has no active mission." << std::endl;
        return true;
    }
    int workload = t.getCurrentWorkload();
    std::cout << "Team " << teamId << " dispatched with workload " << workload << "." << std::endl;
    t.clearMission();
    return true;
}

void QuakeAssistController::printQueues() const {
    //Print queues.
    std::cout << "SUPPLY QUEUE:" << std::endl;
    
    const Request* reqSupply = supplyQueue.getData();
    for(int i = 0; i < supplyQueue.getCount(); i++){

        int index = (i + supplyQueue.getFrontIndex()) % supplyQueue.getCapacity();

        const Request& currentReq = reqSupply[index];
        std::cout << currentReq.getId() << " " << currentReq.getCity() << " " << currentReq.getSupplyType() 
        << " " << currentReq.getAmount() << " " << currentReq.getEmergencyLevel() << std::endl;
    }

    std::cout << "RESCUE QUEUE:" << std::endl;

    const Request* reqRescue = rescueQueue.getData();

    for(int i = 0; i < rescueQueue.getCount(); i++){

        int index = (i + rescueQueue.getFrontIndex()) % rescueQueue.getCapacity();

        const Request& currentReq = reqRescue[index];
        std::cout << currentReq.getId() << " " << currentReq.getCity() << " " << currentReq.getNumPeople() 
        << " " << currentReq.getRisk() << " " << currentReq.getEmergencyLevel() << std::endl;
    }
}

void QuakeAssistController::printTeam(int teamId) const {
    //Print team data using teamId.
    int teamIdx = findTeamIndexById(teamId);
    if(teamIdx == -1){
        
        std::cout << "Error: Team " << teamId << " not found." << std::endl;
        return; 
    }
    
    std::cout << "TEAM " << teamId << " STACK:" << std::endl;

    const MissionStack& missionStack =  teams[teamIdx].getMissionStack();
    const Request *req = missionStack.getData();

    if(req == nullptr){

        std::cout << "Error: Team " << teamId << " has no active mission." << std::endl;
    }

    for(int i= missionStack.getTopIndex(); i >= 0; i--){

        if(req[i].getType() == "SUPPLY"){
            std::cout << req[i].getId() << " " << req[i].getCity() << " " << req[i].getSupplyType() << " " << req[i].getAmount() << " " <<
            req[i].getEmergencyLevel() << std::endl;
        }

        else if(req[i].getType() == "RESCUE"){
            std::cout << req[i].getId() << " " << req[i].getCity() << " " << req[i].getNumPeople() << " " << req[i].getRisk() << " " << 
             req[i].getEmergencyLevel() << std::endl;
        }

    }
    
}

void QuakeAssistController::clear() {
    //Clear data.

    supplyQueue.clear();
    rescueQueue.clear();

    for(int i=0; i < teamCount; i++){
        teams[i].clearMission();
    }

    std::cout << "System cleared." << std::endl;
}
