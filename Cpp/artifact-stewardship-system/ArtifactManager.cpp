
#include "ArtifactManager.h"
#include <iostream>
#include <sstream>

ArtifactManager::ArtifactManager()
{
}

ArtifactManager::~ArtifactManager()
{
}

int ArtifactManager::tokenize(const std::string &line, std::string tokens[], int maxTokens) const
{
    std::istringstream iss(line);
    std::string tok;
    int count = 0;
    while (iss >> tok && count < maxTokens)
    {
        tokens[count++] = tok;
    }
    return count;
}

void ArtifactManager::parseAndExecute(const std::string &line)
{
    // TODO: read lines and execuıte each command
    // Print "Error: Unknown command" if command is not known

    std::string tokens[10];
    int count = tokenize(line, tokens, 10);

    if (count == 0) return;

    std::string command = tokens[0];

    
    if(command == "ADD_ARTIFACT"){
        handleAddArtifact(tokens, count);
    }
    else if (command == "REMOVE_ARTIFACT") {
        handleRemoveArtifact(tokens, count);
    } 
    else if (command == "HIRE_RESEARCHER") {
        handleHireResearcher(tokens, count);
    } 
    else if (command == "FIRE_RESEARCHER") {
        handleFireResearcher(tokens, count);
    } 
    else if (command == "REQUEST") {
        handleRequest(tokens, count);
    } 
    else if (command == "RETURN") {
        handleReturn(tokens, count);
    } 
    else if (command == "RETURN_ALL") {
        handleReturnAll(tokens, count);
    } 
    else if (command == "RESEARCHER_LOAD") {
        handleResearcherLoad(tokens, count);
    } 
    else if (command == "MATCH_RARITY") {
        handleMatchRarity(tokens, count);
    } 
    else if (command == "PRINT_UNASSIGNED") {
        handlePrintUnassigned(tokens, count);
    } 
    else if (command == "PRINT_STATS") {
        handlePrintStats(tokens, count);
    } 
    else if (command == "CLEAR") {
        handleClear(tokens, count);
    } 
    else {
        std::cout << "Error: Unknown command '" << command << "'." << std::endl; 
}
}

// =================== COMMAND HANDLERS ===================

void ArtifactManager::handleAddArtifact(const std::string tokens[], int count)
{
    // Expected: ADD_ARTIFACT <id> <name> <rarity> <value>
    // TODO:
    // 1) Check parameter count.
    // 2) Convert <id>, <rarity>, <value> to integers.
    // 3) Create Artifact and attempt to insert into AVL tree.
    // 4) On success: print "Artifact <id> added."
    // 5) On duplicate: print appropriate error (as in the PDF).

    if(count !=5){
        return;
    }

    //getting parameters and converting to integer 
    int id = std::stoi(tokens[1]);
    std::string name = tokens[2];
    int rarity = std::stoi(tokens[3]);
    int value = std::stoi(tokens[4]);

    //creating artifact 
    Artifact newArtifact(id, name, rarity, value);

    //adding to avl tree if can 
    if (artifactTree.insertArtifact(newArtifact)) {
        std::cout << "Artifact " << id << " added." << std::endl;
    } else {
        std::cout << "Error: Artifact " << id << " already exists." << std::endl;
    }

}

void ArtifactManager::handleRemoveArtifact(const std::string tokens[], int count)
{
    // Expected: REMOVE_ARTIFACT <id>
    // TODO:
    // 1) Parse id.
    // 2) Find artifact in AVL; if not found, print error.
    // 3) If artifact is assigned , find researcher and
    //    remove artifact from their list.
    // 4) Remove artifact from AVL; print success or error message.

    //getting parameters and converting to integer 
    if (count != 2) return;
    int id = std::stoi(tokens[1]);

    //finding given artifact
    ArtifactNode* node = artifactTree.findArtifact(id);

    //if not found
    if (!node) {
        std::cout << "Error: Artifact " << id << " not found." << std::endl;
        return;
    }

    //if artifact is unassigned
    if (node->data.assignedToName != "") {

        //finding given researcher
        ResearcherNode* researcher = researcherTree.findResearcher(node->data.assignedToName);
        if (researcher) {
            researcher->data.removeArtifact(id);
        }
    }

    //removing artifact if found
    if (artifactTree.removeArtifact(id)) {
        std::cout << "Artifact " << id << " removed." << std::endl;
    }
}

void ArtifactManager::handleHireResearcher(const std::string tokens[], int count)
{
    // Expected: HIRE_RESEARCHER <name> <capacity>
    // TODO:
    // 1) Parse name and capacity.
    // 2) Create Researcher and insert into RedBlackTree.
    // 3) On success: "Researcher <name> hired."
    // 4) On duplicate: error message.

    if (count != 3) return;

    //getting parameters and converting to integer 
    std::string name = tokens[1];
    int capacity = std::stoi(tokens[2]);

    //create researcher
    Researcher newResearcher(name, capacity);

    //insert to rbt if can
    if (researcherTree.insertResearcher(newResearcher)) {
        std::cout << "Researcher " << name << " hired." << std::endl;
    }

    //duplicate name
    else {
        std::cout << "Error: Researcher " << name << " already exists." << std::endl;
    }
}

void ArtifactManager::handleFireResearcher(const std::string tokens[], int count)
{
    // Expected: FIRE_RESEARCHER <name>
    // TODO:
    // 1) Find researcher by name. If not found, print error.
    // 2) For each artifact ID in their assignment list:
    //      - clear assignedToName in AVL.
    // 3) Remove researcher from RBT.
    // 4) Print success message.

    //getting parameters and converting to integer 
    if (count != 2) return;
    std::string name = tokens[1];

    //find given researcher
    ResearcherNode*  researcher = researcherTree.findResearcher(name);

    //if not found
    if (!researcher) {
        std::cout << "Error: Researcher " << name << " not found." << std::endl;
        return;
    }

    //clearing all artifact assigned to researcher
    for (int i = 0; i <  researcher->data.numAssigned; ++i) {
        int artifactID = researcher->data.assignedArtifacts[i];
        artifactTree.clearAssignedTo(artifactID);
    }

    //remove researcher
    if (researcherTree.removeResearcher(name)) {
        std::cout << "Researcher " << name << " fired." << std::endl;
    }
}

void ArtifactManager::handleRequest(const std::string tokens[], int count)
{
    // Expected: REQUEST <researcherName> <artifactID>
    // TODO:
    // 1) Find researcher by name; error if missing.
    // 2) Find artifact by ID; error if missing.
    // 3) Check artifact is unassigned; error if already assigned.
    // 4) Check researcher capacity; error if at full capacity.
    // 5) On success: add artifact to researcher list AND set assignedToName in AVL.
    //    Print "Artifact <id> assigned to <name>."

    //getting parameters and converting to integer 
    if (count != 3) return;
    std::string researcherName = tokens[1];
    int artifactID = std::stoi(tokens[2]);

    //find given researcher
    ResearcherNode* researcher = researcherTree.findResearcher(researcherName);

    //if not found
    if (!researcher) {
        std::cout << "Error: Researcher " << researcherName << " not found." << std::endl;
        return;
    }

    //find given artifact
    ArtifactNode* artifact = artifactTree.findArtifact(artifactID);

    //if not found
    if (!artifact) {
        std::cout << "Error: Artifact " << artifactID << " not found." << std::endl;
        return;
    }

    //if it is already assigned
    if (artifact->data.assignedToName != "") {
        std::cout << "Error: Artifact " << artifactID << " is already assigned." << std::endl;
        return;
    }

    //if researcher is at full capacity
    if (researcher->data.numAssigned >= researcher->data.capacity) {
        std::cout << "Error: Researcher " << researcherName << " is at full capacity." << std::endl;
        return;
    }

    //assign artifact to researcher
    if (researcher->data.addArtifact(artifactID)) {
        artifactTree.setAssignedTo(artifactID, researcherName);
        artifact->data.updateValueBasedOnUsage();
        std::cout << "Artifact " << artifactID << " assigned to " << researcherName << "." << std::endl;
    }
}

void ArtifactManager::handleReturn(const std::string tokens[], int count)
{
    // Expected: RETURN <researcherName> <artifactID>
    // TODO:
    // 1) Validate existence of researcher and artifact.
    // 2) Check that artifact.assignedToName == researcherName.
    // 3) If not, print error.
    // 4) Otherwise, remove artifact from researcher list, clear assignedToName in AVL.
    //    Print "Artifact <id> returned by <name>."

    //getting parameters and converting to integer 
    if (count != 3) return;
    std::string researcherName = tokens[1];
    int artifactID = std::stoi(tokens[2]);

    //find given researcher
    ResearcherNode* researcher = researcherTree.findResearcher(researcherName);

    //if not found
    if (!researcher) {
        std::cout << "Error: Researcher " <<researcherName << " not found." << std::endl;
        return;
    }

    //find given artifact
    ArtifactNode*  artifact = artifactTree.findArtifact(artifactID);

    //if not found
    if (!artifact) {
        std::cout << "Error: Artifact " << artifactID << " not found." << std::endl;
        return;
    }

    //if artifact is not assigned to researcher
    if (artifact->data.assignedToName != researcherName || !researcher->data.hasArtifact(artifactID)) {
        std::cout << "Error: Artifact " << artifactID << " not assigned to " << researcherName << "." << std::endl;
        return;
    }

    //remove artifact from researcher
    researcher->data.removeArtifact(artifactID);
    artifactTree.clearAssignedTo(artifactID);
    artifact->data.updateValueBasedOnUsage();
    std::cout << "Artifact " << artifactID << " returned by " << researcherName << "." << std::endl;
}


void ArtifactManager::handleReturnAll(const std::string tokens[], int count)
{
    // Expected: RETURN_ALL <researcherName>
    // TODO:
    // 1) Find researcher; error if missing.
    // 2) For each artifact they supervise, clear assignedToName in AVL.
    // 3) Clear researcher's assignment list (removeAllArtifacts()).
    // 4) Print appropriate confirmation message.

    //getting parameters and converting to integer 
    if (count != 2) return;
    std::string researcherName = tokens[1];

    //find given researcher
    ResearcherNode* researcher = researcherTree.findResearcher(researcherName);

    //if not found
    if (!researcher) {
        std::cout << "Error: Researcher " << researcherName << " not found." << std::endl;
        return;
    }

    //return all artifacts 
    for (int i = 0; i < researcher->data.numAssigned; ++i) {
        int artifactID = researcher->data.assignedArtifacts[i];
        artifactTree.clearAssignedTo(artifactID);
    }
    researcher->data.removeAllArtifacts();
    std::cout << "All artifacts returned by " << researcherName << "." << std::endl;
}

void ArtifactManager::handleResearcherLoad(const std::string tokens[], int count)
{
    // Expected: RESEARCHER_LOAD <name>
    // TODO:
    // 1) Find researcher by name.
    // 2) If not found, print error.
    // 3) Otherwise, print number of supervised artifacts in required format.

    //getting parameters and converting to integer 
    if (count != 2) return;
    std::string researcherName = tokens[1];

    //find given researcher
    ResearcherNode* researcher = researcherTree.findResearcher(researcherName);

    //if not found
    if (!researcher) {
        std::cout << "Error: Researcher " << researcherName << " not found." << std::endl;
        return;
    }

    //print number of artifacts researcher has
    std::cout << "Researcher " << researcherName << " is supervising " << researcher->data.numAssigned << " artifacts." << std::endl;
}

void ArtifactManager::handleMatchRarity(const std::string tokens[], int count)
{
    // Expected: MATCH_RARITY <minRarity>
    // TODO:
    // Traverse AVL tree and print all artifacts with rarity >= minRarity.
    // You may choose any reasonable order (probably inorder) unless specified otherwise
    // in your PDF. Artifacts may be assigned or unassigned; print as required.

    //getting parameters and converting to integer 
    if (count != 2) return;
    int minRarity = std::stoi(tokens[1]);
    
    std::cout << "=== MATCH_RARITY " << minRarity << " ===" << std::endl;
    artifactTree.printArtifactsWithRarity(minRarity);
}

void ArtifactManager::handlePrintUnassigned(const std::string tokens[], int count)
{
    // Expected: PRINT_UNASSIGNED
    // TODO:
    // Print a header if needed, then call artifactTree.printUnassigned().
    std::cout << "Unassigned artifacts:" << std::endl;
    artifactTree.printUnassigned();
    
}

void ArtifactManager::handlePrintStats(const std::string tokens[], int count)
{
    // Expected: PRINT_STATS
    // TODO:
    // 1) Compute:
    //    - totalArtifacts (artifactTree.getArtifactCount())
    //    - totalResearchers (researcherTree.getResearcherCount())
    //    - average artifact rarity (floor of totalRarity / totalArtifacts)
    //    - average researcher load (floor of totalLoad / totalResearchers)
    // 2) Print summary lines exactly as in the spec.
    // 3) Then:
    //    - Print researchers using preorder traversal:
    //      researcherTree.traversePreOrderForStats()
    //    - Print artifacts using postorder traversal:
    //      artifactTree.traversePostOrderForStats()
    //    (Exact formatting defined in your PDF.)

    int totalArtifacts = artifactTree.getArtifactCount();
    int totalResearchers = researcherTree.getResearcherCount();
    int totalRarity = artifactTree.getTotalRarity();
    int totalLoad = researcherTree.getTotalLoad();

    int avgRarity = 0;
    int avgLoad = 0;

    if(totalArtifacts > 0){

        avgRarity = totalRarity / totalArtifacts;
    }

    else{
        avgRarity = 0;
    }

    if(totalResearchers > 0){

        avgLoad = totalLoad / totalResearchers;
    }

    else{
        avgLoad = 0;
    }


    std::cout << "=== SYSTEM STATISTICS ===" << std::endl;
    std::cout << "Artifacts: " << totalArtifacts << std::endl;
    std::cout << "Researchers: " << totalResearchers << std::endl;
    std::cout << "Average rarity: " << avgRarity << std::endl;
    std::cout << "Average load: " << avgLoad << std::endl;

    std::cout << "Researchers:" << std::endl;
    researcherTree.traversePreOrderForStats();
    
    std::cout << "Artifacts:" << std::endl;
    artifactTree.traversePostOrderForStats();
}

void ArtifactManager::handleClear(const std::string tokens[], int count)
{
    // Expected: CLEAR
    // TODO:
    // Clear both trees and print confirmation message.
    artifactTree.clear();
    researcherTree.clear();
    std::cout << "All data cleared." << std::endl;
    // e.g. "All data cleared."
}
