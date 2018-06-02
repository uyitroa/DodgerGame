javac -cp .:lib/* $1
old=$1
charlen=${#old}
new=${old:0:charlen-5}
java -cp .:lib/* -Djava.library.path=native/macosx/ $new
