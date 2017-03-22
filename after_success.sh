#!/bin/bash

mvn clean install deploy:deploy -q --settings settings.xml
