#!/bin/sh
# This is a comment!
cd /home/ubuntu/plutarch/prod-deployment-script
chmod 777 deploy-frontend.sh deploy-backend.sh
./deploy-backend.sh
./deploy-frontend.sh

