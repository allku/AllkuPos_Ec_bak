#!/usr/bin/env bash
directory=$1
auth=$2
for i in $(ls $directory)
	do curl -X POST https://content.dropboxapi.com/2/files/upload \
    --header "Authorization: Bearer $auth" \
    --header "Dropbox-API-Arg: {\"path\": \"/installers/$i\",\"mode\": \"add\",\"autorename\": true,\"mute\": false}" \
    --header "Content-Type: application/octet-stream" \
    --data-binary @$directory/$i
done