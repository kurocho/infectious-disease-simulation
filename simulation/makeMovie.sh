#!/bin/bash
for n in {001..100}; do
    cp 004691.png 0047$n.png
done
 EXT=png
 OPT="vcodec=mpeg4:vqscale=1:vhq:v4mv:trell:autoaspect"
 FPS=25
 PREFIX=""
 OUTPUT="simulation-movie.avi"

 mencoder "mf://$PREFIX*.$EXT" -mf fps=$FPS:type=$EXT -ovc lavc -lavcopts $OPT -o $OUTPUT -nosound -vf scale
