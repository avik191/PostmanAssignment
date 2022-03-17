# PostmanAssignment
Aim is to build a system which is able to handle long running processes in a distributed fashion.

<h4> Steps to run the code :</h4> </br>

Reference - https://www.youtube.com/watch?v=ck6xQqSOlpw </br>
1. Install Docker latest version
2. Download docker plugin from market place in intellij if not already present.
  <img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2010.42.03%20AM.png" width="350"/>
3. If docker is running in your system, intellij will establish a connection automatically when you add docker in preferences. We can see connection successful msg <img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2010.42.45%20AM.png" width="450"/>
4. Check services window and start docker in intellij. we can go to services window using cmd+8 in mac system </br>
5. Download postgres image. It will download the latest version <img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2010.47.10%20AM.png" width="450"/>
6. Create new container by right cick on image => select create container => create. </br> Add the following configurations </br> </t>
	a. go to modify options -> environment variables : POSTGRES_PASSWORD=test; POSTGRES_USER=test; POSTGRES_DB=test </br>
	b. go to modify options -> binding ports : host port = 5432; container port = 5432 </br>
	click run.<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2010.53.42%20AM.png" width="550"/>
<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2010.55.51%20AM.png" width="500"/>
7. right click on container and select exec and type "psql postgres -U test". We will now be redirected to postgres cli.<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2010.57.26%20AM.png" width="450"/>
8. connect to our test DB using the cmd "\connect test" </br>
9. create our product and aggregated tables and check using cmd "\dt" </br>
<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2011.00.55%20AM.png" width="700"/>

Table Schema:- </br>
<h4> CREATE TABLE IF NOT EXISTS product (name VARCHAR(500) NOT NULL,sku VARCHAR(500) NOT NULL,description VARCHAR(500) NOT NULL,PRIMARY KEY (sku)); </h4>
<h4> CREATE TABLE IF NOT EXISTS aggregated_tbl (name VARCHAR(500) NOT NULL, no_of_products INT NOT NULL); </h4>

10. Once our tables are created, we can connect them in intellij. Initially both tables will be empty

<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2011.02.21%20AM.png" width="500"/>
<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2011.02.49%20AM.png" width="500"/>
username = test </br>
password = test </br>
db = test </br>
port = 5432 </br>

11. Now our docker environment is ready with the required tables. </br> Goto Driver class and run the main method. Wait for sometime for the job to complete. </br>
Depending on the system it may take 5-10 mins as we have a single executor right now.
Once the job is completed we can see text "********Records are ingested successfully*********" in the logs. Now check the tables to verify the data.
<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2011.16.24%20AM.png" width="400"/>

Even after ingesting the same file again, the counts are same which means upsert functionality is working fine.</br>
Total records in product table = 466694 </br>
Total records in aggregated_tbl table = 212784 </br> </br>
<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2011.17.22%20AM.png" width="600"/>
<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2011.17.52%20AM.png" width="600"/>
<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2011.18.03%20AM.png" width="600"/>

<h4> Additional Metrics</h4>
<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2011.15.39%20AM.png" width="600"/>
<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2011.15.51%20AM.png" width="600"/>
<img src="https://github.com/avik191/PostmanAssignment/blob/main/src/main/resources/Screenshot%202022-03-17%20at%2011.15.59%20AM.png" width="600"/>
</p>
</br>

<h4> Table Schema </h4> </br>
1. product - CREATE TABLE IF NOT EXISTS product (name VARCHAR(500) NOT NULL,sku VARCHAR(500) NOT NULL,description VARCHAR(500) NOT NULL,PRIMARY KEY (sku)); </br>
2. aggregated_tbl - CREATE TABLE IF NOT EXISTS aggregated_tbl (name VARCHAR(500) NOT NULL, no_of_products INT NOT NULL); </br>

<h4> All points are covered from points to achieve </h4> </br>
<h4> Given more time, I would tried to run this job in an actual cluster to and do some tuning on batch sizes or executor numbers to optimize the flow and verify performance gain. Now in local it is running on 1 executor so I am not able to take advantage of parallelism. Also i would have to use some distributed DB like scylla in place of postgres where i would want to partition it based on primary key 'sku'. We will also get performance gain over there</h4> </br>
