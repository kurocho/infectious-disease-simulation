#!/bin/bash
 EXT=png
 OPT="vcodec=mpeg4:vqscale=1:vhq:v4mv:trell:autoaspect"
 FPS=25
 PREFIX=""
 OUTPUT="simulation-movie.avi"

 mencoder "mf://$PREFIX*.$EXT" -mf fps=$FPS:type=$EXT -ovc lavc -lavcopts $OPT -o $OUTPUT -nosound -vf scale
