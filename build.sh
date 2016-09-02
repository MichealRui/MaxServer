#!/bin/bash
cd `dirname $0`
activator clean
activator dist
