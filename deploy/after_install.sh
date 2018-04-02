#!/bin/sh
# This is a comment!
cd /home/ubuntu/plutarch/Plutarch/frontend/src
npm install

cd /home/ubuntu/plutarch/Plutarch/backend
cp pom_build_backend.xml pom.xml

cd /home/ubuntu/plutarch/Plutarch
chmod 777 build-proj.sh
./build-proj.sh

cd backend
chmod 777 docker-build-backend.sh
./docker-build-backend.sh

cd ../frontend
chmod 777 docker-build-front.sh
./docker-build-front.sh

