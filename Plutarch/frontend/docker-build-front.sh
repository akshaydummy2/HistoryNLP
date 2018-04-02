#!/bin/sh

#Stop and remove old image
docker stop plutarch-front
docker rm plutarch-front
docker rmi plutarch-front

#Build new local image
docker build -t plutarch-front .
