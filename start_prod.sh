#!/bin/bash
cd `dirname $0`
rm -rf ./shop-jide-com-1.0
unzip shopserver-1.0.zip
screen -S shopserver -d -m ./shopserver-1.0/bin/shopserver -mem 128 -Dhttp.port=32008 -DlogDir=/shopserver/log
