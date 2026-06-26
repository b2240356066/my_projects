#include "ClassificationYard.h"
#include <iostream>

ClassificationYard::ClassificationYard() {}
ClassificationYard::~ClassificationYard() { clear(); }

WagonList &ClassificationYard::getBlockTrain(int destination, int cargoType)
{
    return blockTrains[destination][cargoType];
}

WagonList *ClassificationYard::getBlocksFor(Destination dest)
{
    return blockTrains[static_cast<int>(dest)];
}

// Inserts vagon to the corract place at the yard
void ClassificationYard::insertWagon(Wagon *w)
{
    if (!w)
        return;
    int dest = static_cast<int>(w->getDestination());
    int cargo = static_cast<int>(w->getCargoType());
    blockTrains[dest][cargo].insertSorted(w);
}

// Merges multiple blocks into a train while keeping blocks grouped
Train *ClassificationYard::assembleTrain(Destination dest, const std::string &trainName)
{
    // TODO: Collect wagons of the same destination and assemble them into a single Train.
    /**
     * - Blocks of the same cargo type must remain grouped together.
     * - These groups must be appended to the train in descending order
     *   based on their heaviest wagon.
     * - Hazardous cargo must always be placed at the very end of the train,
     *   and only one hazardous block can be included per train.*/

    Train *newTrain = new Train(trainName,dest); // creating the new train with given name and destionation
    
    int destIndex= static_cast<int>(dest);
    int hazardousIndex = static_cast<int>(CargoType::HAZARDOUS);
    int blockCount=0; //how many blocks we have added is stored here

    WagonList* blocksToSort[NUM_CARGOTYPES_INT];

    for (int i=0; i<NUM_CARGOTYPES_INT;i++){
        if(i==hazardousIndex){
            continue;
        }

        WagonList &currentList = this->blockTrains[destIndex][i];

        if(!currentList.isEmpty()){
            blocksToSort[blockCount] = &currentList;
            blockCount++;
        }
    }

    for (int i = 1; i < blockCount; ++i) 
    {
        WagonList* keyList = blocksToSort[i]; 
        int keyWeight = keyList->getFront()->getWeight();
        int j = i - 1;

        while (j >= 0 && blocksToSort[j]->getFront()->getWeight() < keyWeight) 
        {
            blocksToSort[j + 1] = blocksToSort[j];
            j = j - 1;
        }
        blocksToSort[j + 1] = keyList;
    }
    for (int i = 0; i < blockCount; ++i)
    {
        newTrain->appendWagonList(*blocksToSort[i]);
    }

    // 6. Append the hazardous block at the very end
    WagonList& hazardousBlock = this->blockTrains[destIndex][hazardousIndex];
    if (!hazardousBlock.isEmpty())
    {
        Wagon* heaviestHazardousWagon = hazardousBlock.getFront();
        int wagonIDDetached = heaviestHazardousWagon->getID();

        Wagon* detachedWagon = hazardousBlock.detachById(wagonIDDetached);

        if (detachedWagon != nullptr) {
            newTrain->addWagonToRear(detachedWagon);
        }
    }

    return newTrain;
}

bool ClassificationYard::isEmpty() const
{
    /** TODO: Check if the entire classification yard is empty.
     *
     * The yard is empty if every blockTrain list for all destination-cargo pairs is empty.
     */
    // Iterate over all destinations (rows)
    for (int i = 0; i < NUM_DESTINATIONS_INT; ++i)
    {
        // Iterate over all cargo types (columns)
        for (int j = 0; j < NUM_CARGOTYPES_INT; ++j)
        {
            
            if (!this->blockTrains[i][j].isEmpty())
            {
                return false;
            }
        }
    }
    // non-empty list. Therefore, the yard is empty.
    return true;
}

void ClassificationYard::clear()
{
    /** TODO: Clear all wagons from the classification yard.
     *
     * Used when resetting or ending the simulation.
     */

    // Iterate over all destinations (rows)
    for (int i = 0; i < NUM_DESTINATIONS_INT; ++i)
    {
        // Iterate over all cargo types (columns)
        for (int j = 0; j < NUM_CARGOTYPES_INT; ++j)
        {
            // Call the clear() method for the WagonList at [i][j].
            // This will delete all wagons in that specific list.
            this->blockTrains[i][j].clear();
        }
    }
}

// Print function is already implemented to keep output uniform
void ClassificationYard::print() const
{
    for (int i = 0; i < static_cast<int>(Destination::NUM_DESTINATIONS); ++i)
    {
        auto dest = destinationToString(static_cast<Destination>(i));
        std::cout << "Destination " << dest << ":\n";
        for (int j = 0; j < static_cast<int>(CargoType::NUM_CARGOTYPES); ++j)
        {
            if (!blockTrains[i][j].isEmpty())
            {
                auto type = cargoTypeToString(static_cast<CargoType>(j));
                std::cout << "  CargoType " << type << ": ";
                blockTrains[i][j].print();
            }
        }
    }
}