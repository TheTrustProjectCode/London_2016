### Economist Trust Project Indicator Validation Tool
### Trust Project Hackathon

### BUILDING THE SERVICE

The service currently is built in [Go](https://golang.org/dl/), you will need the latest version of Go installed on your machine.

Go Build will generate the service binary. Run the binary script and the service will be running on your localhost port 9494.

### GOALS

The goal of this service is to help newsrooms and news consumers use and understand the Open Trust Project Indicators.

The Indicators can be found here: https://scu.edu/media/ethics-center/journalism-ethics/OpenTrustProtocol.pdf

This project aimed to align the indicators with the [schema.org](http://schema.org/Organization) standards in order to implement a simple, best practices based system for including trust indicators on news platforms. To review the recommended indicators from schema.org, view the API Documentation at the root of the application (localhost:9494/).

Users can navigate to the /validate endpoint, submit a URL and recieve an anlysis on their implementation of the Indicators and recommendations on what information is missing. Currently the focus is on organization data.

Users can navigate to the /generate endpoint, fill out the form with the relevant organization data, click generate, and receive a metadata snippet that can be added to their site.

### TO DO

Several features need development to establish an MVP:

* Generate: The generate endpoint isn't fully functional, the processing of the form data has an issue.
* Validate: The validation needs to be strengthened to include checking of URLs, use reflection for better mapping and type checks, and include additional type checking for valid content.
* UX: Eventually a front end would need to be built to make the tool friendly to use.
* Articles: Should extend the indicators to use the schema.org NewsArticle object to include article specific data.
