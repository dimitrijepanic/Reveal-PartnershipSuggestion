
## Overview
Dear Reader,

I have implemented the Reveal Backend Engineer Project - PartnershipSuggestion in Java. Overall idea was to try to decouple the system services, while theoretically allowing for them to be expanded in the future, with adequate scallability patterns. The diagram below depicts the high level architecture of the system. For adapters I have chosen to use the Dependecy Injection Pattern. The communication between services is done through Domain Objects, while sending requests to Adapters through Command, and lastly Data Transfer Objects are used to receive results from adapter method calls.
<p align="center" width="100%">
   <img width="33%" alt="overview" src="https://github.com/dimitrijepanic/Reveal-PartnershipSuggestion/assets/82520610/6d29f5b9-58d5-494e-8cb3-41e54fe34474">
</p>
Future sections will briefly go over each of the domain logic components.
</br>
Author: Dimitrije Panic

## Cache Service
Having in mind the cost of fetching data from databases, I have added a caching/message broker service. Current implementation is using Redis, but this can be changed by implementing the CacheService interface. Redis was chosen as it very efficient for storing lists, while also allowing for easier use in multi-threaded environemnts because of its single thread nature. 

It is used in 3 cases: 
   * To store the pair (+companyId, list_of_suggestions) to allow for fast retrieval
   * To store the pair (-companyId, mail_list) to allow for fast retrieval of what is the next email to be sent
   * To store the pair (companyId, lsit_of_updates) so we can compare it to the current list and know which one have been Accepte/Declined

Caching eviction policies should be further discussed, however nowaday Redis is even used as a full database.
## Suggestion Generating Service
Suggestion Generating Service is the service that actually collects the suggestions, and then adds it to the Cache. Furthermore, it calls the Timer Start Service to start the timer for the first Email to be sent (possibly).
<p align="center" width="100%">
   <img width="66%" alt="suggestion_generating_sequence" src="https://github.com/dimitrijepanic/Reveal-PartnershipSuggestion/assets/82520610/e1305c7f-1b82-44a0-b1a8-9e22f99b55b3">
</p>

> **_NOTE:_** In the diagram Command Factory and Response Payload Utility are not present - to reduce the overall complexity of the diagram I only kept the key points of the algorithm.

## Timer Receiver Service
Timer Receiver Service receives the command acknowledging the previous timer expiration. First it collects the list from the cache, then compares it to updates done since the last timer expired. If there is still a need to send the email, it will do so. Lastly, it will return the list to the cache, and schedule a new timer by calling the timer start service.
<p align="center" width="100%">
 <img width="66%" alt="timer_receiver_service_sequence_diagram" src="https://github.com/dimitrijepanic/Reveal-PartnershipSuggestion/assets/82520610/02d005b9-34a2-4997-958c-e89f2f6539f8">
</p>

## Timer Start Service
Creating a seperate service that start the timer was done so we would have the logic that is reused in multiple services in one place.
<p align="center" width="100%">
  <img width="66%" alt="timer_start_service" src="https://github.com/dimitrijepanic/Reveal-PartnershipSuggestion/assets/82520610/aec455f6-4bd8-439a-9183-1e9b0b545496">
</p>


## Suggestion Update Service
Suggestion Update Service acknowledges the update and adds it to the cache, while also persisting it to the DB.
<p align="center" width="100%">
   <img width="66%" alt="suggestion_update_sequence" src="https://github.com/dimitrijepanic/Reveal-PartnershipSuggestion/assets/82520610/8390c3fc-9409-429c-bef9-5121849d6b0b">
</p>

> **_NOTE:_** It is important to note here is the Authentication done before? Is the Token already checked? Are we in a private network? I presume all the answers are "Yes".


## Testing 
I have written 29 tests to cover both the Functional and Unit aspects.
## Future Works
* Service Registry
  - Instead of utilizing dependency injection for Timer Start Service, it would make a lot more sense to have a Service Registry component so they can actually communicate properly
* Docker
  - Creating an image and deploying it would be a good next step
* Horizontal Scaling
  - Components were designed in a way to allow for easier horizontal scaling (increasing the number of nodes). For example if we saw that the Update Suggestion Service was doing a lot of work, we could always increase the number and add a Load Balancer to distribute the request. This goes in hand with the stateless architecture of the system.
* Cache eviction policy
  - Theoretically Redis can get full. However, the cache empties it self after there are no more emails to be sent, so actually only the recent requests will remain in the cache. With proper monitoring we could see if this is good enough.
* SQL or NoSQL
  - NoSQL will allow for faster reads, however since the DB is completely denormalized updating one recommendation we will have to fetch the complete file
  - Writes are more common than reads
* Why not base Command and/or base Data Transfer CLass
  - It would not make much sense because practically every object is unique.. we could maybe make it just for the general result, however it seems like it is a bit overcoding
* System design important characterictics - with Analytics and Monitoring can be improved
  - Availability - depends on the replicas we have and patterns used - not discussed at this point
  - Consistency - this models works perfectly fine with eventual consistency (NoSQL), no need for strict ACID properties
  - Reliability - depends a lot on the cache service and DB implementation
  - Security - depends on the authentication and tokenization, also our domain logic can completely be in a private network
  - Extensibility - high, can add any type of software that we need with minimal code change
  - Robustness - depends on which part, but I would say system was designed with robustness in mind
  - Asynchronous communication - can also be added for database writing since eventual consistency is fine in this use case

## How To Use
The Project uses external jars that need to be imported by Project -> Properties -> Java Build Path -> Classpath -> Add External Jars. 

```bash
# Download and install Redis
$ curl -fsSL https://packages.redis.io/gpg | sudo gpg --dearmor -o /usr/share/keyrings/redis-archive-keyring.gpg

$ echo "deb [signed-by=/usr/share/keyrings/redis-archive-keyring.gpg] https://packages.redis.io/deb $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/redis.list

$ sudo apt-get update
$ sudo apt-get install redis

```

```bash
# Run Redis 
$ redis-server
```
