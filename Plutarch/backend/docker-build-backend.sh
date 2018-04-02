#!/bin/sh

#Stop and remove old image
docker stop plutarch-backend
docker rm plutarch-backend
docker rmi plutarch-backend

#Build new local image
docker build -t plutarch-backend .
