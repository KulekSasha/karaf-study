#Simple osgi application.

For app installation follow next steps:
 1. clone this repository;
 2. run `mvn clean install`;
 3. download Apache Karaf v4.0.8 from https://karaf.apache.org/download.html;
 4. run Karaf instance and add repo: `feature:repo-add mvn:com.nix/user-app-features/0.0.1/xml`;
 5. go to [http://localhost:8181/cxf/user-app/rest/users](http://localhost:8181/cxf/user-app/rest/users) to verify is everything ok.