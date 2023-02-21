cd src/main/resources
cp application.yml application-copy.yml
cp application-unused.yml application-copy2.yml
rm application.yml
rm application-unused.yml
cp application-copy.yml application-unused.yml
cp application-copy2.yml application.yml
rm application-copy.yml
rm application-copy2.yml
cd ../../..
