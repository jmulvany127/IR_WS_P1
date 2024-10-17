#!/bin/bash

# Run Maven clean and package
mvn clean package

# Check if Maven command was successful
if [ $? -eq 0 ]; then
    echo "Maven build successful, running the jar..."
    # Run the jar file
    java -jar target/IR_WS_P1-1.2.jar
else
    echo "Maven build failed, exiting..."
fi


