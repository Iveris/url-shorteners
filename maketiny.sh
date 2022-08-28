#!/bin/bash
URL=$1;

echo ""
echo "tinyurl.com"
# tinyurl.com
curl http://tinyurl.com/api-create.php?url=$URL;

echo ""
echo "is.gd"

# is.gd
curl "https://is.gd/create.php?format=simple&url=${URL}";

echo ""
