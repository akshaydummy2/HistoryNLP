#!/bin/sh

#Pull and star the new image
docker run --name plutarch-backend -d -p 8080:8080 plutarch-backend
