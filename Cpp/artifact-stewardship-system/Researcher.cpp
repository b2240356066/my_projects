#include "Researcher.h"

Researcher::Researcher()
    : fullName(""),
      capacity(0),
      assignedArtifacts(nullptr),
      numAssigned(0)
{
}

Researcher::Researcher(const std::string &name, int cap)
    : fullName(name),
      capacity(cap),
      assignedArtifacts(nullptr),
      numAssigned(0)
{
    if (capacity > 0)
    {
        assignedArtifacts = new int[capacity];
    }
}

// added copy constructor to make them share the same memory space
Researcher::Researcher(const Researcher &other)
    : fullName(other.fullName),
      capacity(other.capacity),
      numAssigned(other.numAssigned),
      assignedArtifacts(nullptr)
{
    if (capacity > 0)
    {
        assignedArtifacts = new int[capacity];
        for (int i = 0; i < numAssigned; ++i)
        {
            assignedArtifacts[i] = other.assignedArtifacts[i];
        }
    }
}

Researcher &Researcher::operator=(const Researcher &other)
{
    if (this == &other)
        return *this; // Self-assignment check

    // Clean up existing memory
    delete[] assignedArtifacts;

    // Copy data
    fullName = other.fullName;
    capacity = other.capacity;
    numAssigned = other.numAssigned;
    assignedArtifacts = nullptr;

    if (capacity > 0)
    {
        assignedArtifacts = new int[capacity];
        for (int i = 0; i < numAssigned; ++i)
        {
            assignedArtifacts[i] = other.assignedArtifacts[i];
        }
    }

    return *this;
}

Researcher::~Researcher()
{
    delete[] assignedArtifacts;
    assignedArtifacts = nullptr;
}

bool Researcher::addArtifact(int artifactID)
{
    // TODO:
    // 1) If hasArtifact(artifactID) is true, return false (already supervised).
    // 2) If numAssigned >= capacity, return false (at full capacity).
    // 3) Otherwise, insert artifactID at assignedArtifacts[numAssigned] and increment numAssigned.
    // 4) Return true.

    if (hasArtifact(artifactID))
    {
        return false; // researcher is already supervising this artifact.
    }

    if (numAssigned >= capacity)
    {
        return false; // reached full capacity
    }

    assignedArtifacts[numAssigned] = artifactID; // adding to artifact to assignedArtifacts array
    numAssigned++;                               // increasing the assigned count
    return true;
}

bool Researcher::removeArtifact(int artifactID)
{
    // TODO:
    // 1) Find index i where assignedArtifacts[i] == artifactID.
    // 2) If not found, return false.
    // 3) Shift elements after i one step to the left.
    // 4) Decrement numAssigned and return true.

    for (int i = 0; i < numAssigned; i++)
    {
        if (assignedArtifacts[i] == artifactID){ // if the artifact is found
            for (int j = i; j < numAssigned - 1; j++)
            {
                assignedArtifacts[j] = assignedArtifacts[j + 1]; // shift artifacts by one to left
            }
            numAssigned--; // decreasing the assigned count
            return true;
        }
    }
    return false;
}

void Researcher::removeAllArtifacts()
{
    // TODO:
    // Simply reset numAssigned to 0.
    // (The actual artifacts' assignedToName in the AVL tree will be cleared by the controller.)
    numAssigned = 0;
}

bool Researcher::hasArtifact(int artifactID) const
{
    // TODO:
    // Return true if artifactID appears in assignedArtifacts[0..numAssigned-1], false otherwise.
    
    for (int i = 0; i < numAssigned; i++)
    { // traverse all the artifacts
        if (assignedArtifacts[i] == artifactID)
        { // if it find the wanted artifactID return true
            return true;
        }
    }
    return false;
}