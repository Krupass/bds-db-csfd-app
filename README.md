# How to run app:
## On Windows
#### To open app
##### (1) open git bash
##### (2) `git clone https://github.com/Krupass/bds-db-csfd-app.git`
##### (3) open git bash in the folder bds-db-csfd-app or get in the folder from current folder
##### (4) `mvn clean install`
##### (5) `java -jar target/bds-csfd-1.0.0.jar`
##### (6) login with any valid username and password
##### `username: admin`
##### `password: admin`
#### Generate project and external libraries licenses
##### (1) in root folder `mvn project-info-reports:dependencies`
##### (2) result located in target/site
