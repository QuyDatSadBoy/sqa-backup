# How to run this project

### 1. Clone this project 
<pre>
git clone https://github.com/jnp2018/mid-project-222474653.git
</pre>

### 2. Export database schemas (into Mysql Workbench)
Export dump file in services folder into Mysql Workbench

### 3. Run gateway and services (on Intelliji IDEA)
- Open one of gateway/service folders in Intelliji and add all remaining services via gradle tab on right side bar
- Change db configuration in application.properties files in user-service, movie-service, payment-service
- Run registry > gateway > auth > <target_services> in order to not make conflict related to dependencies
