Moustache
===================

**Moustache** leverages [Moustache.java](https://github.com/spullara/mustache.java) and [Jodd.Props](https://github.com/oblac/jodd).

With **Moustache** you can use a Jodd style property file as the variable substitution source for a template file that is marked up with Moustache.java style handlebars.

License:
Licensed under [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Example Properties file:

```shell
# The configuration capability of this app leverages
# JODD Props:  http://jodd.org/doc/props.html
#

# You can establish multiple profiles for different environments
# To declare the profile, simple change the following to the 
# desired profile
@profiles=dev
[demo<dev>]
account.name=Ping Identity Demo
account.street1=1001 17th St.
account.street2=Suite 100
account.fullAddress=${demo.account.street1} ${demo.account.street2}
account.city=Denver
account.region=CO
account.country=US
account.postal=80202
account.domain=pingidentity.com	
```

Example template file

	-- Account Name: {{demo.account.name}} 
	-- Account Full Address: '{{demo.account.fullAddress}}' 
	-- Account City: "{{demo.account.city}}"
	-- Account Region: &{{demo.account.region}}&
	-- Account Country: {{demo.account.country}}
	-- Account Postal: {{demo.account.postal}};
	-- Account Domain: {{demo.account.domain}}
	
When the above is compiled with **Moustache**, the following output is created:

	-- Account Name: Ping Identity Demo 
	-- Account Full Address: '1001 17th St. Suite 100' 
	-- Account City: "Denver"
	-- Account Region: &CO&
	-- Account Country: US
	-- Account Postal: 80202;
	-- Account Domain: pingidentity.com

Running as a command line utility:

```shell
usage: java -cp moustache.jar Main
-help              Display this message and exit
-verbose           Show status messages during processing
-version           Display version and exit
-props <file>      Jodd.Props style properties file
-template <file>   file with moustache handlebars to process
-out <file>        [optional]output file to create.  If not present, stdout.
```
Embedding within your Java app:

```java
public String compileTemplate(InputStream template, InputStream props) throws Exception
{
	MoustacheCompiler compiler = new MoustacheCompiler();
	compiler.setPropertiesFile(props);
	compiler.setTemplateFile(template);
	String out = compiler.compile();
	return out;
}
```

Building Moustache:

1. Ensure you have Java 1.7 or greater installed
2. Ensure you have Maven 3 or greater installed
3. Download the pom.xml file.
4. Run: mvn scm:checkout
