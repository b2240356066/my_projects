#include "RailMarshal.h"
#include <iostream>
#include <sstream>
#include <algorithm>

RailMarshal::RailMarshal()
{
    // TODO: Initialize each track in the departure yard.
    // Each TrainTrack corresponds to one Destination.

    for(int i=0; i< NUM_DESTINATIONS_INT;i++){
        Destination dest = static_cast<Destination>(i);

        departureYard[i] = TrainTrack(dest);
    }
}

RailMarshal::~RailMarshal()
{
    // TODO: Cleanup remaining trains, prevent memory leaks
    // can be empty because we got destructors in ClassificationYard and TrainTrack.
}

// Getter (ready)
ClassificationYard &RailMarshal::getClassificationYard()
{
    return classificationYard;
}

// Getter (ready)
TrainTrack &RailMarshal::getDepartureYard(Destination dest)
{
    int idx = static_cast<int>(dest);
    return departureYard[idx];
}

void RailMarshal::processCommand(const std::string &line)
{
    // TODO: Parse user commands from input lines.

    std::stringstream ss(line);
    std::string command;

    ss >> command;

    if(command.empty()){
        return ;
    }

    // if ADD_WAGON
    // Use: std::cout << "Error: Invalid ADD_WAGON parameters.\n";
    // Use: std::cout << "Wagon " << *w << " added to yard." << std::endl;
    if(command == "ADD_WAGON"){
        int id, weight , maxLoad ;
        std::string cargoS, destS; 

        if(!(ss >> id >> cargoS >> destS >> weight >> maxLoad)){

            std::cout << "Error: Invalid ADD_WAGON parameters.\n";
            return;
        }

        else{
            CargoType cargo = parseCargo(cargoS);
            Destination dest = parseDestination(destS);

            Wagon* wagon = new Wagon(id, cargo, dest, weight, maxLoad);

            classificationYard.insertWagon( wagon );
            std::cout << "Wagon " << *wagon << " added to yard." << std::endl;

        }
    }

    // if REMOVE_WAGON
    // Use: std::cout << "Error: Invalid REMOVE_WAGON parameters.\n";
    // Use: std::cout << "Wagon " << id << " removed." << std::endl;
    // Use: std::cout << "Error: Wagon " << id << " not found." << std::endl;

    else if(command == "REMOVE_WAGON"){

        int id;
        std::string invalidParameter;

        if(!(ss >> id)){

            std::cout << "Error: Invalid REMOVE_WAGON parameters.\n";
            return;    
        }

        else if( ss >> invalidParameter){

            std::cout << "Error: Invalid REMOVE_WAGON parameters.\n";
            return;
        }
        else {

            Wagon* removedWagon = nullptr;
            bool success = false;

            for (int i = 0; i < NUM_DESTINATIONS_INT && !success; ++i)
            {
                for (int j = 0; j < NUM_CARGOTYPES_INT && !success; ++j)
            {
                    removedWagon = classificationYard.getBlockTrain(i, j).detachById(id);
                    if (removedWagon != nullptr)
                    {
                        success = true;
                    }
                }
            }

            if (success)
            {
                std::cout << "Wagon " << id << " removed." << std::endl;
                delete removedWagon; 
            }
            else
            {
                std::cout << "Error: Wagon " << id << " not found." << std::endl;
                return;
            }
        }


    }

    else if(command == "ASSEMBLE_TRAIN"){
        // if ASSEMBLE_TRAIN
    //  Use: std::cout << "Error: Invalid ASSEMBLE_TRAIN parameters.\n";
    //  Use: std::cout << "No wagons to assemble for " << destStr << std::endl;
    //  verify couplers and possibly split (deterministic)
    //  Keep splitting the *front* train until no more overloaded couplers found
    //  create new train with same destination and name suffix
    //  use std::cout << "Train " << newTrain->getName() << " assembled after split with "
    //  << newTrain->getWagons()<< " wagons." << std::endl;
    // use std::cout << "Train " << t->getName() << " assembled with " << t->getWagons() << " wagons." << std::endl;

    std::string destS;
    if(!(ss >> destS)){
        std::cout << "Error: Invalid ASSEMBLE_TRAIN parameters.\n";
        return;
    }
    else {

        Destination dest = parseDestination(destS);

        TrainTrack& track = departureYard[static_cast<int>(dest)];

        std::string trainName = track.generateTrainName();

        Train* newTrain = classificationYard.assembleTrain(dest, trainName);

        if(newTrain == nullptr || newTrain->getWagons().isEmpty()){
            std::cout << "No wagons to assemble for " << destS << std::endl;
                if (newTrain) delete newTrain; 
        }

        else{
            
            int SplitCount = 1;

            std::string firstTrain = newTrain->getName();
            
            Train* splitTrain = newTrain->verifyCouplersAndSplit(SplitCount);
    
    
        while (splitTrain != nullptr){
        
        
        std::cout << "Train " << splitTrain->getName() 
                  << " assembled after split with " 
                  << splitTrain->getWagons() << " wagons." 
                  << std::endl;
        
    
        track.addTrain(splitTrain);
        
        
        splitTrain = newTrain->verifyCouplersAndSplit(++SplitCount);
    }


    track.addTrain(newTrain);
    std::stringstream ss_wagons;

            ss_wagons << newTrain->getWagons();
            std::string wagonListS = ss_wagons.str();

    
    std::cout << "Train " << firstTrain
              << " assembled with " << wagonListS
              << " wagons." << std::endl;
            }
        }
    }

    else if(command == "DISPATCH_TRAIN" ){
        // if DISPATCH_TRAIN
    //  use: std::cout << "Error: Invalid DISPATCH parameters.\n";
    //  use: std::cout << "Error: No trains to dispatch from track " << destStr << ".\n";
    //  use:  std::cout << "Dispatching " << train->getName() << " (" << t->getTotalWeight() << " tons)." << std::endl;

    std::string destS;
    if(!(ss >> destS)){
        std::cout << "Error: Invalid DISPATCH parameters.\n";
        return;
    }

    else{
        Destination dest = parseDestination(destS);

        dispatchFromTrack(dest);
    }
    }

    else if( command == "PRINT_YARD"){
        // if PRINT_YARD
    //  use std::cout << "--- classification Yard ---\n";

        std::cout << "--- classification Yard ---\n";
        classificationYard.print();
    }

    else if(command == "PRINT_TRACK"){
        // if PRINT_TRACK
    //  use std::cout << "Error: Invalid PRINT_TRACK parameters.\n";

    
        std::string destS;
        if (!(ss >> destS))
        {
            std::cout << "Error: Invalid PRINT_TRACK parameters.\n";
            return;
        }
        else
        {
            Destination dest = parseDestination(destS);
            departureYard[static_cast<int>(dest)].printTrack(); 
        }

    }

    else if(command == "AUTO_DISPATCH"){

        // if AUTO_DISPATCH <ON/OFF>
    // Enable or disable automatic dispatch when weight exceeds limits.
    // std::cout << "Error: Invalid AUTO_DISPATCH parameters.\n";
    // print "Auto dispatch "enabled" / "disabled"

        std::string auto_switch;
        if(!(ss >> auto_switch)){
            std::cout << "Error: Invalid AUTO_DISPATCH parameters.\n";
            return;
        }

        else{
            if(auto_switch == "ON"){
                TrainTrack::autoDispatch = true; 
                std::cout << "Auto dispatch enabled\n";
            }

            else if(auto_switch == "OFF"){
                TrainTrack::autoDispatch = false; 
                std::cout << "Auto dispatch disabled\n";
            }

            else{
                std::cout << "Error: Invalid AUTO_DISPATCH parameters.\n";
                return;
            }
        }

    }

    else if(command == "CLEAR"){
        // if CLEAR
    // Completely reset the system (yard + departure tracks).
    // std::cout << "System cleared." << std::endl;

    // else std::cout << "Error: Unknown command '" << cmd << "'" << std::endl;
        classificationYard.clear();

        for (int i = 0; i < NUM_DESTINATIONS_INT; ++i){
            departureYard[i].clear();
        }
        std::cout << "System cleared." << std::endl;

    }

    else{
            std::cout << "Error: Unknown command '" << command << "'" << std::endl;
            return;
        }
    
    
}
void RailMarshal::dispatchFromTrack(Destination track)
{
    // TODO: Dispatch the next train (frontmost) from the specified track.
    // std::cout << "Error: No trains to dispatch from Track " << destIndex << ".\n";
    /*std::cout << "Train " << t->getName()
              << " departed from Track " << destIndex
              << " (" << destinationToString(static_cast<Destination>(destIndex)) << ").\n";
     */

     int destIndex = static_cast<int>(track);

     TrainTrack& thisTrack = departureYard[destIndex];

     if(thisTrack.isEmpty()){

        std::cout << "Error: No trains to dispatch from track " << destinationToString(track) << ".\n";
        return;
     }

     Train *departedTrain = thisTrack.departTrain();

     if(departedTrain!= nullptr){
        std::cout << "Dispatching " << departedTrain->getName() 
                  << " (" << departedTrain->getTotalWeight() << " tons)." << std::endl;

                  delete departedTrain;

     }
}

void RailMarshal::printDepartureYard() const
{
    for (int i = 0; i < NUM_DESTINATIONS_INT; ++i)
    {
        std::cout << "Track " << i << " ("
                  << destinationToString(static_cast<Destination>(i)) << "):\n";
        departureYard[i].printTrack();
    }
}

// Debug helper functions
void RailMarshal::printStatus() const
{
    std::cout << "--- classification Yard ---\n";
    classificationYard.print();

    std::cout << "--- Departure Yard ---\n";
    for (int i = 0; i < static_cast<int>(Destination::NUM_DESTINATIONS); ++i)
    {
        departureYard[i].printTrack();
    }
}
