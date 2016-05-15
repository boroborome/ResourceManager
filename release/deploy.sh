#!/bin/sh
set -e
#Usage: deploy.sh server targetDir profile
#Example: deploy.sh appadmin@10.99.246.110 /data/lesc/jboss-5.1.0.GA dev
server=$1
targetDir=$2
profile=$3


echo "Clean dir $targetDir on $server..."
ssh $server /bin/bash << EOF
cd $targetDir
if [ -f "stop.sh" ]; then
    ./stop.sh
fi
set +e
rm -rf $targetDir
mkdir -p $targetDir
EOF

echo $profile>profile
echo "Copy file to $server..."
scp -r * $server:$targetDir

echo "Set right on $server..."
ssh $server /bin/bash << EOF
set +e
sh /etc/profile
cd $targetDir
chmod +x *.sh
sh start.sh wave3_sit
exit
EOF

echo "deployed preselect-service on $server..."