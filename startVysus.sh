#!/bin/sh
sudo iptables -t nat -I PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
sudo $CATALINA_HOME/bin/catalina.sh start


