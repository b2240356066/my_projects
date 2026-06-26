#include "MissionStack.h"
#include <new>     // for std::nothrow

MissionStack::MissionStack()
    : data(nullptr),
      capacity(0),
      top(-1) {
    resize(4);
}

MissionStack::MissionStack(int initialCapacity)
    : data(nullptr),
      capacity(0),
      top(-1) {
    if (initialCapacity < 1) {
        initialCapacity = 4;
    }
    resize(initialCapacity);
}

MissionStack::~MissionStack() {
    delete[] data;
}

bool MissionStack::isEmpty() const {
    return top == -1;
}

int MissionStack::size() const {
    return top + 1;
}

bool MissionStack::push(const Request& req) {
    //Implement push function as explained in the PDF.
    
    if(size() == capacity){ //stack is full

        resize(capacity*2); //doubling the capacity
    }

    top++; //increment the data 

    data[top] = req; // pushed request is added to last index (top index)

    return true;
}

bool MissionStack::pop(Request& outReq) {
    //Implement pop function as explained in the PDF.

    if(isEmpty()){

        return false; //stack is empty
    }

    outReq = data[top]; //last added request 

    top--; //decrement of count

    return true;
}

bool MissionStack::peek(Request& outReq) const {
    //Implement peek function as explained in the PDF.
    if(isEmpty()){
        
        return false; //stack is empty
    }

    outReq = data[top]; //last added request 
    
    return true;
}

void MissionStack::clear() {
    top = -1;
}

bool MissionStack::resize(int newCapacity) {
    //Implement resize function as explained in the PDF.
    if(capacity >= newCapacity || size() >= newCapacity){

        return false; //if newCapacity isnt larger than current capacity, its meaningless
    }

    Request* ResizedData = new Request[newCapacity]; //creating a new array with new size(newCapacity)

    if(ResizedData == nullptr){

        return false; //checking for allocation failure
    }

    for(int i=0; i < size(); i++){

        ResizedData[i] = data[i]; // adding the elements in the new array ResizedData
    }

    delete[] data; //deleting the content

    data = ResizedData;  // to use data for other functions

    capacity = newCapacity; //updating capacity 
    
    return true;
}
