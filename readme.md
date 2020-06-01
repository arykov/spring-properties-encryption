## Purpose
This small library is created to simplify sensitive data encryption when writing spring based applications 
## Long winded motivation
[https://blog.ryaltech.com/2019/11/28/spring-properties-encryption](https://blog.ryaltech.com/2019/11/28/spring-properties-encryption)
## How it works
### Application
This library is implemented as a set of aspects. In order to include it in your application execution, you need to use load time(preferred) or compile time weaving. AspectJ library is required to make it work.
Example of using spring boot jar execution with load time weaving
``` java -javaagent:aspectjweaver-1.9.4.jar -classpath spring-properties-encryption-aspects-0.0.4.jar;sample-app-0.0.4.jar org.springframework.boot.loader.JarLauncher ```
### Encryption
Unless [explicitly disabled](#disableencryption) encryption of sensitive values in all file based property sources is performed automatically on application load. 

Alternatively CLI tool can be used to encrypt. It is useful when property sources are something other than filesystem - config servers, SCM, etc.
```
To run java -jar spring-properties-encryption-aspects-0.0.4.jar
Usage: <main class> [options]
  Options:
    --addExcludePatterns, -aep
      Properties following this pattern will not be encrypted. These patterns
      are in addition to defaults.
    --addIncludePatterns, -aip
      Properties following this pattern will be encrypted. These patterns are
      in addition to defaults.
    --excludePatterns, -ep
      Properties following this pattern will not be encrypted. This overrides
      defaults.
    --includePatterns, -ip
      Properties following this pattern will be encrypted. This overrides
      defaults.
  * --source, -s
      Files to encrypt or directories to encrypt files within. At least one
      must be specified.
```      

<a name="disableencryption"></a>
When using CLI to encrypt it is useful to disable automatic encryption. You can do this by passing system property ```-Dcom.ryaltech.utils.spring.encryption.disableAutoEncrypt=true``` or by modifying readOnly setting in  [config.yml](#configuration). 
### Decryption
All properties that look like ENC(...) are decrypted prior to injection.
### Configuration yaml<a name="configuration"></a>
Configuration yaml is packaged in the spring-properties-encryption-aspects-0.0.4.jar in the package com.ryaltech.utils.spring.encryption. Feel free to replace it with something that suits your use case better. 
```
#indicates whether on the fly encryption should be done by default
readOnly: false
#if readOnly is false
#patterns for properties to be encrypted
includePatterns:
    - .*password[^a-z]*
    - .*passphrase[^a-z]*
#patterns of properties that should be skipped even if includPatterns are met
excludePatterns:
#file to keep encryption key in
keyFileName: syskey.dat
```
## Usage sample
Usage example is available here [https://github.com/arykov/spring-properties-encryption/tree/master/sample-app](https://github.com/arykov/spring-properties-encryption/tree/master/sample-app)
## Maven central
None of your code will depend on this library, but built artifacts you will use are distributed via maven central
[Download here](https://search.maven.org/remotecontent?filepath=com/ryaltech/utils/spring/encryption/spring-properties-encryption-aspects/0.0.4/spring-properties-encryption-aspects-0.0.4.jar)
