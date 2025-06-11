## Packet tracer automation tool

This Java-based desktop app automates network configuration and monitoring in Cisco Packet Tracer. It enables interaction with virtual network, allowing users to programmatically configure devices. It interacts with Packet Tracer through **inter-process communication (IPC)**.

<img width="1313" alt="Screenshot 2025-03-26 at 21 41 46" src="https://github.com/user-attachments/assets/9e6189ba-c87b-4e8c-9b95-b23f49deb30d" />

![diplom_net](https://github.com/user-attachments/assets/c0f49637-e36a-40dc-a670-7b36fa4181f2)

## How to Run Locally  

### Prerequisites  
Before running the service, ensure you have:  
- **Java** installed  
- **Cisco Packet Tracer** installed and running
- **Maven** 

### Steps to Run  

### 1. **Clone the Repository**  
   ```bash
   git clone https://github.com/Qraree/packet_tracer_auto_net.git
   ```

---

### 2. **Build the project**
   Install framework for ipc connection
   ```bash
   mvn install:install-file -Dfile=libs/pt-cep-java-framework-8.1.0.0.jar -DgroupId=com.cisco.pt -DartifactId=pt-cep-java-framework -Dversion=8.1.0.0 -Dpackaging=jar
   ```

   Then install the project
   ```bash
    mvn clean install
   ```

---

### 4. **Configure Packet Tracer for IPC**
   Before connecting to Packet Tracer (PT), you must configure PT to allow your application. By default, PT looks for applications in the PacketTracer/extensions directory.

   ---
   
### 5. **Create an XML Configuration File**
   Place the following XML configuration in the PacketTracer/extensions directory (replace placeholders like appname with your actual application name):
  ```xml
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE PT_APP_META>
<PT_APP_META>
    <PT_VERSION>8.2.2</PT_VERSION>
    <IPC_VERSION>1.2</IPC_VERSION>
    <NAME>appname</NAME>
    <VERSION>1.2</VERSION>
    <ID>com.example.myapp</ID>
    <DESCRIPTION>Description</DESCRIPTION>
    <AUTHOR>Name</AUTHOR>
    <CONTACT>email</CONTACT>
    <EXECUTABLE_PATH>appname.class</EXECUTABLE_PATH>
    <KEY>cisco</KEY>
    <SECURITY_SETTINGS>
        <PRIVILEGE>GET_NETWORK_INFO</PRIVILEGE>
        <PRIVILEGE>CHANGE_NETWORK_INFO</PRIVILEGE>
        <PRIVILEGE>SIMULATION_MODE</PRIVILEGE>
        <PRIVILEGE>MISC_GUI</PRIVILEGE>
        <PRIVILEGE>FILE</PRIVILEGE>
        <PRIVILEGE>CHANGE_PREFERENCES</PRIVILEGE>
        <PRIVILEGE>CHANGE_GUI</PRIVILEGE>
        <PRIVILEGE>ACTIVITY_WIZARD</PRIVILEGE>
        <PRIVILEGE>MULTIUSER</PRIVILEGE>
        <PRIVILEGE>IPC</PRIVILEGE>
        <PRIVILEGE>APPLICATION</PRIVILEGE>
    </SECURITY_SETTINGS>
    <LOADING>ON_STARTUP</LOADING>
    <SAVING>NEVER</SAVING>
    <INSTANCES>1</INSTANCES>
</PT_APP_META>
```
---

### 6. **Environment Variables Configuration** 

The application requires these environment variables to be set:

| Variable         | Value Example          | Description                                                                 |
|------------------|-----------------------|-----------------------------------------------------------------------------|
| `SECRET`         | `cisco`               | Use the `<KEY>` value from your XML configuration file                      |
| `AUTH_APPLICATION` | `com.example.myapp` | Use the `<ID>` value from your XML configuration file                      |
| `PORT`           | `39000`               | Default IPC port (can be changed in Packet Tracer settings)                 |
| `URL`            | `localhost`           | Default connection URL (change if connecting to remote instance)            |

**Configuration Tips:**
- 🔑 The `<ID>` from your XML becomes `AUTH_APPLICATION`
- 🔑 The `<KEY>` from your XML becomes `SECRET`
- ⚙️ To change default port:  
  ```bash
  Extensions → IPC → Options → Change port number

---

### 7. **Generate the .pta File**
Your application must include an encrypted <appname>.pta file for security. To generate it:
1. **Remove unnecessary privileges from the XML file**
2. **Use the meta utility (located in the extensions folder) to encrypt the XML:**
```bash
meta appname.pta appname.xml appname_executable
```
3. **Ensure your PacketTracer/extensions directory contains both the .xml and .pta files.**

  ---
  
### 8. **Configure Packet Tracer**  
1. **Open Packet Tracer**  

2. **Go to: Extensions → IPC → Configure Apps**

3. **Add your .pta file**

4. **Under Extensions → IPC → Options, enable:**
- ☑️ **Allow remote applications**  
- ☑️ **Always listen on Start**  

 ---

### 9. **Run the Application**
   ```bash
    mvn clean javafx:run
   ```


   

