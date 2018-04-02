#!/bin/sh

#Pull and star the new image
docker run --name plutarch-front -d -p 80:8080 plutarch-front
