# JAVA WSO2 Client Application

## Example commands to call services

#### GET Method/List

`java <file_name>.java list`

#### POST/Create

`java <file_name>.java create <employeeId> <employeeName> <contact> <email> <salary>`

#### PUT/Update

`java <file_name>.java update <employeeId> <column> <value>`

#### DELETE/Delete

`java <file_name>.java delete <employeeId>`

#### POST/Add Resume (upload)

`java <file_name>.java upload <employeeId> <filePath>`

#### GET/Download Resume (download)

`java <file_name>.java download <employeeId>`

##### If Using IntelliJ IDEA use the following method to input inline arguments (Windows)

- ALT+SHIFT+F10, Type 0.
- Enter your Arguments in "Program Arguments" box and Run.
- Eg: `create <employeeId> <employeeName> <contact> <email> <salary>`

##### Run **Main.java** to test all services. All other methods are defined under **API.Java**
