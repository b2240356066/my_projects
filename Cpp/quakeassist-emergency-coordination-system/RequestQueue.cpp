#include "RequestQueue.h"
#include <new> // for std::nothrow

RequestQueue::RequestQueue()
    : data(nullptr),
      capacity(0),
      front(0),
      rear(0),
      count(0) {
    // start with a small default capacity
    resize(4);
}

RequestQueue::RequestQueue(int initialCapacity)
    : data(nullptr),
      capacity(0),
      front(0),
      rear(0),
      count(0) {
    if (initialCapacity < 1) {
        initialCapacity = 4;
    }
    resize(initialCapacity);
}

RequestQueue::~RequestQueue() {
    delete[] data;
}

bool RequestQueue::isEmpty() const {
    return count == 0;
}

bool RequestQueue::isFull() const {
    return count == capacity;
}

int RequestQueue::size() const {
    return count;
}

int RequestQueue::nextIndex(int idx) const {
    if (capacity == 0) return 0;
    return (idx + 1) % capacity;
}

bool RequestQueue::enqueue(const Request& req) {
    //Implement enqueue function as explained in the PDF.

    if(isFull()){
        resize(capacity*2); //resizing if its full
    }

    data[rear] = req; //appending it to last one 

    rear = (rear+1) % capacity; //updating rear 

    count++; //increment request count
    return true;
}

bool RequestQueue::dequeue(Request& outReq) {
    //Implement dequeue function as explained in the PDF.

    if(count == 0){
        return false; //queue is empty 
    }

    outReq = data[front]; //the first request that will be dequeued

    front = nextIndex(front); //updating front to next one (no need to slide the remaining beacause it is a circular queue)

    count--; //decrement of count 
    return true; 
}

bool RequestQueue::peek(Request& outReq) const {
    //Implement peek function as explained in the PDF.
    
    if(count == 0){

        return false; //if the queue is empty
    }

    outReq = data[front]; //the first request that will be shown

    return true;
}

void RequestQueue::clear() {
    front = 0;
    rear = 0;
    count = 0;
}

bool RequestQueue::removeById(const std::string& id) {
    //Implement removeById function as explained in the PDF.


    if (count == 0) {
        
        return false; // queue is empty
    }

    int removedRequestIdx = -1 ; //if we find the matching request this will change. 

    int currentIdx = front; // starting from the front 

    for (int i = 0; i < count; i++) {

        if (data[currentIdx].getId() == id) {

            removedRequestIdx = currentIdx; // if we get a matching id update removedRequestIdx 
            break;
        }
        
        currentIdx = nextIndex(currentIdx);
    }

    if(removedRequestIdx == -1){

        return false; // if it doesn't change that means it is not found
    }

    int curr = removedRequestIdx;

    int next = nextIndex(curr);

    for (int i = 0; i < count - 1; i++) { // sliding the next ones in the array 

        data[curr] = data[next]; 
        curr = next;
        next = nextIndex(next);
    }

    rear = (rear-1 + capacity) % capacity; //updating rear  


    count--; //decrement requests count

    return true;
}

bool RequestQueue::resize(int newCapacity) {
    //Implement resize function as explained in the PDF.

    if(capacity >= newCapacity || count >= newCapacity){

        return false; //if newCapacity isnt larger than current capacity, its meaningless
    }

    Request* ResizedData = new Request[newCapacity]; //creating a new array with new size(newCapacity)

    if(ResizedData == nullptr){
        return false; //checking for allocation failure
    }

    for(int i=0; i < count; i++){
        //adding old data to new data with wrap-around logic, front might not be at the first index of the list
        ResizedData[i] = data[(front +i)%capacity];
    }

    delete[] data; //delete old array to prevetn memory leak

    data = ResizedData; //updating array

    capacity = newCapacity; //updating capacity

    front = 0; //updating front to 0 to first index is the first request

    rear = count; //updating rear to last request

    return true;
}
 